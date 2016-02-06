package export;

import messages.Messages;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;
import xstpa.ui.View;

public class LTLPDFWizard extends AbstractExportWizard {

	public LTLPDFWizard() {
		super(View.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.LTLFormulasTable + " " + Messages.AsPDF)); //$NON-NLS-2$
		
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport("/fopLTLPropertys.xsl", false,//$NON-NLS-1$
						Messages.LTLFormulasTable); 
	}
}
