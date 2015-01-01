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
 * A causal factor
 * 
 * @author Fabian
 * 
 */
public class CausalFactor implements ICausalFactor {

	private UUID id;
	private String text;
	private CausalSafetyConstraint safetyConstraint;
	private String note;
	private String links;

	/**
	 * Constructor of a causal factor
	 * 
	 * @author Fabian Toth
	 * 
	 * @param text
	 *            the text of the new causal factor
	 */
	public CausalFactor(String text) {
		this.id = UUID.randomUUID();
		this.text = text;
		this.safetyConstraint = new CausalSafetyConstraint(""); //$NON-NLS-1$
		this.note = ""; //$NON-NLS-1$
	}

	/**
	 * Empty constructor used for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 */
	public CausalFactor() {
		// empty constructor for JAXB
	}

	@Override
	public UUID getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String getText() {
		return this.text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public CausalSafetyConstraint getSafetyConstraint() {
		return this.safetyConstraint;
	}

	/**
	 * @param safetyConstraint
	 *            the safetyConstraint to set
	 */
	public void setSafetyConstraint(CausalSafetyConstraint safetyConstraint) {
		this.safetyConstraint = safetyConstraint;
	}

	@Override
	public String getNote() {
		return this.note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the links
	 */
	public String getLinks() {
		return this.links;
	}

	/**
	 * @param links
	 *            the links to set
	 */
	public void setLinks(String links) {
		this.links = links;
	}

}
