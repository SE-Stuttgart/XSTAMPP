package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.ui.acchaz.AccidentsView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

public class AccidentsImgWizard extends AbstractExportWizard {

	public AccidentsImgWizard() {
		super(AccidentsView.ID);
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.Accidents + Messages.AsImage));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopAccidents.xsl", Messages.ExportingCSC, false); ////$NON-NLS-1$
	}
}
