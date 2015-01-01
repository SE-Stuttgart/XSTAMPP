package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.ui.causalfactors.CausalFactorsView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;
import xstampp.preferences.IPreferenceConstants;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class CausalFactorsTableWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public CausalFactorsTableWizard() {
		super(CausalFactorsView.ID);
		String[] filters = new String[] { "*.png" }; //$NON-NLS-1$
		this.setExportPage(new TableExportPage(filters,
				Messages.CausalFactorsTable, this.getStore().getString(
						IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(
				"/fopcausal.xsl", Messages.ExportingCFTable, false); ////$NON-NLS-1$
	}
}
