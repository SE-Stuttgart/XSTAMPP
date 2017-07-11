/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.interfaces;

import java.util.List;
import java.util.Observable;
import java.util.UUID;

import xstampp.astpa.model.controlaction.ControlActionController;
import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.model.IDataModel;
import xstampp.model.IEntryFilter;

/**
 * Interface to the Data Model for the Control Actions Table
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser
 * 
 */
public interface IUnsafeControlActionDataModel extends IDataModel, ISeverityDataModel {

  /**
   * Getter for the Control Actions
   *
   * @author Benedikt Markt
   *
   * @return the list of Control Actions
   */
  List<IControlAction> getAllControlActionsU();

  /**
   * creates a new list with all entries according to the given {@link IEntryFilter} or with all
   * uca's defined if the filter is given as <b>null</b> <p> Note that modifications of the returned
   * list will not affect the list stored in the dataModel
   * 
   * @param filter
   *          an implementation of {@link IEntryFilter} which checks {@link IUnsafeControlAction}'s
   *          or <b>null</b> if there shouldn't be a check
   * @return a new list with all suiting uca entries, or an empty list if either all entries have
   *         been filtered or there are no etries
   * 
   * @author Lukas Balzer
   */
  List<ICorrespondingUnsafeControlAction> getUCAList(IEntryFilter<IUnsafeControlAction> filter);

  /**
   * Get the control action by its id
   * 
   * @author Benedikt Markt, Lukas Balzer
   * @param controlActionId
   *          id of the control action
   * 
   * @return the control action with the given id or null
   */
  IControlAction getControlActionU(UUID controlActionId);

  /**
   * Add an unsafe control action to a given control action.<br> Triggers an update for
   * {@link org.extended.safetyproject.model.ObserverValue#UNSAFE_CONTROL_ACTION}
   * 
   * @author Benedikt Markt, Patrick Wickenhaeuser
   * @param controlActionId
   *          the control action the unsafe control action will be added to.
   * @param description
   *          the description of the new unsafe control action.
   * @param unsafeControlActionType
   *          the type of the new unsafe control action
   * @return the id of the new unsafe control action. null if the unsafe control action could not be
   *         added
   */
  UUID addUnsafeControlAction(UUID controlActionId, String description,
      UnsafeControlActionType unsafeControlActionType);

  /**
   * Set the description of an unsafe control action. <br> Triggers an update for
   * {@link org.extended.safetyproject.model.ObserverValue#UNSAFE_CONTROL_ACTION}
   * 
   * @param controlActionId
   *          the id of the parent.
   * @param unsafeControlActionId
   *          the id of the unsafe control action.
   * @param description
   *          the new description.
   * 
   * @author Patrick Wickenhaeuser
   * @return true if the description has been set
   */
  boolean setUcaDescription(UUID unsafeControlActionId, String description);

  /**
   * Removes a unsafe control action.<br> Triggers an update for
   * {@link org.extended.safetyproject.model.ObserverValue#UNSAFE_CONTROL_ACTION}
   * 
   * @author Fabian Toth
   * 
   * @param unsafeControlActionId
   *          the id of the unsafe control action to delete
   * @return true if the unsafe control action could be removed
   */
  boolean removeUnsafeControlAction(UUID unsafeControlActionId);

  /**
   * Getter for the hazards that are linked to a unsafe control action
   * 
   * @param unsafeControlActionId
   *          the id of the unsafe control action
   * @return the linked hazards of the unsafe control action
   * 
   * @author Fabian Toth
   */
  List<ITableModel> getLinkedHazardsOfUCA(UUID unsafeControlActionId);

  /**
   * Adds a link between a unsafe control action and a hazard.<br> Triggers an update for
   * {@link org.extended.safetyproject.model.ObserverValue#UNSAFE_CONTROL_ACTION}
   * 
   * @param unsafeControlActionId
   *          the id of the unsafe control action
   * @param hazardId
   *          the id of the hazard
   * 
   * @author Fabian Toth
   * @return true if the link has been added
   */
  boolean addUCAHazardLink(UUID unsafeControlActionId, UUID hazardId);

  /**
   * Deletes the link between an accident and a hazard. <br> Triggers an update for
   * {@link org.extended.safetyproject.model.ObserverValue#UNSAFE_CONTROL_ACTION}
   * 
   * @param unsafeControlActionId
   *          the unsafe control action of which a link will be deleted.
   * @param hazardId
   *          the hazard of which a link will be deleted.
   * 
   * @author Fabian Toth
   * @return true if the link has been removed
   */
  boolean removeUCAHazardLink(UUID unsafeControlActionId, UUID hazardId);

  /**
   * Gets the links of the unsafe control action
   * 
   * @param unsafeControlActionId
   *          the id of the unsafe control action
   * @return the links of the unsafe control action
   * 
   * @author Fabian Toth
   */
  List<UUID> getLinksOfUCA(UUID unsafeControlActionId);

  /**
   * Get all hazards.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the list containing all hazards
   */
  List<ITableModel> getAllHazards();

  /**
   * returns the current id number of the UnsafeControlAction with the given ucaID
   * 
   * @param ucaID
   *          the UnsafeControlAction id
   * @return the current id
   */
  int getUCANumber(UUID ucaID);

  boolean removeAllUCAHazardLinks(UUID unsafeControlActionId);

  /**
   * by calling this method the {@link ControlActionController} of the data model is added to the
   * list of {@link Observable}'s of the {@link IDataModel} and the {@link ControlActionController} is returned
   * and can be used to call any available method with the {@link IDataModel} observing any changes.
   *  
   * @return the {@link ControlActionController} of this project
   */
  IControlActionController getControlActionController();
}
