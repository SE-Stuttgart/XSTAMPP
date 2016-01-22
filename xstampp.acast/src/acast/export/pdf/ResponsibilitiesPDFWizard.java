package acast.export.pdf;

import messages.Messages;
import acast.export.TableExportPage;
import acast.wizards.AbstractExportWizard;

public class ResponsibilitiesPDFWizard extends AbstractExportWizard {
	public ResponsibilitiesPDFWizard() {
		super("A-CAST.view1");
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$
														// $NON-NLS-1$
														//$NON-NLS-1$ //$NON-NLS-3$
														// $NON-NLS-1$
														//$NON-NLS-1$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters, "Roles and Responsibilities" + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport("/fopResponsibilities.xsl", Messages.ExportingPdf, false);
	}
}
