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

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * A button in a cell.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class CellButton implements ICellButton {

  private Rectangle rect;

  private Image image;

  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param rect
   *          the bounds of the button, relative to the top left of the cell.
   * @param image
   *          the image drawn in the rectangle.
   */
  public CellButton(Rectangle rect, Image image) {
    this.rect = rect;
    this.image = image;
  }

  @Override
  public Rectangle getBounds() {
    return this.rect;
  }

  @Override
  public void onPaint(GC gc, Rectangle cellBounds) {
    gc.drawImage(this.image, 0, 0, this.image.getBounds().width, this.image.getBounds().height,
        cellBounds.x + this.getBounds().x, cellBounds.y + this.getBounds().y, this.getBounds().width,
        this.getBounds().height);
  }

  @Override
  public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
    Logger.getRootLogger().debug("Button pressed onButtonDown"); //$NON-NLS-1$
  }
}