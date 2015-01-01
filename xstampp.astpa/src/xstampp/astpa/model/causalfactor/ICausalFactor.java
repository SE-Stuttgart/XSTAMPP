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

package xstampp.astpa.model.causalfactor;

import java.util.UUID;

/**
 * Interface for a causal factor
 * 
 * @author Fabian
 * 
 */
public interface ICausalFactor {

	/**
	 * @return the id
	 */
	UUID getId();

	/**
	 * @return the text
	 */
	String getText();

	/**
	 * @return the safetyConstraint
	 */
	CausalSafetyConstraint getSafetyConstraint();

	/**
	 * @return the note
	 */
	String getNote();

}
