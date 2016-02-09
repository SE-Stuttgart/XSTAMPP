package xstpa.export;

import java.io.IOException;

import messages.Messages;

import org.eclipse.jface.dialogs.MessageDialog;

import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.astpa.util.jobs.StpaCSVExport;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.CSVExportPage;
import xstampp.astpa.wizards.pages.STPADataPage;
import xstampp.model.IDataModel;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.common.ProjectManager;
import xstpa.ui.View;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class XSTPAdataWizard extends AbstractExportWizard {

	STPADataPage site;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public XSTPAdataWizard() {
		super(View.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		
		this.site = new STPADataPage(XCSVExportJob.STEPS,filters, "Custom Data", this.getStore()
				.getString(IPreferenceConstants.PROJECT_NAME));
		this.setExportPage(this.site);
	}

	@Override
	public boolean performFinish() {
		String filePath = getExportPage().getExportPath();
		try {
			if (this.checkError(this.checkPath(filePath))) {
				IDataModel model = ProjectManager.getContainerInstance()
						.getDataModel(this.getExportPage().getProjectID());
				XCSVExportJob export = new XCSVExportJob("Export CSV",	filePath,
						((CSVExportPage) this.getExportPage()).getSeperator(),
						model, this.site.getSteps());
				export.schedule();
			} else {
				return false;
			}
		} catch (IOException e) {
			MessageDialog.openWarning(this.getShell(), Messages.Warning,
					Messages.ChooseTheDestination);
			return false;
		}
		return true;
	}

}