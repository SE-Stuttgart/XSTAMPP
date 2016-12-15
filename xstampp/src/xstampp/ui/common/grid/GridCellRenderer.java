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

import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * The renderer of a cell in the GridWrapper.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 * 
 */
public class GridCellRenderer extends org.eclipse.nebula.widgets.grid.GridCellRenderer {

  private GridWrapper gridWrapper;

  private final int borderSize = 2;

  private int leftMargin = 4;

  int rightMargin = 4;

  int topMargin = 0;

  int bottomMargin = 0;

  int textTopMargin = 1;

  int textBottomMargin = 2;

  private int insideMargin = 3;

  int treeIndent = 20;

  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param grid
   *          the GridWrapper.
   */
  public GridCellRenderer(GridWrapper grid) {
    this.gridWrapper = grid;

    this.setWordWrap(true);
  }

  @Override
  public void paint(GC gc, Object value) {
    if (value instanceof NebulaGridRowWrapper) {
      NebulaGridRowWrapper item = (NebulaGridRowWrapper) value;

      IGridCell cell = item.getCell(this.getColumn());

      if (cell != null) {
        cell.paint(this, gc, item);
        Color fColor = gc.getForeground();
        gc.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
        gc.drawRectangle(this.getBounds().x, this.getBounds().y, this.getBounds().width, this.getBounds().height);
        // restore old foreground color
        gc.setForeground(fColor);
      }
    }
  }

  public Point computeSize(GC gc, int wHint, int hHint, Object value) {
    GridItem item = (GridItem) value;

    gc.setFont(item.getFont(getColumn()));

    int x = 0;

    x += leftMargin;

    int y = 0;

    Image image = item.getImage(getColumn());
    if (image != null) {
      y = topMargin + image.getBounds().height + bottomMargin;

      x += image.getBounds().width + insideMargin;
    }

    // MOPR-DND
    // MOPR: replaced this code (to get correct preferred height for cells in
    // word-wrap columns)
    //
    // x += gc.stringExtent(item.getText(column)).x + rightMargin;
    //
    // y = Math.max(y,topMargin + gc.getFontMetrics().getHeight() +
    // bottomMargin);
    //
    // with this code:

    int textHeight = 0;
    if (!isWordWrap()) {
      x += gc.textExtent(item.getText(getColumn())).x + rightMargin;

      textHeight = topMargin + textTopMargin + gc.getFontMetrics().getHeight() + textBottomMargin + bottomMargin;
    } else {
      int plainTextWidth;
      if (wHint == SWT.DEFAULT)
        plainTextWidth = gc.textExtent(item.getText(getColumn())).x;
      else
        plainTextWidth = wHint - x - rightMargin;

      TextLayout currTextLayout = new TextLayout(gc.getDevice());
      currTextLayout.setFont(gc.getFont());
      currTextLayout.setText(item.getText(getColumn()));
      currTextLayout.setAlignment(getAlignment());
      currTextLayout.setWidth(plainTextWidth < 1 ? 1 : plainTextWidth);

      x += plainTextWidth + rightMargin;

      textHeight += topMargin + textTopMargin;
      for (int cnt = 0; cnt < currTextLayout.getLineCount(); cnt++)
        textHeight += currTextLayout.getLineBounds(cnt).height;
      textHeight += textBottomMargin + bottomMargin;

      currTextLayout.dispose();
    }

    y = Math.max(y, textHeight);

    return new Point(x, y);
  }

  /**
   * {@inheritDoc}
   */
  public boolean notify(int event, Point point, Object value) {
    return false;
  }

  /**
   * Get the GridWrapper.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the GridWrapper.
   */
  public GridWrapper getGridWrapper() {
    return this.gridWrapper;
  }

  /**
   * Returns the drawable area of a cell
   * 
   * @author Benedikt Markt
   * 
   * @return the Dimensions of the drawable area
   */
  public Rectangle getDrawBounds() {
    Rectangle renderBounds = this.getBounds();
    Rectangle bounds = new Rectangle(renderBounds.x + this.borderSize + 1, renderBounds.y + this.borderSize,
        renderBounds.width - this.borderSize, renderBounds.height - this.borderSize);

    return bounds;
  }
}