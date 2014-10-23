package astpa.wizards.stepImages;

import messages.Messages;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.causalfactors.CausalFactorsView;
import astpa.wizards.AbstractExportWizard;
import astpa.wizards.pages.TableExportPage;

/**
 *
 * @author Lukas Balzer
 *
 */
public class CausalFactorsTableWizard extends AbstractExportWizard{

	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public CausalFactorsTableWizard() {
		super(CausalFactorsView.ID);
		String[] filters= new String[] {"*.png"}; //$NON-NLS-1$
		setExportPage(new TableExportPage(filters,Messages.CausalFactorsTable,
										getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}


	@Override
	public boolean performFinish() {
		return this.performXSLExport("/fopcausal.xsl",Messages.ExportingCFTable,false); ////$NON-NLS-1$
	}
}
