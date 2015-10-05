package acast.export.csv;

import acast.export.TableExportPage;
import acast.jobs.ICSVExportConstants;
import acast.ui.accidentDescription.RecommandationsView;
import acast.wizards.AbstractExportWizard;
import acast.wizards.pages.CSVExportPage;
import messages.Messages;

public class RecommendationsCSVWizard extends AbstractExportWizard {
	public RecommendationsCSVWizard() {
		super(RecommandationsView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters, ICSVExportConstants.RECOMMENDATIONS + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.RECOMMENDATIONS);
	}
}
