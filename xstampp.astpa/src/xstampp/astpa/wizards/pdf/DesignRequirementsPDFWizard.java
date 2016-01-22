package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.ui.sds.DesignRequirementView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class DesignRequirementsPDFWizard extends AbstractExportWizard {

	public DesignRequirementsPDFWizard() {
		super(DesignRequirementView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.DesignRequirements + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopDesignRequironments.xsl", Messages.ExportingPdf, false); ////$NON-NLS-1$
	}
}
