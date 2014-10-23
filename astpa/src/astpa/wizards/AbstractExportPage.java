package astpa.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import astpa.wizards.pages.IExportPage;

/**
 *a class to prevent code cloning in the export Pages
 *
 * @author Lukas Balzer
 *
 */
public abstract class AbstractExportPage extends AbstractWizardPage implements IExportPage{

	

	protected AbstractExportPage(String pageName, String projectName) {
		super(pageName,projectName);
	}


	@Override
	public abstract String getExportPath();

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param path
	 * 			the path as String
	 */
	public abstract void setExportPath(String path);
	

	@Override
	protected String openExportDialog(String[] filters,String[] names) {
		FileDialog fileDialog = new FileDialog(this.getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(filters); 
		fileDialog.setFilterNames(names); 
		fileDialog.setFileName(this.getProjectName());
		String filePath = fileDialog.open();
		if (filePath != null) {
			setExportPath(filePath);
			setPageComplete(true);
			return filePath;
		}
		
		return ""; ////$NON-NLS-1$
	}
}
