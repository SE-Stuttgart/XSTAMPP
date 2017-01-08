/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.ui.editors;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Point;

/**
 * THis type stores a text selection of a styled text which alos contains
 * informations about it's font
 * 
 * @author Lukas Balzer
 * @since 1.0
 */
public class StyledTextSelection implements ISelection {

  private int fontSize;
  private Point selectionRange;
  private String fontName;
  private int fontStyle;
  private boolean underline;
  private boolean strikeout;

  /**
   *
   * @author Lukas Balzer
   *
   * @param range
   *          the range of the selection
   */
  public StyledTextSelection(Point range) {
    this.setSelectionRange(range);
    fontStyle = 0;
    underline = false;
    strikeout = false;
  }

  @Override
  public boolean isEmpty() {
    return this.selectionRange.y >= 0;
  }

  /**
   * @return the selectionRange
   */
  public Point getSelectionRange() {
    return this.selectionRange;
  }

  /**
   * @param selectionRange
   *          the selectionRange to set
   */
  public void setSelectionRange(Point selectionRange) {
    this.selectionRange = selectionRange;
  }

  /**
   * @return the fontSize
   */
  public int getFontSize() {
    return this.fontSize;
  }

  /**
   * @param fontSize
   *          the fontSize to set
   */
  public void setFontSize(int fontSize) {
    this.fontSize = fontSize;
  }

  /**
   * @return the fontName
   */
  public String getFontName() {
    return this.fontName;
  }

  /**
   * @param fontName
   *          the fontName to set
   */
  public void setFontName(String fontName) {
    this.fontName = fontName;
  }

  /**
   * @return the fontStyle
   */
  public int getFontStyle() {
    return this.fontStyle;
  }

  /**
   * @param fontStyle
   *          the fontStyle to set
   */
  public void setFontStyle(int fontStyle) {
    this.fontStyle = fontStyle;
  }

  /**
   * @return the underline
   */
  public boolean isUnderline() {
    return this.underline;
  }

  /**
   * @param underline
   *          the underline to set
   */
  public void setUnderline(boolean underline) {
    this.underline = underline;
  }

  /**
   * @return the strikeout
   */
  public boolean isStrikeout() {
    return this.strikeout;
  }

  /**
   * @param strikeout
   *          the strikeout to set
   */
  public void setStrikeout(boolean strikeout) {
    this.strikeout = strikeout;
  }

}
