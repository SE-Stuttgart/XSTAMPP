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

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlType;

import xstampp.astpa.haz.ITableModel;

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
	public static <T> boolean move(boolean up,UUID id, List<T> list){
		for (int i = 0; i < list.size(); i++) {
			if(((ITableModel)list.get(i)).getId().equals(id)){
				T downModel = null;
				T upModel = null;
				int moveIndex = i;
				/* if up is true than the ITable model with the given id should move up
				 * if this is possible(if there is a model right to it in the list) than 
				 * the model which is right to it is moved down else the model itself is moved down
				 */
				if(up && i + 1 > list.size()){
					return false;
				}else if(up){
					downModel = ((T)list.get(i+1));
					moveIndex = i;
				}else if(i == 0){
					return false;
				}else{
					downModel = ((T)list.get(i));
					moveIndex = i-1;
				}
				upModel = ((T)list.get(moveIndex));
				if(upModel instanceof ATableModel && downModel instanceof ATableModel){
					((ATableModel) downModel).setNumber(moveIndex + 1);
					((ATableModel) upModel).setNumber(moveIndex + 2);
				}
				list.remove(downModel);
				list.add(moveIndex, downModel);
				return true;
			}
		}
		return false;
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
