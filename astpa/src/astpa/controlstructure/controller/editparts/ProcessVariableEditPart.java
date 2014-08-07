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
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.SWT;

import astpa.controlstructure.figure.IControlStructureFigure;
import astpa.controlstructure.figure.ProcessFigure;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * @author Lukas
 * 
 */
public class ProcessVariableEditPart extends CSAbstractEditPart {
	
	private static final int TOP_OFFSET = 5;
	
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model The DataModel which contains all model classes
	 * @param stepId TODO
	 */
	public ProcessVariableEditPart(IControlStructureEditorDataModel model, String stepId) {
		super(model, stepId);
		
	}
	
	@Override
	protected IFigure createFigure() {
		IControlStructureFigure tmpFigure = new ProcessFigure(this.getId(), ProcessVariableEditPart.TOP_OFFSET);
		tmpFigure.setBorder(null);
		tmpFigure.getTextField().setFontStyle(SWT.BOLD);
		tmpFigure.setParent(((CSAbstractEditPart) this.getParent()).getFigure());
		tmpFigure.setToolTip(new Label(Messages.ProcessVariable));
		return tmpFigure;
	}
	
	@Override
	public void translateToRoot(Translatable t) {
		this.getFigure().getParent().translateFromParent(t);
		this.getFigure().getParent().getParent().translateFromParent(t);
	}
	
}
