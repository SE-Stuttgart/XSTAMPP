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

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorUCAEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.IEntryFilter;
import xstampp.ui.common.contentassist.ITableContentProvider;

/**
 * 
 * @author Benedikt Markt, Lukas Balzer
 * 
 */
public class ContentProviderScenarios implements ITableContentProvider<AbstractLTLProvider> {

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
	public ContentProviderScenarios(final ICausalFactorDataModel caInterface,UUID componentId,UUID factorId, ICausalFactorEntry entry) {
		this.caInterface = caInterface;
    this.componentId = componentId;
    this.factorId = factorId;
    this.entry = entry;
    this.ucaId = entry.getUcaLink();
	}

	@Override
	public List<AbstractLTLProvider> getAllItems() {
		return this.caInterface.getAllRefinedRules(new IEntryFilter<AbstractLTLProvider>() {
      
      @Override
      public boolean check(AbstractLTLProvider model) {
        if(model.getUCALinks() == null){
          return false;
        }
        return model.getUCALinks().contains(ucaId);
      }
    });
	}

	@Override
	public List<AbstractLTLProvider> getLinkedItems(final UUID itemId) {
	  List<AbstractLTLProvider> linkedScenarios = new ArrayList<>();
	  if(entry.getScenarioLinks() != null){
  	  for(UUID scenarios : entry.getScenarioLinks()){
  	    linkedScenarios.add(caInterface.getRefinedScenario(scenarios));
  	  }
	  }
		return linkedScenarios;
	}

	@Override
	public void addLink(final UUID item1, final UUID item2) {
	  CausalFactorUCAEntryData data = new CausalFactorUCAEntryData(entry.getId());
	  List<UUID> ids = new ArrayList<>();
	  if(entry.getScenarioLinks() != null){
	    ids.addAll(entry.getScenarioLinks());
	  }
	  ids.add(item2);
	  data.setScenarioLinks(ids);
		this.caInterface.changeCausalEntry(componentId,factorId, data);
	}

	@Override
	public void removeLink(final UUID item, final UUID removeItem) {
	  CausalFactorUCAEntryData data = new CausalFactorUCAEntryData(entry.getId());
	  List<UUID> ids = new ArrayList<>();
    if(entry.getScenarioLinks() != null){
      ids.addAll(entry.getScenarioLinks());
    }
    ids.remove(removeItem);
    data.setScenarioLinks(ids);
    this.caInterface.changeCausalEntry(componentId,factorId, data);
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
