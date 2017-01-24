/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.figure.CSAnchor;
import xstampp.astpa.controlstructure.figure.CSFlyAnchor;
import xstampp.astpa.controlstructure.figure.IAnchorFigure;
import xstampp.astpa.controlstructure.figure.IControlStructureFigure;
import xstampp.astpa.model.controlstructure.components.Anchor;
import xstampp.astpa.model.controlstructure.components.CSConnection;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * 
 * 
 * @author Aliaksei Babkovich
 * @version 1.0
 */
public class ConnectionReconnectCommand extends ControlStructureAbstractCommand {

	private UUID connId;
	private Anchor oldSourceAnchorModel;
	private Anchor oldTargetAnchorModel;
	private Anchor newSourceAnchorModel;
	private Anchor newTargetAnchorModel;

	/**
	 * 
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * 
	 * @param conn
	 *            The Connection to manipulate
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepID
	 *            the stepEditor ID
	 */
	public ConnectionReconnectCommand(CSConnection conn,
			IControlStructureEditorDataModel model, String stepID) {
		super(model, stepID);
		if (conn == null) {
			throw new IllegalArgumentException();
		}
		this.connId = conn.getId();
		this.oldSourceAnchorModel = conn.getSourceAnchor();
		this.oldTargetAnchorModel = conn.getTargetAnchor();

	}

	@Override
	public boolean canExecute() {

		if (this.newSourceAnchorModel != null) {
			return this.checkSourceReconnection();
		} else if (this.newTargetAnchorModel != null) {
			return this.checkTargetReconnection();
		}
		return this.getStepID().equals(CSEditor.ID);
	}

	private boolean checkSourceReconnection() {
		if (this.newSourceAnchorModel == null) {
			return false;
		} else if (this.newSourceAnchorModel.equals(this.oldTargetAnchorModel)) {
			return false;
		} else if (this.oldTargetAnchorModel.isFlying()) {
			return false;
		}
		return true;
	}

	private boolean checkTargetReconnection() {
		if (this.newTargetAnchorModel == null) {
			return false;
		} else if (this.oldSourceAnchorModel.equals(this.newTargetAnchorModel)) {
			return false;
		} else if (this.oldSourceAnchorModel.isFlying()) {
			return false;
		}
		return true;
	}

	/**
	 * This function should only be called when reconnecting the sourceAnchor,
	 * it sets the targetAnchorFgiure so it can be referenced in case that
	 * source is flying
	 * 
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * 
	 * @param target
	 *            The old TargetAnchor
	 * @param source
	 *            the new Source the connection should be get
	 * 
	 */
	public void setNewSourceNode(IAnchorFigure target, IAnchorFigure source) {

		// first all values are copied from the old Model
		UUID id = this.oldSourceAnchorModel.getOwnerId();
		int x = this.oldSourceAnchorModel.getxOrientation();
		int y = this.oldSourceAnchorModel.getyOrientation();
		boolean flys = this.oldSourceAnchorModel.isFlying();

		// a flying Anchor can not be transformed into a fixed one
		if ((source instanceof CSFlyAnchor) && flys) {
			((CSFlyAnchor) source).setRelation((CSAnchor) target);
			x = source.getAnchorFactor().x;
			y = source.getAnchorFactor().y;
		} else if ((target instanceof CSAnchor) && !flys) {
			id = ((IControlStructureFigure) source.getOwner()).getId();
			x = source.getAnchorFactor().x;
			y = source.getAnchorFactor().y;
		}

		this.newSourceAnchorModel = new Anchor(flys, x, y, id);
		this.newTargetAnchorModel = null;
	}

	/**
	 * This function should only be called when reconnecting the targetAnchor,
	 * it sets the sourceAnchorFgiure so it can be referenced in case that
	 * target is flying
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * @param target
	 *            the target Anchor of this Connection
	 * @param source
	 *            The old SourceAnchor
	 */
	public void setNewTargetNode(IAnchorFigure target, IAnchorFigure source) {

		// first all values are copied from the old Model
		UUID id = this.oldTargetAnchorModel.getOwnerId();
		int x = this.oldTargetAnchorModel.getxOrientation();
		int y = this.oldTargetAnchorModel.getyOrientation();
		boolean flys = this.oldTargetAnchorModel.isFlying();

		// a flying Anchor can not be transformed into a fixed one,
		// to assure that the flys is checked
		if ((target instanceof CSFlyAnchor) && flys) {
			((CSFlyAnchor) target).setRelation((CSAnchor) source);
			x = target.getAnchorFactor().x;
			y = target.getAnchorFactor().y;
		} else if ((target instanceof CSAnchor) && !flys) {
			id = ((IControlStructureFigure) target.getOwner()).getId();
			x = target.getAnchorFactor().x;
			y = target.getAnchorFactor().y;
		}
		this.newSourceAnchorModel = null;
		this.newTargetAnchorModel = new Anchor(flys, x, y, id);
	}

	@Override
	public void execute() {
		if (this.newSourceAnchorModel != null) {
			this.getDataModel().changeConnectionSource(this.connId,
					this.newSourceAnchorModel);
		} else if (this.newTargetAnchorModel != null) {
			this.getDataModel().changeConnectionTarget(this.connId,
					this.newTargetAnchorModel);
		} else {
			throw new IllegalStateException("Should not happen"); //$NON-NLS-1$
		}
	}

	@Override
	public void undo() {
		this.getDataModel().changeConnectionSource(this.connId,
				this.oldSourceAnchorModel);
		this.getDataModel().changeConnectionTarget(this.connId,
				this.oldTargetAnchorModel);
	}

}
