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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.RuleType;
import xstampp.model.AbstractLtlProvider;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IEntryFilter;
import xstampp.model.IValueCombie;

public class ExtendedDataController implements IExtendedDataController {
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
      ruleIndex = 0;
    }
    
    private boolean validateType(String type){
        switch(type){
          case IValueCombie.TYPE_ANYTIME:
          case IValueCombie.TYPE_NOT_PROVIDED:
          case IValueCombie.TYPE_TOO_EARLY:
          case IValueCombie.TYPE_TOO_LATE:
            return true;
          default: 
            return false;
        }
      
    }
    private UUID addRuleEntry(List<AbstractLtlProvider> list,AbstractLtlProviderData data,UUID linkedControlActionID, String type){
      if(list != null && data != null && validateType(type)){
        
        RefinedSafetyRule safetyRule = new RefinedSafetyRule(data,linkedControlActionID,type, ruleIndex);
        ruleIndex++;
        if(list.add(safetyRule)){
          return safetyRule.getRuleId();
        }
      }
      return null;
    }

    
    /* (non-Javadoc)
     * @see xstampp.astpa.model.extendedData.IExtendedDataController#addRuleEntry(xstampp.astpa.model.interfaces.IExtendedDataModel.RuleType, xstampp.model.AbstractLtlProviderData, java.util.UUID, java.lang.String)
     */
    @Override
    public UUID addRuleEntry(IExtendedDataModel.RuleType ruleType,AbstractLtlProviderData data,UUID caID, String type){
      if(ruleType != null){
        switch(ruleType){
        case CUSTOM_LTL:
          if(customLTLs == null){
            customLTLs = new ArrayList<>();
          }
          return addRuleEntry(customLTLs, data, caID, type);
        case REFINED_RULE:
          if(rules == null){
            rules = new ArrayList<>();
          }
          return addRuleEntry(rules, data, caID, type);
        case SCENARIO:
          if(scenarios == null){
            scenarios = new ArrayList<>();
          }
          return addRuleEntry(scenarios, data, caID, type);
        }
      }
      return null;
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
    
    /* (non-Javadoc)
     * @see xstampp.astpa.model.extendedData.IExtendedDataController#updateRefinedRule(java.util.UUID, xstampp.model.AbstractLtlProviderData, java.util.UUID)
     */
    @Override
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
    /* (non-Javadoc)
     * @see xstampp.astpa.model.extendedData.IExtendedDataController#getAllRefinedRules(boolean, boolean, boolean)
     */
    @Override
    public List<AbstractLtlProvider> getAllRefinedRules(boolean includeRules,
                                                        boolean includeScenarios,
                                                        boolean includeLTL){

      List<AbstractLtlProvider> tmp = new ArrayList<>();
      if(rules != null && includeRules){
        tmp.addAll(rules);
      }
      if(scenarios != null && includeScenarios){
        tmp.addAll(scenarios);
      }
      if(customLTLs != null && includeLTL){
        tmp.addAll(customLTLs);
      }
      return tmp;
    }
    
    /* (non-Javadoc)
     * @see xstampp.astpa.model.extendedData.IExtendedDataController#getAllRefinedRules(xstampp.model.IEntryFilter)
     */
    @Override
    public List<AbstractLtlProvider> getAllRefinedRules(IEntryFilter<AbstractLtlProvider> filter){
      List<AbstractLtlProvider> result = new ArrayList<>();
      for(AbstractLtlProvider data : getAllRefinedRules(true, true, true)){
        if(filter.check(data)){
          result.add(data);
        }
      }
      Collections.sort(result);
      return result;
    }
    
    private boolean removeEntry(List<AbstractLtlProvider> list, boolean removeAll, UUID id){
      boolean result = false;
      if(list != null && removeAll){
        if(removeAll){
          //if removeAll than the rule index is set to 0 so the next rule is added with the index 0
          list.clear();
        }else{
          // the rule which should be removed is searched for in both the 
          // general rules list and in the control actions
          for (AbstractLtlProvider refinedSafetyRule : list) {
            if(refinedSafetyRule.getRuleId().equals(id)){
              result = list.remove(refinedSafetyRule);
            }
          }
        }
        if(list.isEmpty()){
          list = null;
        }
      }
      return result;
    }
    
    @Override
    public boolean removeRefinedSafetyRule(RuleType type, boolean removeAll, UUID ruleId){
      boolean result = false;
      switch(type){
      case REFINED_RULE:
        result = removeEntry(rules, removeAll, ruleId);
        break;
      case CUSTOM_LTL:
        result = removeEntry(customLTLs, removeAll, ruleId);
        break;
      case SCENARIO:
        result = removeEntry(scenarios, removeAll, ruleId);
        break;
      }
      return result;
    }
}
