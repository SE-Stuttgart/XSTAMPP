package astpa.wizards.stepData;

import messages.Messages;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import astpa.wizards.AbstractExportWizard;
import astpa.wizards.pages.CSVExportPage;

/**
 *
 * @author Lukas Balzer
 *
 */
public class UnsafeControlActionsDataWizard extends AbstractExportWizard{
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public UnsafeControlActionsDataWizard() {
		super(UnsafeControlActionsView.ID);
		String[] filters= new String[] {"*.csv"}; //$NON-NLS-1$
		setExportPage(new CSVExportPage(filters,Messages.UnsafeControlActions,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return performCSVExport();
	}
}
