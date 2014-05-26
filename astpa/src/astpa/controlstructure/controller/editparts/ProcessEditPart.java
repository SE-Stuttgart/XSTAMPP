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

package astpa.controlstructure.controller.editparts;

import messages.Messages;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;

import astpa.controlstructure.controller.policys.CSConnectionPolicy;
import astpa.controlstructure.figure.ComponentFigure;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 * 
 */
public class ProcessEditPart extends CSAbstractEditPart {
	
	/**
	 * this constuctor sets the unique ID of the {@link ProcessEditPart} which
	 * is the same in its model and figure
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model The DataModel which contains all model classes
	 */
	public ProcessEditPart(IControlStructureEditorDataModel model) {
		super(model);
	}
	
	@Override
	protected IFigure createFigure() {
		ComponentFigure tmpFigure = new ComponentFigure(this.getId());
		tmpFigure.setParent(((CSAbstractEditPart) this.getParent()).getFigure());
		tmpFigure.setToolTip(new Label(Messages.ControlledProcess));
		return tmpFigure;
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		this.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new CSConnectionPolicy(this.getDataModel()));
	}
	
}
