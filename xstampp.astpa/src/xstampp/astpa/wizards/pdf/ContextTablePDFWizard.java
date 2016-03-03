package xstampp.astpa.wizards.pdf;

import messages.Messages;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.TableExportPage;
import xstampp.ui.common.ProjectManager;

public class ContextTablePDFWizard extends AbstractExportWizard {

	public ContextTablePDFWizard() {
		super("");
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ 
		this.setExportPage(new TableExportPage(filters,
				Messages.ContextTables + Messages.AsPDF));
		
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
