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
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.model.IDataModel;

/**
 * Interface to the Data Model for the control action View
 * 
 * @author Jarkko Heidenwag, Fabian Toth
 * 
 */
public interface IControlActionViewDataModel extends IDataModel,ICommonTables {

	/**
	 * Getter for all existing control actions
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @return All control actions
	 */
	List<IControlAction> getAllControlActions();

	/**
	 * Adds a control action. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#CONTROL_ACTION}
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param description
	 *            The description of the new control action
	 * @param title
	 *            The title of the new control action
	 * @return String ID of the new control action
	 */
	UUID addControlAction(String title, String description);
	
	/**
	 * Searches recursively for the component with the given id
	 * 
	 * @param componentId
	 *            the id of the child
	 * @return the component with the given id, null if the component does not
	 *         exist
	 * 
	 * @author Fabian Toth
	 */
	IRectangleComponent getComponent(UUID componentId);
	
	/**
	 * Gets the connection with the given id
	 * 
	 * @param connectionId
	 *            the id of the connection
	 * @return the connection with the given id, null if the connection does not
	 *         exist
	 * 
	 * @author Fabian Toth
	 */
	IConnection getConnection(UUID connectionId);
	
	/**
	 * Removes a control action. <br>
	 * Triggers an update for {@link org.extended.safetyproject.model.ObserverValue#CONTROL_ACTION}
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param controlActionId
	 *            The ID of the control action which has to be deleted
	 * @return true if the control action has been removed
	 */
	boolean removeControlAction(UUID controlActionId);

	/**
	 * Setter for the title of a control action. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#CONTROL_ACTION}
	 * 
	 * @author Jarkko Heidenwag
	 * @param controlActionId
	 *            The control action's id
	 * 
	 * @param title
	 *            The control action's new title
	 * @return true if the title has been set
	 */
	boolean setControlActionTitle(UUID controlActionId, String title);

	/**
	 * Setter for the description of a control action. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#CONTROL_ACTION}
	 * 
	 * @author Jarkko Heidenwag
	 * @param controlActionId
	 *            The control action's id
	 * 
	 * @param description
	 *            The control action's new description
	 * @return true if the description has been set
	 */
	boolean setControlActionDescription(UUID controlActionId, String description);

	/**
	 * Get a control action by it's ID.
	 * 
	 * @author Jarkko Heidenwag, Patrick Wickenhaeuser
	 * @param controlActionId
	 *            the ID of the control action.
	 * 
	 * @return the control action.
	 */
	ITableModel getControlAction(UUID controlActionId);

}
