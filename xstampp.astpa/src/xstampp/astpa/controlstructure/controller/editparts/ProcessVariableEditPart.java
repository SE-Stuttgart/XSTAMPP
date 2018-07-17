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

package xstampp.astpa.controlstructure.controller.editparts;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.SWT;

import messages.Messages;
import xstampp.astpa.controlstructure.figure.ProcessValueFigure;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.preferences.IControlStructureConstants;

/**
 * 
 * @author Lukas
 * 
 */
public class ProcessVariableEditPart extends CSAbstractEditPart {

  static final int TOP_OFFSET = 5;

  /**
   * <p>
   * calls
   * {@link CSAbstractEditPart#CSAbstractEditPart(IControlStructureEditorDataModel, String, Integer)}
   * with 1
   */
  public ProcessVariableEditPart(IControlStructureEditorDataModel model,
      String stepId) {
    super(model, stepId, 1);

  }

  @Override
  protected IFigure createFigure() {
    ProcessValueFigure tmpFigure = new ProcessValueFigure(this.getId(),
        ProcessVariableEditPart.TOP_OFFSET);
    tmpFigure.setStepId(getStepId());
    tmpFigure.setAutoPositioning(false);
    tmpFigure.setPreferenceStore(getStore());
    LineBorder border = new LineBorder(1) {
      @Override
      public void paint(IFigure figure1, Graphics graphics, Insets insets) {
        if (getStore()
            .getBoolean(IControlStructureConstants.CONTROLSTRUCTURE_PROCESS_MODEL_BORDER)) {
          super.paint(figure1, graphics, insets);
        }
      }
    };
    tmpFigure.setBorder(border);
    tmpFigure.getTextField().setFontStyle(SWT.BOLD);
    tmpFigure.getTextField().setLineVisible(true);
    tmpFigure.setParent(((IControlStructureEditPart) this.getParent()).getContentPane());
    tmpFigure.setToolTip(new Label(Messages.ProcessVariable));
    return tmpFigure;
  }

  @Override
  public void translateToRoot(Translatable t) {
    this.getFigure().getParent().translateFromParent(t);
    this.getFigure().getParent().getParent().translateFromParent(t);
  }

}
