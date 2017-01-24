/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
/**
 * 
 * @author Lukas Balzer
 */
package xstampp.astpa.controlstructure.controller.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

import xstampp.astpa.controlstructure.controller.policys.CSConnectionPolicy;
import xstampp.astpa.controlstructure.controller.policys.CSDeletePolicy;
import xstampp.astpa.controlstructure.controller.policys.CSDirectEditPolicy;
import xstampp.astpa.controlstructure.figure.TextFieldFigure;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 *
 * @author Lukas Balzer
 *
 */
public class DashedBoxEditPart extends CSAbstractEditPart {

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param model
	 * @param stepId
	 */
	public DashedBoxEditPart(IControlStructureEditorDataModel model,
			String stepId) {
		super(model, stepId, 2);
	}
	
	@Override
	protected IFigure createFigure() {
		TextFieldFigure tmpFigure = new TextFieldFigure(this.getId(),true,true);
		tmpFigure.setPreferenceStore(getStore());
		tmpFigure.setDashed();
		tmpFigure.setCanConnect(true);
		tmpFigure.setBackgroundColor(null);
		tmpFigure.setParent(((IControlStructureEditPart) this.getParent()).getContentPane());
		return tmpFigure;
	}
	@Override
	protected void createEditPolicies() {
		/*
		 * the Edit role is a constant which tells the program in what policy is
		 * to use in what situation when performed,
		 * performRequest(EditPolicy.constant) is called
		 */
//		this.installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
		this.installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new CSDirectEditPolicy(this.getDataModel(), this.getStepId()));
//		this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new CSEditPolicy(
//				this.getDataModel(), this.getStepId()));
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new CSDeletePolicy(
				this.getDataModel(), this.getStepId()));
		this.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new CSConnectionPolicy(this.getDataModel(), this.getStepId()));
	}
}
