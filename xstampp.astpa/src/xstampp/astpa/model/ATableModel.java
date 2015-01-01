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

package xstampp.astpa.model;

import java.util.UUID;

import javax.xml.bind.annotation.XmlType;

/**
 * Abstract class for everything that can be shown in a table
 * 
 * @author Fabian Toth
 * @since 2.1
 * 
 */
@XmlType(propOrder = { "number", "title", "description", "links", "id" })
public abstract class ATableModel implements ITableModel {

	private UUID id;
	private String title;
	private String description;
	private int number;
	private String links;

	/**
	 * constructor of a table model
	 * 
	 * @param title
	 *            the title of the new element
	 * @param description
	 *            the description of the new element
	 * @param number
	 *            the number of the new element
	 * 
	 * @author Fabian Toth
	 */
	public ATableModel(String title, String description, int number) {
		this.id = UUID.randomUUID();
		this.title = title;
		this.description = description;
		this.number = number;
	}

	/**
	 * Empty constructor used for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 */
	public ATableModel() {
		// empty constructor for JAXB
	}

	/**
	 * Setter for the description
	 * 
	 * @param description
	 *            the new description
	 * 
	 * @author Fabian Toth
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * Setter for the title
	 * 
	 * @param title
	 *            the new title
	 * 
	 * @author Fabian Toth
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return this.title;
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

	@Override
	public int getNumber() {
		return this.number;
	}

	/**
	 * Setter for the number
	 * 
	 * @param number
	 *            the new number
	 * 
	 * @author Fabian Toth
	 */
	public void setNumber(int number) {
		this.number = number;
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

	@Override
	public int compareTo(ITableModel o) {
		return this.getNumber() - o.getNumber();
	}

}
