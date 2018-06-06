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

package xstampp.astpa.controlstructure.controller.factorys;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.requests.CreationFactory;

import messages.Messages;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;

/**
 *
 * This class is used to create new object instance of ComponentModel
 *
 * @version 1.0
 * @author Aliaksei Babkovich
 *
 */

public class CSModelCreationFactory implements CreationFactory {

  private ComponentType type;
  private static Map<ComponentType, Integer> countMap;
  private final IRectangleComponent dataModel;
  private UUID chosenUUID = null;

  /**
   *
   *
   * @author Lukas Balzer, Aliaksei Babkovich
   * @param type
   *          the Type of the model which should be constructed
   * @param model
   *          The DataModel which contains all model classes
   *
   */
  public CSModelCreationFactory(ComponentType type, IRectangleComponent model) {
    this.type = type;
    this.dataModel = model;
  }

  public void setChosenUUID(UUID chosenUUID) {
    this.chosenUUID = chosenUUID;
  }

  /**
   * @return the requested component object
   * @see Component
   */
  @Override
  public Object getNewObject() {
    String text;
    if (countMap == null) {
      countMap = new HashMap<>();
    }
    if (countMap.containsKey(this.type)) {
      countMap.put(this.type, countMap.get(this.type) + 1);
    } else {
      countMap.put(this.type, 1);
    }
    int count = countMap.get(this.type);
    switch (this.type) {
    case CONTROLACTION: {
      text = Messages.ControlAction + " " + count; //$NON-NLS-1$
      UUID uuid = chosenUUID;
      setChosenUUID(null);
      return new Component(uuid, text, new Rectangle(), this.type);
    }
    case ACTUATOR: {
      text = Messages.Actuator + " " + count; //$NON-NLS-1$
      for (IRectangleComponent x : this.dataModel.getChildren()) {
        if (x.getText().equals(text)) {
          text = text + " (2)";
        }
      }
      break;
    }
    case DASHEDBOX: {

      text = Messages.DashedBox + " " + count; //$NON-NLS-1$
      for (IRectangleComponent x : this.dataModel.getChildren()) {
        if (x.getText().equals(text)) {
          text = text + " (2)";
        }
      }
      break;
    }
    case CONTAINER: {

      text = "?";
      break;

    }
    case UNDEFINED: {

      text = "Component";
      break;

    }
    case CONTROLLER: {
      text = Messages.Controller + " " + count; //$NON-NLS-1$
      for (IRectangleComponent x : this.dataModel.getChildren()) {
        if (x.getText().equals(text)) {
          text = text + " (2)";
        }
      }
      break;
    }
    case CONTROLLED_PROCESS: {
      text = Messages.ControlledProcess + " " + count; //$NON-NLS-1$
      for (IRectangleComponent x : this.dataModel.getChildren()) {
        if (x.getText().equals(text)) {
          text = text + " (2)";
        }
      }
      break;
    }
    case PROCESS_MODEL: {
      text = Messages.ProcessModel + " " + count; //$NON-NLS-1$
      for (IRectangleComponent x : this.dataModel.getChildren()) {
        if (x.getText().equals(text)) {
          text = text + " (2)";
        }
      }
      break;
    }
    case PROCESS_VARIABLE: {
      text = Messages.ProcessVariable + " " + count; //$NON-NLS-1$
      for (IRectangleComponent x : this.dataModel.getChildren()) {
        if (x.getText().equals(text)) {
          text = text + " (2)";
        }
      }
      break;
    }
    case PROCESS_VALUE: {
      text = Messages.ProcessValue + " " + count; //$NON-NLS-1$
      break;
    }
    case SENSOR: {
      text = Messages.Sensor + " " + count; //$NON-NLS-1$
      for (IRectangleComponent x : this.dataModel.getChildren()) {
        if (x.getText().equals(text)) {
          text = text + " (2)";
        }
      }
      break;
    }
    case FEEDBACK: {
      text = "Feedback" + " " + count; //$NON-NLS-1$
      for (IRectangleComponent x : this.dataModel.getChildren()) {
        if (x.getText().equals(text)) {
          text = text + " (2)";
        }
      }
      break;
    }
    case TEXTFIELD: {
      text = Messages.TextBox + " " + count; //$NON-NLS-1$
      for (IRectangleComponent x : this.dataModel.getChildren()) {
        if (x.getText().equals(text)) {
          text = text + " (2)";
        }
      }
      break;
    }
    default: {
      text = ""; //$NON-NLS-1$
      break;
    }
    }
    return new Component(text, new Rectangle(), this.type);

  }

  @Override
  public Object getObjectType() {
    return this.type;
  }

}
