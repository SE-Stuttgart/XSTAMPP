package xstampp.astpa.wizards.stepData;

import messages.Messages;
import xstampp.astpa.ui.sds.SystemGoalView;
import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.CSVExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class SystemGoalsWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public SystemGoalsWizard() {
		super(SystemGoalView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$ 
		this.setExportPage(new CSVExportPage(filters, Messages.SystemGoals + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.SYSTEM_GOAL);
	}
}
