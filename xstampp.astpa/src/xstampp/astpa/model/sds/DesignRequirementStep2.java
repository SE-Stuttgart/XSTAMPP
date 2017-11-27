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

package xstampp.astpa.model.sds;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.interfaces.ITableModel;

/**
 * Class representing the design requirement objects.
 * 
 * @author Jaqueline Patzek, Fabian Toth
 * @since 2.0
 */
public class DesignRequirementStep2 extends ATableModel {

  /**
   * {@link ATableModel#ATableModel(String, String, int)}
   */
  public DesignRequirementStep2(String title, String description, int number) {
    super(title, description, number);
  }

  /**
   * {@link ATableModel#ATableModel(String, String)}
   */
  public DesignRequirementStep2(String title, String description) {
    super(title, description);
  }

  public DesignRequirementStep2(ITableModel model) {
    super(model, model.getNumber());
  }

  /**
   * {@link ATableModel#ATableModel()}
   */
  public DesignRequirementStep2() {
    // empty constructor for JAXB
  }

  @Override
  public String getIdString() {
    return "DR2." + getNumber();
  }
}
