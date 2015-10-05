package acast.export.pdf;

import acast.export.TableExportPage;
import acast.model.controlstructure.ResponsibilitiesView;
import acast.wizards.AbstractExportWizard;
import messages.Messages;

public class ResponsibilitiesPDFWizard extends AbstractExportWizard {
	public ResponsibilitiesPDFWizard() {
		super(ResponsibilitiesView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters,
				"Roles and Responsibilities" + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopResponsibilities.xsl", Messages.ExportingPdf, false); 
	}
}
