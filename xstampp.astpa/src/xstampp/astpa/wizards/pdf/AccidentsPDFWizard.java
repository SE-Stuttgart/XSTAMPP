package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.ui.acchaz.AccidentsView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class AccidentsPDFWizard extends AbstractExportWizard {

	public AccidentsPDFWizard() {
		super(AccidentsView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.Accidents + Messages.AsPDF));
		
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopAccidents.xsl", Messages.ExportingCSC, false, Messages.Accidents); ////$NON-NLS-1$
	}
}
