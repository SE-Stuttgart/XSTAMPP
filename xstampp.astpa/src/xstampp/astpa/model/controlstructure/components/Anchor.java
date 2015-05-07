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

package xstampp.astpa.model.controlstructure.components;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

import xstampp.astpa.model.controlstructure.interfaces.IAnchor;

/**
 * Class that represents anchor points of a connection in the control structure
 * diagram
 * 
 * @author Fabian Toth
 */
@XmlRootElement(name = "anchor")
public class Anchor implements IAnchor {

	private boolean isFlying;
	private int xOrientation;
	private int yOrientation;
	private UUID ownerId;
	private UUID id;

	/**
	 * Constructs a new anchor with the given values
	 * 
	 * @param isFlying
	 *            true if this anchor is flying, which means that it is directly
	 *            created on the root
	 * @param xOrientation
	 *            the xOrientation of the new anchor
	 * @param yOrientation
	 *            the yOrientation of the new anchor
	 * @param ownerId
	 *            the id of the owner component
	 * 
	 * @author Fabian Toth
	 */
	public Anchor(boolean isFlying, int xOrientation, int yOrientation,
			UUID ownerId) {
		this.isFlying = isFlying;
		this.xOrientation = xOrientation;
		this.yOrientation = yOrientation;
		this.ownerId = ownerId;
		this.id = UUID.randomUUID();
	}

	/**
	 * Empty constructor for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 */
	public Anchor() {
		// empty constructor for JAXB
	}

	/**
	 * @return the isSource
	 */
	@Override
	public boolean isFlying() {
		return this.isFlying;
	}

	@Override
	public void setIsFlying(boolean isFlying){
		this.isFlying = isFlying;
	}
	/**
	 * @param isSource
	 *            the isSource to set
	 */
	public void setSource(boolean isSource) {
		this.isFlying = isSource;
	}

	/**
	 * @return the xOrientation
	 */
	@Override
	public int getxOrientation() {
		return this.xOrientation;
	}

	/**
	 * @param xOrientation
	 *            the xOrientation to set
	 */
	public void setxOrientation(int xOrientation) {
		this.xOrientation = xOrientation;
	}

	/**
	 * @return the yOrientation
	 */
	@Override
	public int getyOrientation() {
		return this.yOrientation;
	}

	/**
	 * @param yOrientation
	 *            the yOrientation to set
	 */
	public void setyOrientation(int yOrientation) {
		this.yOrientation = yOrientation;
	}

	/**
	 * @return the ownerId
	 */
	@Override
	public UUID getOwnerId() {
		return this.ownerId;
	}

	/**
	 * @param ownerId
	 *            the ownerId to set
	 */
	public void setOwnerId(UUID ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return the id
	 */
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

}
