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

package astpa.controlstructure.controller.commands;

import java.util.Map;
import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;

import astpa.controlstructure.CSEditor;
import astpa.model.controlstructure.components.ComponentType;
import astpa.model.controlstructure.interfaces.IRectangleComponent;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * 
 * 
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 */
public class ComponentChangeLayoutCommand extends ControlStructureAbstractCommand {
	
	private IRectangleComponent model;
	private Rectangle layout;
	private Rectangle oldLayout;
	private static final int MIN_WIDTH = 80;
	private static final int MIN_HEIGHT = 25;
	
	
	/**
	 * 
	 * @author Lukas
	 * @param idMap the map in which all components are mapped so that a delete
	 *            and a change of the id can be tracked
	 * 
	 * @param model The dataModel which contains all model classes
	 * @param stepID the stepEditor ID
	 */
	public ComponentChangeLayoutCommand(Map<UUID, UUID> idMap, IControlStructureEditorDataModel model, String stepID) {
		super(idMap, model, stepID);
	}
	
	@Override
	public void execute() {
		this.getDataModel()
			.changeComponentLayout(this.model.getId(), this.layout, this.getStepID().equals(CSEditor.ID));
		
	}
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param rect the new layout constraint
	 */
	public void setConstraint(Rectangle rect) {
		
		if (rect.width < ComponentChangeLayoutCommand.MIN_WIDTH) {
			rect.width = ComponentChangeLayoutCommand.MIN_WIDTH;
		}
		if (rect.height < ComponentChangeLayoutCommand.MIN_HEIGHT) {
			rect.height = ComponentChangeLayoutCommand.MIN_HEIGHT;
		}
		
		this.layout = rect;
	}
	
	/**
	 * 
	 * @author Lukas
	 * 
	 * @param layout the layout
	 */
	public void setOldLayout(Rectangle layout) {
		this.oldLayout = layout;
	}
	
	@Override
	public boolean canExecute() {
		if ((this.model.getComponentType() == ComponentType.PROCESS_VALUE)
			|| (this.model.getComponentType() == ComponentType.PROCESS_VARIABLE)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model the new model
	 */
	public void setModel(Object model) {
		this.model = (IRectangleComponent) model;
		this.oldLayout = ((IRectangleComponent) model).getLayout(this.getStepID().equals(CSEditor.ID));
		
	}
	
	@Override
	public void undo() {
		this.getDataModel().changeComponentLayout(this.model.getId(), this.oldLayout,
			this.getStepID().equals(CSEditor.ID));
	}
	
}
