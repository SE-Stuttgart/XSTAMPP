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

package xstampp.astpa.model.sds;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.ATableModel;
import xstampp.model.ObserverValue;

/**
 * Class for managing safety constraints, system goals and design requirements.
 * 
 * @author Jaqueline Patzek, Fabian Toth
 * @since 2.0
 * 
 */
public class SDSController {

	@XmlElementWrapper(name = "safetyConstraints")
	@XmlElement(name = "safetyConstraint")
	private List<SafetyConstraint> safetyConstraints;

	@XmlElementWrapper(name = "systemGoals")
	@XmlElement(name = "systemGoal")
	private List<SystemGoal> systemGoals;

	@XmlElementWrapper(name = "designRequirements")
	@XmlElement(name = "designRequirement")
	private List<DesignRequirement> designRequirements;

	/**
	 * 
	 * Constructor of the SDSCotnroller
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public SDSController() {
		this.safetyConstraints = new ArrayList<>();
		this.systemGoals = new ArrayList<>();
		this.designRequirements = new ArrayList<>();
	}

	/**
	 * Adds a new safety constraint to the list of safety constraints.
	 * 
	 * @param title
	 *            the title of the new safety constraint
	 * @param description
	 *            the description of the new safety constraint
	 * 
	 * @return the id of the new safety constraint
	 * 
	 * @author Fabian Toth
	 */
	public UUID addSafetyConstraint(String title, String description) {
		SafetyConstraint safetyConstraint = new SafetyConstraint(title,
				description, this.safetyConstraints.size() + 1);
		this.safetyConstraints.add(safetyConstraint);
		return safetyConstraint.getId();
	}

	/**
	 * Gives a list of all Safety Constraints.
	 * 
	 * @return a list of Safety Contraints
	 * 
	 * @author Fabian Toth
	 */
	public List<ITableModel> getAllSafetyConstraints() {
		List<ITableModel> result = new ArrayList<>();
		for (SafetyConstraint safetyConstraint : this.safetyConstraints) {
			result.add(safetyConstraint);
		}
		return result;
	}
	public boolean moveEntry(boolean moveUp,UUID id,ObserverValue value){
		if(value.equals(ObserverValue.SYSTEM_GOAL)){
			move(moveUp, id, systemGoals);
		}else if(value.equals(ObserverValue.SAFETY_CONSTRAINT)){
			move(moveUp, id, safetyConstraints);
		}else if(value.equals(ObserverValue.DESIGN_REQUIREMENT)){
			move(moveUp, id, designRequirements);
		}
		return true;
	}
	
	private <T> boolean move(boolean up,UUID id, List<T> list){
		for (int i = 0; i < list.size(); i++) {
			if(((ITableModel)list.get(i)).getId().equals(id)){
				T downModel = null;
				T upModel = null;
				int moveIndex = i;
				/* if up is true than the ITable model with the given id should move up
				 * if this is possible(if there is a model right to it in the list) than 
				 * the model which is right to it is moved down else the model itself is moved down
				 */
				if(up && i + 1 > list.size()){
					return false;
				}else if(up){
					downModel = ((T)list.get(i+1));
					moveIndex = i;
				}else if(i == 0){
					return false;
				}else{
					downModel = ((T)list.get(i));
					moveIndex = i-1;
				}
				upModel = ((T)list.get(moveIndex));
				if(upModel instanceof ATableModel && downModel instanceof ATableModel){
					((ATableModel) downModel).setNumber(moveIndex + 1);
					((ATableModel) upModel).setNumber(moveIndex + 2);
				}
				list.remove(downModel);
				list.add(moveIndex, downModel);
				return true;
			}
		}
		return false;
	}
	/**
	 * Getter for a specific Safety Constraint. Returns null if there is no
	 * safety constraint with this id
	 * 
	 * @param safetyConstraintId
	 *            the id of the safety constraint
	 * @return safety constraint object
	 * 
	 * @author Fabian Toth
	 */
	public ITableModel getSafetyConstraint(UUID safetyConstraintId) {
		for (ITableModel s : this.safetyConstraints) {
			if (s.getId().equals(safetyConstraintId)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Removes a safety constraint from the list of safety constraints.
	 * 
	 * @param safetyConstraintId
	 *            the id of the safety constraint
	 * 
	 * @return true if the safety constraint has been removed
	 * 
	 * @author Fabian Toth
	 */
	public boolean removeSafetyConstraint(UUID safetyConstraintId) {
		ITableModel safetyConstraint = this
				.getSafetyConstraint(safetyConstraintId);
		int index = this.safetyConstraints.indexOf(safetyConstraint);
		this.safetyConstraints.remove(index);
		for (; index < this.safetyConstraints.size(); index++) {
			this.safetyConstraints.get(index).setNumber(index + 1);
		}
		return true;
	}

	/**
	 * Adds a new system goal to the list of system goals.
	 * 
	 * @param title
	 *            the title of the new system goal
	 * @param description
	 *            the description of the new system goal
	 * 
	 * @return the id of the new system goal
	 * 
	 * @author Fabian Toth
	 */
	public UUID addSystemGoal(String title, String description) {
		SystemGoal systemGoal = new SystemGoal(title, description,
				this.systemGoals.size() + 1);
		this.systemGoals.add(systemGoal);
		return systemGoal.getId();
	}

	/**
	 * Gives a list of all System Goals.
	 * 
	 * @return a list of Safety Goals
	 * 
	 * @author Fabian Toth
	 */
	public List<ITableModel> getAllSystemGoals() {
		List<ITableModel> result = new ArrayList<>();
		for (SystemGoal systemGoal : this.systemGoals) {
			result.add(systemGoal);
		}
		return result;
	}

	/**
	 * Gives the system goal object that belongs to the given id
	 * 
	 * @param systemGoalId
	 *            the id of the system goal
	 * @return system goal object
	 * 
	 * @author Jaqueline Patzek, Fabian Toth
	 */
	public ITableModel getSystemGoal(UUID systemGoalId) {
		for (ITableModel s : this.systemGoals) {
			if (s.getId().equals(systemGoalId)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Removes the system goal with the given id from the list of system goals.
	 * 
	 * @param systemGoalId
	 *            the id of the system goal
	 * 
	 * @return true if the system goal has been removed
	 * 
	 * @author Jaqueline Patzek, Fabian Toth
	 */
	public boolean removeSystemGoal(UUID systemGoalId) {
		ITableModel systemGoal = this.getSystemGoal(systemGoalId);
		int index = this.systemGoals.indexOf(systemGoal);
		this.systemGoals.remove(index);
		for (; index < this.systemGoals.size(); index++) {
			this.systemGoals.get(index).setNumber(index + 1);
		}
		return true;
	}

	/**
	 * Adds a new design requirement to the list of design requirements.
	 * 
	 * @param title
	 *            the title of the new design requirement
	 * @param description
	 *            the title of the new design requirement
	 * 
	 * @return the id of the new design requirement
	 * 
	 * @author Fabian Toth
	 */
	public UUID addDesignRequirement(String title, String description) {
		DesignRequirement designRequirement = new DesignRequirement(title,
				description, this.designRequirements.size() + 1);
		this.designRequirements.add(designRequirement);
		return designRequirement.getId();
	}

	/**
	 * Gives a list of all Design Requirements.
	 * 
	 * @return a list of Design Requirements
	 * 
	 * @author Fabian Toth
	 */
	public List<ITableModel> getAllDesignRequirements() {
		List<ITableModel> result = new ArrayList<>();
		for (DesignRequirement designRequirement : this.designRequirements) {
			result.add(designRequirement);
		}
		return result;
	}

	/**
	 * Gives the design requirement object that belongs to the given id
	 * 
	 * @param designRequirementId
	 *            ID of the design requirement
	 * @return design requirement object
	 * 
	 * @author Jaqueline Patzek, Fabian Toth
	 */
	public ITableModel getDesignRequirement(UUID designRequirementId) {
		for (ITableModel d : this.designRequirements) {
			if (d.getId().equals(designRequirementId)) {
				return d;
			}
		}

		return null;
	}

	/**
	 * Removes the design requirement with the given id from the list of design
	 * requirements.
	 * 
	 * @param designRequirementId
	 *            ID of the design requirement
	 * @return true if the design requirement has been removed
	 * 
	 * @author Jaqueline Patzek, Fabian Toth
	 */
	public boolean removeDesignRequirement(UUID designRequirementId) {
		ITableModel designRequirement = this
				.getDesignRequirement(designRequirementId);
		int index = this.designRequirements.indexOf(designRequirement);
		this.designRequirements.remove(index);
		for (; index < this.designRequirements.size(); index++) {
			this.designRequirements.get(index).setNumber(index + 1);
		}
		return true;
	}
}
