package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class XRefinedSCIMGWizard extends AbstractExportWizard {

	public XRefinedSCIMGWizard() {
		super("");
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.RefinedSafetyConstraintsTable + " " + Messages.AsImage, Activator.PLUGIN_ID)); //$NON-NLS-1$
		
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport("/fopRefinedConstraints.xsl", false,//$NON-NLS-1$
									Messages.RefinedSafetyConstraintsTable); 
	}
}
