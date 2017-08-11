package xstampp.stpasec.wizards.pdf;

import messages.Messages;
import xstampp.stpasec.Activator;
import xstampp.stpasec.messages.SecMessages;
import xstampp.stpasec.ui.results.ResultEditor;
import xstampp.stpasec.wizards.AbstractPrivacyExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class ResultPDFWizard extends AbstractPrivacyExportWizard{
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
