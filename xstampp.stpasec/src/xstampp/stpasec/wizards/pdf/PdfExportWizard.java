/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.stpasec.wizards.pdf;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.preference.IPreferenceStore;

import messages.Messages;
import xstampp.preferences.IPreferenceConstants;
import xstampp.stpapriv.model.PrivacyController;
import xstampp.stpasec.Activator;
import xstampp.stpasec.util.jobs.STPAWordJob;
import xstampp.stpasec.wizards.AbstractExportWizard;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.wizards.PdfExportPage;

/**
 * Creates wizard for export.
 * 
 * @author Sebastian Sieber
 * 
 */
public class PdfExportWizard extends AbstractExportWizard {

	private final PdfExportPage page;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public PdfExportWizard() {
		super();
		String projectName = this.getStore().getString(
				IPreferenceConstants.PROJECT_NAME);
		this.page = new PdfExportPage("PDF Report", projectName,Activator.PLUGIN_ID);
		this.page.setFilterExtensions(new String[]{"*.pdf","*.docx"}, new String[]{"PDF","Word Document"});
		this.setExportPage(this.page);
	}

	@Override
	public boolean performFinish() {
		
		this.getStore().setValue(IPreferenceConstants.COMPANY_NAME,
				this.page.getTextCompany().getText());

		this.getStore().setValue(IPreferenceConstants.COMPANY_LOGO,
				this.page.getTextLogo());
		if(getExportPage().getExportPath().endsWith("docx")){
			final PrivacyController controller = (PrivacyController) ProjectManager.getContainerInstance().getDataModel(page.getProjectID());

			STPAWordJob exportJob = new STPAWordJob("STPA Word Report",getExportPage().getExportPath(),controller,true);
			exportJob.addJobChangeListener(new JobChangeAdapter(){
				@Override
				public void done(IJobChangeEvent event) {
					controller.prepareForSave();
				}
			});
			exportJob.setPdfTitle("STPA-Sec Final Report");
			exportJob.setTextSize(getExportPage().getContentSize());
			exportJob.setTableHeadSize(getExportPage().getHeadSize());
			exportJob.setTitleSize(getExportPage().getTitleSize());
			exportJob.setDecorate(page.getDecoChoice());
			controller.prepareForExport();
			exportJob.schedule();
			return true;
		}else{
			return this
					.performXSLExport(
							"/fopxsl.xsl", this.page.getDecoChoice(), Messages.STPAPDFReport, true); //$NON-NLS-1$
		}
	}

	@Override
	public IPreferenceStore getStore() {
		return Activator.getDefault().getPreferenceStore();
	}
}
