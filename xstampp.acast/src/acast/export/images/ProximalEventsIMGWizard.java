package acast.export.images;

import acast.export.TableExportPage;
import acast.jobs.ICSVExportConstants;
import acast.ui.accidentDescription.ProximalEventsView;
import acast.wizards.AbstractExportWizard;
import acast.wizards.pages.CSVExportPage;
import messages.Messages;

public class ProximalEventsIMGWizard extends AbstractExportWizard {
	public ProximalEventsIMGWizard() {
		super(ProximalEventsView.ID);
		String[] filters = new String[] {"*.png"}; //$NON-NLS-1$ //$NON-NLS-2$ 
		this.setExportPage(new TableExportPage(filters,
				ICSVExportConstants.PROX_EVENTS + Messages.AsImage));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopProxEvents.xsl", "Exporting Image", false); ////$NON-NLS-1$
	}
}
