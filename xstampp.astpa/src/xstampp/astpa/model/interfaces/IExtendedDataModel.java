/*************************************************************************
 * Copyright (c) 2014-2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************/

package xstampp.astpa.model.interfaces;

import java.util.List;
import java.util.Observer;
import java.util.UUID;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.haz.controlaction.UCAHazLink;
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.controlaction.NotProvidedValuesCombi;
import xstampp.astpa.model.controlaction.ProvidedValuesCombi;
import xstampp.astpa.model.controlaction.interfaces.IHAZXControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.model.AbstractLtlProvider;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IValueCombie;
import xstampp.model.ObserverValue;

public interface IExtendedDataModel {
  
  public static enum RuleType{
    REFINED_RULE,
    SCENARIO,
    CUSTOM_LTL
  }
  /**
   * @param ruleType
   *        
   * @param data
   *          {@link AbstractLtlProviderData}
   * @param caID
   *          {@link AbstractLtlProvider#getRelatedControlActionID()}
   * @param type 
   *          {@link AbstractLtlProvider#getType()}
   * 
   * @see IValueCombie
   * @return the id of rule which has been added if 
   */
  public UUID addRuleEntry(IExtendedDataModel.RuleType ruleType,AbstractLtlProviderData data,UUID caID, String type);
  
  /**
   * 
   * @param data
   *          {@link AbstractLtlProviderData}
   * @param ruleId
   *          {@link AbstractLtlProvider#getRuleId()}
   *
   * @param linkedControlActionID
   *          {@link AbstractLtlProvider#getRelatedControlActionID()}
   * @return
   *        The UUID of the Refined rule which was updated in the data model or null if no rule could be found
   *         
   * @see IUnsafeControlAction
   * @see IControlAction
   * @see IValueCombie
   */
  public boolean updateRefinedRule(UUID ruleId, AbstractLtlProviderData data,UUID linkedControlActionID);
  
  /**
   * @param removeAll whether all currently stored RefinedSafetyRule objects should be deleted<br>
   *          when this is true than the ruleId will be ignored
   * @param ruleId an id of a RefinedSafetyRule object stored in a controlAction 
   * 
   * @return whether the delete was successful or not, also returns false if the rule could not be found or the 
   *          id was illegal
   */
  boolean removeRefinedSafetyRule(RuleType type, boolean removeAll, UUID ruleId);

  /**
   * 
   * @param includeRules
   *          whether or not the list with the generated refined rules from the extended stpa should be included
   * @param includeScenarios
   *          whether or not the list with the custom defined Causal Scenarios should be included
   * @param includeLTL
   *          whether or not the list with the custom defined LTL Functions should be included
   * @return a list containing the rules stored in the chosen lists of the extendedDataModel
   */
  public List<AbstractLtlProvider> getAllRefinedRules(boolean includeRules,
      boolean includeScenarios,
      boolean includeLTL);

  List<AbstractLtlProvider> getLTLPropertys();

  
  IRectangleComponent getComponent(UUID key);

  void setCSComponentComment(UUID id, String value);

  IRectangleComponent getIgnoreLTLValue();

  List<ICausalComponent> getCausalComponents();
  
  void setCASafetyCritical(UUID id, boolean safetyCritical);

  boolean setControlActionDescription(UUID id, String value);


  void addCAProvidedVariable(UUID id, UUID id2);

  void addCANotProvidedVariable(UUID id, UUID id2);

  boolean removeCAProvidedVariable(UUID id, UUID id2);

  boolean removeCANotProvidedVariable(UUID id, UUID id2);

  void setValuesWhenCAProvided(UUID id, List<ProvidedValuesCombi> valuesIfProvided);

  void setValuesWhenCANotProvided(UUID id, List<NotProvidedValuesCombi> valuesIfProvided);

  List<IValueCombie> getIvaluesWhenCAProvided(UUID id);

  List<IValueCombie> getIValuesWhenCANotProvided(UUID id);
  

  int getUCANumber(UUID ucaID);
  List<UCAHazLink> getAllUCALinks();

  List<ITableModel> getLinkedHazardsOfUCA(UUID ucaID);
  List<ICorrespondingUnsafeControlAction> getAllUnsafeControlActions();

  List<IHAZXControlAction> getAllControlActionsU();
  
  List<IControlAction> getAllControlActions();
  
  ITableModel getHazard(UUID hazardId);

  boolean isCASafetyCritical(UUID id);

  void lockUpdate();

  void releaseLockAndUpdate(ObserverValue[] observerValues);

  void deleteObserver(Observer observer);


  void addObserver(Observer observer);


  AbstractLtlProvider getRefinedRule(UUID randomUUID);

}
