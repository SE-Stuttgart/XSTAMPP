/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.extendedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.model.AbstractLtlProvider;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IValueCombie;

public class ExtendedDataController {
    @XmlElementWrapper(name = "rules")
    @XmlElement(name = "rule")
    private List<AbstractLtlProvider> rules;

    @XmlElementWrapper(name = "scenarios")
    @XmlElement(name = "scenario")
    private List<AbstractLtlProvider> scenarios;
    

    @XmlElementWrapper(name = "customLTLs")
    @XmlElement(name = "customLTL")
    private List<AbstractLtlProvider> customLTLs;
    
    
    private int ruleIndex;
    
    public ExtendedDataController() {
      rules = new ArrayList<>();
      scenarios= new ArrayList<>();
      customLTLs = new ArrayList<>();
      ruleIndex = 0;
    }

    private UUID addRuleEntry(List<AbstractLtlProvider> list,AbstractLtlProviderData data,UUID linkedControlActionID, String type){
      if(data != null){
        if(list == null){
          list= new ArrayList<>();
        }
        RefinedSafetyRule safetyRule = new RefinedSafetyRule(data,linkedControlActionID,type, ruleIndex);
        ruleIndex++;
        if(list.add(safetyRule)){
          return safetyRule.getRuleId();
        }
      }
      return null;
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
    public UUID addRuleEntry(IExtendedDataModel.RuleType ruleType,AbstractLtlProviderData data,UUID caID, String type){
      switch(ruleType){
      case CUSTOM_LTL:
        return addRuleEntry(customLTLs, data, caID, type);
      case REFINED_RULE:
        return addRuleEntry(rules, data, caID, type);
      case SCENARIO:
        return addRuleEntry(scenarios, data, caID, type);
      default:
        return null;
      
      }
      
    }
    /**
     * 
     * @param rule
     * 
     * @see IValueCombie
     * @return
     */
    public boolean addRefinedRule(AbstractLtlProvider rule){
      if(rule != null){
        if(rules == null){
          this.rules= new ArrayList<>();
        }
        if(!this.rules.contains(rule)){
          ruleIndex = Math.max(ruleIndex, rule.getNumber());
          return this.rules.add(rule);
        }
      }
      return false;
    }
    
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
    public boolean updateRefinedRule(UUID ruleId, AbstractLtlProviderData data,UUID linkedControlActionID){
      for(AbstractLtlProvider provider: getAllRefinedRules(true,true,true)){
        if(provider.getRuleId().equals(ruleId)){
          return updateRefinedRule(provider, data, linkedControlActionID);
        }
      }
      return false;
    }
    
    private boolean updateRefinedRule(AbstractLtlProvider provider, AbstractLtlProviderData data,UUID linkedControlActionID){
      boolean changed=false;
      changed = changed ||((RefinedSafetyRule) provider).setLtlProperty(data.getLtlProperty());
      changed = changed ||((RefinedSafetyRule) provider).setRefinedSafetyConstraint(data.getRefinedSafetyConstraint());
      changed = changed ||((RefinedSafetyRule) provider).setRefinedUCA(data.getRefinedUca());
      changed = changed ||((RefinedSafetyRule) provider).setSafetyRule(data.getSafetyRule());
      changed = changed ||((RefinedSafetyRule) provider).setUCALinks(data.getUcaLinks());
      changed = changed ||((RefinedSafetyRule) provider).setCaID(linkedControlActionID);
      changed = changed ||((RefinedSafetyRule) provider).setCriticalCombies(data.getCombies());
      return changed;
     
    }
    public List<AbstractLtlProvider> getAllRefinedRules(boolean includeRules,
                                                        boolean includeScenarios,
                                                        boolean includeLTL){

      List<AbstractLtlProvider> tmp = new ArrayList<>();
      if(rules == null){
        rules = new ArrayList<>();
      }else if(includeRules){
        tmp.addAll(rules);
      }
      if(scenarios == null){
        scenarios = new ArrayList<>();
      }else if(includeScenarios){
        tmp.addAll(scenarios);
      }
      if(customLTLs == null){
        customLTLs = new ArrayList<>();
      }else if(includeLTL){
        tmp.addAll(customLTLs);
      }
      return tmp;
    }
    
    
    private boolean removeEntry(List<AbstractLtlProvider> list, boolean removeAll, UUID id){
      if(removeAll){
        //if removeAll than the rule index is set to 0 so the next rule is added with the index 0
        list = new ArrayList<>();
      }else if(list != null){
        // the rule which should be removed is searched for in both the 
        // general rules list and in the control actions
        for (AbstractLtlProvider refinedSafetyRule : list) {
          if(refinedSafetyRule.getRuleId().equals(id)){
            return list.remove(refinedSafetyRule);
          }
        }
      }
      return false;
    }
    
    /**
     * This method removes a safety rule if it is stored as general rule or as rule in control action
     * 
     * @param removeAll whether all currently stored RefinedSafetyRule objects should be deleted<br>
     *          when this is true than the ruleId will be ignored
     * @param id an id of a RefinedSafetyRule object
     * 
     * @return whether the delete was successful or not, also returns false if the rule could not be found or the 
     *          id was illegal
     */
    public boolean removeSafetyRule(boolean removeAll, UUID id){
      return removeEntry(rules, removeAll, id);
    }
    /**
     * This method removes a scenario 
     * 
     * @param removeAll whether all currently stored RefinedSafetyRule objects should be deleted<br>
     *          when this is true than the id will be ignored
     * @param id an id of a RefinedSafetyRule object
     * 
     * @return whether the delete was successful or not, also returns false if the rule could not be found or the 
     *          id was illegal
     */
    public boolean removeScenario(boolean removeAll, UUID id){
      return removeEntry(scenarios, removeAll, id);
    }
    
    /**
     * This method removes a custom ltl expresion
     * 
     * @param removeAll whether all currently stored RefinedSafetyRule objects should be deleted<br>
     *          when this is true than the ruleId will be ignored
     * @param id an id of a RefinedSafetyRule object
     * 
     * @return whether the delete was successful or not, also returns false if the rule could not be found or the 
     *          id was illegal
     */
    public boolean removeLTL(boolean removeAll, UUID id){
      return removeEntry(customLTLs, removeAll, id);
    }
}
