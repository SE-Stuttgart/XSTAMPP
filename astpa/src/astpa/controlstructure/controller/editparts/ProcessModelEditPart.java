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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Translatable;

import astpa.controlstructure.CSEditorWithPM;
import astpa.controlstructure.CSAbstractEditor;
import astpa.controlstructure.figure.IControlStructureFigure;
import astpa.controlstructure.figure.ProcessFigure;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * @author Aliaksei Babkovich
 * 
 */
public class ProcessModelEditPart extends CSAbstractEditPart {
	
	private static final int TOP_OFFSET = 10;
	
	
	/**
	 * Constructor sets the unique ID of {@link ProcessModelEditPart}
	 * 
	 * @author Aliaksei Babkovich
	 * 
	 * @param model The DataModel which contains all model classes
	 * @param stepId TODO
	 */
	public ProcessModelEditPart(IControlStructureEditorDataModel model, String stepId) {
		super(model, stepId);
		this.activate();
	}
	
	/**
	 * refreshVisuals is overwritten to prevent the process model from being
	 * drawn in the 1st Step
	 */
	@Override
	protected void refreshVisuals() {
		if (this.getViewer().getProperty(CSAbstractEditor.STEP_EDITOR).equals(CSEditorWithPM.ID)) {
			super.refreshVisuals();
		}
	}
	
	@Override
	public void deactivate() {
		((IControlStructureEditPart) this.getParent()).getFigure().setBorder(new LineBorder(ColorConstants.blue,1));
		super.deactivate();
	}
	
	@Override
	protected IFigure createFigure() {
		IControlStructureFigure tmpFigure = new ProcessFigure(this.getId(), TOP_OFFSET);
		tmpFigure.setBorder(new LineBorder(1));
		
		tmpFigure.setParent(((CSAbstractEditPart) this.getParent()).getFigure());
		((CSAbstractEditPart) this.getParent()).getFigure().setBorder(new LineBorder(ColorConstants.blue,2));
		tmpFigure.setToolTip(new Label(Messages.ProcessModel));
		return tmpFigure;
	}
	
	@Override
	public void translateToRoot(Translatable t) {
		this.getFigure().getParent().translateFromParent(t);
	}
}
