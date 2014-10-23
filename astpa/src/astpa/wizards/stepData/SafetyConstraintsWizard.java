package astpa.wizards.stepData;

import messages.Messages;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.sds.SafetyConstraintView;
import astpa.wizards.AbstractExportWizard;
import astpa.wizards.pages.CSVExportPage;

/**
 *
 * @author Lukas Balzer
 *
 */
public class SafetyConstraintsWizard extends AbstractExportWizard{
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public SafetyConstraintsWizard() {
		super(SafetyConstraintView.ID);
		String[] filters= new String[] {"*.csv"}; //$NON-NLS-1$
		setExportPage(new CSVExportPage(filters,Messages.SafetyConstraints,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return performCSVExport();
	}
}