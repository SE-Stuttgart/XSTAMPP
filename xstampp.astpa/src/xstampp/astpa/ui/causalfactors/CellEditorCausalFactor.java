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

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;

public class CellEditorCausalFactor extends GridCellTextEditor {

  private UUID factorId;
  private ICausalFactorDataModel dataInterface;

  public CellEditorCausalFactor(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      String initialText, UUID factorId) {
    super(gridWrapper, initialText, factorId);
    setReadOnly(false);
    setShowDelete(true);
    this.dataInterface = dataInterface;
    this.factorId = factorId;
  }

  @Override
  public void updateDataModel(String newText) {
    dataInterface.getCausalFactorController().setCausalFactorText(factorId, newText);

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
    if (MessageDialog.openConfirm(null, "Delete Causal Factor?",
        "Do you really want to delete this Causal Factor\n"
            + "and all its child entries?")) {
      dataInterface.getCausalFactorController().removeCausalFactor(factorId);
    }
  }

}
