/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.ui.causalfactors;

import java.util.UUID;

import org.eclipse.swt.graphics.GC;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

public class CellEditorSafetyConstraint extends GridCellTextEditor {

  private UUID componentId;
  private UUID factorId;
  private UUID entryId;
  private ICausalFactorDataModel causalDataInterface;
  private ICausalFactorEntry entry;

  public CellEditorSafetyConstraint(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      UUID componentId, UUID factorId, ICausalFactorEntry entry) {
    super(gridWrapper, dataInterface.getCausalFactorController().getConstraintTextFor(entry.getConstraintId()), factorId);
    this.entry = entry;
    setShowDelete(true);
    this.causalDataInterface = dataInterface;
    this.componentId = componentId;
    this.factorId = factorId;
    this.entryId = entry.getId();
  }

  @Override
  public String getCurrentText() {
    return causalDataInterface.getCausalFactorController().getConstraintTextFor(entry.getConstraintId());
  }
  
  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    clearCellButtons();
    if (entry.getConstraintId() == null) {
      addCellButton(new NewConstraintButton(componentId, factorId, entry.getId(), causalDataInterface));
      addCellButton(new CellButtonImportConstraint(getGridWrapper().getGrid(), entry, componentId,
          factorId, causalDataInterface));
      paintFrame(renderer, gc, item);
    } else {
      super.paint(renderer, gc, item);
    }
  }

  @Override
  public void updateDataModel(String newText) {
    CausalFactorEntryData data = new CausalFactorEntryData(entryId);
    data.setConstraint(newText);
    causalDataInterface.changeCausalEntry(componentId, factorId, data);

  }

  @Override
  public void delete() {
    CausalFactorEntryData data = new CausalFactorEntryData(entryId);
    data.setConstraint(null);
    causalDataInterface.lockUpdate();
    causalDataInterface.changeCausalEntry(componentId, factorId, data);
    causalDataInterface.releaseLockAndUpdate(null);
  }

  @Override
  protected void editorOpening() {
    causalDataInterface.lockUpdate();
  }

  @Override
  protected void editorClosing() {
    causalDataInterface.releaseLockAndUpdate(new ObserverValue[] {});
  }

}
