package acast.export.pdf;

import messages.Messages;
import acast.export.TableExportPage;
import acast.ui.accidentDescription.RecommandationsView;
import acast.wizards.AbstractExportWizard;

public class RecommendationsPDFWizard extends AbstractExportWizard {
	public RecommendationsPDFWizard() {
		super(RecommandationsView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters,
				"Findings and Recommendations" + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopRecommendations.xsl", Messages.ExportingPdf, false); 
	}
}
