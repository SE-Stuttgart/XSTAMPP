/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui.sds;

import messages.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;

import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.ISystemGoalViewDataModel;
import xstampp.astpa.model.sds.SystemGoal;
import xstampp.astpa.ui.ATableFilter;
import xstampp.astpa.ui.CommonTableView;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;

import java.util.UUID;

/**
 * @author Jarkko Heidenwag
 * @since 1.0.0
 * @version 2.0.2
 */
public class SystemGoalView extends CommonTableView<ISystemGoalViewDataModel> {

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public static final String ID = "astpa.steps.step1_6"; //$NON-NLS-1$

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public SystemGoalView() {
    super(true);
  }

  /**
   * Create contents of the view part.
   * 
   * @author Jarkko Heidenwag
   * @param parent
   *          The parent composite
   */
  @Override
  public void createPartControl(Composite parent) {
    this.setDataModelInterface(
        ProjectManager.getContainerInstance().getDataModel(this.getProjectID()));

    this.createCommonTableView(parent, Messages.SystemGoals);

    this.getFilterTextField().addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent ke) {
        SystemGoalView.this.getFilter()
            .setSearchText(SystemGoalView.this.getFilterTextField().getText());
        SystemGoalView.this.refreshView();
      }
    });

    this.setFilter(new ATableFilter());
    this.getTableViewer().addFilter(this.getFilter());

    Listener addSystemGoalListener = new Listener() {

      @Override
      public void handleEvent(Event event) {
        if ((event.type == SWT.KeyDown) && (event.keyCode != 'n')) {
          return;
        }
        SystemGoalView.this.getFilter().setSearchText(""); //$NON-NLS-1$
        SystemGoalView.this.getFilterTextField().setText(""); //$NON-NLS-1$
        SystemGoalView.this.refreshView();
        SystemGoalView.this.getDataInterface().addSystemGoal("", //$NON-NLS-1$
            Messages.DescriptionOfThisSystemGoal);
        int newID = SystemGoalView.this.getDataInterface().getAllSystemGoals().size() - 1;
        SystemGoalView.this.updateTable();
        SystemGoalView.this.refreshView();
        SystemGoalView.this.getTableViewer().setSelection(
            new StructuredSelection(SystemGoalView.this.getTableViewer().getElementAt(newID)),
            true);
        SystemGoalView.this.getTitleColumn().getViewer()
            .editElement(SystemGoalView.this.getTableViewer().getElementAt(newID), 1);
      }
    };

    this.getAddNewItemButton().addListener(SWT.Selection, addSystemGoalListener);

    this.getTableViewer().getTable().addListener(SWT.KeyDown, addSystemGoalListener);

    // Listener for editing a title by pressing return
    Listener returnListener = new Listener() {

      @Override
      public void handleEvent(Event event) {
        if ((event.type == SWT.KeyDown) && (event.keyCode == SWT.CR)
            && (!SystemGoalView.this.getTableViewer().getSelection().isEmpty())) {
          int indexFirstSelected = SystemGoalView.this.getTableViewer().getTable()
              .getSelectionIndices()[0];
          SystemGoalView.this.getTitleColumn().getViewer().editElement(
              SystemGoalView.this.getTableViewer().getElementAt(indexFirstSelected), 1);
        }
      }
    };

    this.getTableViewer().getTable().addListener(SWT.KeyDown, returnListener);

    // check if the description is default and delete it in that case
    this.getDescriptionWidget().addFocusListener(new FocusListener() {

      @Override
      public void focusGained(FocusEvent e) {
        Text text = (Text) e.widget;
        String description = text.getText();
        if (description.compareTo(Messages.DescriptionOfThisSystemGoal) == 0) {
          SystemGoalView.this.getDataInterface().setSystemGoalDescription(getCurrentSelection(),
              ""); //$NON-NLS-1$
          text.setText(""); //$NON-NLS-1$
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        // intentionally empty

      }
    });

    // Listener for the Description
    this.getDescriptionWidget().addModifyListener(new ModifyListener() {

      @Override
      public void modifyText(ModifyEvent e) {
          Text text = (Text) e.widget;
          String description = text.getText();
          SystemGoalView.this.getDataInterface().setSystemGoalDescription(getCurrentSelection(), description);
      }
    });

    final EditingSupport titleEditingSupport = new SGEditingSupport(
        SystemGoalView.this.getTableViewer());
    this.getTitleColumn().setEditingSupport(titleEditingSupport);

    // KeyListener for deleting system goals by selecting them and pressing
    // the delete key
    SystemGoalView.this.getTableViewer().getControl().addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(final KeyEvent e) {
        if ((e.keyCode == SWT.DEL) || ((e.stateMask == SWT.COMMAND) && (e.keyCode == SWT.BS))) {
          IStructuredSelection selection = (IStructuredSelection) SystemGoalView.this
              .getTableViewer().getSelection();
          if (selection.isEmpty()) {
            return;
          }
          SystemGoalView.this.deleteItems();
        }
      }
    });

    // Adding a right click context menu and the option to delete an entry
    // this way
    MenuManager menuMgr = new MenuManager();
    Menu menu = menuMgr.createContextMenu(SystemGoalView.this.getTableViewer().getControl());
    menuMgr.addMenuListener(new IMenuListener() {

      @Override
      public void menuAboutToShow(IMenuManager manager) {
        if (SystemGoalView.this.getTableViewer().getSelection().isEmpty()) {
          return;
        }
        if (SystemGoalView.this.getTableViewer().getSelection() instanceof IStructuredSelection) {
          Action deleteSystemGoal = new Action(Messages.DeleteSystemGoals) {

            @Override
            public void run() {
              SystemGoalView.this.deleteItems();
            }
          };
          manager.add(deleteSystemGoal);
        }
      }

    });

    menuMgr.setRemoveAllWhenShown(true);
    SystemGoalView.this.getTableViewer().getControl().setMenu(menu);

    this.updateTable();

  }

  @Override
  protected void deleteEntry(ATableModel model) {
    resetCurrentSelection();
    this.getDataInterface().removeSystemGoal(model.getId());
  }

  private class SGEditingSupport extends AbstractEditingSupport {

    /**
     * 
     * @author Jarkko Heidenwag
     * 
     * @param viewer
     *          the ColumnViewer
     */
    public SGEditingSupport(ColumnViewer viewer) {
      super(viewer);
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
      return new TextCellEditor(SystemGoalView.this.getTableViewer().getTable());
    }

    @Override
    protected Object getValue(Object element) {
      if (element instanceof SystemGoal) {
        // deleting the default title
        if ((((SystemGoal) element).getTitle().compareTo(Messages.DoubleClickToEditTitle)) == 0) {
          ((SystemGoal) element).setTitle(""); //$NON-NLS-1$
        }
        return ((SystemGoal) element).getTitle();
      }
      return null;
    }

    @Override
    protected void setValue(Object element, Object value) {
      if (element instanceof SystemGoal) {
        ((SystemGoal) element).setTitle(String.valueOf(value));
        // Fill in the default title if the user left it blank
        if (((SystemGoal) element).getTitle().length() == 0) {
          ((SystemGoal) element).setTitle(Messages.DoubleClickToEditTitle);
        }
      }
      SystemGoalView.this.refreshView();
    }
  }

  /**
   * @author Jarkko Heidenwag
   * 
   */
  @Override
  public void updateTable() {
    SystemGoalView.this.getTableViewer().setInput(this.getDataInterface().getAllSystemGoals());
  }

  @Override
  public String getId() {
    return SystemGoalView.ID;
  }

  @Override
  public String getTitle() {
    return Messages.SystemGoals;
  }

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   * @return the type of this view
   */
  @Override
  public commonTableType getCommonTableType() {
    return commonTableType.SystemGoalsView;
  }

  @Override
  public void dispose() {
    this.getDataInterface().deleteObserver(this);
    super.dispose();
  }

  @Override
  protected void moveEntry(UUID id, boolean moveUp) {
    getDataInterface().moveEntry(moveUp, id, ObserverValue.SYSTEM_GOAL);
  }
}
