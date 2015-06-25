package xstampp.astpa.wizards.stepData;

import messages.Messages;
import xstampp.astpa.ui.causalfactors.CausalFactorsView;
import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.CSVExportPage;
import xstampp.preferences.IPreferenceConstants;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class CausalFactorsDataWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public CausalFactorsDataWizard() {
		super(CausalFactorsView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters, Messages.CausalFactors + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.CAUSAL_FACTOR);
	}
}