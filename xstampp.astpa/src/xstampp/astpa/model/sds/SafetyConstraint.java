/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.sds;

import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.ITableModel;

/**
 * Class representing the safety constraint objects.
 * 
 * @author Jaqueline Patzek, Fabian Toth
 * @since 2.0
 * 
 */
public class SafetyConstraint extends ATableModel {

  /**
   * Constructor of a safety constraint
   * 
   * @param title
   *          the title of the new safety constraint
   * @param description
   *          the description of the new safety constraint
   * @param number
   *          the number of the new safety constraint
   * 
   * @author Fabian Toth, Jaqueline Patzek
   */
  public SafetyConstraint(String title, String description, int number) {
    super(title, description, number);
  }

  /**
   * Empty constructor used for JAXB. Do not use it!
   * 
   * @author Lukas Balzer
   * @param i
   *          the number of the new safety constraint
   * @param model
   *          The model containing the title, description and id
   */
  public SafetyConstraint(xstampp.astpa.haz.ITableModel model, int i) {
    super(model, i);
  }

  public SafetyConstraint() {
    // empty constructor for jaxb
  }
  
  @Override
  public String getIdString() {
    return "SC0." + getNumber();
  }
}