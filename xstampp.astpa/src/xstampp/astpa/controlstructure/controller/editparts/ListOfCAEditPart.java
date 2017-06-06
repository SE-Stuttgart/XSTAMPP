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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;

import xstampp.astpa.controlstructure.controller.policys.CSDeletePolicy;
import xstampp.astpa.controlstructure.controller.policys.CSEditPolicy;
import xstampp.astpa.controlstructure.controller.policys.CSSelectionEditPolicy;
import xstampp.astpa.controlstructure.figure.CSRectangleContainer;
import xstampp.astpa.messages.Messages;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.preferences.IControlStructureConstants;

/**
 * 
 * TextFieldEditPart
 * 
 * @version 1.0
 * @author Lukas Balzer
 * 
 */
public class ListOfCAEditPart extends AbstractMemberEditPart implements ISelectionChangedListener{

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
	 */
	public ListOfCAEditPart(IControlStructureEditorDataModel model,
			String stepId) {
		super(model, stepId, 1);
	}

	@Override
	public void setParent(EditPart parent) {
		if(parent == null){
			getViewer().removeSelectionChangedListener(this);
			super.setParent(parent);
		}else{
			super.setParent(parent);
			getViewer().addSelectionChangedListener(this);
		}
		
		
	}
	@Override
	protected IFigure createFigure() {
	CSRectangleContainer tmp = new CSRectangleContainer(getId());
	tmp.setBorder(new LineBorder(ColorConstants.black, 1, SWT.BORDER_DASH){
		@Override
		public void paint(IFigure figure, Graphics graphics,
				Insets insets) {
			if(getStore().getBoolean(IControlStructureConstants.CONTROLSTRUCTURE_SHOW_LISTOFCA_BORDER)){
				graphics.setLineStyle(SWT.LINE_CUSTOM);
				graphics.setLineDash(new int[]{4});
				graphics.setLineDashOffset(4);
				super.paint(figure, graphics, insets);
			}
		}
	});
	tmp.setPreferenceStore(getStore());
	tmp.addMouseMotionListener(this);
	tmp.setParent(((IControlStructureEditPart) this.getParent()).getContentPane());
	tmp.setToolTip(new Label(Messages.ListOfCAEditPart_ToolTip0));

		return tmp;
	}
	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new CSSelectionEditPolicy());
		
		this.installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
		this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new CSEditPolicy(
				this.getDataModel(), this.getStepId()));
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new CSDeletePolicy(
				this.getDataModel(), this.getStepId()));
	}
	@Override
	public boolean understandsRequest(Request req) {
//		if(req.getType() == RequestConstants.REQ_DIRECT_EDIT){
//			return false;
//		}
		return super.understandsRequest(req);
	}
	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
	 * @author Lukas Balzer, Aliaksei Babkovich
	 */
	@Override
	public void performRequest(Request req) {
		
		if(req.getType().equals(RequestConstants.REQ_OPEN) ||req.getType().equals(RequestConstants.REQ_SELECTION)) {
	        ((CSRectangleContainer)getFigure()).setSelected(isActive());
	        for(Object child :getFigure().getChildren()){
				((IFigure) child).setBorder(new LineBorder(ColorConstants.gray, 1, SWT.BORDER_DASH));
			}
	    }
		
	}
	

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		StructuredSelection selection = (StructuredSelection) event.getSelection();
		if(((EditPart) selection.getFirstElement()).getParent() != this){
			((CSRectangleContainer)getFigure()).setSelected(false);
			for(Object child :getFigure().getChildren()){
				((IFigure) child).setBorder(null);
			}
		}
	}
}
