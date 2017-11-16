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

import org.eclipse.jface.dialogs.MessageDialog;

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.astpa.model.linking.Link;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;

public class CellEditorCausalScenario extends GridCellTextEditor {

  private UUID ruleId;
  private ICausalFactorDataModel dataInterface;
  private ScenarioType type;
  private Link scenarioLink;

  public CellEditorCausalScenario(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      Link scenarioLink, UUID ruleId, ScenarioType type) {
    super(gridWrapper, dataInterface.getRefinedScenario(ruleId).getSafetyRule(), ruleId);
    this.scenarioLink = scenarioLink;
    this.type = type;
    setReadOnly(type != ScenarioType.CAUSAL_SCENARIO);
    setShowDelete(true);
    this.dataInterface = dataInterface;
    this.ruleId = ruleId;
  }

  @Override
  public void updateDataModel(String newText) {
    AbstractLtlProviderData data = new AbstractLtlProviderData();
    data.setRule(newText);
    dataInterface.updateRefinedRule(ruleId, data, null);

  }

  @Override
  public void delete() {
    if (MessageDialog.openConfirm(null, "Delete Causal Scenario?",
        "Do you really want to delete this Scenario?\n"
            + "Note that all references will be deleted as well")) {
      if (type == ScenarioType.CAUSAL_SCENARIO) {
        dataInterface.removeRefinedSafetyRule(type, false, ruleId);
      }
    this.dataInterface.getLinkController().deleteLink(this.scenarioLink.getLinkType(), this.scenarioLink.getId());
    }
  }

  @Override
  protected void editorOpening() {
    dataInterface.lockUpdate();
  }

  @Override
  protected void editorClosing() {
    dataInterface.releaseLockAndUpdate(new ObserverValue[] { ObserverValue.CAUSAL_FACTOR });
  }
}
