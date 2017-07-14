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

package xstampp.astpa.model.sds;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.NumberedArrayList;
import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

/**
 * Class for managing safety constraints, system goals and design requirements.
 * 
 * @author Jaqueline Patzek, Fabian Toth
 * @since 2.0
 * 
 */
public class SDSController {

  @XmlElementWrapper(name = "safetyConstraints")
  @XmlElement(name = "safetyConstraint")
  private List<SafetyConstraint> safetyConstraints;

  @XmlElementWrapper(name = "systemGoals")
  @XmlElement(name = "systemGoal")
  private List<SystemGoal> systemGoals;

  @XmlElementWrapper(name = "designRequirements")
  @XmlElement(name = "designRequirement")
  private List<DesignRequirement> designRequirements;

  @XmlElementWrapper(name = "designRequirementsStep1")
  @XmlElement(name = "designRequirement")
  private List<DesignRequirement> designRequirementsStep1;

  @XmlElementWrapper(name = "designRequirementsStep2")
  @XmlElement(name = "designRequirement")
  private List<DesignRequirement> designRequirementsStep2;

  /**
   * 
   * Constructor of the SDSCotnroller
   * 
   * @author Fabian Toth
   * 
   */
  public SDSController() {
    this.safetyConstraints = new NumberedArrayList<>();
    this.systemGoals = new NumberedArrayList<>();
    this.designRequirements = new NumberedArrayList<>();
    this.designRequirementsStep1 = new NumberedArrayList<>();
    this.designRequirementsStep2 = new NumberedArrayList<>();
  }

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
  public UUID addSafetyConstraint(String title, String description, UUID createdBy) {
    if (this.safetyConstraints == null) {
      this.safetyConstraints = new NumberedArrayList<>();
    }
    SafetyConstraint safetyConstraint = new SafetyConstraint(title, description,
        this.safetyConstraints.size() + 1);
    safetyConstraint.setCreatedBy(createdBy);
    this.safetyConstraints.add(safetyConstraint);
    return safetyConstraint.getId();
  }

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
  public UUID addSafetyConstraint(ITableModel model) {
    if (this.safetyConstraints == null) {
      this.safetyConstraints = new NumberedArrayList<>();
    }
    SafetyConstraint safetyConstraint = new SafetyConstraint(model,
        this.safetyConstraints.size() + 1);
    this.safetyConstraints.add(safetyConstraint);
    return safetyConstraint.getId();
  }

  /**
   * Gives a list of all Safety Constraints.
   * 
   * @return a list of Safety Contraints
   * 
   * @author Fabian Toth
   */
  public List<ITableModel> getAllSafetyConstraints() {
    List<ITableModel> result = new ArrayList<>();
    if (safetyConstraints != null) {
      for (SafetyConstraint safetyConstraint : this.safetyConstraints) {
        result.add(safetyConstraint);
      }
    }
    return result;
  }

  public boolean moveEntry(boolean moveUp, UUID id, ObserverValue value) {
    if (value.equals(ObserverValue.SYSTEM_GOAL)) {
      return ATableModel.move(moveUp, id, systemGoals);
    } else if (value.equals(ObserverValue.SAFETY_CONSTRAINT)) {
      return ATableModel.move(moveUp, id, safetyConstraints);
    } else if (value.equals(ObserverValue.DESIGN_REQUIREMENT)) {
      return ATableModel.move(moveUp, id, designRequirements);
    }
    return true;
  }

  /**
   * Getter for a specific Safety Constraint. Returns null if there is no safety constraint with
   * this id
   * 
   * @param safetyConstraintId
   *          the id of the safety constraint
   * @return safety constraint object
   * 
   * @author Fabian Toth
   */
  public ITableModel getSafetyConstraint(UUID safetyConstraintId) {
    if (this.safetyConstraints != null) {
      for (ITableModel s : this.safetyConstraints) {
        if (s.getId().equals(safetyConstraintId)) {
          return s;
        }
      }
    }
    return null;
  }

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
  public boolean removeSafetyConstraint(UUID safetyConstraintId) {
    if (this.safetyConstraints != null) {
      ITableModel safetyConstraint = this.getSafetyConstraint(safetyConstraintId);
      int index = this.safetyConstraints.indexOf(safetyConstraint);
      this.safetyConstraints.remove(index);
      for (; index < this.safetyConstraints.size(); index++) {
        this.safetyConstraints.get(index).setNumber(index + 1);
      }
    }
    return true;
  }

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
  public UUID addSystemGoal(String title, String description) {
    if (this.systemGoals == null) {
      this.systemGoals = new NumberedArrayList<>();
    }
    SystemGoal systemGoal = new SystemGoal(title, description, this.systemGoals.size() + 1);
    this.systemGoals.add(systemGoal);
    return systemGoal.getId();
  }

  /**
   * Gives a list of all System Goals.
   * 
   * @return a list of Safety Goals
   * 
   * @author Fabian Toth
   */
  public List<ITableModel> getAllSystemGoals() {
    List<ITableModel> result = new ArrayList<>();
    if (this.systemGoals != null) {
      for (SystemGoal systemGoal : this.systemGoals) {
        result.add(systemGoal);
      }
    }
    return result;
  }

  /**
   * Gives the system goal object that belongs to the given id
   * 
   * @param systemGoalId
   *          the id of the system goal
   * @return system goal object
   * 
   * @author Jaqueline Patzek, Fabian Toth
   */
  public ITableModel getSystemGoal(UUID systemGoalId) {
    if (this.systemGoals != null) {
      for (ITableModel s : this.systemGoals) {
        if (s.getId().equals(systemGoalId)) {
          return s;
        }
      }
    }
    return null;
  }

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
  public boolean removeSystemGoal(UUID systemGoalId) {
    if (this.systemGoals != null) {
      ITableModel systemGoal = this.getSystemGoal(systemGoalId);
      int index = this.systemGoals.indexOf(systemGoal);
      this.systemGoals.remove(index);
      for (; index < this.systemGoals.size(); index++) {
        this.systemGoals.get(index).setNumber(index + 1);
      }
    }
    return true;
  }

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
   */
  public UUID addDesignRequirement(String title, String description) {
    if (this.designRequirements == null) {
      this.designRequirements = new NumberedArrayList<>();
    }
    DesignRequirement designRequirement = new DesignRequirement(title, description,
        this.designRequirements.size() + 1);
    this.designRequirements.add(designRequirement);
    return designRequirement.getId();
  }

  /**
   * Gives a list of all Design Requirements.
   * 
   * @return a list of Design Requirements
   * 
   * @author Fabian Toth
   */
  public List<ITableModel> getAllDesignRequirements() {
    List<ITableModel> result = new ArrayList<>();
    if (this.designRequirements != null) {
      for (DesignRequirement designRequirement : this.designRequirements) {
        result.add(designRequirement);
      }
    }
    return result;
  }

  /**
   * Gives the design requirement object that belongs to the given id
   * 
   * @param designRequirementId
   *          ID of the design requirement
   * @return design requirement object
   * 
   * @author Jaqueline Patzek, Fabian Toth
   */
  public ITableModel getDesignRequirement(UUID designRequirementId) {
    if (this.designRequirements != null) {
      for (ITableModel d : this.designRequirements) {
        if (d.getId().equals(designRequirementId)) {
          return d;
        }
      }
    }

    return null;
  }

  /**
   * Removes the design requirement with the given id from the list of design requirements.
   * 
   * @param designRequirementId
   *          ID of the design requirement
   * @return true if the design requirement has been removed
   * 
   * @author Jaqueline Patzek, Fabian Toth
   */
  public boolean removeDesignRequirement(UUID designRequirementId) {

    if (this.designRequirements != null) {
      ITableModel designRequirement = this.getDesignRequirement(designRequirementId);
      int index = this.designRequirements.indexOf(designRequirement);
      this.designRequirements.remove(index);
      for (; index < this.designRequirements.size(); index++) {
        this.designRequirements.get(index).setNumber(index + 1);
      }
    }
    return true;
  }

  public boolean prepareForSave() {
    boolean isUsed = false;
    if (designRequirements != null && designRequirements.isEmpty()) {
      designRequirements = null;
    }
    isUsed |= designRequirements != null;
    if (designRequirementsStep1 != null && designRequirementsStep1.isEmpty()) {
      designRequirementsStep1 = null;
    }
    isUsed |= designRequirementsStep1 != null;
    if (designRequirementsStep2 != null && designRequirementsStep2.isEmpty()) {
      designRequirementsStep2 = null;
    }
    isUsed |= designRequirementsStep2 != null;
    if (safetyConstraints != null && safetyConstraints.isEmpty()) {
      safetyConstraints = null;
    }
    isUsed |= safetyConstraints != null;
    if (systemGoals != null && systemGoals.isEmpty()) {
      systemGoals = null;
    }
    isUsed |= systemGoals != null;
    return isUsed;
  }
}
