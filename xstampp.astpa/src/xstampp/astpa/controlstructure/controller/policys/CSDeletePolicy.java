/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.controlstructure.controller.policys;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import xstampp.astpa.controlstructure.controller.commands.DeleteCommand;
import xstampp.astpa.controlstructure.controller.editparts.IControlStructureEditPart;
import xstampp.astpa.haz.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * @author Aliaksei Babkovich
 * @version 1.0
 */
public class CSDeletePolicy extends ComponentEditPolicy {

	private IControlStructureEditorDataModel dataModel;
	private final String stepID;

	/**
	 * 
	 * @author Lukas
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepId
	 *            TODO
	 */
	public CSDeletePolicy(IControlStructureEditorDataModel model, String stepId) {
		super();
		this.stepID = stepId;
		this.dataModel = model;
	}

	@Override
	public Command getCommand(Request request) {
		// TODO Auto-generated method stub
		return super.getCommand(request);
	}
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteCommand command = new DeleteCommand(this.dataModel, this.stepID);
		command.setModel((IRectangleComponent) this.getHost().getModel());

		command.setParentModel(this.getHost().getParent().getModel());
		return command;

	}

	@Override
	public IControlStructureEditPart getHost() {
		return (IControlStructureEditPart) super.getHost();
	}

}
