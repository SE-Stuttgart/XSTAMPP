/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.ui.causalfactors;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;

public class CellEditorCausalScenarioConstraint extends GridCellTextEditor {

  private UUID ruleId;
  private ICausalFactorDataModel dataInterface;

  public CellEditorCausalScenarioConstraint(GridWrapper gridWrapper,
      ICausalFactorDataModel dataInterface,
      UUID ruleId, ScenarioType type) {
    super(gridWrapper, dataInterface.getRefinedScenario(ruleId).getRefinedSafetyConstraint(),
        ruleId);
    if (type != ScenarioType.CAUSAL_SCENARIO) {
      setReadOnly(true);
    }
    this.dataInterface = dataInterface;
    this.ruleId = ruleId;
  }

  @Override
  public void updateDataModel(String newText) {
    AbstractLtlProviderData data = new AbstractLtlProviderData();
    data.setRefinedConstraint(newText);
    dataInterface.updateRefinedRule(ruleId, data, null);

  }

  @Override
  public void delete() {
    // not used
  }

  @Override
  protected void editorOpening() {
    dataInterface.lockUpdate();
  }

  @Override
  protected void editorClosing() {
    dataInterface.releaseLockAndUpdate(new ObserverValue[] { ObserverValue.Extended_DATA });
  }
}
