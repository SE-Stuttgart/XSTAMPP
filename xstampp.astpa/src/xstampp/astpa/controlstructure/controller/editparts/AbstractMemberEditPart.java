/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.controlstructure.controller.editparts;

import java.util.UUID;

import org.eclipse.draw2d.IFigure;

import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

public abstract class AbstractMemberEditPart extends CSAbstractEditPart implements IMemberEditPart{

	private UUID relativeID;
	private IRelativePart relativePart;

	public AbstractMemberEditPart(IControlStructureEditorDataModel model,
			String stepId, Integer layer) {
		super(model, stepId, layer);
		
	}
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		
		UUID currentRelativeId=getDataModel().getComponent(getId()).getRelative();
		IConnection conn= getDataModel().getConnection(currentRelativeId);
		if(conn == null){
			this.relativeID = null;
			this.relativePart = null;
		}else if(this.relativeID != currentRelativeId ){
			Object tmp = getViewer().getEditPartRegistry().get(conn);
			if(this.relativePart != null){
				this.relativePart.eraseFeedback();
			}
			if(tmp instanceof IRelativePart){
				this.relativePart = (IRelativePart) tmp;
				this.relativeID = currentRelativeId;
				getDataModel().getRelatedProcessVariables(getId());
			}
		}
		
		if(this.relativePart != null){
			this.relativePart.updateFeedback();
		}

	}

	@Override
	public IFigure getFeedback() {
		if(this.relativePart == null){
			return null;
		}
		return this.relativePart.getFeedback(this);
	}

	@Override
	public UUID getRelativeId() {
		return this.relativeID;
	}

}
