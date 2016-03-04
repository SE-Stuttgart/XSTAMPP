package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.wizards.TableExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class UCATablePDFWizard extends AbstractExportWizard {
	TableExportPage exportPage;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public UCATablePDFWizard() {
		super(UnsafeControlActionsView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$
		this.exportPage = new TableExportPage(filters,
				Messages.UnsafeControlActionsTable + Messages.AsPDF, Activator.PLUGIN_ID);
		this.setExportPage(this.exportPage);
	}

	@Override
	public void addPages() {
		this.addPage(this.exportPage);
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(
				"/fopuca.xsl", false, Messages.UnsafeControlActionsTable); //$NON-NLS-1$
	}
}
