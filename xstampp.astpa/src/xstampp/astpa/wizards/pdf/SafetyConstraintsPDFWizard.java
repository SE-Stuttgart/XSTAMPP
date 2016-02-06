package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.ui.sds.SafetyConstraintView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class SafetyConstraintsPDFWizard extends AbstractExportWizard {

	public SafetyConstraintsPDFWizard() {
		super(SafetyConstraintView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.SafetyConstraints + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopSafetyConstraints.xsl", false, Messages.SafetyConstraints); ////$NON-NLS-1$
	}
}
