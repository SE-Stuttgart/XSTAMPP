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

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;

public class CellEditorCausalEntry extends GridCellTextEditor {

  private UUID componentId;
  private UUID factorId;
  private ICausalFactorDataModel dataInterface;
  private UUID entryId;
  
  public CellEditorCausalEntry(GridWrapper gridWrapper,ICausalFactorDataModel dataInterface,
      String initialText, UUID componentId,UUID factorId,UUID entryId) {
    super(gridWrapper, initialText,factorId);
    setReadOnly(true);
    setShowDelete(true);
    this.dataInterface = dataInterface;
    this.componentId = componentId;
    this.factorId = factorId;
    this.entryId = entryId;
  }

  @Override
  public void updateDataModel(String newText) {
    dataInterface.setCausalFactorText(componentId, factorId, newText);
    
  }
  @Override
  protected void editorOpening() {
    dataInterface.lockUpdate();
  }
  
  @Override
  protected void editorClosing() {
    dataInterface.releaseLockAndUpdate(new ObserverValue[]{ObserverValue.CAUSAL_FACTOR});
  }
  @Override
  public void delete() {
    dataInterface.removeCausalEntry(componentId, factorId, entryId);
  }
  
}
