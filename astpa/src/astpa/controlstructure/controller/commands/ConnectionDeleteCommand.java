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

import astpa.model.controlstructure.components.Anchor;
import astpa.model.controlstructure.components.CSConnection;
import astpa.model.controlstructure.components.ConnectionType;
import astpa.model.controlstructure.interfaces.IAnchor;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * This class contains methods for connection deleting command
 * 
 * @author Aliaksei Babkovich
 * @version 1.0
 */
public class ConnectionDeleteCommand extends ControlStructureAbstractCommand {
	
	private CSConnection connectionModel;
	private UUID connenctionID;
	
	
	/**
	 * 
	 * @author Lukas Balzer
	 * @param idMap the map in which all components are mapped so that a delete
	 *            and a change of the id can be tracked
	 * 
	 * @param model The DataModel which contains all model classes
	 */
	public ConnectionDeleteCommand(Map<UUID, UUID> idMap, IControlStructureEditorDataModel model) {
		super(idMap, model, null);
	}
	
	/**
	 * 
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * 
	 * @param model the ConnectionModel which shall be removed
	 * 
	 */
	public void setLink(CSConnection model) {
		this.connectionModel = model;
		this.connenctionID = model.getId();
		
	}
	
	@Override
	public boolean canExecute() {
		if (this.connenctionID == null) {
			return false;
		}
		return true;
	}
	
	@Override
	public void execute() {
		this.getDataModel().removeConnection(this.connenctionID);
	}
	
	@Override
	public boolean canUndo() {
		if (this.connenctionID == null) {
			return false;
		}
		return true;
	}
	
	@Override
	public void undo() {
		IAnchor source = this.connectionModel.getSourceAnchor();
		IAnchor target = this.connectionModel.getTargetAnchor();
		ConnectionType type = this.connectionModel.getConnectionType();
		this.getDataModel().addConnection((Anchor) source, (Anchor) target, type);
	}
	
}
