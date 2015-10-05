/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package acast.controlstructure.controller.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import acast.Activator;
import acast.controlstructure.controller.policys.CSConnectionPolicy;
import acast.controlstructure.controller.policys.CSDeletePolicy;
import acast.controlstructure.controller.policys.CSDirectEditPolicy;
import acast.controlstructure.controller.policys.CSEditPolicy;
import acast.controlstructure.figure.ComponentFigure;
import acast.model.interfaces.IControlStructureEditorDataModel;
import acast.preferences.IACASTPreferences;
import messages.Messages;



/**
 * @author Aliaksei Babkovich
 * 
 */
public class ControllerEditPart extends CSAbstractEditPart {

	/**
	 * this constuctor sets the unique ID of this EditPart which is the same in
	 * its model and figure
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepId
	 *            this steps id
	 */
	public ControllerEditPart(IControlStructureEditorDataModel model,
			String stepId) {
		super(model, stepId, 1);
	}

	@Override
	protected IFigure createFigure() {
		ImageDescriptor imgDesc = Activator
				.getImageDescriptor("/icons/buttons/controlstructure/controller_icon.png"); //$NON-NLS-1$
		Image img = imgDesc.createImage(null);
		ComponentFigure tmpFigure = new ComponentFigure(this.getId(), img,
				IACASTPreferences.CONTROLSTRUCTURE_CONTROLLER_COLOR);
		tmpFigure.setParent(((IControlStructureEditPart) this.getParent()).getContentPane());
		tmpFigure.setToolTip(new Label(Messages.Controller));
		return tmpFigure;
	}

	@Override
	protected void createEditPolicies() {
		this.installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
		this.installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new CSDirectEditPolicy(this.getDataModel(), this.getStepId()));
		this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new CSEditPolicy(
				this.getDataModel(), this.getStepId()));
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new CSDeletePolicy(
				this.getDataModel(), this.getStepId()));
		this.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new CSConnectionPolicy(this.getDataModel(), this.getStepId()));
	}

}
