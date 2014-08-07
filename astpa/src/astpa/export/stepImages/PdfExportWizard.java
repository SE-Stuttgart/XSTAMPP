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

package astpa.export.stepImages;

import messages.Messages;
import astpa.export.AbstractExportWizard;
import astpa.export.pages.PdfExportPage;
import astpa.preferences.IPreferenceConstants;

/**
 * Creates wizard for export.
 * 
 * @author Sebastian Sieber
 * 
 */
public class PdfExportWizard extends AbstractExportWizard{
	
	private final PdfExportPage page;
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public PdfExportWizard(){
		super();
		String projectName= this.getStore().getString(IPreferenceConstants.PROJECT_NAME);
		this.page=new PdfExportPage("PDF Report", projectName);
		setExportPage(this.page);
	}
	
	@Override
	public boolean performFinish() {
		this.getStore().setValue(IPreferenceConstants.COMPANY_NAME, this.page.getTextCompany().getText());
		
		this.getStore().setValue(IPreferenceConstants.COMPANY_LOGO, this.page.getTextLogo().getText());

		return performXSLExport("/fopxsl.xsl",Messages.ExportingPdf,this.page.getDecoChoice()); //$NON-NLS-1$
	}
	

	
}
