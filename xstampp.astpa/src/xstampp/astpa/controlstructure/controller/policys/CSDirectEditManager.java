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

package xstampp.astpa.controlstructure.controller.policys;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;

import xstampp.astpa.controlstructure.utilities.CSTextLabel;
import xstampp.util.DirectEditor;

/**
 * 
 * CSDirectEditManager
 * 
 * @version 1.0
 * @author Lukas Balzer
 * 
 */
public class CSDirectEditManager extends DirectEditManager {

  private CSTextLabel label;

  /**
   * 
   * 
   * @author Lukas Balzer
   * 
   * @param source
   *          The Component in which the editAction is executed
   * @param editorType
   *          The Type of the Editor which is used to edit the Label
   * @param locator
   *          The locator class decides where the Editor is positioned
   * @param label
   *          The Label whose content is to be manipulated
   */
  public CSDirectEditManager(GraphicalEditPart source, Class<?> editorType,
      CellEditorLocator locator, CSTextLabel label) {
    super(source, editorType, locator);
    this.label = label;

  }

  @Override
  protected void initCellEditor() {
    String initialLabelText = this.label.getText();

    this.getCellEditor().setValue(initialLabelText);

    ((DirectEditor) this.getCellEditor()).setTextFont(this.label
        .getFont());
    ((DirectEditor) this.getCellEditor()).setTextColor(this.label
        .getForegroundColor());

  }

}
