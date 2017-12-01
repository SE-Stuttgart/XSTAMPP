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
package xstampp.astpa.model.extendedData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.eclipse.core.runtime.Assert;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IEntryFilter;
import xstampp.model.IValueCombie;

@XmlAccessorType(XmlAccessType.NONE)
public class ExtendedDataController extends Observable implements IExtendedDataController {

  public static final String CONSTRAINT_ID = "SSC2.";
  @XmlElementWrapper(name = "rules")
  @XmlElement(name = "rule")
  private List<RefinedSafetyRule> rules;

  private Map<UUID, RefinedSafetyRule> ruleMap;

  @XmlElementWrapper(name = "scenarios")
  @XmlElement(name = "scenario")
  private List<RefinedSafetyRule> scenarios;

  private Map<UUID, RefinedSafetyRule> scenarioMap;

  @XmlElementWrapper(name = "customLTLs")
  @XmlElement(name = "customLTL")
  private List<RefinedSafetyRule> customLTLs;

  private Map<UUID, RefinedSafetyRule> ltlMap;

  @XmlAttribute(name = "nextScenarioIndex")
  private int nextScenarioIndex;

  private Map<String, UUID> combiesMap;

  public ExtendedDataController() {
    nextScenarioIndex = 0;
  }

  public void setNextScenarioIndex(int nextScenarioIndex) {
    this.nextScenarioIndex = nextScenarioIndex;
  }

  private boolean validateType(String type) {
    switch (type) {
    case IValueCombie.TYPE_ANYTIME:
    case IValueCombie.TYPE_NOT_PROVIDED:
    case IValueCombie.TYPE_TOO_EARLY:
    case IValueCombie.TYPE_TOO_LATE:
      return true;
    default:
      return false;
    }

  }

  private int getNextIndex() {
    if (nextScenarioIndex == 0) {
      nextScenarioIndex += getMap(ScenarioType.BASIC_SCENARIO).size();
      nextScenarioIndex += getMap(ScenarioType.CAUSAL_SCENARIO).size();
      nextScenarioIndex += getMap(ScenarioType.CUSTOM_LTL).size();
    }
    return ++nextScenarioIndex;
  }

  private Map<UUID, RefinedSafetyRule> getMap(ScenarioType type) {
    Assert.isNotNull(type);
    switch (type) {
    case CUSTOM_LTL:
      if (ltlMap == null) {
        ltlMap = new HashMap<>();
        fillMap(ltlMap, customLTLs);
      }
      return ltlMap;
    case BASIC_SCENARIO:
      if (ruleMap == null) {
        ruleMap = new HashMap<>();
        fillMap(ruleMap, rules);
      }
      return ruleMap;
    case CAUSAL_SCENARIO:
      if (scenarioMap == null) {
        scenarioMap = new HashMap<>();
        fillMap(scenarioMap, scenarios);
      }
      return scenarioMap;
    default:
      return null;
    }

  }

  private void fillMap(Map<UUID, RefinedSafetyRule> entryMap, List<RefinedSafetyRule> list) {
    if (list != null) {
      for (RefinedSafetyRule provider : list) {
        entryMap.put(provider.getId(), provider);
      }
    }
    list = null;
  }

  /**
   * 
   * @param rule
   * @param linkController
   * 
   * @see IValueCombie
   * @return
   */
  UUID addRefinedRule(RefinedSafetyRule rule, IExtendedDataModel.ScenarioType ruleType, LinkController linkController) {
    if (!getMap(ruleType).containsKey(rule.getId())) {
      nextScenarioIndex = Math.max(nextScenarioIndex, rule.getNumber());
      getMap(ruleType).put(rule.getId(), rule);
      setChanged();
      notifyObservers(new UndoAddRule(this, rule, ruleType, linkController));
      return rule.getId();
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.extendedData.IExtendedDataController#addRuleEntry(xstampp.astpa.model.
   * interfaces.IExtendedDataModel.RuleType, xstampp.model.AbstractLtlProviderData, java.util.UUID,
   * java.lang.String)
   */
  @Override
  public UUID addRuleEntry(IExtendedDataModel.ScenarioType ruleType, AbstractLtlProviderData data,
      UUID caID, String type, LinkController linkController) {
    assert (caID != null);
    if (data != null && validateType(type)) {
      if (ruleType.equals(ScenarioType.BASIC_SCENARIO)) {
        UUID uuid = getCombie(data);
        if (uuid != null) {
          updateRefinedRule(uuid, data, caID);
          return uuid;
        }
      }
      RefinedSafetyRule safetyRule = new RefinedSafetyRule(data, caID, type, getNextIndex());
      return addRefinedRule(safetyRule, ruleType, linkController);

    }
    return null;
  }

  /**
   * getter for a map containing the id of all basic scenarios id's mapped to their
   * safety rules/ constraints
   * 
   * @return a map with scenario ids' mapped to the safety rules of the scenario
   */
  private Map<String, UUID> getCombieMap() {
    if (combiesMap == null) {
      combiesMap = new HashMap<>();
      for (AbstractLTLProvider scenario : getMap(ScenarioType.BASIC_SCENARIO).values()) {
        if (((RefinedSafetyRule) scenario).getSafetyRule() != null) {
          combiesMap.put(((RefinedSafetyRule) scenario).getSafetyRule(), scenario.getId());
        }
      }
    }
    return combiesMap;
  }

  /**
   * searches for a scenario that is mapper for the given data's safety rule
   * in the combieMap
   * 
   * @param data
   *          the {@link AbstractLtlProviderData} of the new basic scenario that should be searched
   *          for in
   *          the combieMap
   * @return an UUID if one was mapped for the given data'S safety rule, or null
   */
  private UUID getCombie(AbstractLtlProviderData data) {
    if (data != null && data.getSafetyRule() != null) {
      return getCombieMap().get(data.getSafetyRule());
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.extendedData.IExtendedDataController#updateRefinedRule(java.util.UUID,
   * xstampp.model.AbstractLtlProviderData, java.util.UUID)
   */
  @Override
  public boolean updateRefinedRule(UUID ruleId, AbstractLtlProviderData data,
      UUID linkedControlActionID) {
    for (AbstractLTLProvider provider : getAllScenarios(true, true, true)) {
      if (provider.getRuleId().equals(ruleId)) {
        return updateRefinedRule(provider, data, linkedControlActionID);
      }
    }
    return false;
  }

  private boolean updateRefinedRule(AbstractLTLProvider provider, AbstractLtlProviderData data,
      UUID linkedControlActionID) {
    boolean changed = false;
    changed = changed || ((RefinedSafetyRule) provider).setLtlProperty(data.getLtlProperty());
    changed = changed || ((RefinedSafetyRule) provider)
        .setRefinedSafetyConstraint(data.getRefinedSafetyConstraint());
    changed = changed || ((RefinedSafetyRule) provider).setRefinedUCA(data.getRefinedUca());
    changed = changed || ((RefinedSafetyRule) provider).setSafetyRule(data.getSafetyRule());
    changed = changed || ((RefinedSafetyRule) provider).setUCALinks(data.getUcaLinks());
    changed = changed || ((RefinedSafetyRule) provider).setCaID(linkedControlActionID);
    changed = changed || ((RefinedSafetyRule) provider).setCriticalCombies(data.getCombies());
    return changed;

  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.extendedData.IExtendedDataController#getAllRefinedRules(boolean,
   * boolean, boolean)
   */
  @Override
  public List<AbstractLTLProvider> getAllScenarios(boolean includeRules,
      boolean includeScenarios,
      boolean includeLTL) {

    List<AbstractLTLProvider> tmp = new ArrayList<>();
    if (includeRules) {
      tmp.addAll(getMap(ScenarioType.BASIC_SCENARIO).values());
    }
    if (includeScenarios) {
      tmp.addAll(getMap(ScenarioType.CAUSAL_SCENARIO).values());
    }
    if (includeLTL) {
      tmp.addAll(getMap(ScenarioType.CUSTOM_LTL).values());
    }
    Collections.sort(tmp);
    return tmp;
  }

  @Override
  public List<ITableModel> getSafetyConstraints(boolean includeRules,
      boolean includeScenarios,
      boolean includeLTL) {
    List<ITableModel> list = new ArrayList<>();
    for (AbstractLTLProvider ltlProvider : getAllScenarios(includeRules, includeScenarios, includeLTL)) {
      ITableModel model = new ATableModel(ltlProvider.getTitle(), ltlProvider.getRefinedSafetyConstraint(),
          ltlProvider.getNumber()) {
        @Override
        public UUID getId() {
          return ltlProvider.getId();
        }

        @Override
        public String getIdString() {
          return CONSTRAINT_ID + getNumber();
        }
      };
      list.add(model);
    }
    return list;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.extendedData.IExtendedDataController#getAllRefinedRules(boolean,
   * boolean, boolean)
   */
  @Override
  public AbstractLTLProvider getRefinedScenario(UUID ruleId) {
    if (getMap(ScenarioType.BASIC_SCENARIO).containsKey(ruleId)) {
      return getMap(ScenarioType.BASIC_SCENARIO).get(ruleId);
    }
    if (getMap(ScenarioType.CAUSAL_SCENARIO).containsKey(ruleId)) {
      return getMap(ScenarioType.CAUSAL_SCENARIO).get(ruleId);
    }
    if (getMap(ScenarioType.CUSTOM_LTL).containsKey(ruleId)) {
      return getMap(ScenarioType.CUSTOM_LTL).get(ruleId);
    }
    return null;
  }

  @Override
  public ScenarioType getScenarioType(UUID ruleId) {
    if (getMap(ScenarioType.BASIC_SCENARIO).containsKey(ruleId)) {
      return ScenarioType.BASIC_SCENARIO;
    }
    if (getMap(ScenarioType.CAUSAL_SCENARIO).containsKey(ruleId)) {
      return ScenarioType.CAUSAL_SCENARIO;
    }
    if (getMap(ScenarioType.CUSTOM_LTL).containsKey(ruleId)) {
      return ScenarioType.CUSTOM_LTL;
    }
    return ScenarioType.NO_SCENARIO;
  }

  /*
   * (non-Javadoc)
   * @see xstampp.astpa.model.extendedData.IExtendedDataController#getAllRefinedRules(xstampp.model.
   * IEntryFilter)
   */
  @Override
  public List<AbstractLTLProvider> getAllRefinedRules(IEntryFilter<AbstractLTLProvider> filter) {
    List<AbstractLTLProvider> result = new ArrayList<>();
    for (AbstractLTLProvider data : getAllScenarios(true, true, true)) {
      if (filter.check(data)) {
        result.add(data);
      }
    }
    Collections.sort(result);
    return result;
  }

  private RefinedSafetyRule removeEntry(Map<UUID, RefinedSafetyRule> entryMap, boolean removeAll, UUID id) {
    RefinedSafetyRule result = null;
    if (entryMap == null) {
    } else if (removeAll) {
      // if removeAll than the rule index is set to 0 so the next rule is added with the index 0
      entryMap.clear();
      result = null;
    } else if (entryMap.containsKey(id)) {
      // the rule which should be removed is searched for in both the
      // general rules list and in the control actions
      result = entryMap.remove(id);
    }
    return result;
  }

  @Override
  public boolean removeRefinedSafetyRule(ScenarioType type, boolean removeAll, UUID ruleId,
      LinkController linkController) {
    RefinedSafetyRule entry = removeEntry(getMap(type), removeAll, ruleId);
    if (entry != null) {
      setChanged();
      notifyObservers(new UndoRemoveRule(this, entry, type, linkController));
    }
    return false;
  }

  public void prepareForExport(IControlActionController iControlActionController, LinkController linkController) {
    prepareForSave(iControlActionController, linkController);
  }

  public void prepareForSave(IControlActionController iControlActionController, LinkController linkController) {
    combiesMap = null;
    if (ruleMap != null) {
      rules = new ArrayList<>(ruleMap.values());
      ruleMap = null;
    }
    if (scenarioMap != null) {
      scenarios = new ArrayList<>(scenarioMap.values());
      scenarioMap = null;
    }
    if (ltlMap != null) {
      customLTLs = new ArrayList<>(ltlMap.values());
      ltlMap = null;
    }
    // prepare the rules list for save by moving all rules to the
    // ExtendedDataModel and storing a list of uuids
    for (AbstractLTLProvider refinedRule : iControlActionController.getAllRefinedRules(false)) {
      ((RefinedSafetyRule) refinedRule).setLinks(null);
      addRefinedRule((RefinedSafetyRule) refinedRule, ScenarioType.BASIC_SCENARIO, linkController);
    }

  }

}
