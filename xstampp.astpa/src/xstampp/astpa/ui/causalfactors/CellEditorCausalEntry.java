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
package xstampp.astpa.ui.causalfactors;

import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;

import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;

public class CellEditorCausalEntry extends GridCellTextEditor {

  private ICausalFactorDataModel dataInterface;
  private UUID entryId;

  /**
   * 
   * @param gridWrapper
   * @param dataInterface
   * @param uca
   * @param entryId the id of a Link of type {@link ObserverValue#UcaCfLink_Component_LINK}
   */
  public CellEditorCausalEntry(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      IUnsafeControlAction uca, UUID entryId) {
    super(gridWrapper, getUcaText(uca), entryId);
    setReadOnly(true);
    setShowDelete(true);
    this.dataInterface = dataInterface;
    this.entryId = entryId;
  }

  private static String getUcaText(IUnsafeControlAction uca) {
    // add the uca id + description in a read only cell with an delete button
    return uca.getTitle() + "\n"
        + uca.getDescription();
  }

  @Override
  public void updateDataModel(String newText) {
    // Cannot be modified
  }

  @Override
  protected void editorOpening() {
    dataInterface.lockUpdate();
  }

  @Override
  protected void editorClosing() {
    dataInterface.releaseLockAndUpdate(new ObserverValue[] {});
  }

  @Override
  public void delete() {
    if (MessageDialog.openConfirm(null, "Delete Unsafe Control Action entry?",
        "Do you really want to delete this Unsafe Control Action entry?\n"
            + "Note that this will delete the UCA entry and all stored scenarios.\n"
            + "\nThe Unsafe Control Action itself however will not be deleted")) {
      this.dataInterface.getLinkController().deleteLink(ObserverValue.UcaCfLink_Component_LINK, entryId);
    }
  }

}
