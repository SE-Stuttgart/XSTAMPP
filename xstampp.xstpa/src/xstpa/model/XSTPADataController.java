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
package xstpa.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import xstampp.astpa.model.controlaction.NotProvidedValuesCombi;
import xstampp.astpa.model.controlaction.ProvidedValuesCombi;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.IValueCombie;
import xstampp.model.ObserverValue;

public class XSTPADataController extends Observable implements Observer {

  private static final String CONTEXT_PROVIDED = "provided";
  private static final String CONTEXT_NOT_PROVIDED = "not provided";
  private HashMap<UUID, ProcessModelValue> valuesList;
  private Map<UUID, ControlActionEntry> dependenciesIFProvided;
  private Map<UUID, ProcessModelVariables> variablesList;
  private Map<UUID, ControlActionEntry> dependenciesNotProvided;
  private ControlActionEntry linkedCAE;
  private ProcessModelVariables linkedPMV;
  private IExtendedDataModel model;
  private boolean controlActionProvided;
  private List<RefinedSafetyEntry> refinedEntrys;

  private String constraintPrefix = "SSR1.";
  private String refinedCaPrefix = "RUCA1.";
  private String ruleLiteral = "SR"; //$NON-NLS-1$

  public XSTPADataController(IExtendedDataModel model) {
    this.valuesList = new HashMap<>();
    this.variablesList = new HashMap<>();
    this.refinedEntrys = new ArrayList<>();
    this.dependenciesIFProvided = new HashMap<>();
    this.dependenciesNotProvided = new HashMap<>();
    this.model = (IExtendedDataModel) model;
    model.addObserver(this);
    clear();
  }

  public void setConstraintPrefix(String constraintPrefix) {
    this.constraintPrefix = constraintPrefix;
  }

  public void setRefinedCaPrefix(String rulePrefix) {
    this.refinedCaPrefix = rulePrefix;
  }

  public void setRuleLiteral(String literal) {
    ruleLiteral = literal;
  }

  public void clear() {
    this.linkedCAE = null;
    this.linkedPMV = null;
    this.dependenciesIFProvided.clear();
    this.dependenciesNotProvided.clear();
    if (getModel() != null) {
      this.fetchProcessComponents();
      this.fetchControlActions();
    }
  }

  // ********************************************************************************************************************
  // Management of the PROCESS MODEL VALUES

  /**
   * @param includeDontCare
   *          TODO
   * @return the valuesList
   * @see ProcessModelValue
   */
  public List<ProcessModelValue> getValuesList(boolean includeDontCare) {
    ArrayList<ProcessModelValue> returnedValues = new ArrayList<>();
    for (ProcessModelValue value : this.valuesList.values()) {
      if (includeDontCare || !value.getId().equals(getModel().getIgnoreLTLValue().getId())) {
        returnedValues.add(value);
      }
    }
    Collections.sort(returnedValues);
    return returnedValues;
  }

  public int getValueCount() {
    return this.valuesList.size();
  }

  /**
   * returns a ProcessModelValue
   * 
   * @param index
   *          the index in the list of values
   * @return the ProcessModelValue stored at the given index
   * @see ProcessModelValue
   */
  public ProcessModelValue getValue(int index) {
    return this.valuesList.get(index);
  }

  /**
   * @param value
   *          the value which is to add to the valuesList
   */
  public void addValue(ProcessModelValue value) {
    this.valuesList.put(value.getId(), value);
  }

  public boolean removeValue(int index) {
    return this.valuesList.remove(index) != null;
  }
  // ********************************************************************************************************************
  // Management of the PROCESS MODEL VARIABLES

  /**
   * @return the variablesList
   */
  public ArrayList<ProcessModelVariables> getVariablesList() {
    return new ArrayList<ProcessModelVariables>(this.variablesList.values());
  }

  /**
   * returns a ProcessModelVariables
   * 
   * @param index
   *          the index in the list of values
   * @return the ProcessModelVariable stored at the given index
   * @see ProcessModelVariables
   */
  public ProcessModelVariables getVariable(int index) {
    return this.variablesList.get(index);
  }

  public int getVariablesCount() {
    return this.variablesList.size();
  }

  /**
   * @param varible
   *          the value which is to add to the valuesList
   */
  public void addVariable(ProcessModelVariables varible) {
    varible.setNumber(this.variablesList.size() + 1);
    this.variablesList.put(varible.getId(), varible);
  }

  /**
   * fetches the current available values and variable components from the data model
   * 
   * @param model
   *          the current data model
   */
  private void fetchProcessComponents() {

    this.valuesList.clear();
    this.variablesList.clear();
    IRectangleComponent rootComponent = getModel().getRoot();
    for (IRectangleComponent child : rootComponent.getChildren()) {

      if (child.getComponentType().name().equals("CONTROLLER")) {

        // get the process models
        for (IRectangleComponent tempPM : child.getChildren()) {

          // get the variables
          for (IRectangleComponent tempPMV : tempPM.getChildren()) {
            ProcessModelVariables variable = new ProcessModelVariables();
            variable.setName(tempPMV.getText());
            variable.setId(tempPMV.getId());
            // get the values and add the new object to the processmodel list
            for (IRectangleComponent tempPMVV : tempPMV.getChildren()) {

              ProcessModelValue pmValueObject = new ProcessModelValue();

              pmValueObject.setPM(tempPM.getText());
              pmValueObject.setPMV(tempPMV.getText());
              pmValueObject.setValueText(tempPMVV.getText());
              pmValueObject.setId(tempPMVV.getId());
              pmValueObject.setVariableID(tempPMV.getId());
              pmValueObject.setComments(tempPMVV.getComment());
              variable.setControllerID(child.getId());
              pmValueObject.setController(child.getText());
              variable.addValue(tempPMVV.getText());
              variable.addValueId(tempPMVV.getId());
              addValue(pmValueObject);

            }
            if (!variable.getValues().isEmpty()) {
              addVariable(variable);
            }
          }

        }
      }
    }
    // Add the dontcare obj
    ProcessModelValue finalObj = new ProcessModelValue();
    IRectangleComponent dontCare = getModel().getIgnoreLTLValue();
    finalObj.setValueText(dontCare.getText());
    finalObj.setId(dontCare.getId());
    addValue(finalObj);
  }

  /**
   * this method pulls all control actions and fills the
   * list of dependent variables with the information from
   * this{@link #fetchProcessComponents(IExtendedDataModel)}
   * 
   * @param model
   *          the data model which should be used
   */
  private void fetchControlActions() {
    this.dependenciesIFProvided.clear();
    this.dependenciesNotProvided.clear();
    // get the controlActions
    for (IControlAction entry : getModel().getAllControlActionsU()) {
      this.dependenciesIFProvided.put(entry.getId(),
          getEntryFor(entry, getModel().getIvaluesWhenCAProvided(entry.getId()), CONTEXT_PROVIDED));
      this.dependenciesNotProvided.put(entry.getId(),
          getEntryFor(entry, getModel().getIValuesWhenCANotProvided(entry.getId()), CONTEXT_NOT_PROVIDED));
    }
  }

  private ControlActionEntry getEntryFor(IControlAction entry, List<IValueCombie> combies, String context) {
    ControlActionEntry tempCAEntry = new ControlActionEntry(context);

    // tempCAE.setController(entry.);
    tempCAEntry.setComments(entry.getDescription());
    tempCAEntry.setControlAction(entry.getTitle());
    tempCAEntry.setNumber(entry.getNumber());
    tempCAEntry.setId(entry.getId());
    tempCAEntry.setSafetyCritical(getModel().isCASafetyCritical(entry.getId()));
    List<UUID> linkedIDs;
    if (context.equals(CONTEXT_PROVIDED)) {
      linkedIDs = ((IControlAction) entry).getProvidedVariables();
    } else {
      linkedIDs = ((IControlAction) entry).getNotProvidedVariables();
    }
    /*
     * set linkedItems and available items for the control action entry
     */
    for (ProcessModelVariables var : getVariablesList()) {
      if (linkedIDs.contains(var.getId())) {
        tempCAEntry.addLinkedItem(var);
      } else {
        tempCAEntry.addAvailableItem(var);
      }
    }

    // add all the value combinations for the context table to the two dependencies lists
    boolean invalid = false;
    ContextTableCombination contextTableEntry;
    for (IValueCombie valueCombie : combies) {

      contextTableEntry = new ContextTableCombination();

      contextTableEntry.setUcaLinks(valueCombie.getUCALinks(IValueCombie.TYPE_NOT_PROVIDED),
          IValueCombie.TYPE_NOT_PROVIDED);
      contextTableEntry.setUcaLinks(valueCombie.getUCALinks(IValueCombie.TYPE_ANYTIME), IValueCombie.TYPE_ANYTIME);
      contextTableEntry.setUcaLinks(valueCombie.getUCALinks(IValueCombie.TYPE_TOO_EARLY), IValueCombie.TYPE_TOO_EARLY);
      contextTableEntry.setUcaLinks(valueCombie.getUCALinks(IValueCombie.TYPE_TOO_LATE), IValueCombie.TYPE_TOO_LATE);

      if (valueCombie.getPMValues() == null) {
        Map<UUID, UUID> valuesIdsTOvariableIDs = new HashMap<>();
        for (ProcessModelValue value : getValuesList(true)) {
          if (valueCombie.getValueList().contains(value.getId())) {
            valuesIdsTOvariableIDs.put(value.getVariableID(), value.getId());
          }
        }
        valueCombie.setValues(valuesIdsTOvariableIDs);
      }
      if (valueCombie.getPMValues() == null) {
        HashMap<UUID, UUID> map = new HashMap<>();
        for (UUID valueId : valueCombie.getValueList()) {
          map.put(valuesList.get(valueId).getVariableID(), valueId);
        }
        valueCombie.setValues(map);
      }

      for (ProcessModelVariables var : tempCAEntry.getLinkedItems()) {
        // if the valueCombie contains a value or a variable that is not registered, it is
        // considered invalid
        // and not added
        if (!valuesList.containsKey(valueCombie.getPMValues().get(var.getId()))) {
          invalid = true;
          break;
        } else {
          contextTableEntry.addValueMapping(var.getId(), valueCombie.getPMValues().get(var.getId()));
        }

      }
      contextTableEntry.setLinkedControlActionName(entry.getTitle(), entry.getId());
      contextTableEntry.setContext(context);
      contextTableEntry.setRefinedSafetyRequirements(valueCombie.getSafetyConstraint());
      contextTableEntry.setHazardous(valueCombie.isCombiHazardous(IValueCombie.TYPE_NOT_PROVIDED));
      contextTableEntry.setHAnytime(valueCombie.isCombiHazardous(IValueCombie.TYPE_ANYTIME));
      contextTableEntry.setHEarly(valueCombie.isCombiHazardous(IValueCombie.TYPE_TOO_EARLY));
      contextTableEntry.setHLate(valueCombie.isCombiHazardous(IValueCombie.TYPE_TOO_LATE));

      if (!invalid) {
        tempCAEntry.addContextTableCombination(contextTableEntry);
      }
      invalid = false;
    }

    return tempCAEntry;
  }

  /**
   * 
   * @param caID
   *          an uuid for filtering by a specific controlaction, if null than all ltlEntrys are
   *          generated
   * 
   * @return a map containing three mappings containing:
   *         </ul>
   *         <li>List of RefinedSafetyEntrys to {@value IValueCombie#HAZ_IF_NOT_PROVIDED}
   *         <li>List of RefinedSafetyEntrys to {@value IValueCombie#HAZ_IF_PROVIDED}
   *         <li>List of RefinedSafetyEntrys to {@value IValueCombie#HAZ_IF_WRONG_PROVIDED}
   *         </ul>
   */
  public Map<String, ArrayList<RefinedSafetyEntry>> getHazardousCombinations(UUID caID) {
    HashMap<String, ArrayList<RefinedSafetyEntry>> combiesToContextID = new HashMap<>();

    combiesToContextID.put(IValueCombie.HAZ_IF_NOT_PROVIDED, new ArrayList<RefinedSafetyEntry>());
    combiesToContextID.put(IValueCombie.HAZ_IF_PROVIDED, new ArrayList<RefinedSafetyEntry>());
    combiesToContextID.put(IValueCombie.HAZ_IF_WRONG_PROVIDED, new ArrayList<RefinedSafetyEntry>());
    if (getModel() == null) {
      return combiesToContextID;
    }

    getModel().lockUpdate();

    int count = 0;
    boolean consider;
    ArrayList<UUID> currentRSR = new ArrayList<>();
    for (IControlAction ca : getModel().getAllControlActionsU()) {
      consider = (caID == null) || ca.getId().equals(caID);
      if (getControlActionEntry(true, ca.getId()) == null) {
        fetchControlActions();
      }
      RefinedSafetyEntry entry;
      for (ContextTableCombination variable : getControlActionEntry(true, ca.getId())
          .getContextTableCombinations(false)) {
        if (variable.getConflict()) {
          // only variables without confilcts are forming rules
          continue;
        }
        if (variable.getHAnytime()) {
          count++;
          entry = RefinedSafetyEntry.getAnytimeEntry(count, variable, getModel());
          entry.setConstraintPrefix(constraintPrefix);
          entry.setRefinedCaPrefix(refinedCaPrefix);
          entry.setRuleLiteral(ruleLiteral);
          currentRSR.add(entry.getDataRef());
          if (consider) {
            combiesToContextID.get(IValueCombie.HAZ_IF_PROVIDED)
                .add(entry);
          }
        }
        if (variable.getHEarly()) {
          count++;
          entry = RefinedSafetyEntry.getTooEarlyEntry(count, variable, getModel());
          entry.setConstraintPrefix(constraintPrefix);
          entry.setRefinedCaPrefix(refinedCaPrefix);
          entry.setRuleLiteral(ruleLiteral);
          currentRSR.add(entry.getDataRef());
          if (consider) {
            combiesToContextID.get(IValueCombie.HAZ_IF_WRONG_PROVIDED)
                .add(entry);
          }
        }
        if (variable.getHLate()) {
          count++;
          entry = RefinedSafetyEntry.getTooLateEntry(count, variable, getModel());
          entry.setConstraintPrefix(constraintPrefix);
          entry.setRefinedCaPrefix(refinedCaPrefix);
          entry.setRuleLiteral(ruleLiteral);
          currentRSR.add(entry.getDataRef());
          if (consider) {
            combiesToContextID.get(IValueCombie.HAZ_IF_WRONG_PROVIDED).add(entry);
          }
        }
      }

      for (ContextTableCombination variable : getControlActionEntry(false, ca.getId())
          .getContextTableCombinations(false)) {
        if (variable.getGlobalHazardous() && !variable.getConflict()) {
          count++;
          entry = RefinedSafetyEntry.getNotProvidedEntry(count, variable, getModel());
          entry.setConstraintPrefix(constraintPrefix);
          entry.setRefinedCaPrefix(refinedCaPrefix);
          entry.setRuleLiteral(ruleLiteral);
          currentRSR.add(entry.getDataRef());
          if (consider) {
            combiesToContextID.get(IValueCombie.HAZ_IF_NOT_PROVIDED).add(entry);
          }
        }
      }
    }
    int total = getModel().getLTLPropertys().size() - 1;

    List<AbstractLTLProvider> list = new ArrayList<>(getModel().getLTLPropertys());
    for (int i = total; i >= 0; i--) {
      if (!currentRSR.contains(list.get(i).getRuleId())) {
        getModel().getExtendedDataController().removeRefinedSafetyRule(IExtendedDataModel.ScenarioType.BASIC_SCENARIO,
            false, list.get(i).getRuleId(), getModel().getLinkController());
      }
    }
    total = getModel().getLTLPropertys().size() - 1;
    getModel().releaseLockAndUpdate(new ObserverValue[] { ObserverValue.Extended_DATA });
    return combiesToContextID;
  }
  // =====================================================================
  // START Save function
  // =====================================================================

  /**
   * Store the Boolean Data (from the Context Table) in the Datamodel
   * 
   * @param caEntry
   *          the ControlActionEntrys or null for the currently linked which should be stored in the
   *          data model
   * @param updateValue
   *          TODO
   */
  public void storeBooleans(final ControlActionEntry caEntry, final ObserverValue updateValue) {
    List<ControlActionEntry> entries = null;
    if (caEntry != null) {
      entries = new ArrayList<>();
      entries.add(caEntry);
    }
    storeBooleans(entries, updateValue);

  }

  /**
   * Store the Boolean Data (from the Context Table) in the Datamodel
   * 
   * @param caEntry
   *          the ControlActionEntrys or null for the currently linked which should be stored in the
   *          data model
   * @param updateValue
   *          TODO
   */
  public void storeBooleans(List<ControlActionEntry> entries, ObserverValue updateValue) {
    if (entries == null) {
      entries = new ArrayList<>();
      entries.add(linkedCAE);
    }

    for (ControlActionEntry temp : entries) {
      if (temp == null) {
        temp = linkedCAE;
      }
      if (dependenciesIFProvided.containsValue(temp)) {
        syncCombiesWhenProvided(temp);
      } else {
        syncCombiesWhenNotProvided(temp);
      }
      getHazardousCombinations(null);

    }
    if (updateValue != null) {
      setChanged();
      notifyObservers(updateValue);
    }

  }

  private void syncCombiesWhenProvided(ControlActionEntry caEntry) {
    List<ProvidedValuesCombi> valuesIfProvided = new ArrayList<ProvidedValuesCombi>();
    ProvidedValuesCombi val = new ProvidedValuesCombi();
    // iteration over all value combinations registered for the linked control action
    for (ContextTableCombination combie : caEntry.getContextTableCombinations(false)) {
      val = new ProvidedValuesCombi();
      val.setValues(combie.getValueIDTOVariableIdMap());

      val.setUCALinks(combie.getUcaLinks(IValueCombie.TYPE_ANYTIME), IValueCombie.TYPE_ANYTIME);
      val.setUCALinks(combie.getUcaLinks(IValueCombie.TYPE_TOO_EARLY), IValueCombie.TYPE_TOO_EARLY);
      val.setUCALinks(combie.getUcaLinks(IValueCombie.TYPE_TOO_LATE), IValueCombie.TYPE_TOO_LATE);

      val.setArchived(combie.isArchived());
      val.setConstraint(combie.getRefinedSafetyRequirements());
      val.setHazardousAnyTime(combie.getHAnytime());
      val.setHazardousToEarly(combie.getHEarly());
      val.setHazardousToLate(combie.getHLate());
      valuesIfProvided.add(val);
    }
    getModel().setValuesWhenCAProvided(caEntry.getId(), valuesIfProvided);
  }

  private void syncCombiesWhenNotProvided(ControlActionEntry caEntry) {
    List<NotProvidedValuesCombi> valuesIfProvided = new ArrayList<NotProvidedValuesCombi>();
    NotProvidedValuesCombi val = new NotProvidedValuesCombi();
    // iteration over all value combinations registered for the linked control action
    for (ContextTableCombination combie : caEntry.getContextTableCombinations(false)) {
      val = new NotProvidedValuesCombi();
      val.setValues(combie.getValueIDTOVariableIdMap());

      val.setUCALinks(combie.getUcaLinks(IValueCombie.TYPE_NOT_PROVIDED), IValueCombie.TYPE_NOT_PROVIDED);

      val.setArchived(combie.isArchived());
      val.setConstraint(combie.getRefinedSafetyRequirements());
      val.setHazardous(combie.getGlobalHazardous());
      valuesIfProvided.add(val);
    }
    getModel().setValuesWhenCANotProvided(caEntry.getId(), valuesIfProvided);

  }

  // =====================================================================
  // END Save function
  // =====================================================================

  // ********************************************************************************************************************
  // Management of the control action dependencies

  /**
   * @return the dependenciesIFProvided
   */
  public Collection<ControlActionEntry> getDependenciesIFProvided() {
    return this.dependenciesIFProvided.values();
  }

  /**
   * @return the dependenciesNotProvided
   */
  public Collection<ControlActionEntry> getDependenciesNotProvided() {
    return this.dependenciesNotProvided.values();
  }

  /**
   * 
   * @param providedContext
   *          whether the context is 'provided' or not
   * @param id
   *          the id which is provided by astpa for the requested controlAction
   * @return the control action entry stored in the context map for the given id
   */
  public ControlActionEntry getControlActionEntry(boolean providedContext, UUID id) {
    if (providedContext) {
      return this.dependenciesIFProvided.get(id);
    }
    return this.dependenciesNotProvided.get(id);
  }

  /**
   * @return the linkedPMV
   */
  public ProcessModelVariables getLinkedPMV() {
    return this.linkedPMV;
  }

  /**
   * @param linkedPMV
   *          the linkedPMV to set
   */
  public void setLinkedPMV(ProcessModelVariables linkedPMV) {
    this.linkedPMV = linkedPMV;
  }

  /**
   * @return the linkedCAE
   */
  public ControlActionEntry getLinkedCAE() {
    return this.linkedCAE;
  }

  /**
   * 
   * @param provided
   *          whether the control action entry should be
   *          chosen out of the provided list or not
   * @param i
   *          the linkedCAE to set
   */
  public boolean setLinkedCAE(boolean provided, UUID i) {

    this.controlActionProvided = provided;
    return setLinkedCAE(i);
  }

  /**
   * this method sets the linked control action to the entry stored <br>
   * at the index i either in the {@link #dependenciesIFProvided} or the
   * {@link #dependenciesNotProvided} list<br>
   * depending on the {@link #controlActionProvided} choice
   * 
   * @param i
   *          the index in either the {@link #dependenciesIFProvided} or the
   *          {@link #dependenciesNotProvided} list or null if
   *          there is no linked control action at the time
   * 
   * @return whether the linked control action has changed
   */
  public boolean setLinkedCAE(UUID i) {
    if (i == null || !(this.dependenciesIFProvided.containsKey(i) || this.dependenciesNotProvided.containsKey(i))) {
      this.linkedCAE = null;
      return true;
    }
    if (this.controlActionProvided && !this.dependenciesIFProvided.get(i).equals(this.linkedCAE)) {
      this.linkedCAE = this.dependenciesIFProvided.get(i);
      return true;
    }
    if (!this.controlActionProvided && !this.dependenciesNotProvided.get(i).equals(this.linkedCAE)) {
      this.linkedCAE = this.dependenciesNotProvided.get(i);
      return true;
    }
    return false;
  }

  public boolean isControlActionProvided() {
    return controlActionProvided;
  }

  /**
   * @return the model
   */
  public IExtendedDataModel getModel() {
    return this.model;
  }

  /**
   * @param model
   *          the model to set
   */
  public void setModel(IExtendedDataModel model) {
    this.model = (IExtendedDataModel) model;
  }

  @Override
  public void update(Observable arg0, Object updatedValue) {
    final ObserverValue value = (ObserverValue) updatedValue;
    switch (value) {
    case CONTROL_ACTION:
    case CONTROL_STRUCTURE:
      new Runnable() {
        @Override
        public void run() {
          clear();
          setChanged();
          notifyObservers(value);
        }
      }.run();
    default:
      break;

    }
  }

  /**
   * @return the refinedEntrys
   */
  public List<RefinedSafetyEntry> getRefinedEntrys() {
    return this.refinedEntrys;
  }

  /**
   * @param refinedEntrys
   *          the refinedEntrys to set
   */
  public void setRefinedEntrys(List<RefinedSafetyEntry> refinedEntrys) {
    this.refinedEntrys = refinedEntrys;
  }
}
