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
package xstampp.astpa.model.extendedData.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IEntryFilter;
import xstampp.model.IValueCombie;

public interface IExtendedDataController {

  /**
   * adds a rule of the given ruleType and type
   * 
   * @param ruleType
   * @param data
   *          {@link AbstractLtlProviderData}
   * @param caID
   *          {@link AbstractLTLProvider#getRelatedControlActionID()}
   * @param type
   *          {@link AbstractLTLProvider#getType()}
   * @param linkController
   *          TODO
   * 
   * @see IValueCombie
   * @return the id of rule which has been added, or null if any of the parameters is given as null
   *         or
   *         the given type String is none of the defined
   *         <ul>
   *         <li>{@link IValueCombie#TYPE_ANYTIME}
   *         <li>{@link IValueCombie#TYPE_NOT_PROVIDED}
   *         <li>{@link IValueCombie#TYPE_TOO_EARLY}
   *         <li>{@link IValueCombie#TYPE_TOO_LATE}
   */
  UUID addRuleEntry(IExtendedDataModel.ScenarioType ruleType, AbstractLtlProviderData data,
      UUID caID, String type, LinkController linkController);

  /**
   * 
   * @param data
   *          {@link AbstractLtlProviderData}
   * @param ruleId
   *          {@link AbstractLTLProvider#getRuleId()}
   *
   * @param linkedControlActionID
   *          {@link AbstractLTLProvider#getRelatedControlActionID()}
   * @return
   *         The UUID of the Refined rule which was updated in the data model or null if no rule
   *         could be found
   * 
   * @see IUnsafeControlAction
   * @see IControlAction
   * @see IValueCombie
   */
  boolean updateRefinedRule(UUID ruleId, AbstractLtlProviderData data, UUID linkedControlActionID);

  /**
   * returns a list which contains all scenario objects of the type
   * Basic, causal and/or only ltl depending on the given booleans
   * 
   * @param includeBasic
   *          include all Basic Scenarios, Basic Scenarios are the ones automatically created in the
   *          XSTPA plugin which is optional so this maybe results in an empty list
   * @param includeCausal
   *          include all scenarios created in the causal factors step
   * @param includeLTL
   *          include those scenarios that were created in the ltl table and contain only
   *          an ltl property
   * @return a list containing the chosen scenarios or an empty list if all
   *         values are false or the respective lists are empty
   */
  List<AbstractLTLProvider> getAllScenarios(boolean includeBasic, boolean includeCausal,
      boolean includeLTL);

  List<AbstractLTLProvider> getAllRefinedRules(IEntryFilter<AbstractLTLProvider> filter);

  /**
   * @param removeAll
   *          whether all currently stored RefinedSafetyRule objects should be deleted<br>
   *          when this is true than the ruleId will be ignored
   * @param ruleId
   *          an id of a RefinedSafetyRule object stored in a controlAction
   * @param linkController
   *          TODO
   * @return whether the delete was successful or not, also returns false if the rule could not be
   *         found or the
   *         id was illegal
   */
  boolean removeRefinedSafetyRule(ScenarioType type, boolean removeAll, UUID ruleId, LinkController linkController);

  AbstractLTLProvider getRefinedScenario(UUID randomUUID);

  /**
   * this calculates the type of rule of the ltl provider stored for that
   * id
   * 
   * @param ruleId
   *          a valid rule id
   * @return the {@link ScenarioType} of the rule or {@link ScenarioType#NO_SCENARIO} if the id is
   *         invalid
   */
  ScenarioType getScenarioType(UUID ruleId);

}