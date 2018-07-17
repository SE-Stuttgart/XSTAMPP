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

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Translatable;

import messages.Messages;
import xstampp.astpa.controlstructure.figure.CSFigure;
import xstampp.astpa.controlstructure.figure.ProcessValueFigure;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * @author Lukas
 * 
 */
public class ProcessValueEditPart extends CSAbstractEditPart {

  private static final int TOP_OFFSET = 4;

  /**
   * <p>
   * calls
   * {@link CSAbstractEditPart#CSAbstractEditPart(IControlStructureEditorDataModel, String, Integer)}
   * with 1
   */
  public ProcessValueEditPart(IControlStructureEditorDataModel model,
      String stepId) {
    super(model, stepId, 1);

  }

  @Override
  protected IFigure createFigure() {
    CSFigure tmpFigure = new ProcessValueFigure(this.getId(),
        ProcessValueEditPart.TOP_OFFSET);
    tmpFigure.setStepId(getStepId());
    tmpFigure.setPreferenceStore(getStore());
    tmpFigure.setBorder((Border) null);
    tmpFigure.setParent(((IControlStructureEditPart) this.getParent()).getContentPane());
    tmpFigure.setToolTip(new Label(Messages.ProcessValue));
    return tmpFigure;
  }

  @Override
  public void translateToRoot(Translatable t) {
    this.getFigure().getParent().translateFromParent(t);
    this.getFigure().getParent().getParent().translateFromParent(t);
    this.getFigure().getParent().getParent().getParent()
        .translateFromParent(t);
  }
}
