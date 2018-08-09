/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Fabian Toth, Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.hazacc;

import java.util.List;
import java.util.Observer;
import java.util.UUID;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.BadReferenceModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.sds.ISDSController;
import xstampp.model.ObserverValue;

public interface IHazAccController {

  /**
   * Creates a new accident and adds it to the list of accidents.
   *
   * @param title
   *          the title of the new accident
   * @param description
   *          the description of the new accident
   * @return the ID of the new accident
   *
   * @author Fabian Toth
   */
  UUID addAccident(String title, String description);

  /**
   * Removes the accident from the list of accidents and removes all links
   * associated with this accident.
   *
   * @param id
   *          Accident's ID
   * @return true if the accident has been removed
   *
   * @author Fabian Toth
   *
   */
  boolean removeAccident(UUID id);

  boolean moveEntry(boolean moveUp, UUID id, ObserverValue value);

  /**
   * Searches for an Accident with given ID
   *
   * @param accidentID
   *          the id of the accident
   * @return found accident or a singleton instance of {@link BadReferenceModel} if the given UUID doesn't exist
   *
   * @author Fabian Toth
   */
  ITableModel getAccident(UUID accidentID);

  /**
   * Gets all accidents
   *
   * @return all accidents
   *
   * @author Fabian Toth
   */
  List<ITableModel> getAllAccidents();

  /**
   * Searches for all the hazards linked with given accident.
   *
   * @param accidentId
   *          the id of the accident
   * @return list of hazards linked to the given accident
   *
   * @author Fabian Toth
   */
  List<ITableModel> getLinkedHazards(UUID accidentId);

  /**
   * Creates a new hazard and adds it to the list of hazards.
   *
   * @param title
   *          the title of the new hazard
   * @param description
   *          the description of the new hazard
   * @return the ID of the new hazard
   *
   * @author Fabian Toth
   */
  UUID addHazard(String title, String description);

  /**
   * Removes the hazard from the list of hazards and remove all links associated
   * with this hazard.
   *
   * @param id
   *          the hazard's ID
   * @return true if the hazard has been removed
   *
   * @author Fabian Toth
   *
   */
  boolean removeHazard(UUID id);

  /**
   * Creates a link between an accident and a hazard.
   *
   * @param accidentId
   *          the id of the accident
   * @param hazardId
   *          the id of the hazard
   *
   * @author Fabian Toth
   * @return
   */
  boolean addLink(UUID accidentId, UUID hazardId);

  /**
   * Removes a link between an accident and a hazard.
   *
   * @param accidentId
   *          the id of the accident
   * @param hazardId
   *          the id of the hazard
   * @return true if the link has been removed
   *
   * @author Fabian Toth
   */
  boolean deleteLink(UUID accidentId, UUID hazardId);

  /**
   * Gets all hazards
   *
   * @return all hazards
   *
   * @author Fabian Toth
   */
  List<ITableModel> getAllHazards();

  /**
   * Searches for all the accidents linked with given hazard.
   *
   * @param hazardId
   *          the ID of the hazard
   * @return list of accidents linked with given hazard
   *
   * @author Fabian Toth
   */
  List<ITableModel> getLinkedAccidents(UUID hazardId);

  /**
   * Searches for a Hazard with given ID
   *
   * @param hazardId
   *          the id of the hazard
   * @return found hazard or a singleton instance of {@link BadReferenceModel} if the given UUID doesn't exist
   *
   * @author Fabian Toth
   */
  ATableModel getHazard(UUID hazardId);

  /**
   * Prepares the accidents and hazards for the export
   *
   * @author Fabian Toth
   * @author Lukas Balzer
   * @param linkController
   *
   */
  void prepareForExport(LinkController linkController, ISDSController sdsController);

  /**
   * Removes the preparations that were made for the export
   *
   * @author Fabian Toth
   * @return TODO
   *
   */
  boolean prepareForSave(LinkController linkController);

  List<HazAccLink> getAllHazAccLinks();

  /**
   * @return the useSeverity
   */
  boolean isUseSeverity();

  /**
   * @param useSeverity
   *          the useSeverity to set
   */
  boolean setUseSeverity(boolean useSeverity);

  /**
   * @return Whether or not the System level Hazards defined in Step 1 should be linked directly to
   *         system
   *         level safety constraints as defined by Leveson et al. in the STPA_Handbook.
   */
  boolean isUseHazardConstraints();

  /**
   * @param useHazardConstraints
   *          the useHazardConstraints to that defines whether or not the System level Hazards
   *          defined in Step 1 should be linked directly to system
   *          level safety constraints as defined by Leveson et al. in the STPA_Handbook.
   */
  boolean setUseHazardConstraints(boolean useHazardConstraints);

  void addObserver(Observer obs);

  boolean setHazardDescription(UUID hazardId, String description);

  boolean setHazardTitle(UUID hazardId, String title);

  boolean setAccidentTitle(UUID accidentId, String title);

  boolean setAccidentDescription(UUID accidentId, String description);

}
