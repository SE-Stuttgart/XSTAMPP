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

package acast.model.causalfactor;

import acast.model.ASafetyConstraint;


/**
 * A corresponding safety constraint
 * 
 * @author Fabian Toth
 * 
 */
public class CausalSafetyConstraint extends ASafetyConstraint {

	/**
	 * Constructor of a causal factor safety constraint
	 * 
	 * @param description
	 *            the description of the new safety constraint
	 * 
	 * @author Fabian Toth
	 */
	public CausalSafetyConstraint(String description) {
		super(description);
	}

	/**
	 * Empty constructor used for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 */
	public CausalSafetyConstraint() {
		// empty constructor for JAXB
	}

}
