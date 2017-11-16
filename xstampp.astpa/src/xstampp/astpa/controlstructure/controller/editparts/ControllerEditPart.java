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
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.controlstructure.CSAbstractEditor;
import xstampp.astpa.controlstructure.controller.policys.CSConnectionPolicy;
import xstampp.astpa.controlstructure.controller.policys.CSDeletePolicy;
import xstampp.astpa.controlstructure.controller.policys.CSDirectEditPolicy;
import xstampp.astpa.controlstructure.controller.policys.CSEditPolicy;
import xstampp.astpa.controlstructure.figure.CSFigure;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.preferences.IControlStructureConstants;

/**
 * @author Aliaksei Babkovich
 * 
 */
public class ControllerEditPart extends CSAbstractEditPart {

  /**
   * this constuctor sets the unique ID of this EditPart which is the same in
   * its model and figure
   * <p>
   * calls
   * {@link CSAbstractEditPart#CSAbstractEditPart(IControlStructureEditorDataModel, String, Integer)}
   * with 1
   */
  public ControllerEditPart(IControlStructureEditorDataModel model,
      String stepId) {
    super(model, stepId, 1);
  }

  @Override
  protected IFigure createFigure() {
    ImageDescriptor imgDesc = Activator
        .getImageDescriptor("/icons/buttons/controlstructure/controller_80.png"); //$NON-NLS-1$
    Image img = imgDesc.createImage(null);
    CSFigure tmpFigure = new CSFigure(this.getId(), img,
        IControlStructureConstants.CONTROLSTRUCTURE_CONTROLLER_COLOR);
    tmpFigure.setDeco((boolean) getViewer().getProperty(CSAbstractEditor.IS_DECORATED));
    tmpFigure.setPreferenceStore(getStore());
    tmpFigure.setParent(((IControlStructureEditPart) this.getParent()).getContentPane());
    tmpFigure.setToolTip(new Label(Messages.Controller));
    return tmpFigure;
  }

  @Override
  protected void createEditPolicies() {
    this.installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
    this.installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
        new CSDirectEditPolicy(this.getDataModel()));
    this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new CSEditPolicy(
        this.getDataModel(), this.getStepId()));
    this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new CSDeletePolicy(
        this.getDataModel(), this.getStepId()));
    this.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
        new CSConnectionPolicy(this.getDataModel(), this.getStepId()));
  }

}
