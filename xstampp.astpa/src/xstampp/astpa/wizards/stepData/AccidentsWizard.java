/**
 * 
 * @author Lukas Balzer
 */
package xstampp.astpa.wizards.stepData;

import messages.Messages;
import xstampp.astpa.ui.acchaz.AccidentsView;
import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.CSVExportPage;
import xstampp.preferences.IPreferenceConstants;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class AccidentsWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public AccidentsWizard() {
		super(AccidentsView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters, Messages.Accidents, this
				.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.ACCIDENT);
	}

}
