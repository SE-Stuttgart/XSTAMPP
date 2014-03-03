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

import astpa.controlstructure.figure.CSAnchor;
import astpa.controlstructure.figure.CSFigure;
import astpa.controlstructure.figure.CSFlyAnchor;
import astpa.controlstructure.figure.IAnchorFigure;
import astpa.controlstructure.figure.IControlStructureFigure;
import astpa.model.controlstructure.components.Anchor;
import astpa.model.controlstructure.components.ComponentType;
import astpa.model.controlstructure.components.ConnectionType;
import astpa.model.controlstructure.interfaces.IAnchor;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * This command is called by the <code>CSConnectionPolicy to 
 * create a connection between a sourceFigure and a targetFigure
 * 
 * 
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 */
public class ConnectionCreateCommand extends ControlStructureAbstractCommand {
	
	private Anchor sourceAnchorModel, targetAnchorModel;
	private UUID connectionId;
	private ConnectionType connectionType;
	private CSFigure parent;
	
	private IAnchorFigure sourceAnchorFigure;
	
	
	/**
	 * 
	 * @author Lukas Balzer
	 * @param idMap the map in which all components are mapped so that a delete
	 *            and a change of the id can be tracked
	 * 
	 * @param model the
	 * 
	 */
	public ConnectionCreateCommand(Map<UUID, UUID> idMap, IControlStructureEditorDataModel model) {
		super(idMap, model, null);
	}
	
	/**
	 * 
	 * 
	 * @author Aliaksei Babkovich, Lukas Balzer
	 * 
	 * @param source The Anchor where the connection starts
	 */
	public void setSourceModel(IAnchorFigure source) {
		this.parent = (CSFigure) source.getOwner();
		UUID id = ((IControlStructureFigure) source.getOwner()).getId();
		int x = source.getAnchorFactor().x;
		int y = source.getAnchorFactor().y;
		boolean flys = false;
		if (source instanceof CSFlyAnchor) {
			flys = true;
		}
		this.sourceAnchorFigure = source;
		Anchor sourceAnchor = new Anchor(flys, x, y, id);
		this.sourceAnchorModel = sourceAnchor;
	}
	
	/**
	 * sets the target Anchor of this connection and updates the source anchor
	 * if its a Flying Anchor
	 * 
	 * @author Aliaksei Babkovich, Lukas Balzer
	 * 
	 * @param target The figure an arrow points to or the a connection ends
	 */
	public void setTargetModel(IAnchorFigure target) {
		UUID id = ((IControlStructureFigure) target.getOwner()).getId();
		int x = target.getAnchorFactor().x;
		int y = target.getAnchorFactor().y;
		boolean flys = false;
		
		if ((target instanceof CSFlyAnchor) && (this.sourceAnchorFigure instanceof CSAnchor)) {
			
			x = target.getAnchorFactor().x;
			y = target.getAnchorFactor().y;
			flys = true;
		} else if ((this.sourceAnchorFigure instanceof CSFlyAnchor) && (target instanceof CSAnchor)) {
			((CSFlyAnchor) this.sourceAnchorFigure).setRelation((CSAnchor) target);
			this.sourceAnchorModel.setxOrientation(this.sourceAnchorFigure.getAnchorFactor().x);
			this.sourceAnchorModel.setyOrientation(this.sourceAnchorFigure.getAnchorFactor().y);
		}
		this.targetAnchorModel = new Anchor(flys, x, y, id);
		
	}
	
	@Override
	public boolean canExecute() {
		
		if ((this.sourceAnchorModel == null) || (this.targetAnchorModel == null)) {
			this.parent.removeHighlighter();
			return false;
		} else if (this.sourceAnchorModel.getOwnerId().equals(this.targetAnchorModel.getOwnerId())) {
			this.parent.removeHighlighter();
			return false;
		} else if (!this.checkConnection(this.sourceAnchorModel, this.targetAnchorModel)) {
			return false;
		}
		return true;
	}
	
	private boolean checkConnection(IAnchor source, IAnchor target) {
		ComponentType targetType =
			this.getDataModel().getComponent(this.targetAnchorModel.getOwnerId()).getComponentType();
		ComponentType sourceType =
			this.getDataModel().getComponent(this.sourceAnchorModel.getOwnerId()).getComponentType();
		
		if (!this.flyAnchorConstraint(source, target)) {
			return false;
		}
		if ((sourceType == ComponentType.CONTROLLER) && (targetType == ComponentType.SENSOR)) {
			return false;
		}
		if ((sourceType == ComponentType.ACTUATOR) && (targetType != ComponentType.CONTROLLED_PROCESS)) {
			return false;
		}
		if ((sourceType == ComponentType.SENSOR) && (targetType != ComponentType.CONTROLLER)) {
			return false;
		}
		if ((sourceType == ComponentType.CONTROLLED_PROCESS) && (targetType == ComponentType.ACTUATOR)) {
			return false;
		}
		return true;
	}
	
	private boolean flyAnchorConstraint(IAnchor source, IAnchor target) {
		ComponentType targetType =
			this.getDataModel().getComponent(this.targetAnchorModel.getOwnerId()).getComponentType();
		ComponentType sourceType =
			this.getDataModel().getComponent(this.sourceAnchorModel.getOwnerId()).getComponentType();
		if (source.isFlying() && target.isFlying()) {
			return false;
		}
		if (source.isFlying()
			&& ((targetType != ComponentType.CONTROLLER) && (targetType != ComponentType.CONTROLLED_PROCESS))) {
			return false;
		}
		if (target.isFlying() && (sourceType != ComponentType.CONTROLLED_PROCESS)) {
			return false;
		}
		if (target.isFlying() && (sourceType != ComponentType.CONTROLLED_PROCESS)) {
			return false;
		}
		return true;
	}
	
	private void updateAnchor() {
		UUID newId = this.getComponentIdMap().get(this.sourceAnchorModel.getOwnerId());
		this.sourceAnchorModel.setOwnerId(newId);
		
		newId = this.getComponentIdMap().get(this.targetAnchorModel.getOwnerId());
		this.targetAnchorModel.setOwnerId(newId);
	}
	
	@Override
	public void execute() {
		// this.adjust();
		this.connectionId =
			this.getDataModel().addConnection(this.sourceAnchorModel, this.targetAnchorModel, this.connectionType);
		
	}
	
	@Override
	public boolean canUndo() {
		if ((this.sourceAnchorModel == null) || (this.targetAnchorModel == null) || (this.connectionId == null)) {
			return false;
		}
		return true;
	}
	
	@Override
	public void redo() {
		this.updateAnchor();
		this.execute();
	}
	
	@Override
	public void undo() {
		this.getDataModel().removeConnection(this.connectionId);
	}
	
	/**
	 * 
	 * @author Aliaksei Babkovich, Lukas Balzer
	 * 
	 * @param connectionType <ul>
	 *            switch(connectionType)
	 *            <li>case 1:Arrow
	 *            <li>case 2:Dotted Arrow
	 *            </ul>
	 */
	public void setConnectionType(ConnectionType connectionType) {
		this.connectionType = connectionType;
	}
	
	/**
	 * 
	 * 
	 * @author Aliaksei Babkovich, Lukas Balzer
	 * 
	 * @return the type of the connection
	 */
	public ConnectionType getConnectionType() {
		return this.connectionType;
	}
	
}
