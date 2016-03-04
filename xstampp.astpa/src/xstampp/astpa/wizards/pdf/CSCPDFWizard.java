package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.ui.sds.CSCView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class CSCPDFWizard extends AbstractExportWizard {

	public CSCPDFWizard() {
		super(CSCView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.CorrespondingSafetyConstraints + Messages.AsPDF, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopCorrespondingSafetyConstraints.xsl", false, Messages.CorrespondingSafetyConstraints); ////$NON-NLS-1$
	}
}
