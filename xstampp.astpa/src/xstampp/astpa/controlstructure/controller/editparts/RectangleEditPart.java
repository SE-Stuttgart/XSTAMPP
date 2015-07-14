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

package xstampp.astpa.controlstructure.controller.editparts;

import messages.Messages;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;

import xstampp.astpa.controlstructure.controller.policys.CSDeletePolicy;
import xstampp.astpa.controlstructure.controller.policys.CSDirectEditPolicy;
import xstampp.astpa.controlstructure.controller.policys.CSEditPolicy;
import xstampp.astpa.controlstructure.figure.CSRectangleContainer;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * TextFieldEditPart
 * 
 * @version 1.0
 * @author Lukas Balzer
 * 
 */
public class RectangleEditPart extends CSAbstractEditPart{

	private IFigure parentFigure;
	/**
	 * this constuctor sets the unique ID of this EditPart which is the same in
	 * its model and figure
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepId
	 *            TODO
	 * @param parent TODO
	 */
	public RectangleEditPart(IControlStructureEditorDataModel model,
			String stepId, IFigure figure) {
		super(model, stepId, 1);
		this.parentFigure= figure;
	}

	@Override
	protected IFigure createFigure() {
	RectangleFigure tmp = new CSRectangleContainer(getId());
	tmp.setParent(this.parentFigure);
	tmp.setToolTip(new Label("rec"));

		return tmp;
	}
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
//		this.installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
//		this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new CSEditPolicy(
//				this.getDataModel(), this.getStepId()));
	}
}
