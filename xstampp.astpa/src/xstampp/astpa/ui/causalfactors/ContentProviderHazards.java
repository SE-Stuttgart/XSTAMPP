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

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.ui.common.contentassist.ITableContentProvider;

/**
 * 
 * @author Benedikt Markt, Lukas Balzer
 * 
 */
public class ContentProviderHazards implements ITableContentProvider<ITableModel> {

  private static final String HAZARD_ID_PREFIX = "H-"; //$NON-NLS-1$
	private final transient ICausalFactorDataModel caInterface;
  private ICausalFactorEntry entry;
  private UUID componentId;
  private UUID factorId;

	/**
	 * 
	 * @author Benedikt Markt
	 * 
	 * @param caInterface
	 *            The data model interface
	 * 
	 */
	public ContentProviderHazards(final ICausalFactorDataModel caInterface,UUID componentId,UUID factorId, ICausalFactorEntry entry) {
		this.caInterface = caInterface;
    this.componentId = componentId;
    this.factorId = factorId;
    this.entry = entry;
	}

	@Override
	public List<ITableModel> getAllItems() {
		return this.caInterface.getAllHazards();
	}

	@Override
	public List<ITableModel> getLinkedItems(final UUID itemId) {
		return this.caInterface.getHazards(entry.getHazardIds());
	}

	@Override
	public void addLink(final UUID item1, final UUID item2) {
	  CausalFactorEntryData data = new CausalFactorEntryData(entry.getId());
	  for(UUID id : entry.getHazardIds()){
	    data.addHazardId(id);
	  }
	  data.addHazardId(item2);
		this.caInterface.changeCausalEntry(componentId,factorId, data);
	}

	@Override
	public void removeLink(final UUID item, final UUID removeItem) {
	  CausalFactorEntryData data = new CausalFactorEntryData(entry.getId());
    for(UUID id : entry.getHazardIds()){
      if(!id.equals(removeItem)){
        data.addHazardId(id);
      }
    }
    this.caInterface.changeCausalEntry(componentId,factorId, data);
	}

  @Override
  public String getPrefix() {
    return HAZARD_ID_PREFIX;
  }

}
