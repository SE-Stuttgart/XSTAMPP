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

package acast.model.controlaction.interfaces;

import java.util.UUID;

/**
 * Interface for a link between a hazard and a unsafe control action that shows
 * all getters
 * 
 * @author Fabian Toth
 * 
 */
public interface IUCAHazLink {

	/**
	 * @return the accidentId
	 */
	UUID getUnsafeControlActionId();

	/**
	 * @return the hazardId
	 */
	UUID getHazardId();

	/**
	 * Checks if the given id is in this link
	 * 
	 * @author Fabian Toth
	 * 
	 * @param id
	 *            the id to check
	 * @return true if the given id is in this link
	 */
	boolean containsId(UUID id);
}