package xstampp.stpapriv.wizards.pdf;

import messages.Messages;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.stpapriv.Activator;
import xstampp.stpapriv.messages.SecMessages;
import xstampp.stpapriv.ui.results.ResultEditor;
import xstampp.ui.wizards.TableExportPage;

public class ResultPDFWizard extends AbstractExportWizard{
	public ResultPDFWizard() {
		super(ResultEditor.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				SecMessages.Results + Messages.AsPDF, Activator.PLUGIN_ID));
		
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopResults.xsl", false, SecMessages.Results, false); ////$NON-NLS-1$
	}
}
