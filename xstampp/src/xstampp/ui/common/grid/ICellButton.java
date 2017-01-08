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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Interface for buttons that can be added to a button container, to be display
 * within a cell of the GridWrapper.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public interface ICellButton {

  /**
   * Get the bounds of the button, relative to the top left of the cell.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @return the bounds of the button, relative to the top left of the cell.
   */
  Rectangle getBounds();

  /**
   * Paints the button within the given cell bounds.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param gc
   *          the gc.
   * @param cellBounds
   *          the boundaries of the cell to draw in.
   */
  void onPaint(GC gc, Rectangle cellBounds);

  /**
   * Gets called whenever the button is pressed.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param relativeMouse
   *          the mouse position relative to the top left of the cell.
   * @param cellBounds
   *          the boundaries of the cell.
   * 
   */
  void onButtonDown(Point relativeMouse, Rectangle cellBounds);

  String setToolTip(Point point);
}
