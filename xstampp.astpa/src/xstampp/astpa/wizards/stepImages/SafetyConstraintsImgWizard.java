package xstampp.astpa.wizards.stepImages;

import org.eclipse.ui.PlatformUI;

import messages.Messages;
import xstampp.astpa.ui.causalfactors.CausalFactorsView;
import xstampp.astpa.ui.sds.CSCView;
import xstampp.astpa.ui.sds.SafetyConstraintView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class SafetyConstraintsImgWizard extends AbstractExportWizard {

	public SafetyConstraintsImgWizard() {
		super(SafetyConstraintView.ID);
		String[] filters = new String[] { "*.png", "*.jpg", "*.bmp","*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters,
				Messages.SafetyConstraints));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopSafetyConstraints.xsl", Messages.ExportingPdf, false); ////$NON-NLS-1$
	}
}
