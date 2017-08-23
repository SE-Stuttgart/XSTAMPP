/*************************************************************************
 * Copyright (c) 2014-2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************/

package xstampp.ui.common.grid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

import messages.Messages;
import xstampp.ui.common.ProjectManager;

public abstract class DeleteGridEntryAction<M> extends Action {
  private GridWrapper grid;
  private M dataModel;
  private final String entryType;
  private String prefix;

  /**
   * This constructor constructs a <i>Delete</i> Action
   * and registers a KeyListener in the underlying editor that
   * enables executing the action by pressing the <code>Delete</code> Button
   * on the Keyboard.
   * The Action can delete selected entries in a nebula grid editor.
   * 
   * @param grid
   *          a GridWrapper instance
   * @param dataModel
   *          the dataModel of the type <i>M</i>
   * @param entryType
   *          the type of entry that is handled by the individual implementation
   * @param prefix
   *          the prefix used for creating an id string {@link #getId()}
   */
  public DeleteGridEntryAction(GridWrapper grid, M dataModel, String entryType, String prefix) {
    super(String.format(Messages.DeleteMask, new String()));
    this.entryType = entryType;
    this.grid = grid;
    this.setPrefix(prefix);
    setDataModel(dataModel);

    grid.getGrid().addKeyListener(new KeyAdapter() {

      @Override
      public void keyPressed(KeyEvent event) {
        if ((event.keyCode == SWT.DEL)
            || ((event.stateMask == SWT.COMMAND) && (event.keyCode == SWT.BS))) {
          run();
        }
      }
    });
  }

  /**
   * this implementation of the run method searches for
   * all selected cells in the grid and triggers a deletion of all
   * entries represented by the UUID's stored in the cells
   * 
   */
  @Override
  public void run() {

    Map<UUID, String> deleteList = new HashMap<>();
    List<IGridCell> selected = getGrid().getSelectedCellList();
    String csIDs = new String();
    boolean preformDelete = false;
    for (int i = 0; i < selected.size(); i++) {
      IGridCell cell = selected.get(i);

      if (cell != null && !(cell instanceof GridCellBlank)) {
        IGridCell editor = ((IGridCell) cell);
        UUID ucaID = editor.getUUID();
        if (!deleteList.containsKey(ucaID)) {
          String idString = getIdString(ucaID);
          if (idString != null) {
            csIDs = csIDs + "\n" + idString;
            deleteList.put(ucaID, idString);
            preformDelete = true;
          }
        }
      }
    }
    if (preformDelete && MessageDialog.openConfirm(getGrid().getGrid().getShell(),
        Messages.RemoveAll, String.format(Messages.DeleteQuestionMask, entryType) + csIDs)) {

      for (Entry<UUID, String> id : deleteList.entrySet()) {
        removeEntry(id.getKey());
        ProjectManager.getLOGGER().debug("Delete" + entryType + ":" + id.getValue());
      }
    }

  }

  /**
   * returns a human readable id string of the entry
   * with which the entry can be uniquely identified.
   * 
   * @param id
   *          the UUID of the entry with which the
   *          corresponding data is stored in the data model
   * @return a human readable id string of the entry
   */
  protected abstract String getIdString(UUID id);

  /**
   * calls the function which deletes the entry from the dataModel
   * 
   * @param id
   *          the UUID of the entry with which the
   *          corresponding data is stored in the data model
   */
  protected abstract void removeEntry(UUID id);

  private void setDataModel(M dataModel) {
    this.dataModel = dataModel;
  }

  public M getDataModel() {
    return dataModel;
  }

  @Override
  public boolean isEnabled() {

    if (grid.getGrid().getCellSelectionCount() <= 0) {
      return false;
    }
    List<IGridCell> selected = grid.getSelectedCellList();

    for (int i = 0; i < selected.size(); i++) {
      IGridCell cell = selected.get(i);
      if (!(cell instanceof GridCellBlank) && cell.getUUID() != null) {
        return true;
      }
    }
    return false;
  }

  public GridWrapper getGrid() {
    return grid;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }
}
