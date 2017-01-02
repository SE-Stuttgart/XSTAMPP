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
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.ui.common.contentassist.ITableContentProvider;

/**
 * 
 * @author Benedikt Markt, Lukas Balzer
 * 
 */
public class CausalContentProvider implements ITableContentProvider<ITableModel> {

  private static final String HAZARD_ID_PREFIX = "H-"; //$NON-NLS-1$
	private final transient ICausalFactorDataModel caInterface;

	/**
	 * 
	 * @author Benedikt Markt
	 * 
	 * @param caInterface
	 *            The data model interface
	 * 
	 */
	public CausalContentProvider(final ICausalFactorDataModel caInterface) {
		this.caInterface = caInterface;
	}

	@Override
	public List<ITableModel> getAllItems() {
		return this.caInterface.getAllHazards();
	}

	@Override
	public List<ITableModel> getLinkedItems(final UUID itemId) {
		return this.caInterface.getLinkedHazardsOfCf(itemId);
	}

	@Override
	public void addLink(final UUID item1, final UUID item2) {
		this.caInterface.addCausalFactorHazardLink(item1, item2);

	}

	@Override
	public void removeLink(final UUID item, final UUID removeItem) {
		this.caInterface.removeCausalFactorHazardLink(item, removeItem);

	}

  @Override
  public String getPrefix() {
    return HAZARD_ID_PREFIX;
  }

}
