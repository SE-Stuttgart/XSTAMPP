/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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
 * Interface to the Data Model for the Hazard View
 * 
 * @author Jarkko Heidenwag
 * 
 */
public interface IHazardViewDataModel extends IDataModel,ICommonTables {

	/**
	 * Getter for all existing hazards
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return All hazards
	 */
	List<ITableModel> getAllHazards();

	/**
	 * Adds a hazard. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#HAZARD}
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param description
	 *            The description of the new hazard
	 * @param title
	 *            The title of the new hazard
	 * @return String ID of the new Hazard
	 */
	UUID addHazard(String title, String description);

	/**
	 * Deletes a hazard. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#HAZARD}
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param hazardId
	 *            The ID of the hazards which has to be deleted
	 * @return true if the hazard has been removed
	 */
	boolean removeHazard(UUID hazardId);

	/**
	 * Setter for the title of a hazards. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#HAZARD}
	 * 
	 * @author Jarkko Heidenwag
	 * @param hazardId
	 *            The hazard's id
	 * 
	 * @param title
	 *            The hazards's new title
	 * @return true if the title has been set
	 */
	boolean setHazardTitle(UUID hazardId, String title);

	/**
	 * Setter for the description of a hazards. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#HAZARD}
	 * 
	 * @author Jarkko Heidenwag
	 * @param hazardId
	 *            The hazard's id
	 * 
	 * @param description
	 *            The hazards's new description
	 * @return true if the description has been set
	 */
	boolean setHazardDescription(UUID hazardId, String description);

	/**
	 * Get a hazard by its id.
	 * 
	 * @author Jarkko Heidenwag, Patrick Wickenhaeuser
	 * 
	 * @param hazardId
	 *            the id of the hazard.
	 * @return the requested hazard.
	 */
	ITableModel getHazard(UUID hazardId);

	/**
	 * getter for a sorted list containing all hazards 
	 * for the given list of ids
	 * 
	 * @param ids an a array of ids of hazard entries in the dataModel
	 * @return a sorted list containing with hazard models or or an empty list
	 *         if ids is an empty array
	 *         or no hazards could be found
	 */
	List<ITableModel> getHazards(List<UUID> ids);
	/**
	 * Get all accidents linked to a certain hazard.
	 * 
	 * @author Jarkko Heidenwag, Patrick Wickenhaeuser
	 * 
	 * @param hazardId
	 *            the hazard of which all linked accidents should be returned.
	 * @return the list of all accidents being linked to the given accident.
	 */
	List<ITableModel> getLinkedAccidents(UUID hazardId);
}
