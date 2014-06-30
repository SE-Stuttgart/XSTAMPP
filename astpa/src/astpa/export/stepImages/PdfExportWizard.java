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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.ui.PlatformUI;

import astpa.export.pages.PdfExportPage;
import astpa.export.stepData.AbstractExportWizard;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.common.ViewContainer;

/**
 * Creates wizard for export.
 * 
 * @author Sebastian Sieber
 * 
 */
public class PdfExportWizard extends AbstractImageExportWizard{
	
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public PdfExportWizard(){
		super();
		setFopName("/fopxsl.xsl");
		String projectName= this.getStore().getString(IPreferenceConstants.PROJECT_NAME);
		setExportPage(new PdfExportPage(Messages.ExportPreferences, projectName));
	}
	/**
	 * Constructor.
	 * 
	 * @author Sebastian Sieber
	 * @param projectName the name of the project
	 * 
	 */
	public PdfExportWizard(String projectName) {
		super();
		setExportPage(new PdfExportPage(Messages.ExportPreferences, projectName));
	}
	
	@Override
	public boolean performFinish() {
		this.getStore().setValue(IPreferenceConstants.COMPANY_NAME, this.getExportPage().getTextCompany().getText());
		
		this.getStore().setValue(IPreferenceConstants.COMPANY_LOGO, this.getExportPage().getTextLogo().getText());
		
		PreferenceConverter.setValue(this.getStore(), IPreferenceConstants.COMPANY_BACKGROUND_COLOR,
			this.toRGB(this.getExportPage().getTextBackgroundColor().getText()));
		
		PreferenceConverter.setValue(this.getStore(), IPreferenceConstants.COMPANY_FONT_COLOR,
			this.toRGB(this.getExportPage().getTextFontColor().getText()));
		
		String filePath = this.getExportPage().getExportPath();
		if ((filePath != null) && !filePath.isEmpty()) {
			ViewContainer viewContainer =
				(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.findView(ViewContainer.ID);
			viewContainer.export(filePath);
		} else {
			MessageDialog.openWarning(this.getShell(), Messages.Warning, Messages.ChooseTheDestination);
			return false;
		}
		return true;
	}
	

	
}
