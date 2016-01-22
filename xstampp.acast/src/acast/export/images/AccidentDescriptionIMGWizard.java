package acast.export.images;

import messages.Messages;
import acast.export.TableExportPage;
import acast.jobs.ICSVExportConstants;
import acast.ui.accidentDescription.AccidentDescriptionView;
import acast.wizards.AbstractExportWizard;

public class AccidentDescriptionIMGWizard extends AbstractExportWizard{
	
	public AccidentDescriptionIMGWizard() {
		super(AccidentDescriptionView.ID);
		String[] filters = new String[] {"*.png"}; //$NON-NLS-1$ //$NON-NLS-2$ 
		this.setExportPage(new TableExportPage(filters,
				ICSVExportConstants.ACCIDENT_DESCRIPTION + Messages.AsImage));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopAccidentDescription.xsl", "Exporting Image", false); ////$NON-NLS-1$
	}

}
