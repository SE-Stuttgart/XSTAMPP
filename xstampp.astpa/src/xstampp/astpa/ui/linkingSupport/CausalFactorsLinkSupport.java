/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.ui.linkingSupport;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkingType;

public class CausalFactorsLinkSupport extends LinkSupport<DataModelController> {

  public CausalFactorsLinkSupport(DataModelController dataInterface) {
    super(dataInterface, LinkingType.CausalEntryLink_SC2_LINK, true);
    setWithoutWidget(true);
  }

  @Override
  protected List<ITableModel> getModels() {
    return getDataInterface().getCausalFactorController().getCausalFactors();
  }

  @Override
  public String getTitle() {
    return "Causal Factor Links";
  }

  @Override
  public String getText(UUID id) {
    String result;
    try {
      UUID UcaCfLink = getDataInterface().getLinkController()
          .getLinkObjectFor(LinkingType.UcaCfLink_Component_LINK, id).getLinkA();
      UUID cfLink = getDataInterface().getLinkController()
          .getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK, UcaCfLink).getLinkB();
      ITableModel causalFactor = getDataInterface().getCausalFactorController()
          .getCausalFactor(cfLink);
      result = ((ITableModel) causalFactor).getIdString();
    } catch (Exception e) {
      result = "-";
    }
    return result;
  }
}
