/*******************************************************************************
 * Copyright (c) 2013-2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorUCAEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.model.AbstractLtlProvider;
import xstampp.model.IEntryFilter;
import xstampp.ui.common.contentassist.ITableContentProvider;

/**
 * 
 * @author Benedikt Markt, Lukas Balzer
 * 
 */
public class ScenarioLinkProvider implements ITableContentProvider<AbstractLtlProvider> {

  private static final String HAZARD_ID_PREFIX = "SC-"; //$NON-NLS-1$
	private final transient ICausalFactorDataModel caInterface;
  private ICausalFactorEntry entry;
  private UUID componentId;
  private UUID factorId;
  private UUID ucaId;

	/**
	 * 
	 * @author Benedikt Markt
	 * 
	 * @param caInterface
	 *            The data model interface
	 * 
	 */
	public ScenarioLinkProvider(final ICausalFactorDataModel caInterface,UUID componentId,UUID factorId, ICausalFactorEntry entry) {
		this.caInterface = caInterface;
    this.componentId = componentId;
    this.factorId = factorId;
    this.entry = entry;
    this.ucaId = entry.getUcaLink();
	}

	@Override
	public List<AbstractLtlProvider> getAllItems() {
		return this.caInterface.getAllRefinedRules(new IEntryFilter<AbstractLtlProvider>() {
      
      @Override
      public boolean check(AbstractLtlProvider model) {
        return model.getUCALinks().contains(ucaId);
      }
    });
	}

	@Override
	public List<AbstractLtlProvider> getLinkedItems(final UUID itemId) {
	  List<AbstractLtlProvider> linkedScenarios = new ArrayList<>();
	  for(UUID scenarios : entry.getScenarioLinks()){
	    linkedScenarios.add(caInterface.getRefinedRule(scenarios));
	  }
		return linkedScenarios;
	}

	@Override
	public void addLink(final UUID item1, final UUID item2) {
	  CausalFactorUCAEntryData data = new CausalFactorUCAEntryData(entry.getId());
	  List<UUID> ids = new ArrayList<>(entry.getScenarioLinks());
	  ids.add(item2);
	  data.setScenarioLinks(ids);
		this.caInterface.changeCausalEntry(componentId,factorId, data);
	}

	@Override
	public void removeLink(final UUID item, final UUID removeItem) {
	  CausalFactorUCAEntryData data = new CausalFactorUCAEntryData(entry.getId());
    List<UUID> ids = new ArrayList<>(entry.getScenarioLinks());
    ids.remove(removeItem);
    data.setScenarioLinks(ids);
    this.caInterface.changeCausalEntry(componentId,factorId, data);
	}

  @Override
  public String getPrefix() {
    return "SC-";
  }

}
