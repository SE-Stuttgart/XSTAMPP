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
package xstampp.astpa.controlstructure.controller.commands;

import xstampp.astpa.controlstructure.controller.editparts.IMemberEditPart;
import xstampp.astpa.controlstructure.controller.editparts.IRelativePart;
import xstampp.astpa.model.controlstructure.interfaces.IComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * this command provides functionality to set/change the IRelativePart of a IMemberEditPart
 * 
 * @author Lukas Balzer
 * @since 2.0.2
 *
 */
public class SwapRelativeCommand extends ControlStructureAbstractCommand {
	
	private IComponent member;
	private IComponent relative;

	/**
	 * 
	 * @param model
	 * @param stepID
	 * @param relative
	 * @param connectable
	 */
	public SwapRelativeCommand(IControlStructureEditorDataModel model,String stepID,
							IRelativePart relative,IMemberEditPart connectable) {
		super(model, stepID);
		this.relative = (IComponent) relative.getModel();
		this.member = (IComponent) connectable.getModel();
	}
	
	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return super.canExecute();
	}
	
	@Override
	public void execute() {
		this.getDataModel().setRelativeOfComponent(this.member.getId(), this.relative.getId());
		for(IComponent child: this.member.getChildren()){
			this.getDataModel().setRelativeOfComponent(child.getId(), this.relative.getId());
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
