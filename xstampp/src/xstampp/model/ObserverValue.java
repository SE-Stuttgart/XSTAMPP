/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.model;

/**
 * Enum class for update values.
 * 
 * @author Fabian Toth
 * 
 */
public enum ObserverValue {
  /**
   * Enum for accident changes.
   * 
   * @author Fabian Toth
   */
  ACCIDENT,
  /**
   * Enum for hazard changes.
   * 
   * @author Fabian Toth
   */
  HAZARD,
  /**
   * Enum for changes in linking between hazards and accidents.
   * 
   * @author Fabian Toth
   */
  HAZ_ACC_LINK,
  /**
   * Enum for project name changes.
   * 
   * @author Fabian Toth
   */
  PROJECT_NAME,

  /**
   * Enum for project description changes.
   * 
   * @author Fabian Toth
   */
  PROJECT_DESCRIPTION,

  /**
   * Enum for changes in CS root model.
   * 
   * @author Fabian Toth
   */
  CONTROL_STRUCTURE,

  /**
   * Enum for changes in Control Actions Table.
   * 
   * @author Benedikt Markt
   */
  CONTROL_ACTION,

  /**
   * Enum for changes of the process model value combination states.
   * 
   * @author Benedikt Markt
   */
  COMBINATION_STATES,

  /**
   * Enum for changes in Unsafe Control Actions Table.
   * 
   * @author Benedikt Markt
   */
  UNSAFE_CONTROL_ACTION,

  /**
   * Enum for changes in the safety constraint table.
   * 
   * @author Fabian Toth
   */
  SAFETY_CONSTRAINT,

  /**
   * Enum for changes in the safety constraint table.
   * 
   * @author Fabian Toth
   */
  DESIGN_REQUIREMENT,

  /**
   */
  DESIGN_REQUIREMENT_STEP1,

  /**
   */
  DESIGN_REQUIREMENT_STEP2,

  /**
   * Enum for changes in the system goal table.
   * 
   * @author Fabian Toth
   */
  SYSTEM_GOAL,

  /**
   * Enum for changes of the unsaved Changes value.
   * 
   * @author Fabian Toth
   */
  UNSAVED_CHANGES,

  /**
   * Enum for changes of the causal factors table.
   * 
   * @author Fabian Toth
   */
  CAUSAL_FACTOR,

  /**
   * Enum value that is triggered before the data model will be saved.
   */
  SAVE,

  /**
   * Enum value that is triggered before the data model will be exported.
   */
  EXPORT,

  /**
   * Enum value that is triggered after the data has been exported.
   */
  EXPORT_FINISHED,

  /**
   * Enum value that is triggered before the datamodel is deleted.
   */
  DELETE,

  /**
   */
  CLEAN_UP,

  /**
   */
  Extended_DATA,

  /**
   */
  PROJECT_TREE,

  /**
   */
  UserSystem,

  /**
   */
  SEVERITY,

  /**
   * <ol>
   * <li> A should be the UUID of an Accident in STPA Step 1
   * <LI> B should be the UUID of a Safety Constraint in STPA Step 1
   * </ol>
   */
  ACC_S0_LINK,

  /**
   * <ol>
   * <li> A should be the UUID of a Design Requirement in STPA Step 0
   * <LI> B should be the UUID of a Safety Constraint in STPA Step 0
   * </ol>
   */
  DR0_SC_LINK,

  /**
   * <ol>
   * <li> A should be the UUID of a Design Requirement in STPA Step 1
   * <LI> B should be the UUID of a Safety Constraint in STPA Step 1
   * </ol>
   */
  DR1_CSC_LINK,

  /**
   * <ol>
   * <li> A should be the UUID of a Design Requirement in STPA Step 2
   * <LI> B should be the UUID of a Safety Constraint in STPA Step 2
   * </ol>
   */
  DR2_CausalSC_LINK,

  /**
   * <ol>
   * <li> A should be the UUID of a UCA
   * <LI> B should be the UUID of a Hazard
   * </ol>
   */
  UCA_HAZ_LINK,

  /**
   * <ol>
   * <li> A should be the UUID of a {@link ObserverValue#UCA_HAZ_LINK}
   * <LI> B should be the UUID of a Corresponding Safety Constraint in STPA Step 1
   * </ol>
   */
  UcaHazLink_SC2_LINK,

  /**
   * enum for a link<A,B> between a unsafe control action and a causal factor in a stpa analysis.
   * <ol>
   * <li> A should be the UUID of a UCA
   * <li> B should be the UUID of a CF
   * </ol>
   */
  UCA_CausalFactor_LINK;
}
