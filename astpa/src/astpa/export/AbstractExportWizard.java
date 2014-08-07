package astpa.export;

import java.io.File;
import java.io.IOException;

import messages.Messages;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import astpa.Activator;
import astpa.export.pages.CSVExportPage;
import astpa.export.pages.IExportPage;
import astpa.ui.common.ViewContainer;

/**
 *
 * @author Lukas Balzer
 *
 */
public abstract class AbstractExportWizard extends Wizard implements IExportWizard{

	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	private IExportPage exportPage;
	private String[] viewId;

	private enum Error{
		OK,
		CANT_OVERWRITE,
		EXIT,
		CANT_FIND;
	}
	
	/**
	 * @author Lukas Balzer
	 */
	public AbstractExportWizard() {
		this(new String[]{""}); //$NON-NLS-1$
	}

	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param viewId
	 * 			the id of ther view for which the export shall be executed
	 */
	public AbstractExportWizard(String viewId) {
		this(new String[]{viewId});
	}
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param viewId
	 * 			the id of ther view for which the export shall be executed
	 */
	public AbstractExportWizard(String[] viewId) {
		super();
		this.setHelpAvailable(true);
		this.viewId=viewId;
	}
	protected boolean performCSVExport() {
		String filePath = this.exportPage.getExportPath();
		try {
			if(checkError(checkPath(filePath))) {
				ViewContainer viewContainer =
					(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.findView(ViewContainer.ID);
				viewContainer.exportViewData(filePath,this.viewId,((CSVExportPage) this.exportPage).getSeperator());
			} else {
				return false;
			}
		} catch (IOException e) {
			MessageDialog.openWarning(this.getShell(), Messages.Warning, Messages.ChooseTheDestination);
			return false;
		}
		return true;
	}
	
	
	protected boolean performNormalExport() {
		return performNormalExport(null);
	}
		
	protected boolean performNormalExport(Object[] values) {
		for(Object o: values){
			System.out.println(o);
		}
		try {
			if(checkError(checkPath((String) values[0]))) {
				ViewContainer viewContainer =
					(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.findView(ViewContainer.ID);
				viewContainer.export(values,this.getExportedViews()[0]);
			} else {
				return false;
			}
		} catch (IOException e) {
			MessageDialog.openWarning(this.getShell(), Messages.Warning, Messages.ChooseTheDestination);
			return false;
		}
		return true;
	}
	
	protected boolean performXSLExport(String fopName, String jobMessage, boolean forceCSDeco){
		
		String filePath = this.getExportPage().getExportPath();
		if ((filePath != null) && !filePath.isEmpty()) {
			File newPDF=new File(filePath);
			if(newPDF.isFile() && !MessageDialog.openConfirm(this.getShell(), Messages.Warning,
									String.format(Messages.DoYouReallyWantToOverwriteTheFile,newPDF.getName()))){
				return false;
			}
			
			try {
				newPDF.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			if(!newPDF.canWrite()){
				MessageDialog.openWarning(this.getShell(), Messages.Warning, Messages.CantOverride);
					return false;
			}
			ViewContainer viewContainer =
				(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.findView(ViewContainer.ID);
			if(!viewContainer.export(filePath, getMimeConstant(filePath), fopName,getExportPage().asOne(),jobMessage,forceCSDeco)){
				MessageDialog.openWarning(this.getShell(), Messages.Warning, Messages.CantOverride);
				return false;
			}
		} else {
			MessageDialog.openWarning(this.getShell(), Messages.Warning, Messages.ChooseTheDestination);
			return false;
		}
		return true;
	}
	
	protected String getMimeConstant(String path){
		if(path.endsWith("pdf")){ //$NON-NLS-1$
			return org.apache.xmlgraphics.util.MimeConstants.MIME_PDF;
		}
		if(path.endsWith("png")){ //$NON-NLS-1$
			return org.apache.xmlgraphics.util.MimeConstants.MIME_PNG;
		}
		if(path.endsWith("svg")){ //$NON-NLS-1$
			return org.apache.xmlgraphics.util.MimeConstants.MIME_SVG;
		}
		return null;
	}
	/**
	 * Parse string to RGB Object
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param rgbString String
	 * @return RGB
	 */
	protected RGB toRGB(String rgbString) {
		int rgbRed = Integer.parseInt(rgbString.substring(rgbString.indexOf("{") + 1, rgbString.indexOf(",")).trim()); //$NON-NLS-1$ //$NON-NLS-2$
		int rgbGreen =
			Integer.parseInt(rgbString.substring(rgbString.indexOf(",") + 1, rgbString.lastIndexOf(",")).trim()); //$NON-NLS-1$ //$NON-NLS-2$
		int rgbBlue =
			Integer.parseInt(rgbString.substring(rgbString.lastIndexOf(",") + 1, rgbString.indexOf("}")).trim()); //$NON-NLS-1$ //$NON-NLS-2$
		RGB rgbCompanyColor = new RGB(rgbRed, rgbGreen, rgbBlue);
		return rgbCompanyColor;
	}
	
	protected Error checkPath(String filePath) throws IOException{
		if ((filePath == null) || filePath.isEmpty()){
			return Error.CANT_FIND;
		}
		File file = new File(filePath);
		if(!file.exists() && file.createNewFile()){
			return Error.OK;
		}
		if(MessageDialog.openQuestion(this.getShell(),Messages.Warning,
									String.format(Messages.DoYouReallyWantToOverwriteTheFile,file.getName()))){
			if(!file.delete()){
				return Error.CANT_OVERWRITE;
			}
			file.createNewFile();
		}else{
			return Error.EXIT;
		}
		return Error.OK;
		
	}

	protected boolean checkError(Error err){
		switch(err){
		case CANT_FIND:
			MessageDialog.openWarning(this.getShell(), Messages.Warning, Messages.ChooseTheDestination);
			break;
		case CANT_OVERWRITE:
			MessageDialog.openWarning(this.getShell(), Messages.Warning, Messages.CantOverride);
			break;
		case EXIT:
			break;
		case OK:
			return true;
		}
		return false;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		// Does nothing by default
		
	}
	
	@Override
	public void addPages() {
		this.addPage(this.exportPage);

	}

	/**
	 * @return the store
	 */
	public IPreferenceStore getStore() {
		return this.store;
	}

	/**
	 * @return the exportPage
	 */
	public IExportPage getExportPage() {
		return this.exportPage;
	}

	/**
	 * @param exportPage the exportPage to set
	 */
	public void setExportPage(IExportPage exportPage) {
		this.exportPage = exportPage;
	}

	protected void setExportedViews(String[] ids){
		this.viewId=ids;
	}
	
	protected String[] getExportedViews(){
		return this.viewId;
	}
}