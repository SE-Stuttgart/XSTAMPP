package acast.export.csv;

import messages.Messages;
import acast.jobs.ICSVExportConstants;
import acast.ui.accidentDescription.AccidentDescriptionView;
import acast.wizards.AbstractExportWizard;
import acast.wizards.pages.CSVExportPage;

public class AccidentDescriptionCSVWizard extends AbstractExportWizard{
	
	public AccidentDescriptionCSVWizard() {
		super(AccidentDescriptionView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters, ICSVExportConstants.ACCIDENT_DESCRIPTION + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.ACCIDENT_DESCRIPTION);
	}

}
