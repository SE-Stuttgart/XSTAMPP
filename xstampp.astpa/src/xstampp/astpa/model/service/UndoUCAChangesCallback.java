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
import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoUCAChangesCallback implements IUndoCallback {

  private IControlActionController dataModel;
  private String newDescription;
  private String oldDescription;
  private boolean descriptionDirty;
  private UUID ucaID;

  public UndoUCAChangesCallback(IControlActionController dataModel, UUID ucaID) {
    this.dataModel = dataModel;
    this.ucaID = ucaID;
    this.descriptionDirty = false;
  }

  public void setDescriptionChange(String oldDescription, String newDescription) {
    this.oldDescription = oldDescription;
    this.newDescription = newDescription;
    descriptionDirty = true;
  }

  @Override
  public void undo() {
    if (descriptionDirty) {
      this.dataModel.setUcaDescription(ucaID, oldDescription);
    }
  }

  @Override
  public void redo() {
    if (descriptionDirty) {
      this.dataModel.setUcaDescription(ucaID, newDescription);
    }
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.UNSAFE_CONTROL_ACTION;
  }

  @Override
  public String getChangeMessage() {
    return "Change description of Unsafe Control Action";
  }

}
