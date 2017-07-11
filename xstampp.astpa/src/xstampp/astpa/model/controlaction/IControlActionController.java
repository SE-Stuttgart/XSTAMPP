package xstampp.astpa.model.controlaction;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.ControlStructureController;
import xstampp.astpa.model.extendedData.ExtendedDataController;
import xstampp.astpa.model.extendedData.RefinedSafetyRule;
import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ICorrespondingSafetyConstraintDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.IEntryFilter;
import xstampp.model.IValueCombie;
import xstampp.model.ObserverValue;

public interface IControlActionController {

  /**
   * Creates a new control action and adds it to the list of control actions.
   * 
   * @param title
   *          the title of the new control action
   * @param description
   *          the description of the new control action
   * @return the ID of the new control action
   * 
   * @author Fabian Toth
   */
  UUID addControlAction(String title, String description);

  /**
   * Removes the control action from the list of control actions
   * 
   * @param controlActionId
   *          control action's ID
   * 
   * @return true if the control action has been removeds
   * 
   * @author Fabian Toth
   */
  boolean removeControlAction(UUID controlActionId);

  /**
   * This function pops ControlActions out of a Trash
   * 
   * @author Lukas Balzer
   * 
   * @param id
   *          the id of the ControlAction which shall be recovered
   * @return whether the ControlAction has been recovered or not
   */
  boolean recoverControlAction(UUID id);

  /**
   * Searches for an Accident with given ID returns null if there is no one with this id
   * 
   * @param controlActionId
   *          the id of the control action
   * @return found control action
   * 
   * @author Fabian Toth
   */
  ITableModel getControlAction(UUID controlActionId);

  /**
   * Gets all control actions
   * 
   * @return all control actions
   * 
   * @author Fabian Toth
   */
  List<ITableModel> getAllControlActions();

  /**
   * Getter for the Control Actions
   * 
   * @return the list of Control Actions
   * 
   * @author Fabian Toth
   */
  List<IControlAction> getAllControlActionsU();

  /**
   * Get the control action by its id
   * 
   * @param controlActionId
   *          id of the control action
   * @return the control action with the given id
   * 
   * @author Fabian Toth
   */
  IControlAction getControlActionU(UUID controlActionId);

  /**
   * Adds a unsafe control action to the control action with the given id
   * 
   * @param controlActionId
   *          the id of the control action
   * @param description
   *          the description of the new unsafe control action
   * @param unsafeControlActionType
   *          the type of the new unsafe control action
   * @return the id of the new unsafe control action
   * 
   * @author Fabian Toth
   */
  UUID addUnsafeControlAction(UUID controlActionId, String description,
      UnsafeControlActionType unsafeControlActionType);

  /**
   * Adds a unsafe control action to the control action with the given id
   * 
   * @param controlActionId
   *          the id of the control action
   * @param description
   *          the description of the new unsafe control action
   * @param unsafeControlActionType
   *          the type of the new unsafe control action
   * @return the id of the new unsafe control action
   * 
   * @author Fabian Toth
   */
  UUID addUnsafeControlAction(UUID controlActionId, String description,
      UnsafeControlActionType unsafeControlActionType, UUID ucaId);

  /**
   * Searches the unsafe control action and removes it
   * 
   * @param unsafeControlActionId
   *          the id of the unsafe control action to delete
   * @return true if the unsafe control action has been removed
   * 
   * @author Fabian Toth
   */
  boolean removeUnsafeControlAction(UUID unsafeControlActionId);

  /**
   * Gets the links of the unsafe control action
   * 
   * @param unsafeControlActionId
   *          the id of the unsafe control action
   * @return the links of the unsafe control action
   * 
   * @author Fabian Toth
   */
  List<UUID> getLinksOfUCA(UUID unsafeControlActionId);

  /**
   * Adds a link between a unsafe control action and a hazard
   * 
   * @param unsafeControlActionId
   *          the id of the unsafe control action
   * @param hazardId
   *          the id of the hazard
   * 
   * @return true if the link has been added
   * 
   * @author Fabian Toth
   */
  boolean addUCAHazardLink(UUID unsafeControlActionId, UUID hazardId);

  /**
   * Removes the link between a unsafe control action and a hazard
   * 
   * @param unsafeControlActionId
   *          the unsafe control action of which a link will be deleted.
   * @param hazardId
   *          the hazard of which a link will be deleted.
   * 
   * @return true if the link has been removed
   * 
   * @author Fabian Toth
   */
  boolean removeUCAHazardLink(UUID unsafeControlActionId, UUID hazardId);

  /**
   * Removes all links that links the given id
   * 
   * @author Fabian Toth
   * 
   * @param id
   *          the id of one part of the link
   * @return true, if every link has been removed
   */
  boolean removeAllLinks(UUID id);

  /**
   * Set the description of an unsafe control action.
   * 
   * @param unsafeControlActionId
   *          the uca's id.
   * @param description
   *          the new description.
   * 
   * @author Patrick Wickenhaeuser, Fabian Toth
   * @return true, if the description has been set
   */
  String setUcaDescription(UUID unsafeControlActionId, String description);

  /**
   * gets all uca entries that are marked as hazardous means that they are linked to at least one
   * hazard and calls {@link ICorrespondingSafetyConstraintDataModel#getAllUnsafeControlActions()}
   * 
   * @author Fabian Toth, Lukas Balzer
   * 
   * @return the list of all ucas with at leatst one hazard link
   */
  List<ICorrespondingUnsafeControlAction> getAllUnsafeControlActions();

  /**
   * creates a new list with all entries according to the given {@link IEntryFilter} or with all
   * uca's defined if the filter is given as <b>null</b> <p> Note that modifications of the returned
   * list will not affect the list stored in the dataModel
   * 
   * @param filter
   *          an implementation of {@link IEntryFilter} which checks {@link IUnsafeControlAction}'s
   *          or <b>null</b> if there shouldn't be a check
   * @return a new list with all suiting uca entries, or an empty list if either all entries have
   *         been filtered or there are no etries
   */
  List<ICorrespondingUnsafeControlAction> getUCAList(IEntryFilter<IUnsafeControlAction> filter);

  /**
   * returns the current id number of the UnsafeControlAction with the given ucaID
   * 
   * @param ucaID
   *          the UnsafeControlAction id
   * @return the current id
   */
  int getUCANumber(UUID ucaID);

  /**
   * Sets the corresponding safety constraint of the unsafe control action which is identified by
   * the given id
   * 
   * @author Fabian Toth
   * 
   * @param unsafeControlActionId
   *          the id of the unsafe control action
   * @param safetyConstraintDescription
   *          the text of the corresponding safety constraint
   * @return the id of the corresponding safety constraint. null if the action fails
   */
  String setCorrespondingSafetyConstraint(UUID unsafeControlActionId,
      String safetyConstraintDescription);

  /**
   * Gets the list of all corresponding safety constraints
   * 
   * @author Fabian Toth
   * 
   * @return the list of all corresponding safety constraints
   */
  List<ITableModel> getCorrespondingSafetyConstraints();

  /**
   * Prepares the control actions for the export
   * 
   * @author Fabian Toth
   * @param linkController
   * 
   * @param hazAccController
   *          the hazAccController to get the Accidents as objects
   * @param extendedData
   * 
   */
  void prepareForExport(LinkController linkController, IHazAccController hazAccController,
      ControlStructureController csController, String defaultLabel,
      IExtendedDataController extendedData);

  /**
   * Prepares the control actions for save
   * 
   * @author Fabian Toth
   * @param extendedData
   * @param linkController
   * 
   */
  void prepareForSave(ExtendedDataController extendedData, LinkController linkController);

  List<UCAHazLink> getAllUCALinks();

  /**
   * @param componentLink
   *          the componentLink to set
   * @param caId
   *          the control action which should be linked
   * @return
   */
  boolean setComponentLink(UUID componentLink, UUID caId);

  /**
   * @return the isSafetyCritical
   * @param caID
   *          the control action id which is used to look up the action
   */
  boolean isSafetyCritical(UUID caID);

  /**
   * @param caID
   *          the control action id which is used to look up the action
   * @param isSafetyCritical
   *          the isSafetyCritical to set
   */
  boolean setSafetyCritical(UUID caID, boolean isSafetyCritical);

  /**
   * @param caID
   *          the control action id which is used to look up the action
   * @return the valuesWhenNotProvided
   */
  List<NotProvidedValuesCombi> getValuesWhenNotProvided(UUID caID);

  /**
   * @param caID
   *          the control action id which is used to look up the action
   * @param valuesWhenNotProvided
   *          the valuesWhenNotProvided to set
   */
  void setValuesWhenNotProvided(UUID caID, List<NotProvidedValuesCombi> valuesWhenNotProvided);

  /**
   * adds the given values combination to the list of value combinations in which the system gets
   * into a hazardous state if the control action is not provided
   * 
   * @param caID
   *          the uuid object of the control action
   * @param valueWhenNotProvided
   *          the values combination
   * @return whether or not the operation was successful, null if the given uuid is no legal
   *         controlAction id
   */
  boolean addValueWhenNotProvided(UUID caID, NotProvidedValuesCombi valueWhenNotProvided);

  /**
   * removes the given value combinations from the list of value combinations in which the system
   * gets into a hazardous state if the control action is not provided
   * 
   * @param caID
   *          the uuid object of the control action
   * @param combieId
   *          TODO
   * @return whether or not the operation was successful, null if the given uuid is no legal
   *         controlAction id
   */
  boolean removeValueWhenNotProvided(UUID caID, UUID combieId);

  /**
   * @param caID
   *          the control action id which is used to look up the action
   * @return the valuesWhenProvided
   */
  List<ProvidedValuesCombi> getValuesWhenProvided(UUID caID);

  /**
   * @param caID
   *          the control action id which is used to look up the action
   * @param valuesWhenProvided
   *          the valuesWhenProvided to set
   */
  void setValuesWhenProvided(UUID caID, List<ProvidedValuesCombi> valuesWhenProvided);

  /**
   * adds the given values combination to the list of value combinations in which the system gets
   * into a hazardous state if the control action is provided
   * 
   * @param caID
   *          the uuid object of the control action
   * @param valueWhenNotProvided
   *          the values combination
   * @return whether or not the operation was successful, null if the given uuid is no legal
   *         controlAction id
   */
  boolean addValueWhenProvided(UUID caID, ProvidedValuesCombi valueWhenProvided);

  /**
   * removes the given value combinations from the list of value combinations in which the system
   * gets into a hazardous state if the control action is provided
   * 
   * @param caID
   *          the uuid object of the control action
   * @param combieId
   *          TODO
   * @param valueWhenNotProvided
   *          the values combination
   * @return whether or not the operation was successful, null if the given uuid is no legal
   *         controlAction id
   */
  boolean removeValueWhenProvided(UUID caID, UUID combieId);

  /**
   * @param caID
   *          the control action id which is used to look up the action
   *          {@link ControlAction#getNotProvidedVariables()}
   * @return {@link ControlAction#getProvidedVariables()}
   */
  List<UUID> getNotProvidedVariables(UUID caID);

  /**
   * 
   * {@link ControlAction#getProvidedVariables()}
   * 
   * @param caID
   *          the control action id which is used to look up the action
   * 
   * @param notProvidedVariable
   *          the notProvidedVariables to set
   */
  void addNotProvidedVariable(UUID caID, UUID notProvidedVariable);

  /**
   * @param caID
   *          the control action id which is used to look up the action
   *          {@link ControlAction#getProvidedVariables()}
   * @return a copie of the provided variables list
   */
  List<UUID> getProvidedVariables(UUID caID);

  /**
   * @param caID
   *          the control action id which is used to look up the action
   *          {@link ControlAction#addProvidedVariable(UUID)}
   * 
   * @param providedVariable
   *          the providedVariable to add
   */
  void addProvidedVariable(UUID caID, UUID providedVariable);

  /**
   * 
   * remove the uuid of a process variable component from the list of variables depending on this
   * control action when not provided
   * 
   * @param caID
   *          the control action id which is used to look up the action
   * @param notProvidedVariable
   *          the notProvidedVariables to remove
   * @return return whether the remove was successful or not, also returns false if the list is null
   *         or the uuid is not contained in the list
   */
  boolean removeNotProvidedVariable(UUID caID, UUID notProvidedVariable);

  /**
   * 
   * @param caID
   *          the control action id which is used to look up the action remove the uuid of a process
   *          variable component from the list of variables depending on this control action when
   *          provided
   * 
   * @param providedVariable
   *          the providedVariable to remove
   * @return return whether the remove was successful or not, also returns false if the list is null
   *         or the uuid is not contained in the list
   */
  boolean removeProvidedVariable(UUID caID, UUID providedVariable);

  /**
   * 
   * @param onlyFormal
   *          if the returned list should only include the rules that where created in the Context
   *          Table
   * 
   * @return a list containing all rules or only the rules created formally in the context table
   */
  List<AbstractLTLProvider> getAllRefinedRules(boolean onlyFormal);

  /**
   * adds the refined rule to the set of rules defined in the related control action
   * 
   * @param ucaLinks
   *          {@link AbstractLTLProvider#getUCALinks()}
   * @param combies
   *          {@link RefinedSafetyRule#getCriticalCombies()}
   * @param ltlExp
   *          {@link AbstractLTLProvider#getLtlProperty()}
   * @param rule
   *          {@link AbstractLTLProvider#getSafetyRule()}
   * @param ruca
   *          {@link AbstractLTLProvider#getRefinedUCA()}
   * @param constraint
   *          {@link AbstractLTLProvider#getRefinedSafetyConstraint()}
   * @param nr
   *          {@link AbstractLTLProvider#getNumber()}
   * @param caID
   *          {@link AbstractLTLProvider#getRelatedControlActionID()}
   * @param type
   *          {@link AbstractLTLProvider#getType()}
   * 
   * @see IValueCombie
   * @return the uuid of the added refined rule, or null if no rule could be added because of a bad
   *         value e.g. <code>caID == null</code>
   */
  boolean addRefinedRuleLink(UUID ruleID, UUID caID);

  /**
   * This method removes a safety rule if it is stored as general rule or as rule in control action
   * 
   * @param removeAll
   *          whether all currently stored RefinedSafetyRule objects should be deleted<br> when this
   *          is true than the ruleId will be ignored
   * @param ruleId
   *          an id of a RefinedSafetyRule object stored in a controlAction
   * 
   * @return whether the delete was successful or not, also returns false if the rule could not be
   *         found or the id was illegal
   */
  boolean removeSafetyRule(boolean removeAll, UUID id);

  boolean usesHAZXData();

  IControlAction getControlActionFor(UUID ucaId);

  boolean moveEntry(boolean moveUp, UUID id, ObserverValue value);

  void setNextUcaIndext(int nextUcaIndext);

  void setUCACustomHeaders(String[] ucaHeaders);

  String[] getUCAHeaders();

}