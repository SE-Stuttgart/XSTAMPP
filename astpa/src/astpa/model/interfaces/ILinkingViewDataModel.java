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

package astpa.model.interfaces;

import java.util.List;
import java.util.UUID;

import astpa.model.ITableModel;

/**
 * Interface used to access the data model.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public interface ILinkingViewDataModel extends IDataModel {
	
	/**
	 * Get all accidents.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the list containing accidents.
	 */
	List<ITableModel> getAllAccidents();
	
	/**
	 * Get all hazards.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the list containing all hazards
	 */
	List<ITableModel> getAllHazards();
	
	/**
	 * Get all hazards linked to a certain accident.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param accidentId the accident of which all linked hazards should be
	 *            returned.
	 * @return the list of all hazards being linked to the given accident.
	 */
	List<ITableModel> getLinkedHazards(UUID accidentId);
	
	/**
	 * Get all accidents linked to a certain hazard.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param hazardId the hazard of which all linked accidents should be
	 *            returned.
	 * @return the list of all accidents being linked to the given hazard.
	 */
	List<ITableModel> getLinkedAccidents(UUID hazardId);
	
	/**
	 * Deletes the link between an accident and a hazard. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#HAZ_ACC_LINK}
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * 
	 * @param accidentId the accident of which a link will be deleted.
	 * 
	 * @param hazardId the hazard of which a link will be deleted.
	 * @return true if the link has been removed
	 */
	boolean deleteLink(UUID accidentId, UUID hazardId);
	
	/**
	 * Add a link between an accident and a hazard. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#HAZ_ACC_LINK}
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param accidentId the accident a hazard will be linked to.
	 * 
	 * @param hazardId the hazard an accident will be linked to.
	 * @return true if the link has been added
	 */
	boolean addLink(UUID accidentId, UUID hazardId);
}
