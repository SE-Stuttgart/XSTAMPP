/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.linking;

import java.util.List;
import java.util.UUID;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public abstract class UndoRemoveLinkedComponent implements IUndoCallback {

  private LinkController linkController;
  private List<Link> deleteLinksFor;
  private UUID componentId;
  private int linkDepth;

  public UndoRemoveLinkedComponent(LinkController linkController, UUID componentId, int linkDepth) {
    this.componentId = componentId;
    this.linkDepth = linkDepth;
    this.deleteLinksFor = linkController.deleteLinksFor(this.componentId, this.linkDepth);
    this.linkController = linkController;
  }

  @Override
  public void undo() {
    this.linkController.addLinks(this.deleteLinksFor);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.CAUSAL_FACTOR;
  }

  public LinkController getLinkController() {
    return linkController;
  }
}
