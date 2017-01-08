/*******************************************************************************
 * Copyright (c) 2013-2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.ui.common.grid;

import java.util.UUID;

/**
 * A blank implementation of AbstractGrid Cell just to represent Blank cells,
 * 
 * 
 * @author Lukas Balzer
 * 
 */
public class GridCellBlank extends AbstractGridCell {

  /**
   * this constructs a blank cell with nothing in it.
   * 
   * @author Lukas Balzer
   * 
   */
  public GridCellBlank() {
  }

  @Override
  public void cleanUp() {
    // intentionally empty
  }

  @Override
  public int getPreferredHeight() {
    return AbstractGridCell.DEFAULT_CELL_HEIGHT;
  }

  @Override
  public void addCellButton(CellButton button) {
    this.getButtonContainer().addColumButton(button);
  }

  @Override
  public void clearCellButtons() {
    this.getButtonContainer().clearButtons();
  }

  @Override
  public UUID getUUID() {
    return null;
  }

  @Override
  public void activate() {
    // intentionally empty

  }
}