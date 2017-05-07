package acast.ui.accidentDescription.responsibilities;

import java.util.UUID;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;

import acast.Activator;
import acast.Messages;
import acast.model.interfaces.IResponsibilityDataModel;
import acast.ui.accidentDescription.Responsibility;

public class TableView extends ViewPart implements IPropertyChangeListener {

  private String selectedItem;

  /**
   * Interface to communicate with the data model.
   */
  public IResponsibilityDataModel dataInterface;

  /**
   * ViewPart ID.
   */
  public final String ID = "acast.steps.step2_2_1"; //$NON-NLS-1$

  public ResponsibilityRole responsibility;

  public Combo combo;

  public TableViewer viewer;

  public void setDataModelInterface(IDataModel dataInterface) {
    this.dataInterface = (IResponsibilityDataModel) dataInterface;
  }

  public void updateReponsibilites(UUID id, ResponsibilityRole view) {

    switch (view) {
      case RESPONSIBILITIES:
        for (Responsibility resp : dataInterface.getResponsibilitiesListforComponent(id)) {
          TableItem item = new TableItem(viewer.getTable(), SWT.None);
          item.setText(new String[] { resp.getId(), resp.getDescription() });
        }
        break;

      case CONTEXT:
        for (Responsibility resp : dataInterface.getContextListforComponent(id)) {
          TableItem item = new TableItem(viewer.getTable(), SWT.None);
          item.setText(new String[] { resp.getId(), resp.getDescription() });
        }
        break;

      case FLAWS:
        for (Responsibility resp : dataInterface.getFlawListforComponent(id)) {
          TableItem item = new TableItem(viewer.getTable(), SWT.None);
          item.setText(new String[] { resp.getId(), resp.getDescription() });
        }
        break;

      case UNSAFEACTIONS:
        for (Responsibility resp : dataInterface.getUnsafeActionListforComponent(id)) {
          TableItem item = new TableItem(viewer.getTable(), SWT.None);
          item.setText(new String[] { resp.getId(), resp.getDescription() });
        }
        break;

      case FEEDBACK:
        for (Responsibility resp : dataInterface.getFeedbackListforComponent(id)) {
          TableItem item = new TableItem(viewer.getTable(), SWT.None);
          item.setText(new String[] { resp.getId(), resp.getDescription() });
        }
        break;
      case COORDINATION:
        for (Responsibility resp : dataInterface.getCoordinationListforComponent(id)) {
          TableItem item = new TableItem(viewer.getTable(), SWT.None);
          item.setText(new String[] { resp.getId(), resp.getDescription() });
        }
        break;
    }

  }

  public void removeFromDataModel(UUID name, String id, ResponsibilityRole view) {
    switch (view) {
      case RESPONSIBILITIES:
        dataInterface.removeResponsibility(name, id);
        break;
      case CONTEXT:
        dataInterface.removeContext(name, id);
        break;
      case FLAWS:
        dataInterface.removeFlaw(name, id);
        break;
      case UNSAFEACTIONS:
        dataInterface.removeunsafeAction(name, id);
        break;
      case COORDINATION:
        dataInterface.removeCoordination(name, id);
        break;
      case FEEDBACK:
        dataInterface.removeFeedback(name, id);
        break;
    }
  }

  public void updateTableComponentsInDataStructure(UUID ident, String oldId, String newId,
      String newDescription, ResponsibilityRole view, String name) {
    switch (view) {
      case RESPONSIBILITIES:
        dataInterface.changeResponsibility(ident, oldId, newId, newDescription, name);
        break;
      case CONTEXT:
        dataInterface.changeContext(ident, oldId, newId, newDescription, name);
        break;
      case FLAWS:
        dataInterface.changeFlaw(ident, oldId, newId, newDescription, name);
        break;
      case UNSAFEACTIONS:
        dataInterface.changeUnsafeAction(ident, oldId, newId, newDescription, name);
        break;
      case FEEDBACK:
        dataInterface.changeFeedback(ident, oldId, newId, newDescription, name);
        break;
      case COORDINATION:
        dataInterface.changeCoordination(ident, oldId, newId, newDescription, name);
        break;
    }
  }

  public void applyDataToDataModel(UUID ident, String id, String description, ResponsibilityRole view,
      String name) {

    switch (view) {
      case RESPONSIBILITIES:
        dataInterface.addResponsibility(ident, id, description, name);
        break;
      case CONTEXT:
        dataInterface.addContext(ident, id, description, name);
        break;
      case FLAWS:
        dataInterface.addFlaw(ident, id, description, name);
        break;
      case UNSAFEACTIONS:
        dataInterface.addUnsafeAction(ident, id, description, name);
        break;
      case FEEDBACK:
        dataInterface.addFeedback(ident, id, description, name);
        break;
      case COORDINATION:
        dataInterface.addCoordination(ident, id, description, name);
        break;
    }
  }

  @Override
  public void createPartControl(final Composite parent) {

    PlatformUI.getPreferenceStore().addPropertyChangeListener(this);

    StandartEditorPart e = (StandartEditorPart) (PlatformUI.getWorkbench()
        .getActiveWorkbenchWindow().getActivePage().getActiveEditor());
    setDataModelInterface(ProjectManager.getContainerInstance().getDataModel(e.getProjectID()));
    parent.setLayout(new GridLayout(1, false));
    parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

    Composite layout = new Composite(parent, SWT.NONE);
    layout.setLayout(new GridLayout(1, false));
    layout.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

    Composite description = new Composite(layout, SWT.NONE);
    description.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));

    description.setLayout(new GridLayout(5, false));

    responsibility = ResponsibilityRole.RESPONSIBILITIES;

    Label lblResponsibilities = new Label(description, SWT.RIGHT);

    lblResponsibilities.setText(Messages.TableView_8);
    lblResponsibilities.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true, 1, 1));

    combo = new Combo(description, SWT.DROP_DOWN | SWT.RIGHT | SWT.READ_ONLY);
    combo.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        viewer.getTable().removeAll();
        selectedItem = combo.getItem(combo.getSelectionIndex());

        updateReponsibilites(dataInterface.getComponentNames().get(selectedItem), responsibility);
      }
    });

    combo.setLayoutData(new GridData(SWT.NONE, SWT.FILL, true, true, 1, 1));

    for (String name : dataInterface.getComponentNames().keySet()) {
      combo.add(name);
    }

    Button btnAdd = new Button(description, SWT.CENTER);
    btnAdd.setLayoutData(new GridData(SWT.RIGHT, SWT.None, true, false, 1, 1));
    btnAdd.setImage(Activator.getImageDescriptor("/icons/add.png").createImage()); //$NON-NLS-1$
    btnAdd.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        int count;
        if (dataInterface.getComponentNames().isEmpty()) {
          MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
          dialog.setText(Messages.TableView_10);
          dialog.setMessage(Messages.TableView_11);

          dialog.open();
          return;
        }
        if (combo.getSelectionIndex() == -1) {
          MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
          dialog.setText(Messages.TableView_12);
          dialog.setMessage(Messages.TableView_13);

          dialog.open();
          return;
        }
        if (viewer.getTable().getItemCount() == 0) {
          count = 1;
        } else {
          count = viewer.getTable().getItemCount() + 1;
        }
        TableItem item = new TableItem(viewer.getTable(), SWT.None);
        item.setText(new String[] { count + Messages.TableView_14, Messages.TableView_15 });
        applyDataToDataModel(dataInterface.getComponentNames().get(selectedItem), item.getText(0),
            item.getText(1), responsibility, selectedItem);
      }
    });

    Button btnDel = new Button(description, SWT.CENTER);
    btnDel.setLayoutData(new GridData(SWT.RIGHT, SWT.None, false, false, 1, 1));
    btnDel.setImage(Activator.getImageDescriptor(Messages.TableView_16).createImage());
    btnDel.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        if (viewer.getTable().getSelectionIndex() == -1) {
          MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
          dialog.setText(Messages.TableView_17);
          dialog.setMessage(Messages.TableView_18);

          dialog.open();
          return;
        }
        int itemIndex = viewer.getTable().getSelectionIndex();
        removeFromDataModel(dataInterface.getComponentNames().get(selectedItem),
            viewer.getTable().getItem(itemIndex).getText(0), responsibility);
        viewer.getTable().remove(itemIndex);
        for (int i = itemIndex; i < viewer.getTable().getItemCount(); i++) {
          updateTableComponentsInDataStructure(dataInterface.getComponentNames().get(selectedItem),
              viewer.getTable().getItem(i).getText(0), i + 1 + "", //$NON-NLS-1$
              viewer.getTable().getItem(i).getText(1), responsibility, selectedItem);
          viewer.getTable().getItem(i).setText(0, i + 1 + ""); //$NON-NLS-1$
        }

      }
    });

    GridLayout buttons = new GridLayout(1, true);
    buttons.verticalSpacing = 0;

    GridLayout dataLayout = new GridLayout(5, false);
    dataLayout.horizontalSpacing = 0;
    dataLayout.verticalSpacing = 0;

    Composite data = new Composite(layout, SWT.BORDER);
    data.setLayout(dataLayout);
    data.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

    Composite tab = new Composite(data, SWT.None);
    tab.setLayout(buttons);
    tab.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true, 1, 1));

    final Button safety = new Button(tab, SWT.NONE);
    safety.setText(Messages.TableView_SafetyRelated);
    safety.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1, 2));
    safety.setEnabled(false);
    safety.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
    safety.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));

    final Button unsafeActionsBtn = new Button(tab, SWT.NONE);
    unsafeActionsBtn.setText(Messages.TableView_2);
    unsafeActionsBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1, 2));

    final Button flawsBtn = new Button(tab, SWT.NONE);
    flawsBtn.setText(Messages.TableView_4);
    flawsBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1, 2));

    final Button contextBtn = new Button(tab, SWT.NONE);
    contextBtn.setText(Messages.TableView_3);
    contextBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1, 2));

    final Button feedbackBtn = new Button(tab, SWT.NONE);
    feedbackBtn.setText(Messages.TableView_5);
    feedbackBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1, 2));

    final Button coordinationBtn = new Button(tab, SWT.NONE);
    coordinationBtn.setText(Messages.TableView_6);
    coordinationBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1, 2));

    final Color defaultColor = unsafeActionsBtn.getBackground();

    safety.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        responsibility = ResponsibilityRole.RESPONSIBILITIES;
        contextBtn.setEnabled(true);
        flawsBtn.setEnabled(true);
        unsafeActionsBtn.setEnabled(true);
        feedbackBtn.setEnabled(true);
        coordinationBtn.setEnabled(true);
        coordinationBtn.setBackground(defaultColor);
        feedbackBtn.setBackground(defaultColor);
        contextBtn.setBackground(defaultColor);
        flawsBtn.setBackground(defaultColor);
        unsafeActionsBtn.setBackground(defaultColor);
        safety.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        safety.setEnabled(false);
        PlatformUI.getWorkbench().getDisplay().getActiveShell().forceFocus();
        viewer.getTable().removeAll();
        updateReponsibilites(dataInterface.getComponentNames().get(selectedItem), responsibility);
      }
    });

    contextBtn.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        responsibility = ResponsibilityRole.CONTEXT;
        safety.setEnabled(true);
        flawsBtn.setEnabled(true);
        unsafeActionsBtn.setEnabled(true);
        feedbackBtn.setEnabled(true);
        coordinationBtn.setEnabled(true);
        coordinationBtn.setBackground(defaultColor);
        feedbackBtn.setBackground(defaultColor);
        safety.setBackground(defaultColor);
        flawsBtn.setBackground(defaultColor);
        unsafeActionsBtn.setBackground(defaultColor);
        contextBtn.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        contextBtn.setEnabled(false);
        PlatformUI.getWorkbench().getDisplay().getActiveShell().forceFocus();
        viewer.getTable().removeAll();
        updateReponsibilites(dataInterface.getComponentNames().get(selectedItem), responsibility);
      }
    });

    unsafeActionsBtn.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        responsibility = ResponsibilityRole.UNSAFEACTIONS;
        safety.setEnabled(true);
        flawsBtn.setEnabled(true);
        contextBtn.setEnabled(true);
        feedbackBtn.setEnabled(true);
        coordinationBtn.setEnabled(true);
        coordinationBtn.setBackground(defaultColor);
        feedbackBtn.setBackground(defaultColor);
        safety.setBackground(defaultColor);
        flawsBtn.setBackground(defaultColor);
        contextBtn.setBackground(defaultColor);
        unsafeActionsBtn.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        unsafeActionsBtn.setEnabled(false);
        PlatformUI.getWorkbench().getDisplay().getActiveShell().forceFocus();
        viewer.getTable().removeAll();
        updateReponsibilites(dataInterface.getComponentNames().get(selectedItem), responsibility);
      }
    });

    flawsBtn.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        responsibility = ResponsibilityRole.FLAWS;
        safety.setEnabled(true);
        unsafeActionsBtn.setEnabled(true);
        contextBtn.setEnabled(true);
        feedbackBtn.setEnabled(true);
        coordinationBtn.setEnabled(true);
        coordinationBtn.setBackground(defaultColor);
        feedbackBtn.setBackground(defaultColor);
        safety.setBackground(defaultColor);
        unsafeActionsBtn.setBackground(defaultColor);
        contextBtn.setBackground(defaultColor);
        flawsBtn.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        flawsBtn.setEnabled(false);
        PlatformUI.getWorkbench().getDisplay().getActiveShell().forceFocus();
        viewer.getTable().removeAll();
        updateReponsibilites(dataInterface.getComponentNames().get(selectedItem), responsibility);
      }
    });

    feedbackBtn.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        responsibility = ResponsibilityRole.FEEDBACK;
        safety.setEnabled(true);
        unsafeActionsBtn.setEnabled(true);
        flawsBtn.setEnabled(true);
        contextBtn.setEnabled(true);
        coordinationBtn.setEnabled(true);
        safety.setBackground(defaultColor);
        flawsBtn.setBackground(defaultColor);
        unsafeActionsBtn.setBackground(defaultColor);
        contextBtn.setBackground(defaultColor);
        coordinationBtn.setBackground(defaultColor);
        feedbackBtn.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        feedbackBtn.setEnabled(false);
        PlatformUI.getWorkbench().getDisplay().getActiveShell().forceFocus();
        viewer.getTable().removeAll();
        updateReponsibilites(dataInterface.getComponentNames().get(selectedItem), responsibility);
      }
    });

    coordinationBtn.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        responsibility = ResponsibilityRole.COORDINATION;
        safety.setEnabled(true);
        flawsBtn.setEnabled(true);
        unsafeActionsBtn.setEnabled(true);
        contextBtn.setEnabled(true);
        feedbackBtn.setEnabled(true);
        safety.setBackground(defaultColor);
        unsafeActionsBtn.setBackground(defaultColor);
        contextBtn.setBackground(defaultColor);
        feedbackBtn.setBackground(defaultColor);
        flawsBtn.setBackground(defaultColor);
        coordinationBtn.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
        coordinationBtn.setEnabled(false);
        PlatformUI.getWorkbench().getDisplay().getActiveShell().forceFocus();
        viewer.getTable().removeAll();
        updateReponsibilites(dataInterface.getComponentNames().get(selectedItem), responsibility);
      }
    });

    Composite buttonComposite = new Composite(tab, SWT.BOTTOM);
    buttonComposite.setLayout(new GridLayout(2, false));
    buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

    Composite tableComposite = new Composite(data, SWT.NONE);
    tableComposite.setLayout(new GridLayout(1, false));
    tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));

    viewer = new TableViewer(tableComposite, SWT.FULL_SELECTION);

    TableColumn c1 = new TableColumn(viewer.getTable(), SWT.NONE);
    c1.setText(Messages.TableView_19);
    TableColumnLayout tableColumnLayoutC1 = new TableColumnLayout();
    tableColumnLayoutC1.setColumnData(c1, new ColumnWeightData(10, 25, true));
    tableComposite.setLayout(tableColumnLayoutC1);

    TableColumn c2 = new TableColumn(viewer.getTable(), SWT.NONE);
    c2.setText(Messages.TableView_22);
    TableColumnLayout tableColumnLayoutC2 = new TableColumnLayout();
    tableColumnLayoutC2.setColumnData(c2, new ColumnWeightData(90, SWT.FILL, true));
    tableComposite.setLayout(tableColumnLayoutC2);

    viewer.getTable().addListener(SWT.Resize, new Listener() {

      @Override
      public void handleEvent(Event event) {

        Table table = (Table) event.widget;
        int columnCount = table.getColumnCount();
        if (columnCount == 0) {
          return;
        }
        Rectangle area = table.getClientArea();

        TableColumn lastCol = table.getColumns()[columnCount - 1];

        lastCol.setWidth(
            lastCol.getWidth() + (area.width - lastCol.getWidth() + table.getColumn(0).getWidth()));

      }
    });

    final TableEditor editor = new TableEditor(viewer.getTable());
    editor.horizontalAlignment = SWT.LEFT;
    editor.grabHorizontal = true;
    viewer.getTable().addListener(SWT.MouseDown, new Listener() {
      @Override
      public void handleEvent(Event event) {
        Point pt = new Point(event.x, event.y);
        int index = viewer.getTable().getTopIndex();
        while (index < viewer.getTable().getItemCount()) {
          final TableItem item = viewer.getTable().getItem(index);
          for (int i = 0; i < viewer.getTable().getColumnCount(); i++) {
            boolean inRect = item.getBounds(i).contains(pt);
            if (inRect && i == 1) {
              final Text text = new Text(viewer.getTable(), SWT.NONE);
              Listener textListener = new Listener() {
                @Override
                public void handleEvent(final Event e) {
                  switch (e.type) {
                    case SWT.FocusOut:
                      item.setText(1, text.getText());
                      text.dispose();
                      updateTableComponentsInDataStructure(
                          dataInterface.getComponentNames().get(selectedItem), item.getText(0),
                          item.getText(0), item.getText(1), responsibility, selectedItem);
                      break;
                    case SWT.Traverse:
                      switch (e.detail) {
                        case SWT.TRAVERSE_RETURN:
                          item.setText(1, text.getText());
                        case SWT.TRAVERSE_ESCAPE:
                          text.dispose();
                          e.doit = false;
                      }
                      break;
                  }
                }
              };
              text.addListener(SWT.FocusOut, textListener);
              text.addListener(SWT.Traverse, textListener);
              editor.setEditor(text, item, i);
              text.setText(item.getText(i));
              text.selectAll();
              text.setFocus();
              return;
            } else if(inRect) {
              return;
            }
          }
          index++;
        }
      }
    });
    viewer.getTable().setHeaderVisible(true);
    viewer.getTable().setLinesVisible(true);
    if (combo.getItemCount() > 0) {
      selectedItem = combo.getItem(0);
      combo.select(0);
      viewer.getTable().removeAll();
      updateReponsibilites(dataInterface.getComponentNames().get(selectedItem), responsibility);
    }
  }

  @Override
  public void setFocus() {
  }

  @Override
  public void dispose() {
    PlatformUI.getPreferenceStore().removePropertyChangeListener(this);
    super.dispose();
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    try {
      if (event.getNewValue().equals(Messages.TableView_23)) {
        if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
            .findView("A-CAST.view1") != null) { //$NON-NLS-1$
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(PlatformUI
              .getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("A-CAST.view1")); //$NON-NLS-1$
        }
      } else if (event.getNewValue().equals(Messages.TableView_26)) {
        combo.remove(combo.getSelectionIndex());
        combo.add(String.valueOf(event.getOldValue()));
        combo.select(combo.indexOf(String.valueOf(event.getOldValue())));
      } else if (event.getNewValue().equals(Messages.TableView_27)) {
        combo.add(String.valueOf(event.getOldValue()));
      } else if (event.getNewValue().equals(Messages.TableView_28)) {
        try {
          combo.remove(String.valueOf(event.getOldValue()));
        } catch (IllegalArgumentException exc) {
          System.out.println(String.valueOf(event.getOldValue()));
        }
        if (combo.getItems().length > 0) {
          combo.select(0);
          viewer.getTable().removeAll();
          selectedItem = combo.getItem(combo.getSelectionIndex());
          updateReponsibilites(dataInterface.getComponentNames().get(selectedItem), responsibility);
        } else {
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(PlatformUI
              .getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("A-CAST.view1")); //$NON-NLS-1$
        }
      } else {
        combo.removeAll();
        for (String name : dataInterface.getComponentNames().keySet()) {
          combo.add(name);
        }
        combo.select(combo.indexOf(String.valueOf(event.getNewValue())));
        selectedItem = String.valueOf(event.getNewValue());
        viewer.getTable().removeAll();
        updateReponsibilites(dataInterface.getComponentNames().get(selectedItem), responsibility);
      }
      combo.redraw();
    } catch (SWTException exc) {
      
    }
  }

}