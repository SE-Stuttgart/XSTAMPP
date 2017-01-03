/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.model.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorController;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.sds.interfaces.ISafetyConstraint;
import xstampp.model.AbstractLtlProvider;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IDataModel;
import xstampp.model.IEntryFilter;

/**
 * Interface to the Data Model for the Causal Factors View
 * 
 * @author Fabian Toth, Benedikt Markt
 * 
 */
public interface ICausalFactorDataModel extends IDataModel,ICausalFactorController {

	/**
	 * Get all causal components {@link ICausalComponent}
	 * 
	 * @param a filter object which can also be null
	 *         if given as not null all entries in the returned list
	 *         are checked true
	 * @author Fabian Toth
	 * 
	 * @return all causal components as causalFactor containers
	 */
	List<ICausalComponent> getCausalComponents(IEntryFilter<IRectangleComponent> filter);

	/**
	 * 
	 * @param compId1 The id of a control structure component stored in the dataModel
	 * @return a causal component {@link ICausalComponent} for the control structure component
	 *         or null if the type of the component can not be causal
	 */
	ICausalComponent getCausalComponent(UUID compId1);
	
  /**
   * Adds a causal factor to the causal component with the given id. <br>
   * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
   * 
   * @author Fabian Toth, Lukas Balzer
   * @param id the id of the component for which a new factor should be added
   * @return the id of the new causal factor. null if the action fails
   */
  UUID addCausalFactor(UUID id);
  
  /**
  * Gets the list of all corresponding safety constraints
  * 
  * @author Fabian Toth
  * 
  * @return the list of all corresponding safety constraints
  */
  List<ISafetyConstraint> getCorrespondingSafetyConstraints();

  /**
   * {@link IHazardViewDataModel#getHazards(UUID[])}
   */
  List<ITableModel> getHazards(List<UUID> list);

  /**
   * {@link IExtendedDataModel#getAllRefinedRules(IEntryFilter)}
   */
  List<AbstractLtlProvider> getAllRefinedRules(IEntryFilter<AbstractLtlProvider> filter);
  
  /**
   * {@link IExtendedDataModel#addRuleEntry(IExtendedDataModel.RuleType, AbstractLtlProviderData, UUID, String)}
   */
  UUID addRuleEntry(IExtendedDataModel.RuleType ruleType,AbstractLtlProviderData data,UUID caID, String type);
  
  /**
   * {@link IUnsafeControlActionDataModel#getUCAList(IEntryFilter)} 
   */
  List<ICorrespondingUnsafeControlAction> getUCAList(IEntryFilter<IUnsafeControlAction> filter);
}
