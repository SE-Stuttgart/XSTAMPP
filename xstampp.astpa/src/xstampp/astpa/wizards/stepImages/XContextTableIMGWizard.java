package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.wizards.TableExportPage;

public class XContextTableIMGWizard extends AbstractExportWizard {

	public XContextTableIMGWizard() {
		super("");
		String[] filters = new String[] {"*.png" ,"*.bmp"}; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.ContextTables + Messages.AsImage, Activator.PLUGIN_ID));
		
	}

	@Override
	public boolean performFinish() {
		setExportAddition(calculateContextSize());
		return this.performXSLExport(				
				"/fopContextTable.xsl",  false, Messages.ContextTables); ////$NON-NLS-1$ //$NON-NLS-3$
	}
	
	private String calculateContextSize(){
		DataModelController controller = (DataModelController) ProjectManager.getContainerInstance().
																				getDataModel(getExportPage().
																						getProjectID());
		return "8";
	
	}
}
