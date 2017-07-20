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

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * A button in a cell.
 * 
 * @author Patrick Wickenhaeuser
 * @author Lukas Balzer
 * 
 */
public class CellButton implements ICellButton {

  private Rectangle rect;
  private String text;
  private Image image;

  /**
   * Ctor.
   * 
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

  /**
   * constructs a button with that contains a string
   * 
   * @param rect
   *          the bounds of the button, relative to the top left of the cell.
   * @param text
   *          the string that should be displayed in the bounds of this button.
   */
  public CellButton(Rectangle rect, String text) {
    this.rect = rect;
    this.text = text;
  }

  @Override
  public Rectangle getBounds() {
    return this.rect;
  }

  @Override
  public void onPaint(GC gc, Rectangle cellBounds) {
    onPaint(gc, cellBounds, true);
  }

  
  public void onPaint(GC gc, Rectangle cellBounds, boolean enabled) {
    if(enabled) {
      if (image != null) {
        gc.drawImage(this.image, 0, 0, this.image.getBounds().width, this.image.getBounds().height,
            cellBounds.x + this.getBounds().x, cellBounds.y + this.getBounds().y,
            this.getBounds().width, this.getBounds().height);
      }
      if (text != null) {
        int height = gc.getFont().getFontData()[0].getHeight();
        int topOffset = (getBounds().height / 2 - height / 2) / 2;
        gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
        gc.drawString(text, cellBounds.x + this.getBounds().x + 3,
            cellBounds.y + this.getBounds().y + topOffset, true);
      }
    } else {
      Color background = gc.getBackground();
      gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
      gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
      gc.setBackground(background);
    }
  }

  public Point getPreferedBounds() {
    int height = 0;
    int width = 0;
    if (image != null) {
      height = image.getBounds().height;
      width = image.getBounds().width;
    }
    if (text != null) {
      int fontHeight = Display.getDefault().getSystemFont().getFontData()[0].getHeight();
      height = Math.max(height, fontHeight);
      width = Math.max(width, fontHeight * text.length());
    } 
    return new Point(width, height);
  }
  @Override
  public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
    Logger.getRootLogger().debug("Button pressed onButtonDown"); //$NON-NLS-1$
  }

  @Override
  public String setToolTip(Point point) {
    return null;
  }

  public String getToolTip() {
    return null;
  }

  public void setText(String text) {
    this.text = text;
  }
}