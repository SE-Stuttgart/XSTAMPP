package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.ui.acchaz.HazardsView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class HazardsPDFWizard extends AbstractExportWizard {

	public HazardsPDFWizard() {
		super(HazardsView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.CorrespondingSafetyConstraints + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopHazards.xsl", Messages.ExportingPdf, false); ////$NON-NLS-1$
	}
}
