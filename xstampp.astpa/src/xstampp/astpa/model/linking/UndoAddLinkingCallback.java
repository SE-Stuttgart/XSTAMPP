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

import java.util.ArrayList;
import java.util.List;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoAddLinkingCallback implements IUndoCallback {

  private List<Link> links;
  private ObserverValue linkType;
  private LinkController linkController;

  public UndoAddLinkingCallback(LinkController linkController, ObserverValue linkType,
      List<Link> links) {
    this.linkController = linkController;
    this.linkType = linkType;
    this.links = links;
  }

  public UndoAddLinkingCallback(LinkController linkController, ObserverValue linkType, Link link) {
    this(linkController, linkType, new ArrayList<Link>());
    this.links.add(link);
  }

  @Override
  public void undo() {
    this.linkController.deleteLinks(linkType, links);
  }

  @Override
  public void redo() {
    this.linkController.addLinks(linkType, links);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return linkType;
  }

}
