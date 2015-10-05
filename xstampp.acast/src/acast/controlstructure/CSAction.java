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

package acast.controlstructure;

/**
 * This Enum is used to differ between the different types of actions in the
 * action Registry
 * 
 * @author Lukas Balzer
 * 
 */
public enum CSAction {

	/**
	 * a manipulating action which changes the Layout can be changed with that
	 * Constant name
	 * 
	 * @author Lukas Balzer
	 */
	PROPERTY_ACTION,

	/**
	 * a action which interacts with the stack or any buffered value
	 * 
	 * @author Lukas Balzer
	 */
	STACK_ACTION,

	/**
	 * a selection action that refers to any of the components in the CSDiagram
	 * can be referenced with that enum
	 * 
	 * @author Lukas Balzer
	 */
	SELECTION_ACTION,

	/**
	 * This enumeration is needed to describe a connection reconnect action
	 * 
	 * @author Lukas Balzer
	 */
	RECONNECT_ACTION,

	/**
	 * 
	 * @author Lukas Balzer
	 */
	CREATE_ACTION,

	/**
	 * 
	 * @author Lukas Balzer
	 */
	DEFAULT;
}
