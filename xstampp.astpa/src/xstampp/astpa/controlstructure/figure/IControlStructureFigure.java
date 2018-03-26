/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.controlstructure.figure;

import java.util.UUID;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;

import xstampp.astpa.controlstructure.utilities.CSTextLabel;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public interface IControlStructureFigure extends IFigure {

  public static final Border DASHED_BORDER = new LineBorder(ColorConstants.black, 1,
      SWT.BORDER_DASH) {
    @Override
    public void paint(IFigure figure, Graphics graphics,
        Insets insets) {
      graphics.setLineStyle(SWT.LINE_CUSTOM);
      graphics.setLineDash(new int[] { 4 });
      graphics.setLineDashOffset(4);
      super.paint(figure, graphics, insets);
    }
  };

  /**
   * 
   * 
   * @author Lukas Balzer
   * 
   * @return the id which the figure inherits from its model
   */
  UUID getId();

  /**
   * @author Lukas Balzer
   * 
   * @return the text which is displayed in the CSTextLabel
   */
  String getText();

  /**
   * @author Lukas Balzer
   * 
   * @param text
   *          the new Text
   */
  void setText(String text);

  /**
   * 
   * 
   * @author Lukas Balzer
   * 
   * @return the CSTextLabel
   * @see CSTextLabel#getTextField
   */
  CSTextLabel getTextField();

  /**
   * This method is called when the Layout of the Model changes. It calls
   * setConstraint() in the Parent Figure to change the Layout Constraint of
   * this Figure relatively to it.
   * 
   * @author Lukas Balzer
   * 
   * @param rect
   *          the Rectangle which stores the Layout
   */
  void setLayout(Rectangle rect);

  /**
   * adds a anchor Feedback Rectangle to the Root, the Root has only on
   * Feedback Rectangle which can be added at Points currently to be
   * highlighted
   * 
   * @author Lukas Balzer
   * 
   * @param ref
   *          the anchorPoint for which a feedback should be created
   */
  void addHighlighter(Point ref);

  /**
   * removes the Feedback Recangle from the editor
   * 
   * @author Lukas Balzer
   * 
   */
  void removeHighlighter();

  /**
   * This method disables the use of the offset area around the figures bounds
   * 
   * @author Lukas Balzer
   * 
   */
  void disableOffset();

  /**
   * this method enables an offset around the child figures to find connection
   * anchors with a certain "snap-to" effect
   * 
   * @author Lukas Balzer
   * 
   */
  void enableOffset();

  boolean useOffset();

  /**
   * enables/disables the decoration of this figure,</br>
   * the decoration can exist out of a colored border and an image
   * 
   * @author Lukas
   *
   * @param deco
   *          whther or not to show the decoration
   */
  void setDeco(boolean deco);

  /**
   * if the deco is visible
   * 
   * @author Lukas
   *
   * @return if the decoration is visible
   */
  boolean hasDeco();

  /**
   * triggers a refresh of the component
   * 
   * @param ignoreDirtyCheck
   *          if true than a refresh is executed without checking whether the component is marked as
   *          dirty or not
   */
  void refresh(boolean ignoreDirtyCheck);

  void showFeedback();

  void disableFeedback();

  void setPreferenceStore(IPreferenceStore store);

  void setDirty();

}
