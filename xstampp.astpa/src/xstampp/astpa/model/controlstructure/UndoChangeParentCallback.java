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
package xstampp.astpa.model.controlstructure;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoChangeParentCallback implements IUndoCallback {
  private UUID compId;
  private UUID oldParentId;
  private UUID newParentId;

  private Rectangle oldLayoutStep0;
  private Rectangle oldLayoutStep2;

  private Rectangle newLayoutStep0;
  private Rectangle newLayoutStep2;
  private ControlStructureController controller;

  UndoChangeParentCallback(ControlStructureController controller, UUID componentId,
      UUID oldParentId, UUID newParentId) {
    this.controller = controller;
    this.compId = componentId;
    this.oldParentId = oldParentId;
    this.newParentId = newParentId;
  }

  public void setNewLayout(Rectangle step1, Rectangle step2) {
    this.newLayoutStep0 = step1;
    this.newLayoutStep2 = step2;
  }

  public void setOldLayout(Rectangle step1, Rectangle step2) {
    this.oldLayoutStep0 = step1;
    this.oldLayoutStep2 = step2;
  }

  @Override
  public void undo() {
    controller.changeComponentParent(compId, newParentId, oldParentId, oldLayoutStep0,
        oldLayoutStep2);
  }

  @Override
  public void redo() {
    controller.changeComponentParent(compId, oldParentId, newParentId, newLayoutStep0,
        newLayoutStep2);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.CONTROL_STRUCTURE;
  }

}
