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

/**
 * Interface of an anchor
 * 
 * @author Fabian Toth
 */
public interface IAnchor {

	/**
	 * @return the isSource
	 * 
	 * @author Fabian Toth
	 */
	boolean isFlying();

	/**
	 * @return the x coordintes on the parent component
	 * 
	 * @author Fabian Toth, Lukas Balzer
	 */
	int getxOrientation();

	/**
	 * @return the y coordintes on the parent component
	 * 
	 * @author Fabian Toth, Lukas Balzer
	 */
	int getyOrientation();

	/**
	 * @return the ownerId
	 * 
	 * @author Fabian Toth
	 */
	UUID getOwnerId();

}
