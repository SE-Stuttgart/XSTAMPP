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
package xstampp.astpa.model.sds;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoAddLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoAddDR extends UndoAddLinkedComponent {

  private ITableModel model;
  private SDSController controller;
  private ObserverValue type;

  public UndoAddDR(SDSController controller, ITableModel model,
      LinkController linkController, ObserverValue type) {
    super(linkController, model.getId(), 2);
    this.controller = controller;
    this.model = model;
    this.type = type;
  }

  @Override
  public void undo() {
    this.controller.removeDesignRequirement(this.model.getId(), this.type);
  }

  @Override
  public void redo() {
    super.redo();
    this.controller.addDesignRequirement(this.model, this.type);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return this.type;
  }

  @Override
  public String getChangeMessage() {
    return "Add a " + this.type;
  }
}
