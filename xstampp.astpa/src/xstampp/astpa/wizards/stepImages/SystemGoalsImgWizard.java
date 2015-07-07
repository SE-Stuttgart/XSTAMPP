package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.ui.sds.SystemGoalView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class SystemGoalsImgWizard extends AbstractExportWizard {

	public SystemGoalsImgWizard() {
		super(SystemGoalView.ID);
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters,
				Messages.SystemGoals + Messages.AsImage));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopCorrespondingSafetyConstraints.xsl", Messages.ExportingPdf, false); ////$NON-NLS-1$
	}
}
