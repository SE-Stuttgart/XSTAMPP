package acast.export.csv;

import messages.Messages;
import acast.jobs.ICSVExportConstants;
import acast.ui.accidentDescription.ProximalEventsView;
import acast.wizards.AbstractExportWizard;
import acast.wizards.pages.CSVExportPage;

public class ProximalEventsCSVWizard extends AbstractExportWizard {
	public ProximalEventsCSVWizard() {
		super(ProximalEventsView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters, ICSVExportConstants.PROX_EVENTS + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.PROX_EVENTS);
	}
}
