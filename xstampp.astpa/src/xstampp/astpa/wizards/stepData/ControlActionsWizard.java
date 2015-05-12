package xstampp.astpa.wizards.stepData;

import messages.Messages;
import xstampp.astpa.ui.sds.ControlActionView;
import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.CSVExportPage;
import xstampp.preferences.IPreferenceConstants;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class ControlActionsWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public ControlActionsWizard() {
		super(ControlActionView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters, Messages.ControlAction));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.CONTROL_ACTION);
	}

}
