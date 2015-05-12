package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class UCATableExportWizard extends AbstractExportWizard {
	TableExportPage exportPage;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public UCATableExportWizard() {
		super(UnsafeControlActionsView.ID);
		String[] filters = new String[] { "*.png", "*.jpg", "*.bmp","*.pdf" }; //$NON-NLS-1$
		this.exportPage = new TableExportPage(filters,
				Messages.ExportPreferences);
		this.setExportPage(this.exportPage);
	}

	@Override
	public void addPages() {
		this.addPage(this.exportPage);
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(
				"/fopuca.xsl", Messages.ExportingUCATable, false); //$NON-NLS-1$
	}
}
