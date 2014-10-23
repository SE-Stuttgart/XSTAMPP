package astpa.wizards.stepData;

import messages.Messages;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.sds.SystemGoalView;
import astpa.wizards.AbstractExportWizard;
import astpa.wizards.pages.CSVExportPage;

/**
 *
 * @author Lukas Balzer
 *
 */
public class SystemGoalsWizard extends AbstractExportWizard{
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public SystemGoalsWizard() {
		super(SystemGoalView.ID);
		String[] filters= new String[] {"*.csv"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setExportPage(new CSVExportPage(filters,Messages.SystemGoals,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return performCSVExport();
	}
}

