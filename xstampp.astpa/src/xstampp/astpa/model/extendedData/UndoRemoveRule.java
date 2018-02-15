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
package xstampp.astpa.model.extendedData;

import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.UndoRemoveLinkedComponent;
import xstampp.model.ObserverValue;

public class UndoRemoveRule extends UndoRemoveLinkedComponent {

  private ExtendedDataController causalController;
  private ScenarioType type;
  private RefinedSafetyRule safetyRule;

  public UndoRemoveRule(ExtendedDataController causalController, RefinedSafetyRule safetyRule,  ScenarioType type,
      LinkController linkController) {
    super(linkController, safetyRule.getId(), 2);
    this.causalController = causalController;
    this.safetyRule = safetyRule;
    this.type = type;
  }

  @Override
  public void undo() {
    super.undo();
    this.causalController.addRefinedRule(safetyRule, this.type, getLinkController());
  }

  @Override
  public void redo() {
    this.causalController.removeRefinedSafetyRule(type, false, safetyRule.getId(), getLinkController());
  }

  @Override
  public ObserverValue getChangeConstant() {
    return ObserverValue.Extended_DATA;
  }

  @Override
  public String getChangeMessage() {
    return "Remove " + this.type.name();
  }
}
