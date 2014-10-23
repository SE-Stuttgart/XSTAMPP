package astpa.wizards.stepData;

import messages.Messages;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.sds.CSCView;
import astpa.wizards.AbstractExportWizard;
import astpa.wizards.pages.CSVExportPage;

/**
 *
 *	a Wizard for Exporting the Corresponding Safety Constraints 
 * @author Lukas Balzer
 *
 */
public class CSCWizard extends AbstractExportWizard{
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public CSCWizard() {
		super(CSCView.ID);
		String[] filters= new String[] {"*.csv"}; //$NON-NLS-1$ 
		setExportPage(new CSVExportPage(filters,Messages.CorrespondingSafetyConstraints,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return performCSVExport();
	}
}

