package astpa.wizards.stepImages;

import messages.Messages;


import astpa.preferences.IPreferenceConstants;
import astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import astpa.wizards.AbstractExportWizard;
import astpa.wizards.pages.TableExportPage;

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
		String[] filters= new String[] {"*.png","*.pdf"}; //$NON-NLS-1$
		this.exportPage = new TableExportPage(filters,Messages.ExportPreferences,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME));
		setExportPage(this.exportPage);
	}

	@Override
	public void addPages() {
		this.addPage(this.exportPage);
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport("/fopuca.xsl",Messages.ExportingUCATable,false); //$NON-NLS-1$
	}
}
