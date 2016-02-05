package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.ui.systemdescription.SystemDescriptionView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.SystemDescriptionExportPage;

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
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$
		this.setExportPage(new SystemDescriptionExportPage(filters,
				Messages.SystemDescription + Messages.AsImage));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(
				"/fopSystemDescription.xsl", Messages.ExportingPdf, false, ""); //$NON-NLS-1$
	}
}
