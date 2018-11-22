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

package xstampp.astpa.model.controlstructure.components;

import messages.Messages;

/**
 * Enum for the type of components
 * 
 * @author Lukas Balzer
 * @since 2.0
 * 
 */
public enum ComponentType {
  /**
   * This enum represents a container component
   */
  CONTAINER("?"), 
  /**
   * This constant is used to create Text Fields
   */
  TEXTFIELD(Messages.TextBox),

  /**
   * This constant is used to create Dashed Boxes
   */
  DASHEDBOX(Messages.DashedBox),

  /**
   * This type is used to create/store a visualization of a cointroloAction
   */
  CONTROLACTION(Messages.ControlAction),

  /**
   * This constant is used to create Controller
   */
  CONTROLLER(Messages.Controller),

  /**
   * This constant is used to create Actuator
   */
  ACTUATOR(Messages.Actuator),

  FEEDBACK("Feedback"),
  
  UNDEFINED("Component"),
  /**
   * This constant is used to create a new Process
   */
  CONTROLLED_PROCESS(Messages.ControlledProcess),

  /**
   * This constant is used to create a new Process Model
   */
  PROCESS_MODEL(Messages.ProcessModel),

  /**
   * This constant is used to create a new Process Variable
   */
  PROCESS_VARIABLE(Messages.ProcessVariable),

  /**
   * This constant is used to create a new Process State
   */
  PROCESS_VALUE(Messages.ProcessValue),

  /**
   * This constant is used to create a new Sensor
   */
  SENSOR(Messages.Sensor),

  OTHER_COMPONENT,
  /**
   * This Constant is used to create a root
   */
  ROOT,

  /**
   * This constant is used to create a new Connection
   */
  CONNECTION;

  private String title;

  private ComponentType() {
    this.title = "";
  }
  private ComponentType(String title) {
    this.title = title;
  }
  
  public String getTitle() {
    return title;
  }
}
