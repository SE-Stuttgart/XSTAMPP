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

package astpa.controlstructure.controller.policys;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import astpa.controlstructure.CSEditor;
import astpa.controlstructure.CSAbstractEditor;
import astpa.controlstructure.controller.commands.ComponentChangeLayoutCommand;
import astpa.controlstructure.controller.commands.ComponentCreateCommand;
import astpa.controlstructure.controller.editparts.IControlStructureEditPart;
import astpa.controlstructure.figure.ComponentFigure;
import astpa.controlstructure.figure.IControlStructureFigure;
import astpa.controlstructure.figure.TextFieldFigure;
import astpa.model.controlstructure.components.ComponentType;
import astpa.model.controlstructure.interfaces.IComponent;
import astpa.model.controlstructure.interfaces.IRectangleComponent;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * CSEditPolicy manages the positioning/creation of the components inside their parents
 * 
 * 
 * @version 1.0
 * @author Lukas Balzer, Aliaksei Babkovich
 * 
 */
public class CSEditPolicy extends XYLayoutEditPolicy {
	
	private IControlStructureEditorDataModel dataModel;
	private Figure parentFeedback;
	private final String stepID;
	/**
	 * the offset of the process variables and values
	 * 
	 * @author Lukas Balzer
	 */
	public static final int PROCESS_MODEL_COLUMN = 20;
	
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model The DataModel which contains all model classes
	 * @param stepId TODO
	 */
	public CSEditPolicy(IControlStructureEditorDataModel model, String stepId) {
		super();
		this.stepID=stepId;
		
		this.dataModel = model;
	}
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param rect the rectangle to translate
	 */
	public void getAbsoluteLayout(Translatable rect) {
		
		this.translateFromAbsoluteToLayoutRelative(rect);
		
	}
	
	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		ComponentChangeLayoutCommand command =
			new ComponentChangeLayoutCommand(this.dataModel, this.stepID);
		IFigure childFigure = ((IControlStructureEditPart)child).getFigure();
		command.setMinConstraint(((IControlStructureFigure)childFigure).getTextField().getMinimumSize());
		command.setModel(child.getModel());
		command.setConstraint((Rectangle) constraint);
		return command;
	}
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if ((request.getType() == RequestConstants.REQ_CREATE)) {
			
			ComponentCreateCommand command =
				new ComponentCreateCommand(this.dataModel, this.stepID);
			command.setFeedbackLayer(this.getFeedbackLayer());
			//the root Edit Part is the EditPart on which this policy is installed
			IRectangleComponent rootModel = (IRectangleComponent) this.getHost().getModel();
			//the EditPart on shall be created is the newObject which is given by the request,
			//which is send from the ModelCreationFactory
			IRectangleComponent compModel = (IRectangleComponent) request.getNewObject();
			
			command.setRootModel(rootModel);
			command.setComponentModel(compModel);
			
	
			if (request.getNewObject() instanceof IComponent) {
				
				Rectangle constraint = (Rectangle) this.getConstraintFor(request);
				//if the components are ment for ProcessModel
				if (rootModel.getComponentType() == ComponentType.PROCESS_VARIABLE
					|| rootModel.getComponentType() == ComponentType.PROCESS_MODEL) {
					command.addConstraint(this.getHost().getParent().getModel());
					constraint = this.addProcessModelConstraint(constraint, rootModel, compModel);
				}
				
				if (compModel.getComponentType() == ComponentType.PROCESS_VALUE
					&& rootModel.getComponentType() != ComponentType.PROCESS_VARIABLE) {
					this.getFeedbackLayer().getChildren().clear();
					this.getFeedbackLayer().repaint();
				}
				
				if (constraint.x < 0) {
					constraint.x = 0;
					constraint.y = 0;
				}
				
				if (!constraint.getSize().contains(
					this.getDefaultSizeFor((ComponentType) request.getNewObjectType(), constraint))) {
					constraint.setSize(getDefaultSizeFor((ComponentType) request.getNewObjectType(), constraint));
				}
				
				command.setLayout(constraint);
				return command;
			}
		}
		return null;
	}
	
	@Override
	public IControlStructureEditPart getHost() {
		return (IControlStructureEditPart) super.getHost();
	}
	
	private Dimension getDefaultSizeFor(ComponentType type, Rectangle rect) {
		Dimension dim = new Dimension();
		switch (type) {
		case PROCESS_VALUE:
		case TEXTFIELD:
			dim.setWidth(Math.max(TextFieldFigure.TEXTBOX_FIGURE_DEFSIZE.width, rect.width));
			dim.setHeight(Math.max(TextFieldFigure.TEXTBOX_FIGURE_DEFSIZE.height, rect.height));
			return dim;
		default:
			
			dim.setWidth(Math.max(ComponentFigure.COMPONENT_FIGURE_DEFSIZE.width, rect.width));
			dim.setHeight(Math.max(ComponentFigure.COMPONENT_FIGURE_DEFSIZE.height, rect.height));
			return dim;
		}
	}
	
	
	private Rectangle addProcessModelConstraint(Rectangle constraint, IRectangleComponent rootModel,
		IRectangleComponent compModel) {
		constraint.y = ((IControlStructureFigure) getHost().getFigure()).getTextField().getPreferredSize().height;
		
		if (rootModel.getComponentType() == ComponentType.PROCESS_MODEL) {
			constraint.y += PROCESS_MODEL_COLUMN;
			constraint.setWidth(rootModel.getLayout(this.stepID.equals(CSEditor.ID)).width - 2 * PROCESS_MODEL_COLUMN);
			
		} else if (compModel.getComponentType() == ComponentType.PROCESS_VALUE) {
			this.parentFeedback = new Figure();
			this.parentFeedback.setBorder(new LineBorder(ColorConstants.lightBlue));
			Rectangle bounds = rootModel.getLayout(this.stepID.equals(CSEditor.ID)).getCopy();
			this.getHostFigure().translateToAbsolute(bounds);
			this.parentFeedback.setBounds(bounds);
			if (this.getFeedbackLayer().getChildren().size() > 1) {
				this.getFeedbackLayer().getChildren().clear();
				this.getFeedbackLayer().repaint();
			}
			this.addFeedback(this.parentFeedback);
			
		}
		for (Object constraintChild : getHost().getChildren()) {
			constraint.y += ((IControlStructureEditPart) constraintChild).getFigure().getBounds().height;
			
		}
		constraint.x = PROCESS_MODEL_COLUMN;
		
		return constraint;
	}
	
}
