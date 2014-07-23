/**
 * 
 * @author Lukas Balzer
 */
package astpa.export.stepData;

import messages.Messages;
import astpa.export.AbstractExportWizard;
import astpa.export.pages.CSVExportPage;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.acchaz.HazardsView;

/**
 *
 * @author Lukas Balzer
 *
 */
public class HazardsWizard extends AbstractExportWizard{
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public HazardsWizard() {
		super(HazardsView.ID);
		String[] filters= new String[] {"*.csv"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setExportPage(new CSVExportPage(filters,Messages.Hazards,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return performCSVExport();
	}
}
