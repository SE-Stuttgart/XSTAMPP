/**
 * 
 * @author Lukas Balzer
 */
package astpa.wizards.stepData;

import messages.Messages;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.acchaz.AccidentsView;
import astpa.wizards.AbstractExportWizard;
import astpa.wizards.pages.CSVExportPage;

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
	public AccidentsWizard()  {
		super(AccidentsView.ID);
		String[] filters= new String[] {"*.csv"}; //$NON-NLS-1$
		setExportPage(new CSVExportPage(filters,Messages.Accidents,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return performCSVExport();
	}

	
}
