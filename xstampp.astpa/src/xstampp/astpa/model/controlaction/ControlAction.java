/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.controlaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.extendedData.RefinedSafetyRule;
import xstampp.astpa.model.interfaces.ISTPADataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.IValueCombie;

/**
 * Class representing the control action objects
 * 
 * @author Fabian Toth, Lukas Balzer
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ControlAction extends ATableModel implements IControlAction {

  @XmlElementWrapper(name = "unsafecontrolactions")
  @XmlElement(name = "unsafecontrolaction")
  private List<UnsafeControlAction> unsafeControlActions;

  @XmlElement(name = "componentLink")
  private UUID componentLink;

  @XmlElement(name = "isSafetyCritical")
  private boolean isSafetyCritical;

  @XmlElementWrapper(name = "notProvidedPMVariables")
  @XmlElement(name = "variableID")
  private List<UUID> notProvidedVariables;

  @XmlElementWrapper(name = "providedPMVariables")
  @XmlElement(name = "variableID")
  private List<UUID> providedVariables;

  @XmlElementWrapper(name = "PMCombisWhenNotProvided")
  @XmlElement(name = "combinationOfPMValues")
  private List<NotProvidedValuesCombi> valuesWhenNotProvided;

  @XmlElementWrapper(name = "PMCombisWhenProvided")
  @XmlElement(name = "combinationOfPMValues")
  private List<ProvidedValuesCombi> valuesWhenProvided;

  @XmlElementWrapper(name = "dependenciesForNotProvided")
  @XmlElement(name = "variableName")
  private List<String> notProvidedVariableNames;

  @XmlElementWrapper(name = "dependenciesForProvided")
  @XmlElement(name = "variableName")
  private List<String> providedVariableNames;

  @XmlElementWrapper(name = "rules")
  @XmlElement(name = "rule")
  private List<RefinedSafetyRule> rules;

  @XmlElementWrapper(name = "ruleIds")
  @XmlElement(name = "id")
  private List<UUID> ruleIds;

  /**
   * @param componentLink
   *          the componentLink to set
   * @return
   */
  public boolean setComponentLink(UUID componentLink) {
    if (this.componentLink == null || !this.componentLink.equals(componentLink)) {
      this.componentLink = componentLink;
      return true;
    }
    return false;
  }

  @Override
  public UUID getComponentLink() {
    return this.componentLink;
  }

  /**
   * Constructor of a control action
   * 
   * @param title
   *          the title of the new accident
   * @param description
   *          the description of the new accident
   * @param number
   *          the number of the new accident
   * 
   * @author Fabian Toth
   */
  public ControlAction(String title, String description) {
    super(title, description);
    this.unsafeControlActions = new ArrayList<>();
  }

  /**
   * Constructor of a control action
   * 
   * @param title
   *          the title of the new accident
   * @param description
   *          the description of the new accident
   * @param number
   *          the number of the new accident
   * 
   * @author Fabian Toth
   */
  public ControlAction(String title, String description, int number) {
    super(title, description, number);
    this.unsafeControlActions = new ArrayList<>();
  }

  /**
   * Empty constructor for JAXB. Do not use it!
   * 
   * @author Fabian Toth
   */
  public ControlAction() {
    this.unsafeControlActions = new ArrayList<>();
    // empty constructor for JAXB
  }

  public ControlAction(ITableModel model) {
    super(model, -1);
    this.unsafeControlActions = new ArrayList<>();
  }

  @Override
  public List<IUnsafeControlAction> getUnsafeControlActions() {
    List<IUnsafeControlAction> result = new ArrayList<>();
    for (UnsafeControlAction unsafeControlAction : this.unsafeControlActions) {
      result.add(unsafeControlAction);
    }
    return result;
  }

  @Override
  public IUnsafeControlAction getUnsafeControlAction(UUID ucaId) {
    for (UnsafeControlAction unsafeControlAction : this.unsafeControlActions) {
      if (unsafeControlAction.getId().equals(ucaId)) {
        return unsafeControlAction;
      }
    }
    return null;
  }

  @Override
  public List<IUnsafeControlAction> getUnsafeControlActions(
      UnsafeControlActionType unsafeControlActionType) {
    List<IUnsafeControlAction> result = new ArrayList<>();
    for (UnsafeControlAction unsafeControlAction : this.unsafeControlActions) {
      if (unsafeControlAction.getType().equals(unsafeControlActionType)) {
        result.add(unsafeControlAction);
      }
    }
    return result;
  }

  /**
   * Adds a unsafe control action to this control action
   * 
   * @param description
   *          the description of the new control action
   * @param unsafeControlActionType
   *          the type of the enw control action
   * @return the id of the new control action
   * 
   * @author Fabian Toth
   */
  public UUID addUnsafeControlAction(int number, String description,
      UnsafeControlActionType unsafeControlActionType) {
    UnsafeControlAction unsafeControlAction = new UnsafeControlAction(description,
        unsafeControlActionType);
    this.unsafeControlActions.add(unsafeControlAction);
    unsafeControlAction.setNumber(number);
    return unsafeControlAction.getId();
  }

  /**
   * Adds a unsafe control action to this control action
   * 
   * @param description
   *          the description of the new control action
   * @param unsafeControlActionType
   *          the type of the enw control action
   * @return the id of the new control action
   * 
   * @author Fabian Toth
   */
  public UUID addUnsafeControlAction(int number, String description,
      UnsafeControlActionType unsafeControlActionType, UUID id) {
    UnsafeControlAction unsafeControlAction = new UnsafeControlAction(description,
        unsafeControlActionType);
    unsafeControlAction.setId(id);
    this.unsafeControlActions.add(unsafeControlAction);
    unsafeControlAction.setNumber(number);
    return unsafeControlAction.getId();
  }

  /**
   * Searches the unsafe control action and removes it when it is in the list
   * 
   * @param unsafeControlActionId
   *          the id of the unsafe control action to delete
   * @return true if the unsafe control action has been removed
   * 
   * @author Fabian Toth
   */
  public boolean removeUnsafeControlAction(UUID unsafeControlActionId) {
    for (UnsafeControlAction unsafeControlAction : this.unsafeControlActions) {
      if (unsafeControlAction.getId().equals(unsafeControlActionId)) {
        return this.unsafeControlActions.remove(unsafeControlAction);
      }
    }
    return false;
  }

  /**
   * Gets all unsafe control actions of this control action in an internal type
   * 
   * @return all unsafe control actions of this control action
   * 
   * @author Fabian Toth
   */
  List<UnsafeControlAction> getInternalUnsafeControlActions() {
    return this.unsafeControlActions;
  }

  /**
   * @return the isSafetyCritical
   */
  public boolean isCASafetyCritical() {
    return this.isSafetyCritical;
  }

  /**
   * @param isSafetyCritical
   *          the isSafetyCritical to set
   * @return
   */
  public boolean setSafetyCritical(boolean isSafetyCritical) {
    if (this.isSafetyCritical != isSafetyCritical) {
      this.isSafetyCritical = isSafetyCritical;
      return true;
    }
    return false;
  }

  /**
   * @return the valuesWhenNotProvided
   */
  public List<NotProvidedValuesCombi> getValuesAffectedWhenNotProvided() {
    if (this.valuesWhenNotProvided == null) {
      return new ArrayList<>();
    }
    return this.valuesWhenNotProvided;
  }

  /**
   * @param valuesWhenNotProvided
   *          the valuesWhenNotProvided to set
   */
  public void setValuesWhenNotProvided(List<NotProvidedValuesCombi> valuesWhenNotProvided) {
    this.valuesWhenNotProvided = valuesWhenNotProvided;
  }

  /**
   * @param valuesWhenNotProvided
   *          the valuesWhenNotProvided to set
   */
  public boolean addValuesWhenNotProvided(NotProvidedValuesCombi valueWhenNotProvided) {
    if (this.valuesWhenNotProvided == null) {
      this.valuesWhenNotProvided = new ArrayList<>();
    }
    return this.valuesWhenNotProvided.add(valueWhenNotProvided);
  }

  public boolean removeValuesWhenNotProvided(UUID combieId) {
    for (IValueCombie combie : this.valuesWhenNotProvided) {
      if (combie.getCombieId().equals(combieId)) {
        return this.valuesWhenNotProvided.remove(combie);
      }
    }
    return false;
  }

  /**
   * @return the valuesWhenProvided
   */
  public List<ProvidedValuesCombi> getValuesAffectedWhenProvided() {
    if (this.valuesWhenProvided == null) {
      return new ArrayList<>();
    }
    return this.valuesWhenProvided;
  }

  /**
   * @param valuesWhenProvided
   *          the valuesWhenProvided to set
   */
  public void setValuesWhenProvided(List<ProvidedValuesCombi> valuesWhenProvided) {
    this.valuesWhenProvided = valuesWhenProvided;
  }

  /**
   * @param valuesWhenNotProvided
   *          the valuesWhenNotProvided to set
   */
  public boolean addValueWhenProvided(ProvidedValuesCombi valueWhenNotProvided) {
    if (this.valuesWhenNotProvided == null) {
      this.valuesWhenNotProvided = new ArrayList<>();
    }
    return this.valuesWhenProvided.add(valueWhenNotProvided);
  }

  public boolean removeValueWhenProvided(UUID combieId) {
    for (ProvidedValuesCombi combie : this.valuesWhenProvided) {
      if (combie.getCombieId().equals(combieId)) {
        return this.valuesWhenProvided.remove(combie);
      }
    }
    return false;
  }

  /**
   * @return a copie of the the notProvidedVariables List
   */
  public List<UUID> getNotProvidedVariables() {
    if (this.notProvidedVariables == null) {
      return new ArrayList<>();
    }
    return new ArrayList<>(this.notProvidedVariables);
  }

  /**
   * 
   * addds the uuid of a process variable component to the list of variables depending on this
   * control action when not provided
   * 
   * @param notProvidedVariable
   *          the notProvidedVariables to set
   */
  public void addNotProvidedVariable(UUID notProvidedVariable) {
    if (this.notProvidedVariables == null) {
      this.notProvidedVariables = new ArrayList<>();
    }
    this.notProvidedVariables.add(notProvidedVariable);
  }

  /**
   * @return a copie of the provided variables list
   */
  public List<UUID> getProvidedVariables() {
    if (this.providedVariables == null) {
      this.providedVariables = new ArrayList<>();
    }
    ArrayList<UUID> list = new ArrayList<>(this.providedVariables);
    return list;
  }

  /**
   * addds the uuid of a process variable component to the list of variables depending on this
   * control action when provided
   * 
   * @param providedVariable
   *          the providedVariable to add
   */
  public void addProvidedVariable(UUID providedVariable) {
    if (this.providedVariables == null) {
      this.providedVariables = new ArrayList<>();
    }
    if (!this.providedVariables.contains(providedVariable)) {
      this.providedVariables.add(providedVariable);
    }
  }

  /**
   * 
   * remove the uuid of a process variable component from the list of variables depending on this
   * control action when not provided
   * 
   * @param notProvidedVariable
   *          the notProvidedVariables to remove
   * @return return whether the remove was successful or not, also returns false if the list is null
   *         or the uuid is not contained in the list
   */
  public boolean removeNotProvidedVariable(UUID notProvidedVariable) {
    if (this.notProvidedVariables == null) {
      return false;
    }
    if (this.notProvidedVariables.contains(notProvidedVariable)) {
      return this.notProvidedVariables.remove(notProvidedVariable);
    }
    return false;
  }

  /**
   * 
   * remove the uuid of a process variable component from the list of variables depending on this
   * control action when provided
   * 
   * @param providedVariable
   *          the providedVariable to remove
   * @return return whether the remove was successful or not, also returns false if the list is null
   *         or the uuid is not contained in the list
   */
  public boolean removeProvidedVariable(UUID providedVariable) {
    if (this.providedVariables == null) {
      return false;
    }
    if (this.providedVariables.contains(providedVariable)) {
      return this.providedVariables.remove(providedVariable);
    }
    return false;
  }

  public List<AbstractLTLProvider> getAllRefinedRules() {
    if (rules == null) {
      return new ArrayList<>();
    }
    ArrayList<AbstractLTLProvider> tmp = new ArrayList<>();
    for (RefinedSafetyRule refinedSafetyRule : rules) {
      tmp.add(refinedSafetyRule);
    }
    return tmp;
  }

  /**
   * 
   * @param ruleId
   * 
   * @return
   */
  public boolean addRefinedRuleLink(UUID ruleId) {
    if (ruleId != null) {
      if (ruleIds == null) {
        this.ruleIds = new ArrayList<>();
      }
      return ruleIds.add(ruleId);
    }
    return false;
  }

  /**
   * 
   * @param removeAll
   *          whether all currently stored RefinedSafetyRule objects should be deleted<br>
   *          when this is true than the ruleId will be ignored
   * @param ruleId
   *          an id of a RefinedSafetyRule object stored in a controlAction
   * 
   * @return whether the delete was successful or not, also returns false if the rule could not be
   *         found or the id was illegal
   */
  public boolean removeSafetyRule(boolean removeAll, UUID id) {
    if (removeAll) {
      this.rules.clear();
      this.ruleIds.clear();
      return true;
    }
    if (id != null && ruleIds != null) {
      return ruleIds.remove(id);
    }
    return false;
  }

  /**
   * Prepares the control actions for the export
   * 
   * @author Fabian Toth, Lukas Balzer
   * @param defaultLabel
   *          TODO
   * @param hazAccController
   *          the hazAccController to get the Accidents as objects
   * 
   */
  public void prepareForExport(ISTPADataModel dataModel, String defaultLabel) {
    super.prepareForExport();
    for (UnsafeControlAction unsafeControlAction : unsafeControlActions) {
      unsafeControlAction.prepareForExport(dataModel);
    }
    rules = new ArrayList<>();
    for (AbstractLTLProvider refinedRule : dataModel.getExtendedDataController().getAllScenarios(true, false, false)) {
      if (refinedRule.getRelatedControlActionID().equals(getId())) {
        rules.add((RefinedSafetyRule) refinedRule);
      }
    }
    List<UUID> trash = new ArrayList<>();
    if (notProvidedVariables != null) {
      notProvidedVariableNames = new ArrayList<>();
      for (UUID id : notProvidedVariables) {
        if (dataModel.getControlStructureController().getComponent(id) != null) {
          notProvidedVariableNames.add(dataModel.getControlStructureController().getComponent(id).getText());
        } else {
          trash.add(id);
        }
      }
      notProvidedVariables.removeAll(trash);
    }
    if (providedVariables != null) {
      trash.clear();
      providedVariableNames = new ArrayList<>();
      for (UUID id : providedVariables) {
        if (dataModel.getControlStructureController().getComponent(id) != null) {
          providedVariableNames.add(dataModel.getControlStructureController().getComponent(id).getText());
        } else {
          trash.add(id);
        }
      }
      providedVariables.removeAll(trash);
    }
    if (notProvidedVariableNames != null && !notProvidedVariableNames.isEmpty()
        && valuesWhenNotProvided != null) {
      for (NotProvidedValuesCombi combie : valuesWhenNotProvided) {
        if (combie.getPMValues().keySet().containsAll(notProvidedVariables)
            && combie.getValueList().size() == notProvidedVariableNames.size()) {
          ArrayList<String> list = new ArrayList<>();
          for (UUID id : combie.getValueList()) {
            if (dataModel.getControlStructureController().getComponent(id) != null) {
              list.add(dataModel.getControlStructureController().getComponent(id).getText());
            } else {
              list.add(defaultLabel);
            }
          }
          combie.setValueNames(list);
        } else {
          combie.setValueNames(null);
        }
      }
    }
    /*
     * if he valuesWhenProvided list has been created and there are linked variables than the list
     * is traversed
     */
    if (providedVariableNames != null && !providedVariableNames.isEmpty()
        && valuesWhenProvided != null) {
      for (ProvidedValuesCombi combie : valuesWhenProvided) {
        if (combie.getPMValues().keySet().containsAll(providedVariables)
            && combie.getValueList().size() == providedVariableNames.size()) {
          ArrayList<String> list = new ArrayList<>();
          for (UUID id : combie.getValueList()) {
            if (dataModel.getControlStructureController().getComponent(id) != null) {
              list.add(dataModel.getControlStructureController().getComponent(id).getText());
            } else {
              list.add(defaultLabel);
            }
          }
          combie.setValueNames(list);
        } else {
          combie.setValueNames(null);
        }
      }
    }
  }

  @Override
  public void prepareForSave() {
    super.prepareForSave();
    notProvidedVariableNames = null;
    providedVariableNames = null;

    if (valuesWhenNotProvided != null) {
      for (NotProvidedValuesCombi combie : valuesWhenNotProvided) {
        combie.setValueNames(null);
      }
    }
    if (valuesWhenProvided != null) {
      for (ProvidedValuesCombi combie : valuesWhenProvided) {
        combie.setValueNames(null);
      }
    }
    rules = null;
    for (UnsafeControlAction unsafeControlAction : unsafeControlActions) {
      unsafeControlAction.prepareForSave();
    }
  }

  public boolean intern_addRefinedRule(RefinedSafetyRule rule) {
    if (rule.getRelatedControlActionID().equals(getId())) {
      if (this.rules == null) {
        this.rules = new ArrayList<>();
      }
      this.rules.add(rule);
      return true;
    }
    return false;
  }

  @Override
  public String getIdString() {
    return "CA-" + super.getIdString();
  }
}
