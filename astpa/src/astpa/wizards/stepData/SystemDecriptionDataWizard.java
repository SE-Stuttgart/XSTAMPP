package astpa.wizards.stepData;

import messages.Messages;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.systemdescription.SystemDescriptionView;
import astpa.wizards.AbstractExportWizard;
import astpa.wizards.pages.CSVExportPage;

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
	public SystemDecriptionDataWizard()  {
		super(SystemDescriptionView.ID);
		String[] filters= new String[] {"*.csv"}; //$NON-NLS-1$
		setExportPage(new CSVExportPage(filters,Messages.SystemDescription,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return performCSVExport();
	}

}