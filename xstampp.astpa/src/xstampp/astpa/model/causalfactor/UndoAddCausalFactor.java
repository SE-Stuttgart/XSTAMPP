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
package xstampp.astpa.model.causalfactor;

import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoAddLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoAddCausalFactor extends UndoAddLinkedComponent {

  private CausalFactor factor;
  private CausalFactorController causalController;

  public UndoAddCausalFactor(CausalFactorController causalController, CausalFactor factor,
      LinkController linkController) {
    super(linkController, factor.getId(), 2);
    this.causalController = causalController;
    this.factor = factor;
  }

  @Override
  public void undo() {
    this.causalController.removeCausalFactor(this.factor.getId());
  }

  @Override
  public void redo() {
    super.redo();
    this.causalController.addCausalFactor(this.factor);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.CAUSAL_FACTOR;
  }

  @Override
  public String getChangeMessage() {
    return "Add a Causal Factor";
  }
}
