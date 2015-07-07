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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import messages.Messages;
import xstampp.astpa.model.ITableModel;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.hazacc.HazAccController;

/**
 * Manager class for the causal factors
 * 
 * @author Fabian Toth, Benedikt Markt
 * 
 */
public class CausalFactorController {

	@XmlElementWrapper(name = "causalFactorHazardLinks")
	@XmlElement(name = "causalFactorHazardLink")
	private List<CausalFactorHazardLink> links;

	/**
	 * Constructor of the causal factor controller
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public CausalFactorController() {
		this.links = new ArrayList<>();
	}

	/**
	 * Adds a causal factor to this component
	 * 
	 * @author Fabian Toth
	 * 
	 * @param components
	 *            the list of all components
	 * @param causalComponentId
	 *            the id of the component
	 * @param causalFactorText
	 *            the text of the new causal factor
	 * @return the id of the new causal factor. null if the causal could not be
	 *         added
	 */
	public UUID addCausalFactor(List<Component> components,
			UUID causalComponentId, String causalFactorText) {
		UUID result = null;
		for (Component component : components) {
			if (component.getId().equals(causalComponentId)) {
				result = component.addCausalFactor(causalFactorText);
			}
		}
		return result;
	}

	/**
	 * Searches for the causal factor with the given id and changes the text of
	 * it
	 * 
	 * @author Fabian Toth
	 * 
	 * @param components
	 *            the list of all components
	 * @param causalFactorId
	 *            the id of the causal factor
	 * @param causalFactorText
	 *            the new text for the causal factor
	 * @return true when the text has been changed
	 */
	public boolean setCausalFactorText(List<Component> components,
			UUID causalFactorId, String causalFactorText) {
		for (Component component : components) {
			for (CausalFactor causalFactor : component
					.getInternalCausalFactors()) {
				if (causalFactor.getId().equals(causalFactorId)) {
					causalFactor.setText(causalFactorText);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes a link between a causal factor and a hazard.
	 * 
	 * @param causalFactorId
	 *            the id of the causal factor
	 * @param hazardId
	 *            the id of the hazard
	 * 
	 * @return true if the link has been removed
	 * 
	 * @author Fabian Toth
	 */
	public boolean removeCausalFactorHazardLink(UUID causalFactorId,
			UUID hazardId) {
		return this.links.remove(new CausalFactorHazardLink(causalFactorId,
				hazardId));
	}

	/**
	 * Add a link between a causal factor and a hazard
	 * 
	 * @author Fabian Toth
	 * 
	 * @param causalFactorId
	 *            the id of the causal factor
	 * @param hazardId
	 *            the id of the hazard
	 * @return true if the link has been added
	 */
	public boolean addCausalFactorHazardLink(UUID causalFactorId, UUID hazardId) {
		return this.links.add(new CausalFactorHazardLink(causalFactorId,
				hazardId));
	}

	/**
	 * Removes all links that are associated to this id
	 * 
	 * @param id
	 *            the id of the hazard or causal factor
	 * 
	 * @author Fabian Toth
	 * 
	 * @return true if this list changed as a result of the call
	 */
	public boolean removeAllLinks(UUID id) {
		List<ICausalFactorHazardLink> toDelete = new ArrayList<>();
		for (CausalFactorHazardLink link : this.links) {
			if (link.containsId(id)) {
				toDelete.add(link);
			}
		}
		return this.links.removeAll(toDelete);
	}

	/**
	 * Searches for the causal safety constraint with the given id and changes
	 * the text of it
	 * 
	 * @author Fabian Toth
	 * 
	 * @param components
	 *            the list of all components
	 * @param causalSafetyCosntraintId
	 *            the id of the causal safety constraint
	 * @param causalSafetyConstraintText
	 *            the new text for the causal safety constraint
	 * @return true when the text has been changed
	 */
	public boolean setCausalSafetyConstraintText(List<Component> components,
			UUID causalSafetyCosntraintId, String causalSafetyConstraintText) {
		for (Component component : components) {
			for (CausalFactor causalFactor : component
					.getInternalCausalFactors()) {
				if (causalFactor.getSafetyConstraint().getId()
						.equals(causalSafetyCosntraintId)) {
					causalFactor.getSafetyConstraint().setText(
							causalSafetyConstraintText);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Searches for the causal factor with the given id and changes the note of
	 * it
	 * 
	 * @author Fabian Toth
	 * 
	 * @param components
	 *            the list of all components
	 * @param causalFactorId
	 *            the id of the causal factor
	 * @param noteText
	 *            the new note for the causal factor
	 * @return true when the text has been changed
	 */
	public boolean setCausalFactorNoteText(List<Component> components,
			UUID causalFactorId, String noteText) {
		for (Component component : components) {
			for (CausalFactor causalFactor : component
					.getInternalCausalFactors()) {
				if (causalFactor.getId().equals(causalFactorId)) {
					causalFactor.setNote(noteText);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns a list of Hazard IDs that are linked to the given Causal Factor
	 * 
	 * @author Benedikt Markt
	 * 
	 * @param causalFactorId
	 *            Id of the Causal Factor
	 * @return a list of Hazard IDs
	 */
	public List<UUID> getLinkedHazardsOfCf(UUID causalFactorId) {
		List<UUID> linkedHazardIds = new ArrayList<UUID>();
		for (CausalFactorHazardLink causalFactorHazardLink : this.links) {
			if (causalFactorHazardLink.getCausalFactorId().equals(causalFactorId)&&
					!linkedHazardIds.contains(causalFactorHazardLink.getHazardId())){
				linkedHazardIds.add(causalFactorHazardLink.getHazardId());
			}
		}
		return linkedHazardIds;
	}

	/**
	 * Prepares the causal factors for the export
	 * 
	 * @author Fabian Toth
	 * 
	 * @param hazAccController
	 *            the hazAccController to get the Accidents as objects
	 * @param causalComponents
	 *            all causal components to set the links
	 * 
	 */
	public void prepareForExport(HazAccController hazAccController,
			List<Component> causalComponents) {
		for (Component causalComponent : causalComponents) {
			for (CausalFactor causalFactor : causalComponent
					.getInternalCausalFactors()) {
				List<ITableModel> linkedHazards = new ArrayList<>();
				for (UUID id : this.getLinkedHazardsOfCf(causalFactor.getId())) {
					linkedHazards.add(hazAccController.getHazard(id));
				}
				Collections.sort(linkedHazards);
				StringBuffer linkString = new StringBuffer(); //$NON-NLS-1$
				if (linkedHazards.size() == 0) {
					linkString.append(Messages.NotHazardous);
				} else {
					
					for (int i = 0;i < linkedHazards.size(); i++) {
						if (i != 0) {
							linkString.append(","); //$NON-NLS-1$
						}
						linkString.append("H-" + linkedHazards.get(i).getNumber());
					}
				}
				causalFactor.setLinks(linkString.toString());
				
			}
		}
	}

	/**
	 * Prepares the causal factors for save
	 * 
	 * @author Fabian Toth
	 * 
	 * @param causalComponents
	 *            all causal components to set the links
	 * 
	 */
	public void prepareForSave(List<Component> causalComponents) {
		for (Component causalComponent : causalComponents) {
			for (CausalFactor causalFactor : causalComponent
					.getInternalCausalFactors()) {
				causalFactor.setLinks(null);
			}
		}
	}
}
