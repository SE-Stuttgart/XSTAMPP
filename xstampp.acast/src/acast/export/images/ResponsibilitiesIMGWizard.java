package acast.export.images;

import messages.Messages;
import acast.export.TableExportPage;
import acast.jobs.ICSVExportConstants;
import acast.wizards.AbstractExportWizard;

public class ResponsibilitiesIMGWizard extends AbstractExportWizard {
	public ResponsibilitiesIMGWizard() {
		super("acast.steps.step2_2");
		String[] filters = new String[] { "*.png" }; //$NON-NLS-1$ //$NON-NLS-2$
		this.setExportPage(new TableExportPage(filters, ICSVExportConstants.RESPONSIBILITIES + Messages.AsImage));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport("/fopResponsibilities.xsl", "Exporting Image", false); ////$NON-NLS-1$
	}
}
