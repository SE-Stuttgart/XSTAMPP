package acast.export.pdf;

import messages.Messages;
import acast.export.TableExportPage;
import acast.ui.acchaz.HazardsView;
import acast.wizards.AbstractExportWizard;

public class HazardExportPDFWizard extends AbstractExportWizard {

	public HazardExportPDFWizard() {
		super(HazardsView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters,
				"Hazards" + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopHazards.xsl", Messages.ExportingPdf, false); 
	}

}
