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

import messages.Messages;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import xstampp.astpa.Activator;
import xstampp.astpa.controlstructure.controller.policys.CSConnectionPolicy;
import xstampp.astpa.controlstructure.figure.ComponentFigure;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.preferences.IControlStructureConstants;

/**
 * @author Aliaksei Babkovich
 * @version 1.0
 * 
 */
public class SensorEditPart extends CSAbstractEditPart {

  /**
   * this constuctor sets the unique ID of this EditPart which is the same in
   * its model and figure
   * 
   * @author Lukas Balzer
   * 
   * @param model
   *          The DataModel which contains all model classes
   * @param stepId
   *          this steps id
   */
  public SensorEditPart(IControlStructureEditorDataModel model, String stepId) {
    super(model, stepId, 1);
  }

  @Override
  protected IFigure createFigure() {
    ImageDescriptor imgDesc = Activator
        .getImageDescriptor("/icons/buttons/controlstructure/sensor_80.png"); //$NON-NLS-1$
    Image img = imgDesc.createImage(null);
    ComponentFigure tmpFigure = new ComponentFigure(this.getId(), img,
        IControlStructureConstants.CONTROLSTRUCTURE_SENSOR_COLOR);
    tmpFigure.setPreferenceStore(getStore());
    tmpFigure.setParent(((IControlStructureEditPart) this.getParent()).getContentPane());
    tmpFigure.setToolTip(new Label(Messages.Sensor));
    return tmpFigure;
  }

  @Override
  protected void createEditPolicies() {
    super.createEditPolicies();
    this.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
        new CSConnectionPolicy(this.getDataModel(), this.getStepId()));
  }

}
