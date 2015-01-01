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

package xstampp.astpa.model.controlstructure.interfaces;

import java.util.UUID;

import xstampp.astpa.model.controlstructure.components.ConnectionType;

/**
 * Interface for a connection
 * 
 * @author Fabian Toth
 * 
 */
public interface IConnection extends IComponent {

	/**
	 * Getter for the source anchor
	 * 
	 * @return The anchor where the connection begins
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 */
	IAnchor getSourceAnchor();

	/**
	 * Getter for the targetFigure
	 * 
	 * @return The anchor where the connection ends
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 */
	IAnchor getTargetAnchor();

	/**
	 * Getter for the connection type
	 * 
	 * @return the type constant of the Connection
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 */
	ConnectionType getConnectionType();

	/**
	 * Getter for the id
	 * 
	 * @return the id of the connection
	 * 
	 * @author Fabian Toth
	 */
	UUID getId();

}
