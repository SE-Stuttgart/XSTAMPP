package xstampp.astpa.wizards.stepData;

import xstampp.astpa.ui.acchaz.AccidentsView;
import xstampp.astpa.ui.acchaz.HazardsView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.STPADataPage;
import xstampp.preferences.IPreferenceConstants;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class STPAdataWizard extends AbstractExportWizard {

	STPADataPage site;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public STPAdataWizard() {
		super(new String[] { AccidentsView.ID, HazardsView.ID });
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$

		this.site = new STPADataPage(filters, "Custom Data", this.getStore()
				.getString(IPreferenceConstants.PROJECT_NAME));
		this.setExportPage(this.site);
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(this.site.getSteps());
	}

}