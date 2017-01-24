/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import xstampp.astpa.haz.causalfactor.ICausalFactorHazardLink;

/**
 * A implementation of a link between a causal factor and a hazard
 * 
 * @author Fabian
 * 
 */
public class CausalFactorHazardLink extends AbstractLinkModel implements ICausalFactorHazardLink {

	private UUID causalFactorId;
	private UUID hazardId;

	/**
	 * Constructor for a link
	 * 
	 * @author Fabian Toth
	 * 
	 * @param causalFactorId
	 *            the id of the accident
	 * @param hazardId
	 *            the id of the hazard
	 */
	public CausalFactorHazardLink(UUID causalFactorId, UUID hazardId) {
		this.causalFactorId = causalFactorId;
		this.hazardId = hazardId;
	}

	/**
	 * Empty constructor for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public CausalFactorHazardLink() {
		// empty constructor for JAXB
	}

	@Override
	public UUID getCausalFactorId() {
		return this.causalFactorId;
	}

	/**
	 * @param causalFactorId
	 *            the accidentId to set
	 */
	public void setCausalFactorId(UUID causalFactorId) {
		this.causalFactorId = causalFactorId;
	}

	@Override
	public UUID getHazardId() {
		return this.hazardId;
	}

	/**
	 * @param hazardId
	 *            the hazardId to set
	 */
	public void setHazardId(UUID hazardId) {
		this.hazardId = hazardId;
	}

	@Override
  protected UUID getKeyId(){
	  return getCausalFactorId();
	}
	
	@Override
  protected UUID getValueId(){
	  return getHazardId();
	}

}
