package astpa.wizards.stepData;

import messages.Messages;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.sds.ControlActionView;
import astpa.wizards.AbstractExportWizard;
import astpa.wizards.pages.CSVExportPage;

/**
 *
 * @author Lukas Balzer
 *
 */
public class ControlActionsWizard extends AbstractExportWizard {

	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public ControlActionsWizard() {
		super(ControlActionView.ID);
		String[] filters= new String[] {"*.csv"}; //$NON-NLS-1$
		setExportPage(new CSVExportPage(filters,Messages.ControlAction,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return performCSVExport();
	}


}
