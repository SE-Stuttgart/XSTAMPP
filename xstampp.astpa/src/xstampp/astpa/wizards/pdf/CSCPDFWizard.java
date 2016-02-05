package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.ui.sds.CSCView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class CSCPDFWizard extends AbstractExportWizard {

	public CSCPDFWizard() {
		super(CSCView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.CorrespondingSafetyConstraints + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopCorrespondingSafetyConstraints.xsl", Messages.ExportingCSC, false, Messages.CorrespondingSafetyConstraints); ////$NON-NLS-1$
	}
}
