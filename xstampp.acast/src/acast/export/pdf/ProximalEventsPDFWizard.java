package acast.export.pdf;

import acast.export.TableExportPage;
import acast.ui.accidentDescription.ProximalEventsView;
import acast.wizards.AbstractExportWizard;
import messages.Messages;

public class ProximalEventsPDFWizard extends AbstractExportWizard {
	public ProximalEventsPDFWizard() {
		super(ProximalEventsView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters,
				"Proximal Events" + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopProxEvents.xsl", Messages.ExportingPdf, false); 
	}
}
