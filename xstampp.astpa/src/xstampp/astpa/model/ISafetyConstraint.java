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

package xstampp.astpa.model;

import java.util.UUID;

/**
 * Interface for a safety constraint in the second and third step
 * 
 * @author Fabian Toth
 * @since 2.0
 * 
 */
public interface ISafetyConstraint extends xstampp.astpa.haz.ISafetyConstraint{

	/**
	 * Getter for the id. The id is a unique identifier for the element
	 * 
	 * @return the id
	 * 
	 * @author Fabian Toth
	 */
	@Override
	UUID getId();

	/**
	 * Getter for the text
	 * 
	 * @return the text
	 * 
	 * @author Fabian Toth
	 */
	@Override
	String getText();
}
