package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.ui.sds.SystemGoalView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

public class SystemGoalsPDFWizard extends AbstractExportWizard {

	public SystemGoalsPDFWizard() {
		super(SystemGoalView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.SystemGoals + Messages.AsPDF, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopSystemGoals.xsl", false, Messages.SystemGoals); ////$NON-NLS-1$
	}
}
