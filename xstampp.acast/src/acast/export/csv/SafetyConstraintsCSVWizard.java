package acast.export.csv;

import messages.Messages;
import acast.jobs.ICSVExportConstants;
import acast.ui.sds.SafetyConstraintView;
import acast.wizards.AbstractExportWizard;
import acast.wizards.pages.CSVExportPage;

public class SafetyConstraintsCSVWizard extends AbstractExportWizard{
	public SafetyConstraintsCSVWizard() {
		super(SafetyConstraintView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters, ICSVExportConstants.SAFETY_CONSTRAINT + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.SAFETY_CONSTRAINT);
	}
}
