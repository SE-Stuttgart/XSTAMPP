/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.causalfactor.ICausalController;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorController;
import xstampp.astpa.model.controlaction.UCAHazLink;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IDataModel;
import xstampp.model.IEntryFilter;

/**
 * Interface to the Data Model for the Causal Factors View
 * 
 * @author Fabian Toth, Benedikt Markt
 * 
 */
public interface ICausalFactorDataModel extends IDataModel, ICausalFactorController {

  /**
   * Get all causal components {@link ICausalComponent}
   * 
   * @param a
   *          filter object which can also be null if given as not null all entries in the returned
   *          list are checked true
   * @author Fabian Toth
   * 
   * @return all causal components as causalFactor containers
   */
  List<ICausalComponent> getCausalComponents(IEntryFilter<IRectangleComponent> filter);

  /**
   * {@link IExtendedDataModel#getRefinedScenario(UUID)}
   */
  AbstractLTLProvider getRefinedScenario(UUID randomUUID);

  /**
   * {@link IExtendedDataModel#getScenarioType(UUID)}
   */
  ScenarioType getScenarioType(UUID ruleId);

  /**
   * @return a list of {@link xstampp.astpa.model.controlaction.UCAHazLink}
   */
  List<UCAHazLink> getAllUCALinks();

  /**
   * 
   * @param compId1
   *          The id of a control structure component stored in the dataModel
   * @return a causal component {@link ICausalComponent} for the control structure component or null
   *         if the type of the component can not be causal
   */
  ICausalComponent getCausalComponent(UUID compId1);

  /**
   * Adds a causal factor to the causal component with the given id. <br>
   * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
   * 
   * @author Fabian Toth, Lukas Balzer
   * @param id
   *          the id of the component for which a new factor should be added
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
  List<ITableModel> getCorrespondingSafetyConstraints();

  /**
   * {@link IHazardViewDataModel#getHazards(UUID[])}
   */
  List<ITableModel> getHazards(List<UUID> list);

  /**
   * {@link IHazardViewDataModel#getAllHazards()}
   */
  List<ITableModel> getAllHazards();

  /**
   * {@link IHazardViewDataModel#getHazard(UUID)}
   */
  ITableModel getHazard(UUID id);

  /**
   * {@link IExtendedDataModel#getAllRefinedRules(IEntryFilter)}
   */
  List<AbstractLTLProvider> getAllRefinedRules(IEntryFilter<AbstractLTLProvider> filter);

  /**
   * {@link IExtendedDataModel#addRuleEntry(IExtendedDataModel.ScenarioType, AbstractLtlProviderData, UUID, String)}
   */
  UUID addRuleEntry(IExtendedDataModel.ScenarioType ruleType, AbstractLtlProviderData data,
      UUID caID, String type);

  /**
   * {@link IExtendedDataModel#updateRefinedRule(UUID, AbstractLtlProviderData, UUID)}
   */
  boolean updateRefinedRule(UUID ruleId, AbstractLtlProviderData data, UUID linkedControlActionID);

  /**
   * {@link IExtendedDataModel#updateRefinedRule(UUID, AbstractLtlProviderData, UUID)}
   */
  boolean removeRefinedSafetyRule(ScenarioType type, boolean removeAll, UUID ruleId);

  /**
   * {@link IUnsafeControlActionDataModel#getUCAList(IEntryFilter)}
   */
  List<ICorrespondingUnsafeControlAction> getUCAList(IEntryFilter<IUnsafeControlAction> filter);

  /**
   * {@link IUnsafeControlActionDataModel#getLinksOfUCA(UUID)}
   */
  List<UUID> getLinksOfUCA(UUID unsafeControlActionId);

  /**
   * Returns the the control action as {@link ITableModel} for which the unsafe control action
   * belonging to the given {@link UUID}. If the id is not registered for a uca than <i>null</i> is
   * returned.
   * 
   * @param ucaId
   *          an {@link UUID} for a registered uca in the system
   * @return a {@link ITableModel} for a control action or <i>null</i>
   */
  ITableModel getControlActionForUca(UUID ucaId);

  ICausalController getCausalFactorController();

  LinkController getLinkController();
}
