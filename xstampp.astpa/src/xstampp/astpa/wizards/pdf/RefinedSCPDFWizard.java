package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class RefinedSCPDFWizard extends AbstractExportWizard {

	public RefinedSCPDFWizard() {
		super("");
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.RefinedSafetyConstraintsTable + " " + Messages.AsPDF)); //$NON-NLS-1$
		
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport("/fopRefinedConstraints.xsl", false,//$NON-NLS-1$
									Messages.RefinedSafetyConstraintsTable); 
	}
}
