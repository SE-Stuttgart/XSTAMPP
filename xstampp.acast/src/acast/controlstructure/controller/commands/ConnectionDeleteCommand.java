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

package acast.controlstructure.controller.commands;

import java.util.UUID;

import acast.controlstructure.CSEditor;
import acast.model.controlstructure.components.CSConnection;
import acast.model.interfaces.IControlStructureEditorDataModel;


/**
 * 
 * This class contains methods for connection deleting command
 * 
 * @author Aliaksei Babkovich
 * @version 1.0
 */
public class ConnectionDeleteCommand extends ControlStructureAbstractCommand {

	private UUID connectionId;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepId
	 *            TODO
	 */
	public ConnectionDeleteCommand(IControlStructureEditorDataModel model,
			String stepId) {
		super(model, stepId);
	}

	/**
	 * 
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * 
	 * @param model
	 *            the ConnectionModel which shall be removed
	 * 
	 */
	public void setLink(CSConnection model) {
		this.connectionId = model.getId();

	}

	@Override
	public boolean canExecute() {
		if (this.connectionId == null) {
			return false;
		}
		return this.getStepID().equals(CSEditor.ID);
	}

	@Override
	public void execute() {
		this.getDataModel().removeConnection(this.connectionId);
	}

	@Override
	public boolean canUndo() {
		if (this.connectionId == null) {
			return false;
		}
		return true;
	}

	@Override
	public void undo() {
		this.getDataModel().recoverConnection(this.connectionId);
	}

}
