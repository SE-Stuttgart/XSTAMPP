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

package astpa.export;

import messages.Messages;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import astpa.Activator;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.common.ViewContainer;

/**
 * Creates wizard for export.
 * 
 * @author Sebastian Sieber
 * 
 */
public class ExportWizard extends Wizard implements IExportWizard {
	
	private ExportPage exportPage;
	
	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	
	
	/**
	 * Constructor.
	 * 
	 * @author Sebastian Sieber
	 * @param projectName the name of the project
	 * 
	 */
	public ExportWizard(String projectName) {
		super();
		this.exportPage = new ExportPage(Messages.ExportPreferences, projectName);
	}
	
	@Override
	public void addPages() {
		this.addPage(this.exportPage);
	}
	
	@Override
	public boolean performFinish() {
		this.store.setValue(IPreferenceConstants.COMPANY_NAME, this.exportPage.getTextCompany().getText());
		
		this.store.setValue(IPreferenceConstants.COMPANY_LOGO, this.exportPage.getTextLogo().getText());
		
		PreferenceConverter.setValue(this.store, IPreferenceConstants.COMPANY_BACKGROUND_COLOR,
			this.toRGB(this.exportPage.getTextBackgroundColor().getText()));
		
		PreferenceConverter.setValue(this.store, IPreferenceConstants.COMPANY_FONT_COLOR,
			this.toRGB(this.exportPage.getTextFontColor().getText()));
		
		String filePath = this.exportPage.getExportPath();
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
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// no-op
	}
	
	/**
	 * Parse string to RGB Object
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param rgbString String
	 * @return RGB
	 */
	private RGB toRGB(String rgbString) {
		int rgbRed = Integer.parseInt(rgbString.substring(rgbString.indexOf("{") + 1, rgbString.indexOf(",")).trim()); //$NON-NLS-1$ //$NON-NLS-2$
		int rgbGreen =
			Integer.parseInt(rgbString.substring(rgbString.indexOf(",") + 1, rgbString.lastIndexOf(",")).trim()); //$NON-NLS-1$ //$NON-NLS-2$
		int rgbBlue =
			Integer.parseInt(rgbString.substring(rgbString.lastIndexOf(",") + 1, rgbString.indexOf("}")).trim()); //$NON-NLS-1$ //$NON-NLS-2$
		RGB rgbCompanyColor = new RGB(rgbRed, rgbGreen, rgbBlue);
		return rgbCompanyColor;
	}
	
}
