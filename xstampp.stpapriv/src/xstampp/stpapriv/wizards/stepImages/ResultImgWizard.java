package xstampp.stpapriv.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.stpapriv.Activator;
import xstampp.stpapriv.messages.SecMessages;
import xstampp.stpapriv.ui.results.ResultEditor;
import xstampp.ui.wizards.TableExportPage;

public class ResultImgWizard extends AbstractExportWizard {

	public ResultImgWizard() {
		super(ResultEditor.ID);
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ 
		this.setExportPage(new TableExportPage(filters,
				SecMessages.Results + Messages.AsImage, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopResults.xsl", false, "", false); ////$NON-NLS-1$
	}
}
