/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.model.controlaction;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a link between a unsafe control action and a hazard
 * 
 * @author Fabian Toth
 * 
 */
@XmlRootElement(name = "link")
@XmlAccessorType(XmlAccessType.NONE)
public class UCAHazLink implements IUCAHazLink {

  @XmlElement(name="unsafeControlActionId")
	private UUID unsafeControlActionId;
  
  @XmlElement(name="hazardId")
	private UUID hazardId;

	/**
	 * Constructor for a link
	 * 
	 * @author Fabian Toth
	 * 
	 * @param unsafeControlActionId
	 *            the id of the accident
	 * @param hazardId
	 *            the id of the hazard
	 */
	public UCAHazLink(UUID unsafeControlActionId, UUID hazardId) {
		this.unsafeControlActionId = unsafeControlActionId;
		this.hazardId = hazardId;
	}

	/**
	 * Empty constructor for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public UCAHazLink() {
		// empty constructor for JAXB
	}

	@Override
	public UUID getUnsafeControlActionId() {
		return this.unsafeControlActionId;
	}

	/**
	 * @param unsafeControlActionId
	 *            the accidentId to set
	 */
	public void setUnsafeControlActionId(UUID unsafeControlActionId) {
		this.unsafeControlActionId = unsafeControlActionId;
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
	public boolean containsId(UUID id) {
		return this.unsafeControlActionId.equals(id)
				|| this.hazardId.equals(id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.unsafeControlActionId == null) ? 0
						: this.unsafeControlActionId.hashCode());
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
		UCAHazLink other = (UCAHazLink) obj;
		if (this.unsafeControlActionId == null) {
			if (other.unsafeControlActionId != null) {
				return false;
			}
		} else if (!this.unsafeControlActionId
				.equals(other.unsafeControlActionId)) {
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
