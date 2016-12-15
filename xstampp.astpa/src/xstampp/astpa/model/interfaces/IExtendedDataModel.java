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
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.controlaction.NotProvidedValuesCombi;
import xstampp.astpa.model.controlaction.ProvidedValuesCombi;
import xstampp.astpa.model.controlaction.interfaces.IHAZXControlAction;
import xstampp.astpa.model.controlaction.rules.RefinedSafetyRule;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.model.AbstractLtlProvider;
import xstampp.model.IValueCombie;
import xstampp.model.ObserverValue;

public interface IExtendedDataModel {
  
  /**
   * 
   * @param ucaLinks
   *          {@link AbstractLtlProvider#getUCALinks()}
   * @param combies
   *          {@link RefinedSafetyRule#getCriticalCombies()}
   * @param ltlExp
   *          {@link AbstractLtlProvider#getLtlProperty()}   
   * @param rule
   *          {@link AbstractLtlProvider#getSafetyRule()}
   * @param ruca
   *          {@link AbstractLtlProvider#getRefinedUCA()}
   * @param constraint
   *          {@link AbstractLtlProvider#getRefinedSafetyConstraint()}
   * @param nr
   *          {@link AbstractLtlProvider#getNumber()}
   * @param caID
   *          {@link AbstractLtlProvider#getRelatedControlActionID()}
   * @param type 
   *          {@link AbstractLtlProvider#getType()}
   * 
   * @see IValueCombie
   * @return the id of rule which has been added if 
   */
  UUID addRefinedRule(List<UUID> ucaLinkIDs, String criticalCombinations, String ltlProperty, String refinedRule,
      String refinedUCA, String constraint, int number, UUID linkedControlActionID, String type);

  /**
   * 
   * @param ruleId
   *          {@link AbstractLtlProvider#getSafetyRule()}
   * @param ucaLinkIDs
   *          {@link AbstractLtlProvider#getUCALinks()}
   * @param criticalCombinations
   *          {@link RefinedSafetyRule#getCriticalCombies()}
   * @param ltlProperty
   *          {@link AbstractLtlProvider#getLtlProperty()}   
   * @param refinedRule
   *          {@link AbstractLtlProvider#getSafetyRule()}
   * @param refinedUCA
   *          {@link AbstractLtlProvider#getRefinedUCA()}
   * @param constraint
   *          {@link AbstractLtlProvider#getRefinedSafetyConstraint()}
   * @param number
   *          {@link AbstractLtlProvider#getNumber()}
   * @param linkedControlActionID
   *          {@link AbstractLtlProvider#getRelatedControlActionID()}
   * @param type
   *          {@link AbstractLtlProvider#getType()}
   * @return
   *        The UUID of the Refined rule which was updated in the data model or null if no rule could be found
   *         
   * @see IUnsafeControlAction
   * @see IControlAction
   * @see IValueCombie
   */
  UUID updateRefinedRule(UUID ruleId, List<UUID> ucaLinkIDs, String criticalCombinations, String ltlProperty,
      String refinedRule, String refinedUCA, String constraint, int number, UUID linkedControlActionID, String type);
  
  /**
   * 
   * @param ucaLinkID
   *          {@link AbstractLtlProvider#getUCALinks()}
   * @param combies
   *          {@link RefinedSafetyRule#getCriticalCombies()}
   * @param ltlExp
   *          {@link AbstractLtlProvider#getLtlProperty()}   
   * @param rule
   *          {@link AbstractLtlProvider#getSafetyRule()}
   * @param ruca
   *          {@link AbstractLtlProvider#getRefinedUCA()}
   * @param constraint
   *          {@link AbstractLtlProvider#getRefinedSafetyConstraint()}
   * @param nr
   *          {@link AbstractLtlProvider#getNumber()}
   * @param caID
   *          {@link AbstractLtlProvider#getRelatedControlActionID()}
   * @param type 
   *          {@link AbstractLtlProvider#getType()}
   * 
   * @see IValueCombie
   * @return the id of rule which has been added if 
   */
  UUID addNonFormalRule(UUID ucaLinkID, String criticalCombinations, String ltlProperty, String refinedRule,
      String refinedUCA, String constraint, String type);

  /**
   * @param removeAll whether all currently stored RefinedSafetyRule objects should be deleted<br>
   *          when this is true than the ruleId will be ignored
   * @param ruleId an id of a RefinedSafetyRule object stored in a controlAction 
   * 
   * @return whether the delete was successful or not, also returns false if the rule could not be found or the 
   *          id was illegal
   */
  boolean removeRefinedSafetyRule(boolean removeAll, UUID ruleId);

  List<AbstractLtlProvider> getAllRefinedRules(boolean onlyFormal);

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
