/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.ui;

import java.util.UUID;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import xstampp.astpa.ui.causalScenarios.ActionMenuListener;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.editors.AbstractFilteredEditor;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUserProject;

/**
 * An {@link AbstractFilteredEditor} that contains a {@link Grid}.
 * The default implementation of the {@link CommonGridView#createPartControl(Composite, String[])}
 * method sets the dataModel of the currently selected project as the active Data Model available
 * with {@link CommonGridView#getDataModel()}.
 * It also registers the delete Action provided by {@link CommonGridView#getDeleteAction()} if not
 * null.
 * 
 * @author Lukas Balzer
 *
 * @param <T>
 *          A class extending {@link IDataModel}
 */
public abstract class CommonGridView<T extends IDataModel> extends AbstractFilteredEditor {

  private GridWrapper grid;
  private T dataInterface;
  private IAction deleteAction;
  private int columnCount;

  protected abstract void fillTable();

  @Override
  public void createPartControl(Composite parent) {
    createPartControl(parent, new String[] { "" });
  }

  public void createPartControl(Composite parent, String[] columns) {
    this.setDataModelInterface(ProjectManager.getContainerInstance()
        .getDataModel(this.getProjectID()));
    super.createPartControl(parent);
    parent.setLayout(new GridLayout(1, false));
    this.grid = new GridWrapper(parent, columns);
    this.columnCount = columns.length;
    getGridWrapper().setSelectRow(false);
    getGrid().setVisible(true);

    getGrid().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    this.reloadTable();
    this.deleteAction = getDeleteAction();
    if (this.deleteAction != null) {
      MenuManager menuMgr = new MenuManager();
      Menu menu = menuMgr.createContextMenu(getGrid());
      menuMgr.addMenuListener(new ActionMenuListener(this.deleteAction));
      menuMgr.setRemoveAllWhenShown(true);
      getGrid().setMenu(menu);
    }
  }

  public int getColumnCount() {
    return columnCount;
  }

  /**
   * Reload the whole table.
   * 
   * @author Patrick Wickenhaeuser
   * 
   */
  protected final void reloadTable() {
    if (!this.grid.fetchUpdateLock()) {
      if (this.grid.persistedScrollIndex == null) {
        this.grid.persistedScrollIndex = this.grid.getGrid().getTopIndex();
      }
      this.grid.clearRows();
      this.fillTable();
      this.grid.reloadTable();
    }
  }

  /**
   * sets the data model object for this editor
   *
   * @author Lukas
   *
   * @param dataInterface
   *          the data model object
   */
  @SuppressWarnings("unchecked")
  public void setDataModelInterface(IDataModel dataInterface) {
    this.dataInterface = (T) dataInterface;
    this.dataInterface.addObserver(this);
  }

  protected T getDataModel() {
    return dataInterface;
  }

  protected void deleteEntry() {
    if (this.deleteAction != null) {
      deleteAction.run();
    }
  }

  /**
   * an abstract getter that defines a delete action that is than added to the grids context menu
   * if null is returned no delete action is registered
   * 
   * <i>NOTE: this method is only called once in createPartControl</i>
   * 
   * @return the delete action for this data type or null if no deleteAction should be available
   */
  public abstract DeleteGridEntryAction<T> getDeleteAction();

  /**
   * @return the grid
   */
  public GridWrapper getGridWrapper() {
    return grid;
  }

  /**
   * @return the grid
   */
  public Grid getGrid() {
    return grid.getGrid();
  }

  /**
   * @param grid
   *          the grid to set
   */
  public void setGridWrapper(GridWrapper grid) {
    this.grid = grid;
  }

  @Override
  public void dispose() {
    getDataModel().deleteObserver(this);
    super.dispose();
  }

  protected boolean checkAccess(UUID caId, AccessRights accessRight) {
    if (getDataModel() instanceof IUserProject) {
      return ((IUserProject) getDataModel()).getUserSystem().checkAccess(caId, accessRight);
    }
    return true;
  }

  protected boolean checkAccess(AccessRights accessRight) {
    if (getDataModel() instanceof IUserProject) {
      return ((IUserProject) getDataModel()).getUserSystem().checkAccess(accessRight);
    }
    return true;
  }
}
