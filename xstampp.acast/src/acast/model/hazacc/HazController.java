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

package acast.model.hazacc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import acast.model.ITableModel;


/**
 * Controller-class for working with accidents and hazards and links between
 * them.
 * 
 * @author Fabian Toth
 * 
 */
public class HazController {


	@XmlElementWrapper(name = "hazards")
	@XmlElement(name = "hazard")
	private List<Hazard> hazards;


	/**
	 * Constructor for the controller
	 * 
	 * 
	 * 
	 */
	public HazController() {
		this.hazards = new ArrayList<>();
	}

	


	/**
	 * Creates a new hazard and adds it to the list of hazards.
	 * 
	 * @param title
	 *            the title of the new hazard
	 * @param description
	 *            the description of the new hazard
	 * @return the ID of the new hazard
	 * 
	 * 
	 */
	public UUID addHazard(String title, String description) {
		Hazard newHazard = new Hazard(title, description,
				this.hazards.size() + 1);
		this.hazards.add(newHazard);
		return newHazard.getId();
	}

	/**
	 * Removes the hazard from the list of hazards and remove all links
	 * associated with this hazard.
	 * 
	 * @param id
	 *            the hazard's ID
	 * @return true if the hazard has been removed
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public boolean removeHazard(UUID id) {
		ITableModel hazard = this.getHazard(id);
		int index = this.hazards.indexOf(hazard);
		this.hazards.remove(index);
		for (; index < this.hazards.size(); index++) {
			this.hazards.get(index).setNumber(index + 1);
		}
		return true;
	}

	
	/**
	 * Gets all hazards
	 * 
	 * @return all hazards
	 * 
	 * @author Fabian Toth
	 */
	public List<ITableModel> getAllHazards() {
		List<ITableModel> result = new ArrayList<>();
		for (Hazard hazard : this.hazards) {
			result.add(hazard);
		}
		return result;
	}



	/**
	 * Searches for a Hazard with given ID
	 * 
	 * @param hazardId
	 *            the id of the hazard
	 * @return found hazard
	 * 
	 * @author Fabian Toth
	 */
	public ITableModel getHazard(UUID hazardId) {
		for (ITableModel hazard : this.hazards) {
			if (hazard.getId().equals(hazardId)) {
				return hazard;
			}
		}
		return null;
	}

	/**
	 * TODO
	 * 
	 */
	public void prepareForExport() {
//		for (Accident accident : this.accidents) {
//			List<ITableModel> tempLinks = this.getLinkedHazards(accident
//					.getId());
//			String linkString = ""; //$NON-NLS-1$
//			for (int i = 0; i < tempLinks.size(); i++) {
//				linkString += tempLinks.get(i).getNumber();
//				if (i < (tempLinks.size() - 1)) {
//					linkString += ", "; //$NON-NLS-1$
//				}
//			}
//			accident.setLinks(linkString);
//		}
//		for (Hazard hazard : this.hazards) {
//			List<ITableModel> tempLinks = this.getLinkedAccidents(hazard
//					.getId());
//			String linkString = ""; //$NON-NLS-1$
//			for (int i = 0; i < tempLinks.size(); i++) {
//				linkString += tempLinks.get(i).getNumber();
//				if (i < (tempLinks.size() - 1)) {
//					linkString += ", "; //$NON-NLS-1$
//				}
//			}
//			hazard.setLinks(linkString);
//		}
	}

	/**
	 * TODO
	 * 
	 */
	public void prepareForSave() {
//		for (Accident accident : this.accidents) {
//			accident.setLinks(null);
//		}
//		for (Hazard hazard : this.hazards) {
//			hazard.setLinks(null);
//		}
	}

}
