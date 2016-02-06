package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.ui.causalfactors.CausalFactorsView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class CausalFactorsTablePDFWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public CausalFactorsTablePDFWizard() {
		super(CausalFactorsView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$
		this.setExportPage(new TableExportPage(filters,
				Messages.CausalFactorsTable + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(
				"/fopcausal.xsl", false, Messages.CausalFactorsTable); ////$NON-NLS-1$
	}
}
