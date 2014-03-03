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

package astpa.controlstructure.controller.editParts;

import org.eclipse.draw2d.IFigure;

import astpa.controlstructure.figure.IControlStructureFigure;
import astpa.controlstructure.figure.TextFieldFigure;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * TextFieldEditPart
 * 
 * @version 1.0
 * @author Lukas Balzer
 * 
 */
public class TextFieldEditPart extends CSAbstractEditPart {
	
	/**
	 * this constuctor sets the unique ID of this EditPart which is the same in
	 * its model and figure
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model The DataModel which contains all model classes
	 */
	public TextFieldEditPart(IControlStructureEditorDataModel model) {
		super(model);
	}
	
	@Override
	protected IFigure createFigure() {
		IControlStructureFigure tmpFigure = new TextFieldFigure(this.getId());
		tmpFigure.setParent(((CSAbstractEditPart) this.getParent()).getFigure());
		return tmpFigure;
	}
	
}
