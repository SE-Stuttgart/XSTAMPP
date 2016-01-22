package acast.export.csv;

import xstampp.preferences.IPreferenceConstants;
import acast.export.CASTDataPage;
import acast.ui.acchaz.HazardsView;
import acast.wizards.AbstractExportWizard;

public class AllCSVDataWizard extends AbstractExportWizard {

	CASTDataPage site;

	/**
	 *
	 *
	 */
	public AllCSVDataWizard() {
		super(new String[] { HazardsView.ID });
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$

		this.site = new CASTDataPage(filters, "Custom Data",
				this.getStore().getString(IPreferenceConstants.PROJECT_NAME));
		this.setExportPage(this.site);
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(this.site.getSteps());
	}

}
