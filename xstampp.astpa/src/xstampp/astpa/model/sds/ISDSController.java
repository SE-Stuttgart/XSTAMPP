/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.sds;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.BadReferenceModel;
import xstampp.astpa.model.causalfactor.ICausalController;
import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.ObserverValue;

/**
 * An implementation of this interface holds 3 to n {@link List}'s of {@link ITableModel}'s <ol>
 * <li>safety constraints [1] <li>System goals [1] <li>design requirements [1..n] </ol> The amount
 * of design requirements that can be stored depends on the implementation. The default
 * implementation of ASTPA accepts <b>3 Lists of Design Requirements</b> for each of the 3 STPA
 * steps
 * 
 * @author Lukas Balzer
 *
 */
public interface ISDSController {

  /**
   * Adds a new safety constraint to the list of safety constraints.
   * 
   * @param title
   *          the title of the new safety constraint
   * @param description
   *          the description of the new safety constraint
   * 
   * @return the id of the new safety constraint
   * 
   * @author Fabian Toth
   */
  UUID addSafetyConstraint(String title, String description, UUID createdBy);

  /**
   * Adds a new safety constraint to the list of safety constraints.
   * 
   * @param title
   *          the title of the new safety constraint
   * @param description
   *          the description of the new safety constraint
   * 
   * @return the id of the new safety constraint
   * 
   * @author Fabian Toth
   */
  UUID addSafetyConstraint(ITableModel model);

  /**
   * Gives a list of all Safety Constraints.
   * 
   * @return a list of Safety Contraints
   * 
   * @author Fabian Toth
   */
  List<ITableModel> getAllSafetyConstraints();

  boolean moveEntry(boolean moveUp, UUID id, ObserverValue value);

  /**
   * Getter for a specific Safety Constraint. Returns null if there is no safety constraint with
   * this id
   * 
   * @param safetyConstraintId
   *          the id of the safety constraint
   * @return safety constraint object or a {@link BadReferenceModel} if no safety constraint for the
   *         given id exists
   * 
   * @author Fabian Toth
   */
  ITableModel getSafetyConstraint(UUID safetyConstraintId);

  /**
   * Removes a safety constraint from the list of safety constraints.
   * 
   * @param safetyConstraintId
   *          the id of the safety constraint
   * 
   * @return true if the safety constraint has been removed
   * 
   * @author Fabian Toth
   */
  boolean removeSafetyConstraint(UUID safetyConstraintId);

  /**
   * Adds a new system goal to the list of system goals.
   * 
   * @param title
   *          the title of the new system goal
   * @param description
   *          the description of the new system goal
   * 
   * @return the id of the new system goal
   * 
   * @author Fabian Toth
   */
  UUID addSystemGoal(String title, String description);

  /**
   * Gives a list of all System Goals.
   * 
   * @return a list of Safety Goals
   * 
   * @author Fabian Toth
   */
  List<ITableModel> getAllSystemGoals();

  /**
   * Gives the system goal object that belongs to the given id
   * 
   * @param systemGoalId
   *          the id of the system goal
   * @return system goal object
   * 
   * @author Jaqueline Patzek, Fabian Toth
   */
  ITableModel getSystemGoal(UUID systemGoalId);

  /**
   * Removes the system goal with the given id from the list of system goals.
   * 
   * @param systemGoalId
   *          the id of the system goal
   * 
   * @return true if the system goal has been removed
   * 
   * @author Jaqueline Patzek, Fabian Toth
   */
  boolean removeSystemGoal(UUID systemGoalId);

  /**
   * Adds a new design requirement to the list of design requirements.
   * 
   * @param title
   *          the title of the new design requirement
   * @param description
   *          the title of the new design requirement
   * 
   * @return the id of the new design requirement
   * 
   * @author Fabian Toth
   * @deprecated Use {@link #addDesignRequirement(String,String,ObserverValue)} instead
   */
  UUID addDesignRequirement(String title, String description);

  /**
   * Adds a new design requirement to the list of design requirements.
   * 
   * @param title
   *          the title of the new design requirement
   * @param description
   *          the title of the new design requirement
   * @param type
   *          TODO
   * @return the id of the new design requirement
   * 
   * @author Fabian Toth
   */
  UUID addDesignRequirement(String title, String description, ObserverValue type);

  /**
   * Gives a list of all Design Requirements.
   * 
   * @return a list of Design Requirements
   * 
   * @author Fabian Toth
   */
  List<ITableModel> getAllDesignRequirements();

  List<ITableModel> getAllDesignRequirements(ObserverValue type);

  /**
   * Gives the design requirement object that belongs to the given id
   * 
   * @param designRequirementId
   *          ID of the design requirement
   * @return design requirement object
   * 
   * @author Jaqueline Patzek, Fabian Toth
   * @deprecated Use {@link #getDesignRequirement(UUID,ObserverValue)} instead
   */
  ITableModel getDesignRequirement(UUID designRequirementId);

  /**
   * Gives the design requirement object that belongs to the given id
   * 
   * @param designRequirementId
   *          ID of the design requirement
   * @param type
   *          TODO
   * @return design requirement object
   * 
   * @author Jaqueline Patzek, Fabian Toth
   */
  ITableModel getDesignRequirement(UUID designRequirementId, ObserverValue type);

  /**
   * Removes the design requirement with the given id from the list of design requirements.
   * 
   * @param designRequirementId
   *          ID of the design requirement
   * @return true if the design requirement has been removed
   * 
   * @author Jaqueline Patzek, Fabian Toth
   * @deprecated Use {@link #removeDesignRequirement(UUID,ObserverValue)} instead
   */
  boolean removeDesignRequirement(UUID designRequirementId);

  /**
   * Removes the design requirement with the given id from the list of design requirements.
   * 
   * @param designRequirementId
   *          ID of the design requirement
   * @param type
   *          TODO
   * @return true if the design requirement has been removed
   * 
   * @author Jaqueline Patzek, Fabian Toth
   */
  boolean removeDesignRequirement(UUID designRequirementId, ObserverValue type);

  boolean setDesignRequirementDescription(ObserverValue type, UUID designRequirementId,
      String description);

  boolean setDesignRequirementTitle(ObserverValue type, UUID designRequirementId, String title);

  boolean prepareForSave();

  void prepareForExport(LinkController linkController, IHazAccController hazacc,
      IControlActionController caController, ICausalController causalController);

  boolean setSafetyConstraintTitle(UUID safetyConstraintId, String title);

  boolean setSafetyConstraintDescription(UUID entryId, String description);

  boolean setSystemGoalDescription(UUID systemGoalId, String description);

  boolean setSystemGoalTitle(UUID systemGoalId, String title);

}
