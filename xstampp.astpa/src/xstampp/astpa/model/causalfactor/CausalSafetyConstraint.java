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

package xstampp.astpa.model.causalfactor;

import xstampp.astpa.model.ATableModel;
import xstampp.model.ITableEntry;

/**
 * A corresponding safety constraint
 * 
 * @author Fabian Toth
 * 
 */
public class CausalSafetyConstraint extends ATableModel {

  /**
   * Constructor of a causal factor safety constraint
   * 
   * @param description
   *          the description of the new safety constraint
   * 
   * @author Fabian Toth
   */
  public CausalSafetyConstraint(String description) {
    super(description, null, -1);
  }

  /**
   * Empty constructor used for JAXB. Do not use it!
   * 
   * @author Fabian Toth
   */
  public CausalSafetyConstraint() {
    // empty constructor for JAXB
  }

  @Override
  public int compareTo(ITableEntry o) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public int getNumber() {
    return 0;
  }

}
