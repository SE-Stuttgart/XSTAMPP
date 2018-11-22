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

import java.util.UUID;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public abstract class UndoAddLinkedComponent implements IUndoCallback {

  private LinkController linkController;

  public UndoAddLinkedComponent(LinkController linkController, UUID componentId, int linkDepth) {
    this.linkController = linkController;
  }

  @Override
  public void redo() {
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.CAUSAL_FACTOR;
  }

  public LinkController getLinkController() {
    return linkController;
  }
}
