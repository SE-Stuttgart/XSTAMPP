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
package xstampp.stpasec.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.stpasec.wizards.pages.ControlStructureExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class ControlStructureWithPMWizard extends ControlStructureWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public ControlStructureWithPMWizard() {
		super(CSEditorWithPM.ID);
		String[] filters = new String[] { "*.png", "*.jpg", "*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.setExportPage(new ControlStructureExportPage(filters,
				Messages.ControlStructureDiagramWithProcessModel + Messages.AsImage));
	}

	@Override
	public boolean performFinish() {
		return super.performFinish(CSEditorWithPM.ID);
	}
}
