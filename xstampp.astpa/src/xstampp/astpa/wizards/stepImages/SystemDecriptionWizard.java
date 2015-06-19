package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.ui.systemdescription.SystemDescriptionView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.SystemDescriptionExportPage;
import xstampp.preferences.IPreferenceConstants;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class SystemDecriptionWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public SystemDecriptionWizard() {
		super(SystemDescriptionView.ID);
		String[] filters = new String[] {  "*.png", "*.jpg", "*.bmp","*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$
		this.setExportPage(new SystemDescriptionExportPage(filters,
				Messages.ExportPreferences));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(
				"/fopSystemDescription.xsl", Messages.ExportingPdf, false); //$NON-NLS-1$
	}
}
