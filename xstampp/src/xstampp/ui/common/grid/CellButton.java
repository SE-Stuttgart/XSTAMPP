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

  @FunctionalInterface
  public interface ButtonAction {
    void clickAction();
  }

  private Rectangle rect;
  private Rectangle absoluteBounds;
  private String text;
  private String toolTip;
  private Image image;

  private ButtonAction action = () -> {
  };

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
    this.rect = new Rectangle(rect.x, rect.y, rect.width, rect.height);
    ;
    this.absoluteBounds = new Rectangle(rect.x, rect.y, rect.width, rect.height);
    this.image = image;
  }

  /**
   * Ctor.
   * 
   * 
   * @param rect
   *          the bounds of the button, relative to the top left of the cell.
   * @param image
   *          the image drawn in the rectangle.
   */
  public CellButton(Image image) {
    this(image.getBounds(), image);
  }

  public CellButton(Image image, ButtonAction action) {
    this(image.getBounds(), image);
    this.action = action;
  }

  public CellButton(Image image, ButtonAction action, String toolTip) {
    this(image, action);
    this.toolTip = toolTip;
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
    this(rect, (Image) null);
    this.text = text;
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(rect.x, rect.y, rect.width, rect.height);
  }

  @Override
  public boolean setBounds(Rectangle bounds) {
    if (!rect.equals(bounds)) {
      this.rect = bounds;
      return true;
    }
    return false;
  }

  @Override
  public Rectangle getAbsoluteBounds() {
    return this.absoluteBounds;
  }

  @Override
  public void onPaint(GC gc, Rectangle cellBounds) {
    onPaint(gc, cellBounds, true);
  }

  public void onPaint(GC gc, Rectangle cellBounds, boolean enabled) {
    this.absoluteBounds.x = cellBounds.x + getBounds().x;
    this.absoluteBounds.y = cellBounds.y + getBounds().y;
    if (enabled) {
      if (image != null) {
        gc.drawImage(this.image, 0, 0, this.image.getBounds().width, this.image.getBounds().height,
            this.absoluteBounds.x, this.absoluteBounds.y,
            this.getBounds().width, this.getBounds().height);
      }
      if (text != null) {
        int height = gc.getFont().getFontData()[0].getHeight();
        int topOffset = (getBounds().height / 2 - height / 2) / 2;
        gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
        gc.drawString(text, this.absoluteBounds.x + 3,
            this.absoluteBounds.y + topOffset, true);
      }
    } else {
      Color background = gc.getBackground();
      gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
      gc.fillRectangle(this.absoluteBounds.x, this.absoluteBounds.y, getBounds().width, getBounds().height);
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
    action.clickAction();
  }

  @Override
  public void setToolTip(String toolTip) {
    this.toolTip = toolTip;
  }

  public String getToolTip() {
    return this.toolTip;
  }

  public void setText(String text) {
    this.text = text;
  }
}