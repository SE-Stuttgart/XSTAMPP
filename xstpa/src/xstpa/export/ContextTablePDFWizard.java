package xstpa.export;

import messages.Messages;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;
import xstpa.ui.View;

public class ContextTablePDFWizard extends AbstractExportWizard {

	public ContextTablePDFWizard() {
		super(View.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.ContextTables + Messages.AsPDF));
		
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopContextTable.xsl",  false, Messages.ContextTables); ////$NON-NLS-1$ //$NON-NLS-3$
	}
}
