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

import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class UndoCSCChangeCallback
    extends UndoTableModelChangeCallback<IControlActionController> {

  public UndoCSCChangeCallback(IControlActionController dataModel,
      ITableModel model) {
    super(dataModel, model);
  }

  public UndoCSCChangeCallback(IControlActionController dataModel, UUID model) {
    super(dataModel, model);
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setCorrespondingSafetyConstraint(getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.UNSAFE_CONTROL_ACTION;
  }

}
