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

package acast.model.controlstructure.interfaces;

import java.util.List;
import java.util.UUID;

import acast.model.controlstructure.components.ComponentType;


/**
 * Represents a rectangle component of the control structure diagram
 * 
 * @author Fabian Toth
 */
public interface IRectangleComponent extends IComponent,IRectangleComponent2 {

	@Override
	ComponentType getComponentType();

	@Override
	public List<IRectangleComponent> getChildren();
	
	/**
	 * @return if the compoent is safety critical
	 * 
	 * @author Lukas Balzer
	 */
	public boolean isSafetyCritical();
	
	/**
	 * @return the comment
	 */
	public String getComment();
	
	/**
	 * @return the relative
	 */
	public UUID getRelative();
}
