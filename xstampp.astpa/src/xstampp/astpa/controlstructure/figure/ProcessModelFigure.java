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

package xstampp.astpa.controlstructure.figure;

import java.util.UUID;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class ProcessModelFigure extends CSFigure {

  private final int topOffset;

  /**
   * @author Lukas Balzer
   * @param id
   *          the id which the figure inherits from its model
   * @param top
   *          the offset from the parent models text Label
   */
  public ProcessModelFigure(UUID id, int top) {
    super(id, false);
    this.setOpaque(false);
    this.topOffset = top;
  }

  @Override
  public void setDeco(boolean deco) {
    // there's no decoration on process components
  }

}
