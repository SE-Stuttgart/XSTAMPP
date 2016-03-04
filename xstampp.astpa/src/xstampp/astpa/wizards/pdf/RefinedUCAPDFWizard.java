package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class RefinedUCAPDFWizard extends AbstractExportWizard {

	public RefinedUCAPDFWizard() {
		super("");
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.RefinedUnsafeControlActions + Messages.AsPDF, Activator.PLUGIN_ID)); //$NON-NLS-1$
		
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopRefinedUnsafeControlActions.xsl", false,Messages.RefinedUnsafeControlActions); ////$NON-NLS-1$
	}
}

