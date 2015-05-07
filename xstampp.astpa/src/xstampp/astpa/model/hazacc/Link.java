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

package xstampp.astpa.model.hazacc;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a link between an accident and a hazard
 * 
 * @author Fabian Toth
 * @since 2.0
 * 
 */
@XmlRootElement(name = "link")
@XmlType(propOrder = { "accidentId", "hazardId" })
public class Link implements ILink {

	private UUID accidentId;
	private UUID hazardId;

	/**
	 * Constructor for a link
	 * 
	 * @author Fabian Toth
	 * 
	 * @param accidentId
	 *            the id of the accident
	 * @param hazardId
	 *            the id of the hazard
	 */
	public Link(UUID accidentId, UUID hazardId) {
		this.accidentId = accidentId;
		this.hazardId = hazardId;
	}

	/**
	 * Empty constructor for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public Link() {
		// empty constructor for JAXB
	}

	@Override
	public UUID getAccidentId() {
		return this.accidentId;
	}

	/**
	 * @param accidentId
	 *            the accidentId to set
	 */
	public void setAccidentId(UUID accidentId) {
		this.accidentId = accidentId;
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
		return this.accidentId.equals(id) || this.hazardId.equals(id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.accidentId == null) ? 0 : this.accidentId.hashCode());
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
		Link other = (Link) obj;
		if (this.accidentId == null) {
			if (other.accidentId != null) {
				return false;
			}
		} else if (!this.accidentId.equals(other.accidentId)) {
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
