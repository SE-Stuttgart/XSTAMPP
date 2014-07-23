package astpa.export.stepData;

import messages.Messages;
import astpa.export.AbstractExportWizard;
import astpa.export.pages.CSVExportPage;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.causalfactors.CausalFactorsView;


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