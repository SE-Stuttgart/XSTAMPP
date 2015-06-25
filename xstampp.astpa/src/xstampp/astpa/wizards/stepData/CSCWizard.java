package xstampp.astpa.wizards.stepData;

import messages.Messages;
import xstampp.astpa.ui.sds.CSCView;
import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.CSVExportPage;
import xstampp.preferences.IPreferenceConstants;

/**
 * 
 * a Wizard for Exporting the Corresponding Safety Constraints
 * 
 * @author Lukas Balzer
 * 
 */
public class CSCWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public CSCWizard() {
		super(CSCView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$ 
		this.setExportPage(new CSVExportPage(filters,
				Messages.CorrespondingSafetyConstraints + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this
				.performCSVExport(ICSVExportConstants.CORRESPONDING_SAFETY_CONSTRAINTS);
	}
}
