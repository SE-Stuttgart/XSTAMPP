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
import xstampp.astpa.model.linking.UndoRemoveLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoRemoveSC extends UndoRemoveLinkedComponent {

  private ITableModel model;
  private SDSController controller;

  public UndoRemoveSC(SDSController controller, ITableModel model,
      LinkController linkController) {
    super(linkController, model.getId(), 2);
    this.controller = controller;
    this.model = model;
  }

  @Override
  public void undo() {
    super.undo();
    this.controller.addSafetyConstraint(this.model);
  }

  @Override
  public void redo() {
    this.controller.removeSafetyConstraint(this.model.getId());
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.SAFETY_CONSTRAINT;
  }

  @Override
  public String getChangeMessage() {
    return "Remove Safety Constraint";
  }

}
