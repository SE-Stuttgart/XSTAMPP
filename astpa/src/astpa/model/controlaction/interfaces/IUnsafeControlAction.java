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

package astpa.model.controlaction.interfaces;

import java.util.UUID;

import astpa.model.controlaction.UnsafeControlActionType;

/**
 * Interface for a unsafe control action that shows only the getters
 * 
 * @author Fabian Toth
 * 
 */
public interface IUnsafeControlAction {
	
	/**
	 * @return the description
	 * 
	 * @author Fabian Toth
	 */
	String getDescription();
	
	/**
	 * @return the id
	 * 
	 * @author Fabian Toth
	 */
	UUID getId();
	
	/**
	 * @return the type
	 * 
	 * @author Fabian Toth
	 */
	UnsafeControlActionType getType();
	
}
