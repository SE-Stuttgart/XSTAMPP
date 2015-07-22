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
import xstampp.astpa.haz.controlaction.UnsafeControlActionType;
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.model.IDataModel;

/**
 * Interface to the Data Model for the Control Actions Table
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser
 * 
 */
public interface IUnsafeControlActionDataModel extends IDataModel {

	/**
	 * Getter for the Control Actions
	 *
	 * @author Benedikt Markt
	 *
	 * @return the list of Control Actions
	 */
	List<IControlAction> getAllControlActionsU();

	/**
	 * Get the control action by its id
	 * 
	 * @author Benedikt Markt
	 * @param controlActionId
	 *            id of the control action
	 * 
	 * @return the control action with the given id or null
	 */
	IControlAction getControlActionU(UUID controlActionId);

	/**
	 * Add an unsafe control action to a given control action.<br>
	 * Triggers an update for
	 * {@link xstampp.model.ObserverValue#UNSAFE_CONTROL_ACTION}
	 * 
	 * @author Benedikt Markt, Patrick Wickenhaeuser
	 * @param controlActionId
	 *            the control action the unsafe control action will be added to.
	 * @param description
	 *            the description of the new unsafe control action.
	 * @param unsafeControlActionType
	 *            the type of the new unsafe control action
	 * @return the id of the new unsafe control action. null if the unsafe
	 *         control action could not be added
	 */
	UUID addUnsafeControlAction(UUID controlActionId, String description,
			UnsafeControlActionType unsafeControlActionType);

	/**
	 * Set the description of an unsafe control action. <br>
	 * Triggers an update for
	 * {@link xstampp.model.ObserverValue#UNSAFE_CONTROL_ACTION}
	 * 
	 * @param controlActionId
	 *            the id of the parent.
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action.
	 * @param description
	 *            the new description.
	 * 
	 * @author Patrick Wickenhaeuser
	 * @return true if the description has been set
	 */
	boolean setUcaDescription(UUID unsafeControlActionId, String description);

	/**
	 * Removes a unsafe control action.<br>
	 * Triggers an update for
	 * {@link xstampp.model.ObserverValue#UNSAFE_CONTROL_ACTION}
	 * 
	 * @author Fabian Toth
	 * 
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action to delete
	 * @return true if the unsafe control action could be removed
	 */
	boolean removeUnsafeControlAction(UUID unsafeControlActionId);

	/**
	 * Getter for the hazards that are linked to a unsafe control action
	 * 
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action
	 * @return the linked hazards of the unsafe control action
	 * 
	 * @author Fabian Toth
	 */
	List<ITableModel> getLinkedHazardsOfUCA(UUID unsafeControlActionId);

	/**
	 * Adds a link between a unsafe control action and a hazard.<br>
	 * Triggers an update for
	 * {@link xstampp.model.ObserverValue#UNSAFE_CONTROL_ACTION}
	 * 
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action
	 * @param hazardId
	 *            the id of the hazard
	 * 
	 * @author Fabian Toth
	 * @return true if the link has been added
	 */
	boolean addUCAHazardLink(UUID unsafeControlActionId, UUID hazardId);

	/**
	 * Deletes the link between an accident and a hazard. <br>
	 * Triggers an update for
	 * {@link xstampp.model.ObserverValue#UNSAFE_CONTROL_ACTION}
	 * 
	 * @param unsafeControlActionId
	 *            the unsafe control action of which a link will be deleted.
	 * @param hazardId
	 *            the hazard of which a link will be deleted.
	 * 
	 * @author Fabian Toth
	 * @return true if the link has been removed
	 */
	boolean removeUCAHazardLink(UUID unsafeControlActionId, UUID hazardId);

	/**
	 * Get all hazards.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the list containing all hazards
	 */
	List<ITableModel> getAllHazards();
}
