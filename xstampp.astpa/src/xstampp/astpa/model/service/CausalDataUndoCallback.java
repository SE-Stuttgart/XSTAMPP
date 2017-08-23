/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class CausalDataUndoCallback implements IUndoCallback {

  private UUID componentId;
  private UUID causalFactorId;
  private CausalFactorEntryData oldData;
  private CausalFactorEntryData newData;
  private ICausalFactorDataModel dataModel;

  public CausalDataUndoCallback(ICausalFactorDataModel dataModel,
      UUID componentId,
      UUID causalFactorId,
      CausalFactorEntryData oldData,
      CausalFactorEntryData newData) {
    this.dataModel = dataModel;
    this.componentId = componentId;
    this.causalFactorId = causalFactorId;
    this.oldData = oldData;
    this.newData = newData;
  }

  @Override
  public void undo() {
    dataModel.changeCausalEntry(componentId, causalFactorId, oldData);
  }

  @Override
  public void redo() {
    dataModel.changeCausalEntry(componentId, causalFactorId, newData);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.CAUSAL_FACTOR;
  }
}
