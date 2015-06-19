package xstampp.astpa.wizards.stepImages;

import org.eclipse.ui.PlatformUI;

import messages.Messages;
import xstampp.astpa.ui.causalfactors.CausalFactorsView;
import xstampp.astpa.ui.sds.CSCView;
import xstampp.astpa.ui.sds.DesignRequirementView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class DesignRequirementsImgWizard extends AbstractExportWizard {

	public DesignRequirementsImgWizard() {
		super(DesignRequirementView.ID);
		String[] filters = new String[] { "*.png", "*.jpg", "*.bmp","*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters,
				Messages.DesignRequirements));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopDesignRequironments.xsl", Messages.ExportingPdf, false); ////$NON-NLS-1$
	}
}
