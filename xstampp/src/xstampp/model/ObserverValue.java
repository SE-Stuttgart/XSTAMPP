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
  DELETE, CLEAN_UP, Extended_DATA, PROJECT_TREE,UserSystem;

}
