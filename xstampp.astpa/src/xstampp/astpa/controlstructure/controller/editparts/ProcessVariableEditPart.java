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

package xstampp.astpa.controlstructure.controller.editparts;

import messages.Messages;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.swt.SWT;

import xstampp.astpa.Activator;
import xstampp.astpa.controlstructure.figure.IControlStructureFigure;
import xstampp.astpa.controlstructure.figure.ProcessModelFigure;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.preferences.IControlStructureConstants;

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
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepId
	 *            TODO
	 */
	public ProcessVariableEditPart(IControlStructureEditorDataModel model,
			String stepId) {
		super(model, stepId, 1);

	}

	@Override
	protected IFigure createFigure() {
		IControlStructureFigure tmpFigure = new ProcessModelFigure(this.getId(),
				ProcessVariableEditPart.TOP_OFFSET);
		LineBorder border= new LineBorder(1){
			@Override
			public void paint(IFigure figure, Graphics graphics, Insets insets) {
				// TODO Auto-generated method stub
				if(Activator.getDefault().getPreferenceStore().getBoolean(IControlStructureConstants.CONTROLSTRUCTURE_PROCESS_MODEL_BORDER)){
					super.paint(figure, graphics, insets);
				}
			}
		};
		tmpFigure.setBorder(border);
		tmpFigure.getTextField().setFontStyle(SWT.BOLD);
		tmpFigure.getTextField().setLineVisible(true);
		tmpFigure.setParent(((IControlStructureEditPart) this.getParent()).getContentPane());
		tmpFigure.setToolTip(new Label(Messages.ProcessVariable));
		return tmpFigure;
	}

	@Override
	public void translateToRoot(Translatable t) {
		this.getFigure().getParent().translateFromParent(t);
		this.getFigure().getParent().getParent().translateFromParent(t);
	}

}
