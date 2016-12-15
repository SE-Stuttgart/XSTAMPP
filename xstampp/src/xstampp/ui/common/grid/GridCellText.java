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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * A cell to display text.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class GridCellText extends AbstractGridCell {

  private String text;

  private int textHeight;

  private int cellHeight;

  private UUID uuid;

  private static final Color TEXT_COLOR = new Color(Display.getCurrent(), 0, 0, 0);

  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser
   * 
   */
  public GridCellText() {
    this("NONE");
  }

  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser, Benedikt Markt
   * 
   * @param text
   *          the intial text in the cell.
   * 
   */
  public GridCellText(String text) {
    this(text, null);
  }
  
  public GridCellText(String text, UUID id){
    this.uuid = id;
    this.text = text;
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    super.paint(renderer, gc, item);
    Color bgColor = gc.getBackground();

    gc.setBackground(this.getBackgroundColor(renderer, gc));

    Color fgColor = gc.getForeground();
    gc.setForeground(GridCellText.TEXT_COLOR);

    int newHeight = wrapText(renderer.getDrawBounds(), gc, this.text, 2, 0);
    boolean needRefresh = newHeight > AbstractGridCell.DEFAULT_CELL_HEIGHT && newHeight != this.cellHeight;
    item.getGridRow().setDirty(needRefresh);
    this.textHeight = newHeight;

    cellHeight = Math.max(this.textHeight, AbstractGridCell.DEFAULT_CELL_HEIGHT);
    item.setHeight(cellHeight);
    // restore bg color
    gc.setBackground(bgColor);
    // restore fg color
    gc.setForeground(fgColor);
  }

  protected void setText(String text) {
    this.text = text;
  }

  @Override
  public void cleanUp() {
    // intentionally empty
  }

  @Override
  public int getPreferredHeight() {
    cellHeight = Math.max(this.textHeight, AbstractGridCell.DEFAULT_CELL_HEIGHT);
    return cellHeight;
  }

  @Override
  public void addCellButton(CellButton button) {
    this.getButtonContainer().addCellButton(button);
  }

  @Override
  public void clearCellButtons() {
    this.getButtonContainer().clearButtons();
  }


  @Override
  public void activate() {
    // intentionally empty

  }

  @Override
  public UUID getUUID() {
    return this.uuid;
  }
}
