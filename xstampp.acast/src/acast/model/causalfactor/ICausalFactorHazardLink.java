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

import java.util.UUID;

/**
 * A link between a causal factor and a hazard
 * 
 * @author Fabian
 * 
 */
public interface ICausalFactorHazardLink {

	/**
	 * @return the accidentId
	 */
	UUID getCausalFactorId();

	/**
	 * @return the hazardId
	 */
	UUID getHazardId();

}
