package acast.export.images;

import messages.Messages;
import acast.export.TableExportPage;
import acast.jobs.ICSVExportConstants;
import acast.ui.acchaz.HazardsView;
import acast.wizards.AbstractExportWizard;

public class HazardExportIMGWizard extends AbstractExportWizard {

	public HazardExportIMGWizard() {
		super(HazardsView.ID);
		String[] filters = new String[] {"*.png"}; //$NON-NLS-1$ //$NON-NLS-2$ 
		this.setExportPage(new TableExportPage(filters,
				ICSVExportConstants.HAZARD + Messages.AsImage));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopHazards.xsl", "Exporting Image", false); ////$NON-NLS-1$
	}

}
