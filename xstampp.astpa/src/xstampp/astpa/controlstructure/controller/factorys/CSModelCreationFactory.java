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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.requests.CreationFactory;

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
  }

  /**
   * @return the requested component object
   * @see Component
   */
  @Override
  public Object getNewObject() {
    if (countMap == null) {
      countMap = new HashMap<>();
    }
    if (countMap.containsKey(this.type)) {
      countMap.put(this.type, countMap.get(this.type) + 1);
    } else {
      countMap.put(this.type, 1);
    }
    int count = countMap.get(this.type);
    return new Component(this.type.getTitle()  + " " + count, new Rectangle(), this.type); //$NON-NLS-1$

  }

  @Override
  public Object getObjectType() {
    return this.type;
  }

}
