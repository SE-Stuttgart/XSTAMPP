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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import astpa.controlstructure.controller.commands.DeleteCommand;
import astpa.controlstructure.controller.editParts.IControlStructureEditPart;
import astpa.model.controlstructure.interfaces.IRectangleComponent;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * @author Aliaksei Babkovich
 * @version 1.0
 */
public class CSDeletePolicy extends ComponentEditPolicy {
	
	private IControlStructureEditorDataModel dataModel;
	
	
	/**
	 * 
	 * @author Lukas
	 * 
	 * @param model The DataModel which contains all model classes
	 */
	public CSDeletePolicy(IControlStructureEditorDataModel model) {
		super();
		this.dataModel = model;
	}
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteCommand command = new DeleteCommand(this.getHost().getIdMap(), this.dataModel);
		command.setModel((IRectangleComponent) this.getHost().getModel());
		
		command.setParentModel(this.getHost().getParent().getModel());
		return command;
		
	}
	
	@Override
	public IControlStructureEditPart getHost() {
		return (IControlStructureEditPart) super.getHost();
	}
	
}
