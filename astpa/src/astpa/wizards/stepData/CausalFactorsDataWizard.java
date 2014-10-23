package astpa.wizards.stepData;

import messages.Messages;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.causalfactors.CausalFactorsView;
import astpa.wizards.AbstractExportWizard;
import astpa.wizards.pages.CSVExportPage;


/**
 *
 * @author Lukas Balzer
 *
 */
public class CausalFactorsDataWizard extends AbstractExportWizard{
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public CausalFactorsDataWizard() {
		super(CausalFactorsView.ID);
		String[] filters= new String[] {"*.csv"}; //$NON-NLS-1$
		setExportPage(new CSVExportPage(filters,Messages.CausalFactors,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return performCSVExport();
	}
}