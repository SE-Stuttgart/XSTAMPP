package export;

import messages.Messages;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;
import xstpa.ui.View;

public class RefinedUCAPDFWizard extends AbstractExportWizard {

	public RefinedUCAPDFWizard() {
		super(View.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.RefinedUnsafeControlActions + Messages.AsPDF)); //$NON-NLS-1$
		
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopRefinedUnsafeControlActions.xsl", false,Messages.RefinedUnsafeControlActions); ////$NON-NLS-1$
	}
}
