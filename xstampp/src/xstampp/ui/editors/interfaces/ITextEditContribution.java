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
package xstampp.ui.editors.interfaces;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * an interface that defines classes that provide control over a
 * textControibution
 * 
 * @author Lukas Balzer
 *
 */
public interface ITextEditContribution {

  /**
   * @return the boldControl selection
   */
  abstract boolean getBoldControl();

  /**
   * @return the italicControl
   */
  abstract boolean getItalicControl();

  /**
   * @return the strikeoutControl
   */
  abstract boolean getStrikeoutControl();

  /**
   * @return the underlineControl
   */
  abstract boolean getUnderlineControl();

  /**
   * @return the bulletListControl
   */
  abstract boolean getBulletListControl();

  /**
   *
   * @author Lukas Balzer
   *
   * @return the current background color
   */
  Color getBackground();

  /**
   *
   * @author Lukas Balzer
   *
   * @return the current foreground color
   */
  Color getForeground();

  /**
   *
   * @author Lukas Balzer
   *
   * @return the font
   */
  Font getFont();

}