/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui.causalfactors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.IEntryFilter;
import xstampp.model.ObserverValue;
import xstampp.ui.common.contentassist.ITableContentProvider;

/**
 * 
 * @author Benedikt Markt, Lukas Balzer
 * 
 */
public class ContentProviderScenarios implements ITableContentProvider<AbstractLTLProvider> {

  private final transient ICausalFactorDataModel dataModel;
  private UUID ucaId;
  private Link causalEntryLink;


  /**
   * 
   * @param dataModel
   * @param causalEntryLink
   *          a Link of type {@link ObserverValue#UcaCfLink_Component_LINK}
   * @param uca
   */
  public ContentProviderScenarios(ICausalFactorDataModel dataModel, Link causalEntryLink, IUnsafeControlAction uca) {
    this.dataModel = dataModel;
    this.causalEntryLink = causalEntryLink;
    this.ucaId = uca.getId();
  }

  @Override
  public List<AbstractLTLProvider> getAllItems() {
    return this.dataModel.getExtendedDataController().getAllRefinedRules(new IEntryFilter<AbstractLTLProvider>() {

      @Override
      public boolean check(AbstractLTLProvider model) {
        if (model.getUCALinks() == null) {
          return false;
        }
        return model.getUCALinks().contains(ucaId);
      }
    });
  }

  @Override
  public List<AbstractLTLProvider> getLinkedItems(final UUID itemId) {
    List<AbstractLTLProvider> linkedScenarios= new ArrayList<>();
    for (UUID uuid : this.dataModel.getLinkController().getLinksFor(LinkingType.CausalEntryLink_Scenario_LINK, causalEntryLink.getId())) {
      linkedScenarios.add(this.dataModel.getExtendedDataController().getRefinedScenario(uuid));
    }
    return linkedScenarios;
  }

  @Override
  public void addLink(final UUID item1, final UUID item2) {
    this.dataModel.getLinkController().addLink(LinkingType.CausalEntryLink_Scenario_LINK, causalEntryLink.getId(), item2);
  }

  @Override
  public void removeLink(final UUID item, final UUID removeItem) {

    this.dataModel.getLinkController().deleteLink(LinkingType.CausalEntryLink_Scenario_LINK, causalEntryLink.getId(), removeItem);
  }

  @Override
  public String getPrefix() {
    return "SC-";
  }

  @Override
  public String getEmptyMessage() {
    return "";
  }

}
