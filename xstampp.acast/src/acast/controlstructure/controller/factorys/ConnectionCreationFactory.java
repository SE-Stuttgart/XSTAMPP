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

package acast.controlstructure.controller.factorys;

import org.eclipse.gef.requests.CreationFactory;

import acast.model.controlstructure.components.ConnectionType;


/**
 * @author Aliaksei Babkovich
 * @version 1.0
 * 
 */
public class ConnectionCreationFactory implements CreationFactory {

	private ConnectionType connectionType;

	/**
	 * 
	 * 
	 * @author Lukas Balzer,Aliaksei Babkovich
	 * 
	 * @param typeConstant
	 *            the type of the Connection Described by a constant
	 * 
	 * 
	 */
	public ConnectionCreationFactory(ConnectionType typeConstant) {
		this.connectionType = typeConstant;
	}

	@Override
	public Object getNewObject() {
		return null;
	}

	/**
	 * @return the Type of the Connection as enum type
	 * @see ConnectionType
	 */
	@Override
	public Object getObjectType() {
		return this.connectionType;
	}

}
