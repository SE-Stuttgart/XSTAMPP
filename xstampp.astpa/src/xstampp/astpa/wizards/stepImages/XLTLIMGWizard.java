package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class XLTLIMGWizard extends AbstractExportWizard {

	public XLTLIMGWizard() {
		super("");
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.LTLFormulasTable + " " + Messages.AsImage, Activator.PLUGIN_ID)); //$NON-NLS-2$
		
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport("/fopLTLPropertys.xsl", false,//$NON-NLS-1$
						Messages.LTLFormulasTable); 
	}
}
