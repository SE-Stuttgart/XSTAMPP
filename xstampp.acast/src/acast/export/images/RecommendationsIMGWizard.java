package acast.export.images;

import messages.Messages;
import acast.export.TableExportPage;
import acast.jobs.ICSVExportConstants;
import acast.ui.accidentDescription.RecommandationsView;
import acast.wizards.AbstractExportWizard;

public class RecommendationsIMGWizard extends AbstractExportWizard {
	public RecommendationsIMGWizard() {
		super(RecommandationsView.ID);
		String[] filters = new String[] {"*.png"}; //$NON-NLS-1$ //$NON-NLS-2$ 
		this.setExportPage(new TableExportPage(filters,
				ICSVExportConstants.RECOMMENDATIONS + Messages.AsImage));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopRecommendations.xsl", "Exporting Image", false); ////$NON-NLS-1$
	}
}
