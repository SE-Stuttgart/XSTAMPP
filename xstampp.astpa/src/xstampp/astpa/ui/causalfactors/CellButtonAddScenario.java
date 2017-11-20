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

import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IValueCombie;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.CellButton;
import xstampp.ui.common.grid.GridWrapper;

public class CellButtonAddScenario extends CellButton {

  private ICausalFactorDataModel dataModel;
  private Link causalEntryLink;

  /**
   * 
   * @param dataModel
   * @param causalEntryLink
   *          a Link of type {@link ObserverValue#UcaCfLink_Component_LINK}
   * @param uca
   */
  public CellButtonAddScenario(ICausalFactorDataModel dataModel, Link causalEntryLink, IUnsafeControlAction uca) {
    super(new Rectangle(-1, -1,
        GridWrapper.getAddButton16().getBounds().width,
        GridWrapper.getAddButton16().getBounds().height),
        GridWrapper.getAddButton16());
    this.causalEntryLink = causalEntryLink;
    this.dataModel = dataModel;
  }

  @Override
  public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
    AbstractLtlProviderData data = new AbstractLtlProviderData();
    List<UUID> ids = new ArrayList<>();
    Link link = this.dataModel.getLinkController().getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
        this.causalEntryLink.getLinkA());
    ids.add(link.getLinkA());
    data.setRelatedUcas(ids);
    UUID id = dataModel.getExtendedDataController().addRuleEntry(ScenarioType.CAUSAL_SCENARIO, data, 
        IValueCombie.TYPE_ANYTIME, this.dataModel.getLinkController());
    if (id != null) {
      this.dataModel.getLinkController().addLink(LinkingType.CausalEntryLink_Scenario_LINK, this.causalEntryLink.getId(), id);
    }

  }

  @Override
  public String setToolTip(Point point) {
    return "Import the text of an existing, not empty Safety Constraint";
  }
}
