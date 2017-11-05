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

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.sds.ISDSController;
import xstampp.model.ObserverValue;

public class UndoDesignReqChangeCallback extends UndoTableModelChangeCallback<ISDSController> {

  private ObserverValue type;

  public UndoDesignReqChangeCallback(ISDSController dataModel, ObserverValue type,
      ITableModel model) {
    super(dataModel, model);
    this.type = type;
  }

  @Override
  protected void undoDescription(String description) {
    getDataModel().setDesignRequirementDescription(type, getEntryId(), description);
  }

  @Override
  protected void undoTitle(String title) {
    getDataModel().setDesignRequirementTitle(type, getEntryId(), title);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return this.type;
  }

}
