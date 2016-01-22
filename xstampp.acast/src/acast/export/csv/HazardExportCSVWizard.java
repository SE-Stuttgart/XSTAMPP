package acast.export.csv;

import messages.Messages;
import acast.jobs.ICSVExportConstants;
import acast.ui.acchaz.HazardsView;
import acast.wizards.AbstractExportWizard;
import acast.wizards.pages.CSVExportPage;

public class HazardExportCSVWizard extends AbstractExportWizard {

	public HazardExportCSVWizard() {
		super(HazardsView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters, ICSVExportConstants.HAZARD + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.HAZARD);
	}

}
