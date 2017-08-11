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
package xstampp.stpapriv.wizards.stepData;

import messages.Messages;
import xstampp.astpa.ui.systemdescription.SystemDescriptionView;
import xstampp.stpapriv.Activator;
import xstampp.stpapriv.util.jobs.ICSVExportConstants;
import xstampp.stpapriv.wizards.AbstractPrivacyExportWizard;
import xstampp.ui.wizards.CSVExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class SystemDecriptionDataWizard extends AbstractPrivacyExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public SystemDecriptionDataWizard() {
		super(SystemDescriptionView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters,
				Messages.SystemDescription + Messages.AsDataSet, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.PROJECT_DESCRIPTION);
	}

}