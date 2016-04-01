package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class XRefinedUCAIMGWizard extends AbstractExportWizard {

	public XRefinedUCAIMGWizard() {
		super("");
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.RefinedUnsafeControlActions + Messages.AsImage, Activator.PLUGIN_ID)); //$NON-NLS-1$
		
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopRefinedUnsafeControlActions.xsl", false,Messages.RefinedUnsafeControlActions); ////$NON-NLS-1$
	}
}

