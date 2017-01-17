/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.model.sds;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import xstampp.astpa.model.sds.interfaces.ISafetyConstraint;

/**
 * Abstract class for safety constraints
 * 
 * @author Fabian Toth
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ASafetyConstraint implements ISafetyConstraint {

  @XmlElement(name="id")
	private UUID id;
  
  @XmlElement(name="text")
	private String text;

	/**
	 * Constructor of a safety constraint
	 * 
	 * @param text
	 *            the text of the new safety constraint
	 * 
	 * @author Fabian Toth
	 */
	public ASafetyConstraint(String text) {
		this.id = UUID.randomUUID();
		this.text = text;
	}

	/**
	 * Empty constructor used for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 */
	public ASafetyConstraint() {
		// empty constructor for JAXB
	}

	/**
	 * Setter for the description
	 * 
	 * @param text
	 *            the new text
	 * 
	 * @author Fabian Toth
	 */
	public boolean setText(String text) {
	  if(!this.text.equals(text)){
	    this.text = text;
	    return true;
	  }
	  return false;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public UUID getId() {
		return this.id;
	}

	/**
	 * Setter for the id
	 * 
	 * @param id
	 *            the new id
	 * 
	 * @author Fabian Toth
	 */
	public void setId(UUID id) {
		this.id = id;
	}

}
