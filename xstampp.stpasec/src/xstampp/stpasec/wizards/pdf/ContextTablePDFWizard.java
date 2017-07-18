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
package xstampp.stpasec.wizards.pdf;

import messages.Messages;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.stpapriv.model.PrivacyController;
import xstampp.stpasec.Activator;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.wizards.TableExportPage;

public class ContextTablePDFWizard extends AbstractExportWizard {

	public ContextTablePDFWizard() {
		super("");
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.ContextTables + Messages.AsPDF, Activator.PLUGIN_ID));
		
	}

	@Override
	public boolean performFinish() {
		setExportAddition(calculateContextSize());
		return this.performXSLExport(				
				"/fopContextTable.xsl",  false, Messages.ContextTables, false); ////$NON-NLS-1$ //$NON-NLS-3$
	}
	
	private String calculateContextSize(){
		PrivacyController controller = (PrivacyController) ProjectManager.getContainerInstance().
																				getDataModel(getExportPage().
																						getProjectID());
		return "8";
	
	}
}
