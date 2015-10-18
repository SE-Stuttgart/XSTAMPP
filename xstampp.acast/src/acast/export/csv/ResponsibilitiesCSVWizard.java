package acast.export.csv;

import acast.jobs.ICSVExportConstants;
import acast.wizards.AbstractExportWizard;
import acast.wizards.pages.CSVExportPage;
import messages.Messages;

public class ResponsibilitiesCSVWizard extends AbstractExportWizard {
	public ResponsibilitiesCSVWizard() {
		super("acast.steps.step2_2");
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters, ICSVExportConstants.RESPONSIBILITIES + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.RESPONSIBILITIES);
	}
}
