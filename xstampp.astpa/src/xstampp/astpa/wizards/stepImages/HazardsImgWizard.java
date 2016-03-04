package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.ui.acchaz.HazardsView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class HazardsImgWizard extends AbstractExportWizard {

	public HazardsImgWizard() {
		super(HazardsView.ID);
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$
		this.setExportPage(new TableExportPage(filters,
				Messages.Hazards + Messages.AsImage, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopHazards.xsl", false, ""); ////$NON-NLS-1$
	}
}
