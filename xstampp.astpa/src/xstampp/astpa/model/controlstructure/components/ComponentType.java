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

package xstampp.astpa.model.controlstructure.components;

/**
 * Enum for the type of components
 * 
 * @author Lukas Balzer
 * @since 2.0
 * 
 */
public enum ComponentType{
	/**
	 * This enum represents a container component
	 */
	CONTAINER,
	/**
	 * This constant is used to create Text Fields
	 */
	TEXTFIELD,

	/**
	 * This constant is used to create Dashed Boxes
	 */
	DASHEDBOX,

	/**
	 * This type is used to create/store a visualization of a cointroloAction
	 */
	CONTROLACTION,

	/**
	 * This constant is used to create Controller
	 */
	CONTROLLER,

	/**
	 * This constant is used to create Actuator
	 */
	ACTUATOR,

	/**
	 * This constant is used to create a new Process
	 */
	CONTROLLED_PROCESS,

	/**
	 * This constant is used to create a new Process Model
	 */
	PROCESS_MODEL,

	/**
	 * This constant is used to create a new Process Variable
	 */
	PROCESS_VARIABLE,

	/**
	 * This constant is used to create a new Process State
	 */
	PROCESS_VALUE,

	/**
	 * This constant is used to create a new Sensor
	 */
	SENSOR,

	/**
	 * This Constant is used to create a root
	 */
	ROOT,

	/**
	 * This constant is used to create a new Connection
	 */
	CONNECTION;

}
