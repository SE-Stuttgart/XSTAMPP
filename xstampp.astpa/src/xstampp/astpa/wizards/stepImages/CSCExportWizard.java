package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.ui.causalfactors.CausalFactorsView;
import xstampp.astpa.ui.sds.CSCView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class CSCExportWizard extends AbstractExportWizard {

	public CSCExportWizard() {
		super(CSCView.ID);
		String[] filters = new String[] { "*.png", "*.jpg", "*.bmp","*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters,
				Messages.CorrespondingSafetyConstraints));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(
				"/fopcsc.xsl", Messages.ExportingCSC, false); ////$NON-NLS-1$
	}

}
