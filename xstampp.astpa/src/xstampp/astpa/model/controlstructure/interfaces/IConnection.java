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

package xstampp.astpa.model.controlstructure.interfaces;

import xstampp.astpa.model.controlstructure.components.ConnectionType;

/**
 * Interface for a connection
 * 
 * @author Fabian Toth
 * 
 */
public interface IConnection extends xstampp.astpa.haz.controlstructure.interfaces.IConnection,IComponent {


	

	/**
	 * Getter for the connection type
	 * 
	 * @return the type constant of the Connection
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 */
	@Override
	ConnectionType getConnectionType();

	


}
