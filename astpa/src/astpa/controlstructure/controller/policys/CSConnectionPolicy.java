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

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import astpa.controlstructure.controller.commands.ConnectionCreateCommand;
import astpa.controlstructure.controller.commands.ConnectionReconnectCommand;
import astpa.controlstructure.controller.editParts.CSAbstractEditPart;
import astpa.controlstructure.controller.editParts.CSConnectionEditPart;
import astpa.controlstructure.controller.editParts.IControlStructureEditPart;
import astpa.controlstructure.figure.IAnchorFigure;
import astpa.controlstructure.figure.IControlStructureFigure;
import astpa.model.controlstructure.components.ConnectionType;
import astpa.model.controlstructure.components.CSConnection;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * 
 * 
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 */
public class CSConnectionPolicy extends GraphicalNodeEditPolicy {
	
	private IControlStructureEditorDataModel dataModel;
	
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model The DataModel which contains all model classes
	 */
	public CSConnectionPolicy(IControlStructureEditorDataModel model) {
		super();
		this.dataModel = model;
	}
	
	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		
		ConnectionCreateCommand cmd = (ConnectionCreateCommand) request.getStartCommand();
		
		ConnectionAnchor targetAnchor = ((CSAbstractEditPart) this.getHost()).getTargetConnectionAnchor(request);
		
		cmd.setTargetModel((IAnchorFigure) targetAnchor);
		return cmd;
	}
	
	@Override
	protected void removeFeedback(IFigure figure) {
		((IControlStructureFigure) this.getHostFigure()).disableOffset();
		super.removeFeedback(figure);
	}
	
	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		((IControlStructureFigure) this.getHostFigure()).enableOffset();
		ConnectionCreateCommand cmd = new ConnectionCreateCommand(this.getHost().getIdMap(), this.dataModel);
		ConnectionAnchor sourceAnchor = ((CSAbstractEditPart) this.getHost()).getSourceConnectionAnchor(request);
		cmd.setConnectionType(((ConnectionType) request.getNewObjectType()));
		
		cmd.setSourceModel((IAnchorFigure) sourceAnchor);
		
		request.setStartCommand(cmd);
		return cmd;
	}
	
	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		((IControlStructureFigure) this.getHostFigure()).enableOffset();
		CSConnection conn = (CSConnection) request.getConnectionEditPart().getModel();
		
		ConnectionAnchor sourceAnchor = ((CSAbstractEditPart) this.getHost()).getSourceConnectionAnchor(request);
		ConnectionAnchor targetAnchor = ((CSConnectionEditPart) request.getConnectionEditPart()).getTargetAnchor();
		ConnectionReconnectCommand cmd =
			new ConnectionReconnectCommand(conn, this.getHost().getIdMap(), this.dataModel);
		
		cmd.setNewSourceNode((IAnchorFigure) targetAnchor, (IAnchorFigure) sourceAnchor);
		
		return cmd;
	}
	
	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		((IControlStructureFigure) this.getHostFigure()).enableOffset();
		CSConnection conn = (CSConnection) request.getConnectionEditPart().getModel();
		ConnectionAnchor sourceAnchor = ((CSConnectionEditPart) request.getConnectionEditPart()).getSourceAnchor();
		ConnectionAnchor targetAnchor = ((CSAbstractEditPart) this.getHost()).getTargetConnectionAnchor(request);
		
		ConnectionReconnectCommand cmd =
			new ConnectionReconnectCommand(conn, this.getHost().getIdMap(), this.dataModel);
		
		cmd.setNewTargetNode((IAnchorFigure) targetAnchor, (IAnchorFigure) sourceAnchor);
		return cmd;
	}
	
	@Override
	public IControlStructureEditPart getHost() {
		return (IControlStructureEditPart) super.getHost();
	}
}
