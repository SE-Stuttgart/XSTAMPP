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
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import astpa.controlstructure.controller.commands.ConnectionDeleteCommand;
import astpa.controlstructure.controller.editParts.IControlStructureEditPart;
import astpa.model.controlstructure.components.CSConnection;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * This class contains policy for removing connection
 * 
 * @author Lukas Balzer, Aliaksei Babkovich
 * @version 1.0
 */
public class CSConnectionDeleteEditPolicy extends ConnectionEditPolicy implements IControlStructurePolicy {
	
	private final IControlStructureEditorDataModel dataModel;
	
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model The DataModel which contains all model classes
	 */
	public CSConnectionDeleteEditPolicy(IControlStructureEditorDataModel model) {
		super();
		this.dataModel = model;
	}
	
	@Override
	protected Command getDeleteCommand(GroupRequest arg0) {
		ConnectionDeleteCommand command = new ConnectionDeleteCommand(this.dataModel);
		command.setLink((CSConnection) this.getHost().getModel());
		return command;
	}
	
	@Override
	public IControlStructureEditPart getHost() {
		return (IControlStructureEditPart) super.getHost();
	}
}
