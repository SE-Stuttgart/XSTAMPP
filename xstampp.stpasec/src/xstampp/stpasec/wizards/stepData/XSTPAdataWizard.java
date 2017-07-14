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
package xstampp.stpasec.wizards.stepData;

import java.io.IOException;

import messages.Messages;

import org.eclipse.jface.dialogs.MessageDialog;

import xstampp.model.IDataModel;
import xstampp.preferences.IPreferenceConstants;
import xstampp.stpasec.Activator;
import xstampp.stpasec.util.jobs.XCSVExportJob;
import xstampp.stpasec.wizards.AbstractExportWizard;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.wizards.CSVExportPage;
import xstampp.ui.wizards.MultiDataPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class XSTPAdataWizard extends AbstractExportWizard {

	MultiDataPage site;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public XSTPAdataWizard() {
		super("");
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		
		this.site = new MultiDataPage(XCSVExportJob.STEPS,filters, "Custom Data", this.getStore()
				.getString(IPreferenceConstants.PROJECT_NAME), Activator.PLUGIN_ID);
		this.setExportPage(this.site);
	}

	@Override
	public boolean performFinish() {
		String filePath = getExportPage().getExportPath();
		try {
			if (this.checkError(this.checkPath(filePath))) {
				IDataModel model = ProjectManager.getContainerInstance()
						.getDataModel(this.getExportPage().getProjectID());
				XCSVExportJob export = new XCSVExportJob("Export CSV",	filePath,
						((CSVExportPage) this.getExportPage()).getSeperator(),
						model, this.site.getSteps());
				export.schedule();
			} else {
				return false;
			}
		} catch (IOException e) {
			MessageDialog.openWarning(this.getShell(), Messages.Warning,
					Messages.ChooseTheDestination);
			return false;
		}
		return true;
	}

}