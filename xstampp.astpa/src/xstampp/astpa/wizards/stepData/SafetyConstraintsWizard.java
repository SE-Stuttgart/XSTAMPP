package xstampp.astpa.wizards.stepData;

import messages.Messages;
import xstampp.astpa.ui.sds.SafetyConstraintView;
import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.CSVExportPage;
import xstampp.preferences.IPreferenceConstants;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class SafetyConstraintsWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public SafetyConstraintsWizard() {
		super(SafetyConstraintView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters,
				Messages.SafetyConstraints + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.SAFETY_CONSTRAINT);
	}
}