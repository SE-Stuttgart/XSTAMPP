/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.ui.common.grid;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * The base class representing a cell in the grid.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 */
public abstract class AbstractGridCell implements IGridCell {

  /**
   * The default height in pixel of a cell.
   */
  public static final int DEFAULT_CELL_HEIGHT = 20;

  public static final Color HOVER_COLOR = new Color(null, 205, 245, 205);
  public static final Color PARENT_COLOR = new Color(null, 215, 240, 255);

  private static final Color MOD_2_1_GRAY = new Color(null, 230, 230, 230);

  private static final Color MOD_2_0_GRAY = new Color(null, 245, 245, 245);
  private Rectangle cellBounds;
  private GridRow row = null;
  private boolean hasChildren;
  private int preferredHeight;
  private CellButtonContainer buttonContainer = null;

  private boolean showSelection;

  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser, Benedikt Markt
   * 
   */
  public AbstractGridCell() {
    this.row = null;
    this.showSelection(true);
    this.buttonContainer = new CellButtonContainer();
    this.preferredHeight = DEFAULT_CELL_HEIGHT;
  }

  /**
   * Get the button container.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the button container.
   */
  public CellButtonContainer getButtonContainer() {
    return this.buttonContainer;
  }

  /**
   * Get the background color of the cell.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param renderer
   *          the renderer.
   * @param gc
   *          the GC.
   * @return the background color.
   */
  public Color getBackgroundColor(GridCellRenderer renderer, GC gc) {

    if (renderer.getGridWrapper().isCellSelected(this) && this.showSelection) {
      // selected color constant
      return HOVER_COLOR;
    }

    GridRow parentRow = this.row.getParentRow();
    boolean hasChildren = this.row.getChildren() != null && !this.row.getChildren().isEmpty();
    if (parentRow == null && hasChildren) {
      // parent color
      return PARENT_COLOR;
    }

    // alternating color
    // find out the index of the cell

    int index = this.row.getRowIndex() / this.row.getColorDivider();

    if ((index % 2) == 0) {
      return MOD_2_0_GRAY;
    }
    // every second row
    return MOD_2_1_GRAY;
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    cellBounds = renderer.getBounds();
    if (item.hasChildren()) {
      hasChildren = true;
    }
    this.paintFrame(renderer, gc, item);
  }

  public boolean hasChildren() {
    return hasChildren;
  }

  @Override
  public GridRow getGridRow() {
    return this.row;
  }

  /**
   * Paint the frame.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param renderer
   *          the renderer.
   * @param gc
   *          the GC.
   * @param item
   *          the nebula grid item the cell belongs to.
   */
  public void paintFrame(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {

    gc.setBackground(this.getBackgroundColor(renderer, gc));

    gc.fillRectangle(renderer.getDrawBounds().x, renderer.getDrawBounds().y,
        renderer.getDrawBounds().width, renderer.getDrawBounds().height);

    this.buttonContainer.paintButtons(renderer, gc);

    // restore bg color
    gc.setBackground(gc.getBackground());
  }

  @Override
  public void onMouseDown(MouseEvent e, Point relativeMouse, Rectangle cellBounds) {
    this.buttonContainer.onMouseDown(relativeMouse, cellBounds);
  }

  @Override
  public void onMouseUp(MouseEvent e) {
    // intentionally empty
  }

  @Override
  public void onFocusGained() {
    // intentionally empty
  }

  @Override
  public void onFocusLost() {
    // intentionally empty
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
  public void setGridRow(GridRow gridRow) {
    this.row = gridRow;
  }

  public void showSelection(boolean show) {
    this.showSelection = show;
  }

  /**
   * draws a string in the given bounds on the gc element, this method also performs linebreaks
   *
   * @author Lukas Balzer
   *
   * @param bounds
   *          the bounds the funktion can draw in,
   * @param gc
   *          the GC element to draw on
   * @param text
   *          the given string line( given CRs are considert)
   * @param left_space
   *          the offset from the bounds left end in points
   * @param right_space
   *          the offset from the right side of the bounds
   * @param item
   *          TODO
   * @return the height of the written text sequence
   */
  protected final Point wrapText(Rectangle bounds, GC gc, String text, int left_space,
      int right_space, NebulaGridRowWrapper item) {
    Point textBounds = new Point(bounds.width - left_space - right_space, 0);
    FontMetrics metrics = gc.getFontMetrics();
    if (text.isEmpty() || bounds.width < 0) {
      return textBounds;
    }
    // the line height is set absolute so that the strings are drawn on the
    // right position
    int lineHeight = bounds.y;
    String[] words = text.split(" |\t");
    String line = "";
    String tmpLine = words[0];
    String space = "";
    boolean first = true;
    boolean carryOver = false;
    int wordIndex = 1;
    while (wordIndex <= words.length) {
      if (!first) {
        space = " ";
      } else {
        space = "";
      }
      // the first statement checks whether the tmpLine starts with a line break
      // and when it does
      // a new line is started and the line break is removed
      if (tmpLine.startsWith(System.lineSeparator()) || tmpLine.startsWith("\n")) {
        gc.drawString(line, bounds.x + left_space, lineHeight);
        line = "";
        tmpLine = tmpLine.replaceFirst("\n|" + System.lineSeparator(), "");
        lineHeight += metrics.getHeight();
        first = true;
        carryOver = false;
        continue;
      }
      // the second statement checks whether there is a line break or not when
      // there is one the string is seperated
      // in line and carryover which is placed in words[i-1]
      if (tmpLine.contains("\n") || tmpLine.contains(System.lineSeparator())) {
        words[wordIndex - 1] = tmpLine.substring(tmpLine.indexOf("\n"));
        tmpLine = tmpLine.substring(0, tmpLine.indexOf("\n"));
        first = line.isEmpty();
        carryOver = true;
      } else if (!tmpLine.isEmpty() && gc.stringExtent(tmpLine).x >= textBounds.x) {
        int end = wrap(gc, tmpLine, textBounds.x - 1, 0, tmpLine.length() - 1, 0, 1, 1);
        gc.drawString(tmpLine.substring(0, end), bounds.x + left_space, lineHeight);
        lineHeight += metrics.getHeight();
        tmpLine = tmpLine.substring(end);
      } else if (gc.stringExtent(line + space + tmpLine).x >= textBounds.x) {

        gc.drawString(line, bounds.x + left_space, lineHeight);
        lineHeight += metrics.getHeight();
        first = true;
        line = "";
      } else if (carryOver) {
        line += space + tmpLine;
        tmpLine = words[wordIndex - 1];
        carryOver = false;
        first = false;
      } else if (wordIndex == words.length) {
        line += space + tmpLine;
        gc.drawString(line, bounds.x + left_space, lineHeight);
        lineHeight += metrics.getHeight();
        line = "";
        tmpLine = "";
        wordIndex++;
      } else {
        line += space + tmpLine;

        tmpLine = words[wordIndex++];
        first = false;
      }
    }

    // since line_height was initialized in absolute scale but this function
    // should calculate the
    // relative text height the y -coordinate must be subtracted
    textBounds.y = lineHeight - (bounds.y - 2);
    return textBounds;

  }

  /**
   * 
   *
   * @author Lukas Balzer
   *
   * @param gc
   *          the graphics element
   * @param line
   *          the String line
   * @param width
   *          the max width of the line
   * @param start
   *          the index the line should start with ( usually 0)
   * @param end
   *          the index of the string end, this is not changed but adapted in every recursion step
   *          by res
   * @param depth
   *          the number of the recursion depth (starting with 0)
   * @param endRes
   *          a number between 0 and 1 that defines the end index in each recursion
   * @param ch
   * @return the maximal end index to ensure that the line is still visible
   */
  protected final int wrap(GC gc, String line, int width, int start, int end, int depth,
      double endRes, double ch) {

    double nextEndRes;
    double recursionEnd = endRes * end;
    if (width < 0) {
      return 0;
    }
    // if
    if (gc.stringExtent(line.substring(start, (int) (recursionEnd))).x <= width && ch > 0.1) {
      nextEndRes = Math.min(1, endRes + ch * 0.5);
      return wrap(gc, line, width, start, end, depth + 1, nextEndRes, ch / 2);
    } else if (gc.stringExtent(line.substring(start, (int) (recursionEnd))).x > width && ch > 0.1) {
      nextEndRes = Math.min(1, endRes - ch * 0.5);
      return wrap(gc, line, width, start, end, depth + 1, nextEndRes, ch / 2);
    }
    return (int) recursionEnd;
  }

  @Override
  public String getToolTip(Point point) {
    if (buttonContainer != null) {
      return buttonContainer.getToolTip(point);
    }
    return null;
  }

  @Override
  public int getPreferredHeight() {
    if (hasChildren) {
      return preferredHeight;
    }
    int defaultHeight = Math.max(DEFAULT_CELL_HEIGHT, buttonContainer.getBounds().height);
    return Math.max(preferredHeight, defaultHeight);
  }

  public void setPreferredHeight(NebulaGridRowWrapper item, int preferredHeight) {
    int heightDiff = item.getGridRow().getChildren().size() * DEFAULT_CELL_HEIGHT - preferredHeight;
    if (heightDiff >= 0) {
      this.preferredHeight = 1;
    } else {
      this.preferredHeight = -heightDiff;
    }
    this.preferredHeight = Math.max(this.preferredHeight, this.buttonContainer.getBounds().height);
    item.setHeight(this.preferredHeight);
  }

  public Rectangle getCellBounds() {
    return cellBounds;
  }
}
