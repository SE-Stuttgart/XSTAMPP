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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorUCAEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IValueCombie;
import xstampp.ui.common.grid.CellButton;
import xstampp.ui.common.grid.GridWrapper;

public class CellButtonAddScenario extends CellButton {

  private ICausalFactorEntry entry = null;
  private UUID componentId;
  private UUID factorId;
  private ICausalFactorDataModel dataModel;

  public CellButtonAddScenario(ICausalFactorEntry entry, UUID componentId, UUID factorId,
      ICausalFactorDataModel dataInterface) {
    super(new Rectangle(-1, -1,
        GridWrapper.getAddButton16().getBounds().width,
        GridWrapper.getAddButton16().getBounds().height),
        GridWrapper.getAddButton16());

    this.entry = entry;
    this.componentId = componentId;
    this.factorId = factorId;
    dataModel = dataInterface;
  }

  @Override
  public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
    if (entry.getUcaLink() != null) {
      AbstractLtlProviderData data = new AbstractLtlProviderData();
      List<UUID> ids = new ArrayList<>();
      ids.add(entry.getUcaLink());
      data.setRelatedUcas(ids);
      UUID id = dataModel.addRuleEntry(ScenarioType.CAUSAL_SCENARIO, data, null,
          IValueCombie.TYPE_ANYTIME);
      if (id != null) {
        CausalFactorUCAEntryData entryData = new CausalFactorUCAEntryData(entry.getId());
        List<UUID> scenarioLinks = new ArrayList<>();
        if (entry.getScenarioLinks() != null) {
          scenarioLinks.addAll(entry.getScenarioLinks());
        }
        scenarioLinks.add(id);
        entryData.setScenarioLinks(scenarioLinks);
        dataModel.changeCausalEntry(componentId, factorId, entryData);
      }
    }

  }

  @Override
  public String setToolTip(Point point) {
    return "Import the text of an existing, not empty Safety Constraint";
  }
}
