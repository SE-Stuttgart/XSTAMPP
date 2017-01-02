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

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.controlstructure.components.ComponentType;

/**
 * This interface is used to address the Components without showing their exact
 * structure
 * 
 * @author Lukas
 * 
 */
public interface IComponent extends xstampp.astpa.haz.controlstructure.interfaces.IComponent{

	
	/**
	 * 
	 * @return The ComponentType which is stored in the model
	 * 
	 * @author Lukas Balzer
	 */
	@Override
	ComponentType getComponentType();
	
	/**
	 * Getter for the id
	 * 
	 * @return the id
	 * 
	 * @author Fabian Toth
	 */
	@Override
	UUID getId();

	/**
	 * Getter for the children
	 * 
	 * @return the children
	 * 
	 * @author Fabian Toth
	 */
	List<IRectangleComponent> getChildren();
	
	/**
	 * this function returns the children of the component depending on the 
	 * argument <i>step0</i> if true than all process model components are excluded from the child list
	 * 
	 * @param step0 if false the control structure contains all components including the process Models
	 * @return a List with all child components of the object
	 */
	public List<IRectangleComponent> getChildren(boolean step0);
	
	/**
	 * equal to a call of getChildren(false).size()
	 * 
	 * @return the amount of children stored in the children list
	 */
	public int getChildCount();
}
