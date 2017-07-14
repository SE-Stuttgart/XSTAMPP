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

import xstampp.preferences.IPreferenceConstants;
import xstampp.stpasec.Activator;
import xstampp.stpasec.ui.vulloss.LossesView;
import xstampp.stpasec.ui.vulloss.VulnerabilityView;
import xstampp.stpasec.util.jobs.ICSVExportConstants;
import xstampp.stpasec.wizards.AbstractExportWizard;
import xstampp.ui.wizards.MultiDataPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class STPAdataWizard extends AbstractExportWizard {

	MultiDataPage site;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public STPAdataWizard() {
		super(new String[] { LossesView.ID, VulnerabilityView.ID });
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		
		this.site = new MultiDataPage(ICSVExportConstants.STEPS,filters, "Custom Data", this.getStore()
				.getString(IPreferenceConstants.PROJECT_NAME), Activator.PLUGIN_ID);
		this.setExportPage(this.site);
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(this.site.getSteps());
	}

}