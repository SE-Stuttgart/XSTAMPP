package acast.export.pdf;

import messages.Messages;
import acast.export.TableExportPage;
import acast.ui.sds.SafetyConstraintView;
import acast.wizards.AbstractExportWizard;

public class SafetyConstraintsPDFWizard extends AbstractExportWizard{
	public SafetyConstraintsPDFWizard() {
		super(SafetyConstraintView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters,
				"Safety Constraints" + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopSafetyConstraints.xsl", Messages.ExportingPdf, false); 
	}
}
