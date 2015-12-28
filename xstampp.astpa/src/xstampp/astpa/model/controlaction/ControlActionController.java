/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.controlaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import messages.Messages;
import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.haz.controlaction.UCAHazLink;
import xstampp.astpa.haz.controlaction.UnsafeControlActionType;
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.haz.controlaction.interfaces.IUCAHazLink;
import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.ISafetyConstraint;
import xstampp.astpa.model.controlaction.interfaces.IHAZXControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.hazacc.HazAccController;

/**
 * Manager class for control actions.
 * 
 * @author Jaqueline Patzek, Fabian Toth
 * 
 */
public class ControlActionController {

	@XmlElementWrapper(name = "controlactions")
	@XmlElement(name = "controlaction")
	private List<ControlAction> controlActions;

	@XmlElementWrapper(name = "links")
	@XmlElement(name = "link")
	private List<UCAHazLink> links;

	private final Map<UUID, ControlAction> trash;

	/**
	 * Constructor for the controller
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public ControlActionController() {
		this.trash = new HashMap<>();
		this.controlActions = new ArrayList<>();
		this.links = new ArrayList<>();
	}

	/**
	 * Creates a new control action and adds it to the list of control actions.
	 * 
	 * @param title
	 *            the title of the new control action
	 * @param description
	 *            the description of the new control action
	 * @return the ID of the new control action
	 * 
	 * @author Fabian Toth
	 */
	public UUID addControlAction(String title, String description) {
		ControlAction controlAction = new ControlAction(title, description,
				this.controlActions.size() + 1);
		this.controlActions.add(controlAction);
		return controlAction.getId();
	}

	/**
	 * Removes the control action from the list of control actions
	 * 
	 * @param controlActionId
	 *            control action's ID
	 * 
	 * @return true if the control action has been removeds
	 * 
	 * @author Fabian Toth
	 */
	public boolean removeControlAction(UUID controlActionId) {
		ControlAction controlAction = this
				.getInternalControlAction(controlActionId);
		for (IUnsafeControlAction unsafeControlAction : controlAction
				.getUnsafeControlActions()) {
			this.removeAllLinks(unsafeControlAction.getId());
		}
		int index = this.controlActions.indexOf(controlAction);
		this.controlActions.remove(index);
		this.trash.put(controlActionId, controlAction);
		for (; index < this.controlActions.size(); index++) {
			this.controlActions.get(index).setNumber(index + 1);
		}
		return true;
	}

	/**
	 * This function pops ControlActions out of a Trash
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param id
	 *            the id of the ControlAction which shall be recovered
	 * @return whether the ControlAction has been recovered or not
	 */
	public boolean recoverControlAction(UUID id) {
		if ((this.trash.size() > 0) && this.trash.containsKey(id)) {
			return this.controlActions.add(this.trash.get(id));
		}
		return false;
	}

	/**
	 * Searches for an Accident with given ID returns null if there is no one
	 * with this id
	 * 
	 * @param controlActionId
	 *            the id of the control action
	 * @return found control action
	 * 
	 * @author Fabian Toth
	 */
	public ITableModel getControlAction(UUID controlActionId) {
		for (ITableModel controlAction : this.controlActions) {
			if (controlAction.getId().equals(controlActionId)) {
				return controlAction;
			}
		}
		return null;
	}

	/**
	 * Gets all control actions
	 * 
	 * @return all control actions
	 * 
	 * @author Fabian Toth
	 */
	public List<ITableModel> getAllControlActions() {
		List<ITableModel> result = new ArrayList<>();
		for (ControlAction controlAction : this.controlActions) {
			result.add(controlAction);
		}
		return result;
	}

	/**
	 * Getter for the Control Actions
	 * 
	 * @return the list of Control Actions
	 * 
	 * @author Fabian Toth
	 */
	public List<IControlAction> getAllControlActionsU() {
		List<IControlAction> result = new ArrayList<>();
		for (ControlAction controlAction : this.controlActions) {
			result.add(controlAction);
		}
		return result;
	}

	/**
	 * Get the control action by its id
	 * 
	 * @param controlActionId
	 *            id of the control action
	 * @return the control action with the given id
	 * 
	 * @author Fabian Toth
	 */
	public IHAZXControlAction getControlActionU(UUID controlActionId) {
		for (IHAZXControlAction controlAction : this.controlActions) {
			if (controlAction.getId().equals(controlActionId)) {
				return controlAction;
			}
		}
		return null;
	}

	/**
	 * Adds a unsafe control action to the control action with the given id
	 * 
	 * @param controlActionId
	 *            the id of the control action
	 * @param description
	 *            the description of the new unsafe control action
	 * @param unsafeControlActionType
	 *            the type of the new unsafe control action
	 * @return the id of the new unsafe control action
	 * 
	 * @author Fabian Toth
	 */
	public UUID addUnsafeControlAction(UUID controlActionId,
			String description, UnsafeControlActionType unsafeControlActionType) {
		ControlAction controlAction = this
				.getInternalControlAction(controlActionId);
		if (controlAction == null) {
			return null;
		}
		return controlAction.addUnsafeControlAction(description,
				unsafeControlActionType);
	}

	/**
	 * Searches the unsafe control action and removes it
	 * 
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action to delete
	 * @return true if the unsafe control action has been removed
	 * 
	 * @author Fabian Toth
	 */
	public boolean removeUnsafeControlAction(UUID unsafeControlActionId) {
		for (ControlAction controlAction : this.controlActions) {
			for (IUnsafeControlAction uca : controlAction
					.getUnsafeControlActions()) {
				if (uca.getId().equals(unsafeControlActionId)) {
					this.removeAllLinks(unsafeControlActionId);
					return controlAction
							.removeUnsafeControlAction(unsafeControlActionId);
				}
			}
		}
		return false;
	}

	private ControlAction getInternalControlAction(UUID controlActionId) {
		for (ControlAction controlAction : this.controlActions) {
			if (controlAction.getId().equals(controlActionId)) {
				return controlAction;
			}
		}
		return null;
	}

	/**
	 * Gets the links of the unsafe control action
	 * 
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action
	 * @return the links of the unsafe control action
	 * 
	 * @author Fabian Toth
	 */
	public List<IUCAHazLink> getLinksOfUCA(UUID unsafeControlActionId) {
		List<IUCAHazLink> result = new ArrayList<>();
		for (UCAHazLink link : this.links) {
			if (link.containsId(unsafeControlActionId)) {
				result.add((IUCAHazLink) link);
			}
		}
		return result;
	}

	/**
	 * Adds a link between a unsafe control action and a hazard
	 * 
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action
	 * @param hazardId
	 *            the id of the hazard
	 * 
	 * @return true if the link has been added
	 * 
	 * @author Fabian Toth
	 */
	public boolean addUCAHazardLink(UUID unsafeControlActionId, UUID hazardId) {
		return this.links.add(new UCAHazLink(unsafeControlActionId, hazardId));
	}

	/**
	 * Removes the link between an accident and a hazard
	 * 
	 * @param unsafeControlActionId
	 *            the unsafe control action of which a link will be deleted.
	 * @param hazardId
	 *            the hazard of which a link will be deleted.
	 * 
	 * @return true if the link has been removed
	 * 
	 * @author Fabian Toth
	 */
	public boolean removeUCAHazardLink(UUID unsafeControlActionId, UUID hazardId) {
		return this.links
				.remove(new UCAHazLink(unsafeControlActionId, hazardId));
	}

	/**
	 * Removes all links that links the given id
	 * 
	 * @author Fabian Toth
	 * 
	 * @param id
	 *            the id of one part of the link
	 * @return true, if every link has been removed
	 */
	public boolean removeAllLinks(UUID id) {
		List<UCAHazLink> toDelete = new ArrayList<>();
		for (UCAHazLink link : this.links) {
			if (link.containsId(id)) {
				toDelete.add(link);
			}
		}
		return this.links.removeAll(toDelete);
	}

	/**
	 * Set the description of an unsafe control action.
	 * 
	 * @param unsafeControlActionId
	 *            the uca's id.
	 * @param description
	 *            the new description.
	 * 
	 * @author Patrick Wickenhaeuser, Fabian Toth
	 * @return true, if the description has been set
	 */
	public boolean setUcaDescription(UUID unsafeControlActionId,
			String description) {
		UnsafeControlAction unsafeControlAction = this
				.getInternalUnsafeControlAction(unsafeControlActionId);
		if (unsafeControlAction == null) {
			return false;
		}
		unsafeControlAction.setDescription(description);
		return true;
	}

	/**
	 * Gets all corresponding safety constraints
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the list of all corresponding safety constraints
	 */
	public List<ICorrespondingUnsafeControlAction> getAllUnsafeControlActions() {
		List<ICorrespondingUnsafeControlAction> result = new ArrayList<>();
		for (ControlAction controlAction : this.controlActions) {
			for (IUnsafeControlAction unsafeControlAction : controlAction
					.getUnsafeControlActions()) {
				//an unsafe controlaction is only to be conssidered if it leads to a hazard
				if (!this.getLinksOfUCA(unsafeControlAction.getId()).isEmpty()) {
					result.add((ICorrespondingUnsafeControlAction) unsafeControlAction);
				}
			}
		}
		return result;
	}
	
	/**
	 * returns the current id number of the UnsafeControlAction with the given ucaID
	 *  
	 * @param ucaID the UnsafeControlAction id
	 * @return the current id
	 */
	public int getUCANumber(UUID ucaID){
		int counter = 0;
		for (ControlAction controlAction : this.controlActions) {
			for (IUnsafeControlAction unsafeControlAction : controlAction
					.getUnsafeControlActions()) {
				counter++;
				if(unsafeControlAction.getId().equals(ucaID)){
					return counter;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Sets the corresponding safety constraint of the unsafe control action
	 * which is identified by the given id
	 * 
	 * @author Fabian Toth
	 * 
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action
	 * @param safetyConstraintDescription
	 *            the text of the corresponding safety constraint
	 * @return the id of the corresponding safety constraint. null if the action
	 *         fails
	 */
	public UUID setCorrespondingSafetyConstraint(UUID unsafeControlActionId,
			String safetyConstraintDescription) {
		UnsafeControlAction unsafeControlAction = this
				.getInternalUnsafeControlAction(unsafeControlActionId);
		if (unsafeControlAction == null) {
			return null;
		}
		unsafeControlAction.getCorrespondingSafetyConstraint().setText(
				safetyConstraintDescription);
		return unsafeControlAction.getCorrespondingSafetyConstraint().getId();
	}

	/**
	 * Gets the unsafe control action in an internal type. Do not use outside
	 * the data model
	 * 
	 * @author Fabian Toth
	 * 
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action
	 * @return the unsafe control action with the given id
	 */
	public UnsafeControlAction getInternalUnsafeControlAction(
			UUID unsafeControlActionId) {
		for (ControlAction controlAction : this.controlActions) {
			for (UnsafeControlAction unsafeControlAction : controlAction
					.getInternalUnsafeControlActions()) {
				if (unsafeControlAction.getId().equals(unsafeControlActionId)) {
					return unsafeControlAction;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the list of all corresponding safety constraints
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the list of all corresponding safety constraints
	 */
	public List<ISafetyConstraint> getCorrespondingSafetyConstraints() {
		List<ISafetyConstraint> result = new ArrayList<>();
		for (ICorrespondingUnsafeControlAction unsafeControlAction : this
				.getAllUnsafeControlActions()) {
			result.add(unsafeControlAction.getCorrespondingSafetyConstraint());
		}
		return result;
	}

	/**
	 * Prepares the control actions for the export
	 * 
	 * @author Fabian Toth
	 * 
	 * @param hazAccController
	 *            the hazAccController to get the Accidents as objects
	 * 
	 */
	public void prepareForExport(HazAccController hazAccController) {
		for (ControlAction controlAction : this.controlActions) {
			for (UnsafeControlAction unsafeControlAction : controlAction
					.getInternalUnsafeControlActions()) {
				List<ITableModel> linkedHazards = new ArrayList<>();
				for (IUCAHazLink link : this.getLinksOfUCA(unsafeControlAction
						.getId())) {
					linkedHazards.add(hazAccController.getHazard(link
							.getHazardId()));
				}
				Collections.sort(linkedHazards);
				StringBuffer linkString = new StringBuffer(); //$NON-NLS-1$
				if (linkedHazards.size() == 0) {
					linkString.append(Messages.ControlActionController_NotHazardous);
				} else {
					
					for (int i = 0;i < linkedHazards.size(); i++) {
						if (i != 0) {
							linkString.append(","); //$NON-NLS-1$
						}
						linkString.append("H-" + linkedHazards.get(i).getNumber());
					}
				}
				unsafeControlAction.setLinks(linkString.toString());
			}
		}
	}

	/**
	 * Prepares the control actions for save
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public void prepareForSave() {
		for (ControlAction controlAction : this.controlActions) {
			for (UnsafeControlAction unsafeControlAction : controlAction
					.getInternalUnsafeControlActions()) {
				unsafeControlAction.setLinks(null);
			}
		}
	}
	
	public List<UCAHazLink> getAllUCALinks() {
		return this.links;
	}
	
	/**
	 * @param componentLink the componentLink to set
	 * @param caId the control action which should be linked
	 */
	public void setComponentLink(UUID componentLink,UUID caId){
		ControlAction action = getInternalControlAction(caId);
		action.setComponentLink(componentLink);
	}
	
	/**
	 * @return the isSafetyCritical
	 * @param caID the control action id which is used to look up the action
	 */
	public boolean isSafetyCritical(UUID caID) {
		ControlAction action = getInternalControlAction(caID);
		return action.isCASafetyCritical();
	}


	/**
	 * @param caID the control action id which is used to look up the action
	 * @param isSafetyCritical the isSafetyCritical to set
	 */
	public void setSafetyCritical(UUID caID, boolean isSafetyCritical) {
		ControlAction action = getInternalControlAction(caID);
		action.setSafetyCritical(isSafetyCritical);
	}


	/**
	 * @param caID the control action id which is used to look up the action
	 * @return the valuesWhenNotProvided
	 */
	public List<NotProvidedValuesCombi> getValuesWhenNotProvided(UUID caID) {
		ControlAction action = getInternalControlAction(caID);
		if(action == null){
			return new ArrayList<>();
		}
		return action.getValuesAffectedWhenNotProvided();
	}


	/**
	 * @param caID the control action id which is used to look up the action
	 * @param valuesWhenNotProvided the valuesWhenNotProvided to set
	 */
	public void setValuesWhenNotProvided(UUID caID, List<NotProvidedValuesCombi> valuesWhenNotProvided) {
		ControlAction action = getInternalControlAction(caID);
		if(action == null){
			return;
		}
		action.setValuesWhenNotProvided(valuesWhenNotProvided);
	}


	/**
	 * adds the given values combination to the list of value combinations
	 * in which the system gets into a hazardous state if the control action is not provided
	 * 
	 * @param caID the uuid object of the control action
	 * @param valueWhenNotProvided the values combination
	 * @return whether or not the operation was successful, null if the given uuid is no legal controlAction id 
	 */
	public boolean addValueWhenNotProvided(UUID caID, NotProvidedValuesCombi valueWhenNotProvided) {
		ControlAction action = getInternalControlAction(caID);
		if(action == null){
			return false;
		}
		return action.addValuesWhenNotProvided(valueWhenNotProvided);
	}

	/**
	 * removes the given value combinations from the list of value combinations
	 * in which the system gets into a hazardous state if the control action is not provided
	 * 
	 * @param caID the uuid object of the control action
	 * @param combieId TODO
	 * @return whether or not the operation was successful, null if the given uuid is no legal controlAction id 
	 */
	public boolean removeValueWhenNotProvided(UUID caID, UUID combieId) {
		ControlAction action = getInternalControlAction(caID);
		if(action == null){
			return false;
		}
		return action.removeValuesWhenNotProvided(combieId);
	}
	
	/**
	 * @param caID the control action id which is used to look up the action
	 * @return the valuesWhenProvided
	 */
	public List<ProvidedValuesCombi> getValuesWhenProvided(UUID caID) {
		ControlAction action = getInternalControlAction(caID);
		return action.getValuesAffectedWhenProvided();
	}


	/**
	 * @param caID the control action id which is used to look up the action
	 * @param valuesWhenProvided the valuesWhenProvided to set
	 */
	public void setValuesWhenProvided(UUID caID, List<ProvidedValuesCombi> valuesWhenProvided) {
		ControlAction action = getInternalControlAction(caID);
		action.setValuesWhenProvided(valuesWhenProvided);
	}

	/**
	 * adds the given values combination to the list of value combinations
	 * in which the system gets into a hazardous state if the control action is provided
	 * 
	 * @param caID the uuid object of the control action
	 * @param valueWhenNotProvided the values combination
	 * @return whether or not the operation was successful, null if the given uuid is no legal controlAction id 
	 */
	public boolean addValueWhenProvided(UUID caID, ProvidedValuesCombi valueWhenProvided) {
		ControlAction action = getInternalControlAction(caID);
		if(action == null){
			return false;
		}
		return action.addValueWhenProvided(valueWhenProvided);
	}

	/**
	 * removes the given value combinations from the list of value combinations
	 * in which the system gets into a hazardous state if the control action is provided
	 * 
	 * @param caID the uuid object of the control action
	 * @param combieId TODO
	 * @param valueWhenNotProvided the values combination
	 * @return whether or not the operation was successful, null if the given uuid is no legal controlAction id 
	 */
	public boolean removeValueWhenProvided(UUID caID, UUID combieId) {
		ControlAction action = getInternalControlAction(caID);
		if(action == null){
			return false;
		}
		return action.removeValueWhenProvided(combieId);
	}
	/**
	 * @param caID the control action id which is used to look up the action
	 * {@link ControlAction#getNotProvidedVariables()}
	 * @return {@link ControlAction#getProvidedVariables()}
	 */
	public List<UUID> getNotProvidedVariables(UUID caID) {
		ControlAction action = getInternalControlAction(caID);
		return action.getNotProvidedVariables();
	}


	/**
	 * 
	 * {@link ControlAction#getProvidedVariables()}
	 * @param caID the control action id which is used to look up the action
	 * 
	 * @param notProvidedVariable the notProvidedVariables to set
	 */
	public void addNotProvidedVariable(UUID caID, UUID notProvidedVariable) {
		ControlAction action = getInternalControlAction(caID);
		action.addNotProvidedVariable(notProvidedVariable);
	}


	/**
	 * @param caID the control action id which is used to look up the action
	 * {@link ControlAction#getProvidedVariables()}
	 * @return a copie of the provided variables list
	 */
	public List<UUID> getProvidedVariables(UUID caID) {
		ControlAction action = getInternalControlAction(caID);
		return action.getProvidedVariables();
	}


	/**
	 * @param caID the control action id which is used to look up the action
	 * {@link ControlAction#addProvidedVariable(UUID)}
	 * 
	 * @param providedVariable the providedVariable to add
	 */
	public void addProvidedVariable(UUID caID, UUID providedVariable) {
		ControlAction action = getInternalControlAction(caID);
		action.addProvidedVariable(providedVariable);
	}
	
	/**
	 * 
	 * remove the uuid of a process variable component from the list
	 * of variables depending on this control action when not provided
	 * 
	 * @param caID the control action id which is used to look up the action
	 * @param notProvidedVariable the notProvidedVariables to remove
	 * @return return whether the remove was successful or not, also returns false
	 * 			if the list is null or the uuid is not contained in the list 
	 */
	public boolean removeNotProvidedVariable(UUID caID, UUID notProvidedVariable) {
		ControlAction action = getInternalControlAction(caID);
		if(action == null){
			return false;
		}
		
		return action.removeNotProvidedVariable(notProvidedVariable);
	}
	
	/**
	 * 
	 * @param caID the control action id which is used to look up the action
	 * remove the uuid of a process variable component from the list
	 * of variables depending on this control action when provided
	 * 
	 * @param providedVariable the providedVariable to remove
	 * @return return whether the remove was successful or not, also returns false
	 * 			if the list is null or the uuid is not contained in the list 
	 */
	public boolean removeProvidedVariable(UUID caID, UUID providedVariable) {
		ControlAction action = getInternalControlAction(caID);
		if(action == null){
			return false;
		}
		
		return action.removeProvidedVariable(providedVariable);
	}
	
	public boolean usesHAZXData(){
		for(ControlAction action : this.controlActions){
			if(action.isCASafetyCritical()) return true;
			if(!action.getNotProvidedVariables().isEmpty()) return true;
			if(!action.getProvidedVariables().isEmpty()) return true;
		}
		return false;
	}
}
