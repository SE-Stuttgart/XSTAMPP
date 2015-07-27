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

package xstampp.astpa.controlstructure.controller.commands;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.controller.policys.CSEditPolicy;
import xstampp.astpa.controlstructure.figure.ConnectionFigure;
import xstampp.astpa.haz.controlstructure.interfaces.IComponent;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * This class is command that uses ComponentCreationFactory class and memorize
 * useful information and creates aktuator component. Used in palette.
 * 
 * @version 1.0
 * @author Aliaksei Babkovich, Lukas Balzer
 * 
 */

public class ComponentCreateCommand extends ControlStructureAbstractCommand {

	private IRectangleComponent rootModel;
	private IRectangleComponent compModel;
	private IRectangleComponent constraintModel;
	private UUID componentId;
	private UUID rootId;
	private Rectangle layout;
	private Rectangle rootLayout;
	private Rectangle oldRootLayout;
	private UUID relativeId;

	/**
	 * This Constructor sets the Model of the Component and of it's root to null
	 * so the Component has no root when this command is called
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * @param model
	 *            The dataModel which contains all model classes
	 * @param stepID
	 *            the stepEditor ID
	 * 
	 */
	public ComponentCreateCommand(IControlStructureEditorDataModel model,
			String stepID) {
		super(model, stepID);
		this.constraintModel = new Component();
		this.rootLayout = new Rectangle();
		this.oldRootLayout =  new Rectangle();
		this.compModel = null;
		this.rootModel = null;

	}

	/**
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * 
	 * @param csModel
	 *            The Model of the Component which shall be set
	 */
	public void setComponentModel(IRectangleComponent csModel) {

		this.compModel = csModel;
	}

	/**
	 * 
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * 
	 * @param csModel
	 *            The Model of the Root, which shall be set as the parent of the
	 *            Component
	 */
	public void setRootModel(IRectangleComponent csModel) {
		this.rootModel = csModel;
		this.rootLayout = csModel.getLayout(
				this.getStepID().equals(CSEditor.ID)).getCopy();
		this.oldRootLayout = this.rootLayout.getCopy();
		this.rootId = csModel.getId();
	}
	
	/**
	 * this adds a component to the map, which needs to be refreshed after
	 * execution
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            the layout constraint
	 */
	public void addConstraint(Object model) {
		this.constraintModel = (IRectangleComponent) model;
	}

	/**
	 * 
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * 
	 * @param rect
	 *            the rectangle which stores the new Layout
	 */
	public void setLayout(Rectangle rect) {
		if (this.compModel == null) {
			return;
		}
		if (this.rootModel.getComponentType() == ComponentType.PROCESS_VARIABLE) {
			this.rootLayout.setHeight(this.oldRootLayout.height + rect.height);
			this.rootLayout.setWidth(Math.max(this.oldRootLayout.width,
					rect.width + CSEditPolicy.PROCESS_MODEL_COLUMN));

		}
		this.layout = rect;
	}

	@Override
	public boolean canExecute() {
		if ((this.rootModel == null) || (this.compModel == null)) {
			return false;
		} else if (this.rootModel.getComponentType() == ComponentType.TEXTFIELD) {
			return false;
		}
		switch (this.compModel.getComponentType()) {
		case PROCESS_MODEL: {
			return this.rootModel.getComponentType() == ComponentType.CONTROLLER;
		}
		case PROCESS_VARIABLE: {
			return this.rootModel.getComponentType() == ComponentType.PROCESS_MODEL;
		}
		case PROCESS_VALUE: {
			return this.rootModel.getComponentType() == ComponentType.PROCESS_VARIABLE;
		}
		case TEXTFIELD: {
			return true;
		}
		case CONTROLACTION: {
			return true;
		}
		default: {
			return this.rootModel.getComponentType() == ComponentType.ROOT;
		}
		}

	}

	/**
	 * add new figure to root figure as child
	 */
	@Override
	public void execute() {
		this.deleteFeedback();
		this.componentId = this.getDataModel().addComponent(
				this.compModel.getControlActionLink(), this.rootModel.getId(),
				this.layout, this.compModel.getText(),
				this.compModel.getComponentType(), 0);
		if(this.rootModel.getComponentType() == ComponentType.CONTAINER){
			this.getDataModel().setRelativeOfComponent(this.componentId, this.rootModel.getRelative());
		}else{
			this.getDataModel().setRelativeOfComponent(this.componentId, this.relativeId);
		}
		this.updateParentConstraint();

	}

	@Override
	public boolean canUndo() {
		if ((this.rootModel == null) || (this.compModel == null)) {
			return false;
		}
		return true;

	}

	@Override
	public void undo() {
		this.getDataModel().removeComponent(this.componentId);
		this.getDataModel().changeComponentLayout(this.rootId,
				this.oldRootLayout, this.getStepID().equals(CSEditor.ID));

	}

	@Override
	public void redo() {
		this.getDataModel().recoverComponent(this.rootId,
				this.compModel.getId());
		// updateParentConstraint();

	}

	private void updateParentConstraint() {
		this.getDataModel().changeComponentLayout(this.rootId, this.rootLayout,
				this.getStepID().equals(CSEditor.ID));
		this.compModel = this.getDataModel().getComponent(this.componentId);

		Rectangle conflict = this.rootLayout;
		// this loop updates all children of the constraint component
		for (IRectangleComponent child : this.constraintModel.getChildren()) {
			Rectangle constrainRect = child.getLayout(
					this.getStepID().equals(CSEditor.ID)).getCopy();
			// if the conflict intersects the child than the layout is moved
			// down
			if (conflict.intersects(constrainRect)
					&& !child.equals(this.rootModel)) {
				constrainRect.y += conflict.getIntersection(constrainRect).height;
				this.getDataModel().changeComponentLayout(child.getId(),
						constrainRect, this.getStepID().equals(CSEditor.ID));
				conflict = constrainRect;
			}
		}
	}

	public void setRelative(UUID relativeID) {
		this.relativeId =relativeID;
	}
}
