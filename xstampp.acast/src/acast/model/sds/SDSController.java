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

package acast.model.sds;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import acast.model.ITableModel;


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

	

	/**
	 * 
	 * Constructor of the SDSCotnroller
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public SDSController() {
		this.safetyConstraints = new ArrayList<>();
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

}
