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

import java.util.List;
import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Represents a rectangle component of the control structure diagram
 * 
 * @author Fabian Toth
 */
public interface IRectangleComponent extends IComponent {

	/**
	 * Getter for the layout
	 * 
	 * @param step1
	 *            true for the layout of the first step
	 * @return the layout
	 * 
	 * @author Fabian Toth, Lukas Balzer
	 */
	Rectangle getLayout(boolean step1);

	/**
	 * Getter for the text
	 * 
	 * @return the text
	 * 
	 * @author Fabian Toth
	 */
	String getText();

	/**
	 * Getter for the id
	 * 
	 * @return the id
	 * 
	 * @author Fabian Toth
	 */
	UUID getId();

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return returns the link to the related ControlAction or null if there is
	 *         no relation
	 */
	UUID getControlActionLink();

	/**
	 * 
	 * @author Lukas Balzer
	 * @param id
	 *            must be the id of a ControlAction
	 * @return whether the linking was successful
	 * 
	 */
	boolean linktoControlAction(UUID id);

	/**
	 * Getter for the children
	 * 
	 * @return the children
	 * 
	 * @author Fabian Toth
	 */
	List<IRectangleComponent> getChildren();

}
