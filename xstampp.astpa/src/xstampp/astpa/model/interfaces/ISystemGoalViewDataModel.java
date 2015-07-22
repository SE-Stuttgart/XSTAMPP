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
public interface ISystemGoalViewDataModel extends IDataModel {

	/**
	 * Getter for all existing system goals
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return All system goals
	 */
	List<ITableModel> getAllSystemGoals();

	/**
	 * Adds an system goal. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#SYSTEM_GOAL}
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param description
	 *            The description of the new system goal
	 * @param title
	 *            The title of the new system goal
	 * @return String ID of the new Accident
	 */
	UUID addSystemGoal(String title, String description);

	/**
	 * Deletes an system goal. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#SYSTEM_GOAL}
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param systemGoalId
	 *            The ID of the system goal which has to be deleted
	 * @return true if the system goal has been removed
	 */
	boolean removeSystemGoal(UUID systemGoalId);

	/**
	 * Setter for the title of an system goal. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#SYSTEM_GOAL}
	 * 
	 * @author Jarkko Heidenwag
	 * @param systemGoalId
	 *            The system goal's id
	 * 
	 * @param title
	 *            The system goal's new title
	 * @return true if the title has been set
	 */
	boolean setSystemGoalTitle(UUID systemGoalId, String title);

	/**
	 * Setter for the description of an system goal. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#SYSTEM_GOAL}
	 * 
	 * @author Jarkko Heidenwag
	 * @param systemGoalId
	 *            The system goals's id
	 * 
	 * @param description
	 *            The system goal's new description
	 * @return true if the description has been set
	 */
	boolean setSystemGoalDescription(UUID systemGoalId, String description);

	/**
	 * Get an system goal by it's ID.
	 * 
	 * @author Jarkko Heidenwag, Patrick Wickenhaeuser
	 * @param systemGoalId
	 *            the ID of the system goal.
	 * 
	 * @return the system goal.
	 */
	ITableModel getSystemGoal(UUID systemGoalId);

}
