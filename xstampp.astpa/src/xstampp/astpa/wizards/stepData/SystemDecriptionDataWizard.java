package xstampp.astpa.wizards.stepData;

import messages.Messages;
import xstampp.astpa.ui.systemdescription.SystemDescriptionView;
import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.CSVExportPage;
import xstampp.preferences.IPreferenceConstants;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class SystemDecriptionDataWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public SystemDecriptionDataWizard() {
		super(SystemDescriptionView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters,
				Messages.SystemDescription + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.PROJECT_DESCRIPTION);
	}

}