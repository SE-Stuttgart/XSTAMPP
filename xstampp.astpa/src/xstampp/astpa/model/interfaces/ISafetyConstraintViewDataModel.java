/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.model.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.ITableModel;
import xstampp.model.IDataModel;

/**
 * Interface to the Data Model for the Accident View
 * 
 * @author Jarkko Heidenwag
 * 
 */
public interface ISafetyConstraintViewDataModel extends IDataModel,ICommonTables {

	/**
	 * Getter for all existing safety constraints
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return All safety constraints
	 */
	List<ITableModel> getAllSafetyConstraints();

	/**
	 * Adds an safety constraint. <br>
	 * Triggers an update for
	 * {@link astpa.model.ObserverValue#SAFETY_CONSTRAINT}
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param description
	 *            The description of the new safety constraint
	 * @param title
	 *            The title of the new safety constraint
	 * @return String ID of the new Accident
	 */
	UUID addSafetyConstraint(String title, String description);

	/**
	 * Deletes an safety constraint. <br>
	 * Triggers an update for
	 * {@link astpa.model.ObserverValue#SAFETY_CONSTRAINT}
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param safetyConstraintId
	 *            The ID of the safety constraint which has to be deleted
	 * @return true if the safety constraint has been removed
	 */
	boolean removeSafetyConstraint(UUID safetyConstraintId);

	/**
	 * Setter for the title of an safety constraint. <br>
	 * Triggers an update for
	 * {@link astpa.model.ObserverValue#SAFETY_CONSTRAINT}
	 * 
	 * @author Jarkko Heidenwag
	 * @param safetyConstraintId
	 *            The safety constraint's id
	 * 
	 * @param title
	 *            The safety constraint's new title
	 * @return true if the title has been set
	 */
	boolean setSafetyConstraintTitle(UUID safetyConstraintId, String title);

	/**
	 * Setter for the description of an safety constraint. <br>
	 * Triggers an update for
	 * {@link astpa.model.ObserverValue#SAFETY_CONSTRAINT}
	 * 
	 * @author Jarkko Heidenwag
	 * @param safetyConstraintId
	 *            The safety constraints's id
	 * 
	 * @param description
	 *            The safety constraint's new description
	 * @return true if the description has been set
	 */
	boolean setSafetyConstraintDescription(UUID safetyConstraintId,
			String description);

	/**
	 * Get an safety constraint by it's ID.
	 * 
	 * @author Jarkko Heidenwag, Patrick Wickenhaeuser
	 * @param safetyConstraintId
	 *            the ID of the safety constraint.
	 * 
	 * @return the safety constraint.
	 */
	ITableModel getSafetyConstraint(UUID safetyConstraintId);

}
