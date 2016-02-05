package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.ui.sds.DesignRequirementView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class DesignRequirementsImgWizard extends AbstractExportWizard {

	public DesignRequirementsImgWizard() {
		super(DesignRequirementView.ID);
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.DesignRequirements + Messages.AsImage));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopDesignRequironments.xsl", Messages.ExportingPdf, false, ""); ////$NON-NLS-1$
	}
}
