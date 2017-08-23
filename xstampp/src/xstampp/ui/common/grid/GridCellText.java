/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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
import org.eclipse.swt.graphics.Point;
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

  public GridCellText(String text, UUID id) {
    this.uuid = id;
    this.text = text;
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    super.paint(renderer, gc, item);
    Color bgColor = gc.getBackground();

    gc.setBackground(this.getBackgroundColor(renderer, gc));

    final Color fgColor = gc.getForeground();
    gc.setForeground(GridCellText.TEXT_COLOR);

    Point textBounds = wrapText(renderer.getDrawBounds(), gc, this.text.trim(), 2, 0);
    boolean needRefresh = textBounds.y > AbstractGridCell.DEFAULT_CELL_HEIGHT
        && textBounds.y != getPreferredHeight();
    item.getGridRow().setDirty(needRefresh);
    int cellHeight = Math.max(textBounds.y, AbstractGridCell.DEFAULT_CELL_HEIGHT);
    setPreferredHeight(item, cellHeight);
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
  public void activate() {
    // intentionally empty

  }

  @Override
  public UUID getUUID() {
    return this.uuid;
  }
}
