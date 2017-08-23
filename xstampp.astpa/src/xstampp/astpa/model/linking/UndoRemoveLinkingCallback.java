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

import java.util.List;

import xstampp.model.ObserverValue;

public class UndoRemoveLinkingCallback extends UndoAddLinkingCallback {

  public UndoRemoveLinkingCallback(LinkController linkController, ObserverValue linkType,
      List<Link> links) {
    super(linkController, linkType, links);
  }

  public UndoRemoveLinkingCallback(LinkController linkController, ObserverValue linkType,
      Link link) {
    super(linkController, linkType, link);
  }

  @Override
  public void undo() {
    super.redo();
  }

  @Override
  public void redo() {
    super.undo();
  }

}
