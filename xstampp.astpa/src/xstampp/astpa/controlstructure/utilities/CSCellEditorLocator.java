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

package xstampp.astpa.controlstructure.utilities;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * CSCellEditorLocator defines where to display the TextEditor frame which is
 * displayed, when the user clicks on the text he wants to edit
 * 
 * @version 1.1
 * @author Lukas Balzer
 * 
 */
public class CSCellEditorLocator implements CellEditorLocator {

  /**
   * The Label which shall be edited
   */
  private final IFigure nameLabel;

  /**
   * the offset is used to adapt the width if the editor so that the lineFeeds
   * are set similar to the CSTaxtLabel
   */
  private static final int WIDTH_OFFSET = 9;
  private static final int HEIGHT_OFFSET = 0;
  private static final int X_OFFSET = -3;
  private static final int Y_OFFSET = 0;

  /**
   * 
   * 
   * @author Lukas Balzer
   * 
   * @param label
   *          The label is set as the nameLabel
   */
  public CSCellEditorLocator(IFigure label) {
    this.nameLabel = label;

  }

  @Override
  public void relocate(CellEditor celleditor) {
    Rectangle rect = this.nameLabel.getBounds().getCopy();
    rect.setWidth(this.nameLabel.getBounds().width);
    this.nameLabel.translateToAbsolute(rect);
    int editorHeight = this.nameLabel.getBounds().height;
    /*
     * in text the input, which is initially given by the current component
     * name is stored. The variable
     */
    Text text = (Text) celleditor.getControl();
    text.setSize(text.computeSize(rect.width + CSCellEditorLocator.WIDTH_OFFSET, SWT.DEFAULT));
    // if the size is determined to be larger than the text lines itself
    // this the original size, will be displayed as long as it not
    // overwritten by text
    editorHeight = Math.max(rect.height,
        text.getLineHeight() * text.getLineCount());
    text.setBounds(rect.x + CSCellEditorLocator.X_OFFSET, rect.y
        + CSCellEditorLocator.Y_OFFSET,
        rect.width
            + CSCellEditorLocator.WIDTH_OFFSET,
        editorHeight
            + CSCellEditorLocator.HEIGHT_OFFSET);

  }

}
