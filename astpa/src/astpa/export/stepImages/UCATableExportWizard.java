package astpa.export.stepImages;

import messages.Messages;


import astpa.export.AbstractExportWizard;
import astpa.export.pages.TableExportPage;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.unsafecontrolaction.UnsafeControlActionsView;

/**
 *
 * @author Lukas Balzer
 *
 */
public class UCATableExportWizard extends AbstractExportWizard{
	TableExportPage exportPage;
	
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public UCATableExportWizard() {
		super(UnsafeControlActionsView.ID);
		String[] filters= new String[] {"*.png"}; //$NON-NLS-1$
		this.exportPage = new TableExportPage(filters,Messages.ExportPreferences,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME));
	}

	@Override
	public void addPages() {
		this.addPage(this.exportPage);
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport("/fopuca.xsl"); //$NON-NLS-1$
	}
}
