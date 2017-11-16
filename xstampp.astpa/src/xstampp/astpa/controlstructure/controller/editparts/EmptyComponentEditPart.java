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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;

import xstampp.astpa.controlstructure.controller.policys.CSConnectionPolicy;
import xstampp.astpa.controlstructure.figure.TextFieldFigure;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 * 
 */
public class EmptyComponentEditPart extends CSAbstractEditPart {

  /**
   * this constuctor sets the unique ID of the
   * {@link EmptyComponentEditPart} which is the same in its model and
   * figure
   * <p>
   * calls
   * {@link CSAbstractEditPart#CSAbstractEditPart(IControlStructureEditorDataModel, String, Integer)}
   * with 1
   */
  public EmptyComponentEditPart(IControlStructureEditorDataModel model,
      String stepId) {
    super(model, stepId, 1);
  }

  @Override
  protected IFigure createFigure() {
    TextFieldFigure tmpFigure = new TextFieldFigure(this.getId(), true, true);
    tmpFigure.setPreferenceStore(getStore());
    tmpFigure.setParent(((IControlStructureEditPart) this.getParent()).getContentPane());
    tmpFigure.setToolTip(new Label("Undefined Component"));
    tmpFigure.setBordered();
    return tmpFigure;
  }

  @Override
  protected void createEditPolicies() {
    super.createEditPolicies();
    this.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
        new CSConnectionPolicy(this.getDataModel(), this.getStepId()));
  }

}
