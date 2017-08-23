/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.model.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.sds.ISDSController;
import xstampp.model.IDataModel;

/**
 * Interface to the Data Model for the Accident View
 * 
 * @author Jarkko Heidenwag
 * 
 */
public interface IDesignRequirementViewDataModel extends IDataModel, ICommonTables {

  /**
   * Getter for all existing design requirements
   * 
   * @author Jarkko Heidenwag
   * 
   * @return All design requirements
   */
  List<ITableModel> getAllDesignRequirements();

  /**
   * Adds an design requirement. <br>
   * Triggers an update for
   * {@link astpa.model.ObserverValue#DESIGN_REQUIREMENT}
   * 
   * @author Jarkko Heidenwag
   * 
   * @param description
   *          The description of the new design requirement
   * @param title
   *          The title of the new design requirement
   * @return String ID of the new Accident
   */
  UUID addDesignRequirement(String title, String description);

  /**
   * Removes a design requirement. <br>
   * Triggers an update for
   * {@link astpa.model.ObserverValue#DESIGN_REQUIREMENT}
   * 
   * @author Jarkko Heidenwag
   * 
   * @param designRequirementId
   *          The ID of the design requirement which has to be deleted
   * @return true if the design requirement has been removed
   */
  boolean removeDesignRequirement(UUID designRequirementId);

  /**
   * Setter for the title of a design requirement. <br>
   * Triggers an update for
   * {@link astpa.model.ObserverValue#DESIGN_REQUIREMENT}
   * 
   * @author Jarkko Heidenwag
   * @param designRequirementId
   *          The design requirement's id
   * 
   * @param title
   *          The design requirement's new title
   * @return true if the title has been set
   */
  boolean setDesignRequirementTitle(UUID designRequirementId, String title);

  public ISDSController getSdsController();

  /**
   * Setter for the description of an design requirement. <br>
   * Triggers an update for
   * {@link astpa.model.ObserverValue#DESIGN_REQUIREMENT}
   * 
   * @author Jarkko Heidenwag
   * @param designRequirementId
   *          The design requirements's id
   * 
   * @param description
   *          The design requirement's new description
   * @return true if the description has been set
   */
  boolean setDesignRequirementDescription(UUID designRequirementId,
      String description);

  /**
   * Get an design requirement by it's ID.
   * 
   * @author Jarkko Heidenwag, Patrick Wickenhaeuser
   * @param designRequirementId
   *          the ID of the design requirement.
   * 
   * @return the design requirement.
   */
  ITableModel getDesignRequirement(UUID designRequirementId);

}
