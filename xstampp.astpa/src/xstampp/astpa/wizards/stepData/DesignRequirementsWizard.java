package xstampp.astpa.wizards.stepData;

import messages.Messages;
import xstampp.astpa.ui.sds.DesignRequirementView;
import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.CSVExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class DesignRequirementsWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public DesignRequirementsWizard() {
		super(DesignRequirementView.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$ 
		this.setExportPage(new CSVExportPage(filters,
				Messages.DesignRequirements + Messages.AsDataSet));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.DESIGN_REQUIREMENT);
	}
}
