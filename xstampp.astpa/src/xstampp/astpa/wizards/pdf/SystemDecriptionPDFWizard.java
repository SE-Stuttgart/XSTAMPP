package xstampp.astpa.wizards.pdf;

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
public class SystemDecriptionPDFWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public SystemDecriptionPDFWizard() {
		super(SystemDescriptionView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$
		this.setExportPage(new SystemDescriptionExportPage(filters,
				Messages.ExportPreferences + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(
				"/fopSystemDescription.xsl", Messages.ExportingPdf, false); //$NON-NLS-1$
	}
}
