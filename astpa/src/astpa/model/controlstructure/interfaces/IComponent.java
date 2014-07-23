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

package astpa.model.controlstructure.interfaces;

import astpa.model.controlstructure.components.ComponentType;

/**
 * This interface is used to address the Components without showing their exact structure
 * 
 * @author Lukas
 * 
 */
public interface IComponent {
	
	/**
	 * 
	 * @return The ComponentType which is stored in the model
	 * 
	 * @author Lukas Balzer
	 */
	ComponentType getComponentType();
	
}
