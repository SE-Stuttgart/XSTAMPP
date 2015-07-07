package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.ui.sds.SafetyConstraintView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class SafetyConstraintsImgWizard extends AbstractExportWizard {

	public SafetyConstraintsImgWizard() {
		super(SafetyConstraintView.ID);
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters,
				Messages.SafetyConstraints + Messages.AsImage));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopSystemDescription.xsl", Messages.ExportingPdf, false); ////$NON-NLS-1$
	}
}
