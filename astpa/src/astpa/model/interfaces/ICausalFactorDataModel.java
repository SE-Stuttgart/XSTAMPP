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

package astpa.model.interfaces;

import java.util.List;
import java.util.UUID;

import astpa.model.ISafetyConstraint;
import astpa.model.ITableModel;
import astpa.model.causalfactor.ICausalComponent;

/**
 * Interface to the Data Model for the Causal Factors View
 * 
 * @author Fabian Toth, Benedikt Markt
 * 
 */
public interface ICausalFactorDataModel extends IDataModel {
	
	/**
	 * Get all causal components
	 * 
	 * @author Fabian Toth
	 * 
	 * @return all causal components
	 */
	List<ICausalComponent> getCausalComponents();
	
	/**
	 * Adds a causal factor to the causal component with the given id. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
	 * 
	 * @author Fabian Toth this
	 * @param causalComponentId the id of the causal component
	 * @param causalFactorText the text of the causal factor
	 * 
	 * @return the id of the new causal factor. null if the action fails
	 */
	UUID addCausalFactor(UUID causalComponentId, String causalFactorText);
	
	/**
	 * Sets the text of the causal factor with the given id. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
	 * 
	 * @author Fabian Toth
	 * 
	 * @param causalFactorId the id of the causal factor to change
	 * @param causalFactorText the text of the causal factor to change
	 * @return true if the text has been set
	 */
	boolean setCausalFactorText(UUID causalFactorId, String causalFactorText);
	
	/**
	 * Add a link between a causal factor and a hazard. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
	 * 
	 * @author Fabian Toth
	 * 
	 * @param causalFactorId the id of the causal factor
	 * @param hazardId the id of the hazard
	 * @return true if the link has been added
	 */
	boolean addCausalFactorHazardLink(UUID causalFactorId, UUID hazardId);
	
	/**
	 * Removes a link between a causal factor and a hazard. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
	 * 
	 * @param causalFactorId the id of the causal factor
	 * @param hazardId the id of the hazard
	 * 
	 * @return true if the link has been removed
	 * 
	 * @author Fabian Toth
	 */
	boolean removeCausalFactorHazardLink(UUID causalFactorId, UUID hazardId);
	
	/**
	 * Get all hazards.
	 * 
	 * @author Benedikt Markt
	 * 
	 * @return the list containing all hazards
	 */
	
	List<ITableModel> getAllHazards();
	
	/**
	 * Returns a list of Hazards linked to the given Casual Factor
	 * 
	 * @author Benedikt Markt
	 * 
	 * @param causalFactorId Id of the Causal Factor
	 * @return a list of Hazards that are currently linked to the Causal Factor
	 */
	List<ITableModel> getLinkedHazardsOfCf(UUID causalFactorId);
	
	/**
	 * Sets the text of the causal safety constraint with the given id. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
	 * 
	 * @author Fabian Toth
	 * 
	 * @param causalSafetyCosntraintId the id of the causal safety constraint
	 * @param causalSafetyConstraintText the new text for the causal safety
	 *            constraint
	 * @return true if the text has been set
	 */
	boolean setCausalSafetyConstraintText(UUID causalSafetyCosntraintId, String causalSafetyConstraintText);
	
	/**
	 * Sets the text of the note. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
	 * 
	 * @author Fabian Toth
	 * 
	 * @param causalFactorId the id of the causal factor
	 * @param noteText the new text of the note
	 * @return true if the text has been set
	 */
	boolean setNoteText(UUID causalFactorId, String noteText);
	
	/**
	 * Removes a causal factor. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
	 * 
	 * @author Fabian Toth
	 * 
	 * @param causalFactorId the id of the causal factor to remove
	 * @return true, if the causal factor has been removed
	 */
	boolean removeCausalFactor(UUID causalFactorId);
	
	/**
	 * Gets the list of all corresponding safety constraints
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the list of all corresponding safety constraints
	 */
	List<ISafetyConstraint> getCorrespondingSafetyConstraints();
	
}
