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
package xstampp.astpa.model.linking;

import java.util.UUID;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoChangeLinkingCallback implements IUndoCallback {

  private ObserverValue linkType;
  private LinkController linkController;
  private UUID oldA;
  private UUID oldB;
  private UUID newA;
  private UUID newB;
  private UUID linkId;

  /**
   * 
   * @param linkController the {@link LinkController} that contains the linking information
   * @param linkType
   *          one of the LINK constants in {@link ObserverValue}
   * @param linkId
   *          the {@link UUID} of a {@link Link}
   * @param oldA
   * @param oldB
   * @param newA
   * @param newB
   */
  public UndoChangeLinkingCallback(LinkController linkController, ObserverValue linkType,
      UUID linkId, UUID oldA, UUID oldB,UUID newA, UUID newB) {
    this.linkController = linkController;
    this.linkType = linkType;
    this.linkId = linkId;
    this.oldA = oldA;
    this.oldB = oldB;
    this.newA = newA;
    this.newB = newB;
  }

  @Override
  public void undo() {
    this.linkController.changeLink(this.linkType, this.linkId, this.oldA, this.oldB);
  }

  @Override
  public void redo() {
    this.linkController.changeLink(this.linkType, this.linkId, this.newA, this.newB);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return linkType;
  }

}
