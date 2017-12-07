/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.ui.common.grid;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * The container for buttons in a GridWrapper cell.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class CellButtonContainer {

  private List<ICellButton> buttons;
  private List<ICellButton> buttonColumn;
  private Rectangle bounds;

  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser
   * 
   */
  public CellButtonContainer() {
    this.buttons = new ArrayList<ICellButton>();
    this.buttonColumn = new ArrayList<ICellButton>();
    this.bounds = new Rectangle(4, 4, 4, 4);
  }

  /**
   * Paint the buttons and updates the buttons' bounds relative to the cells bounds.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param renderer
   *          the renderer.
   * @param gc
   *          the GC.
   */
  public void paintButtons(GridCellRenderer renderer, GC gc) {
    Rectangle cellBounds = renderer.getBounds();
    if (buttonColumn.size() > 0) {
      int columnTopY = bounds.y + (cellBounds.height - getBounds().height) / 2;
      int columnX = cellBounds.width - this.bounds.width;
      for (ICellButton button : buttonColumn) {
        Rectangle rectangle = button.getBounds();
        rectangle.x = columnX;
        rectangle.y = columnTopY;
        button.setBounds(rectangle);
        columnTopY += rectangle.height + bounds.y;
      }
    }
    for (int i = 0; i < this.buttons.size(); i++) {
      ICellButton button = this.buttons.get(i);
      button.onPaint(gc, cellBounds);
    }
  }

  /**
   * Adds a button to the cell container which is placed in a button column at the right border of
   * cell. for that the x and y values are adapted each time the button is drawn
   * 
   * @author Patrick Wickenhaeuser, Lukas Balzer
   * 
   * @param button
   *          the new button.
   */
  public void addColumButton(ICellButton button) {
    buttonColumn.add(button);
    this.bounds.width = Math.max(this.bounds.width, button.getBounds().width);
    this.bounds.height += button.getBounds().height + bounds.y;
    addCellButton(button);
  }

  /**
   * Adds a normal Cell button which is than printed in the
   * {@link #paintButtons(GridCellRenderer, GC)} method
   * 
   * @param button
   *          the new button
   */
  public void addCellButton(ICellButton button) {
    this.buttons.add(button);
  }

  /**
   * Checks if any buttons is hit.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param relativeMouse
   *          the mouse position relative to the top left of the cell.
   * @param cellBounds
   *          the boundaries of the cell.
   * 
   * @return true if a buttons was been hit.
   */
  public boolean onMouseDown(Point relativeMouse, Rectangle cellBounds) {
    boolean hit = false;
    for (int i = 0; i < this.buttons.size(); i++) {
      ICellButton button = this.buttons.get(i);
      if (button.getBounds().contains(relativeMouse)) {
        button.onButtonDown(relativeMouse, cellBounds);
        hit = true;
      }
    }

    return hit;
  }

  /**
   * Clears all cell buttons.
   * 
   * @author Patrick Wickenhaeuser
   * 
   */
  public void clearButtons() {
    this.bounds = new Rectangle(4, 4, 4, 4);
    this.buttons.clear();
    this.buttonColumn.clear();
  }

  /**
   * Returns if the button container is empty
   * 
   * @author Benedikt Markt
   * 
   * @return true if empty, otherwise false
   */
  public boolean isEmpty() {
    return this.buttons.size() == 0;
  }

  public String getToolTip(Point point) {
    if (buttons != null) {
      for (ICellButton button : buttons) {
        if (button.getAbsoluteBounds().contains(point)) {
          return button.getToolTip();
        }
      }
    }
    return null;
  }

  public Rectangle getBounds() {
    return bounds;
  }
}
