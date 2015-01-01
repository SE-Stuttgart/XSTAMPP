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
 * A implementation of a link between a causal factor and a hazard
 * 
 * @author Fabian
 * 
 */
public class CausalFactorHazardLink implements ICausalFactorHazardLink {

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

	/**
	 * Checks if the given id is in this link
	 * 
	 * @author Fabian Toth
	 * 
	 * @param id
	 *            the id to check
	 * @return id the given id is in this link
	 */
	public boolean containsId(UUID id) {
		return this.causalFactorId.equals(id) || this.hazardId.equals(id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.causalFactorId == null) ? 0 : this.causalFactorId
						.hashCode());
		result = (prime * result)
				+ ((this.hazardId == null) ? 0 : this.hazardId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		CausalFactorHazardLink other = (CausalFactorHazardLink) obj;
		if (this.causalFactorId == null) {
			if (other.causalFactorId != null) {
				return false;
			}
		} else if (!this.causalFactorId.equals(other.causalFactorId)) {
			return false;
		}
		if (this.hazardId == null) {
			if (other.hazardId != null) {
				return false;
			}
		} else if (!this.hazardId.equals(other.hazardId)) {
			return false;
		}
		return true;
	}

}
