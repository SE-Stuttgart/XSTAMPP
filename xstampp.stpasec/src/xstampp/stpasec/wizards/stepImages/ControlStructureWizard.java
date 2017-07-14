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
import xstampp.astpa.controlstructure.CSEditor;
import xstampp.stpasec.util.jobs.CSExportJob;
import xstampp.stpasec.wizards.AbstractExportWizard;
import xstampp.stpasec.wizards.pages.ControlStructureExportPage;

/**
 * The Wizard for Exporting the control structure as image
 * 
 * @author Lukas Balzer
 * 
 */
public class ControlStructureWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public ControlStructureWizard() {
		super(CSEditor.ID);
		String[] filters = new String[] {  "*.png", "*.jpg", "*.bmp" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.setExportPage(new ControlStructureExportPage(filters,
				Messages.ControlStructure + Messages.AsImage));
	}

	protected ControlStructureWizard(String id) {
		super(id);
	}

	@Override
	public boolean performFinish() {
		return this.performFinish(CSEditor.ID);
	}

	protected boolean performFinish(String editorID) {
		int offset = ((ControlStructureExportPage) this.getExportPage())
				.getImgOffset();
		boolean decoCoice = ((ControlStructureExportPage) this.getExportPage())
				.getDecoChoice();
		CSExportJob job = new CSExportJob(this.getExportPage().getExportPath(),
				editorID, this.getExportPage().getProjectID(), offset,
				decoCoice);
		job.setPreview(true);
		job.schedule();
		return true;
	}

}
