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

package xstampp.astpa.model.controlaction;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

import messages.Messages;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.CorrespondingSafetyConstraint;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;

/**
 * Class for unsafe control action objects.
 * 
 * @author Fabian Toth
 * 
 */
@XmlRootElement(name = "unsafecontrolaction")
public class UnsafeControlAction implements IUnsafeControlAction,
		ICorrespondingUnsafeControlAction {

	private String description;
	private UUID id;
	private UnsafeControlActionType type;
	private CorrespondingSafetyConstraint correspondingSafetyConstraint;
	private String links;
	private String notHazardousMsg = Messages.ControlActionController_NotHazardous;
	/**
	 * Constructs a new unsafe control action with the given values
	 * 
	 * @param description
	 *            the description of the new unsafe control action
	 * @param type
	 *            the type of the new unsafe control action
	 * 
	 * @author Fabian Toth
	 */
	public UnsafeControlAction(String description, UnsafeControlActionType type) {
		this.description = description;
		this.type = type;
		this.correspondingSafetyConstraint = new CorrespondingSafetyConstraint(
				""); //$NON-NLS-1$
		this.id = UUID.randomUUID();
	}

	/**
	 * Empty constructor for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 */
	public UnsafeControlAction() {
		// empty constructor for JAXB
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	public UnsafeControlActionType getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(UnsafeControlActionType type) {
		this.type = type;
	}

	@Override
	public CorrespondingSafetyConstraint getCorrespondingSafetyConstraint() {
		return this.correspondingSafetyConstraint;
	}

	/**
	 * @param correspondingSafetyConstraint
	 *            the correspondingSafetyConstraint to set
	 */
	public void setCorrespondingSafetyConstraint(
			CorrespondingSafetyConstraint correspondingSafetyConstraint) {
		this.correspondingSafetyConstraint = correspondingSafetyConstraint;
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

	/**
	 * @return the notHazardousMsg
	 */
	public String getNotHazardousMsg() {
		return this.notHazardousMsg;
	}

	/**
	 * @param notHazardousMsg the notHazardousMsg to set
	 */
	public void setNotHazardousMsg(String notHazardousMsg) {
		this.notHazardousMsg = notHazardousMsg;
	}

}
