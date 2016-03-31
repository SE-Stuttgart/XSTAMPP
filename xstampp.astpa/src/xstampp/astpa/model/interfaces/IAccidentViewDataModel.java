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
public interface IAccidentViewDataModel extends IDataModel,ICommonTables {

	/**
	 * Getter for all existing accidents
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return All accidents
	 */
	List<ITableModel> getAllAccidents();

	/**
	 * Adds an accident. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#ACCIDENT}
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param description
	 *            The description of the new accident
	 * @param title
	 *            The title of the new accident
	 * @return String ID of the new Accident
	 */
	UUID addAccident(String title, String description);

	/**
	 * Deletes an accident. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#ACCIDENT}
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param accidentId
	 *            The ID of the accident which has to be deleted
	 * @return true if the accident has been removed
	 */
	boolean removeAccident(UUID accidentId);

	/**
	 * Setter for the title of an accident. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#ACCIDENT}
	 * 
	 * @author Jarkko Heidenwag
	 * @param accidentId
	 *            The accident's id
	 * 
	 * @param title
	 *            The accident's new title
	 * @return true if the title has been set
	 */
	boolean setAccidentTitle(UUID accidentId, String title);

	/**
	 * Setter for the description of an accident. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#ACCIDENT}
	 * 
	 * @author Jarkko Heidenwag
	 * @param accidentId
	 *            The accidents's id
	 * 
	 * @param description
	 *            The accident's new description
	 * @return true if the description has been set
	 */
	boolean setAccidentDescription(UUID accidentId, String description);

	/**
	 * Get an accident by it's ID.
	 * 
	 * @author Jarkko Heidenwag, Patrick Wickenhaeuser
	 * @param accidentId
	 *            the ID of the accident.
	 * 
	 * @return the accident.
	 */
	ITableModel getAccident(UUID accidentId);

	/**
	 * Get all hazards linked to a certain accident.
	 * 
	 * @author Jarkko Heidenwag, Patrick Wickenhaeuser
	 * 
	 * @param accidentId
	 *            the accident of which all linked hazards should be returned.
	 * @return the list of all hazards being linked to the given accident.
	 */
	List<ITableModel> getLinkedHazards(UUID accidentId);
}
