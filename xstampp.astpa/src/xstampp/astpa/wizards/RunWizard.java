package xstampp.astpa.wizards;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.util.jobs.Run;
import xstampp.astpa.wizards.pages.PdfExportPage;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;

public class RunWizard extends AbstractExportWizard {

	private final static String OUTPUT ="Output"; //$NON-NLS-1$
	private final static String IMAGES="images";//$NON-NLS-1$
	private final static String PDFS="pdf";//$NON-NLS-1$
	private final static String CSV="csv";//$NON-NLS-1$
	private RunPage page;

	public RunWizard() {
		super();
		Object selection =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ProjectExplorer.ID).getViewSite()
				.getSelectionProvider().getSelection();
		String projectName="";
		if(selection instanceof IProjectSelection){
			projectName = ProjectManager.getContainerInstance().
									getTitle(((IProjectSelection) selection).getProjectId());
		}
		File outputDir =new File(Platform.getInstanceLocation().getURL().getPath().toString()
								+ OUTPUT);
		if(!outputDir.exists()){
				System.out.println(outputDir.mkdirs());
		}
		this.page = new RunPage("Run Export", projectName);
		this.setExportPage(this.page);
	}

	@Override
	public boolean performFinish() {
		File outputDir =new File(this.page.getExportPath());
		if(!outputDir.exists()){
			System.out.println(outputDir.mkdirs());
		}
		File exportDir;
		for(String name: new String[]{IMAGES,PDFS,CSV}){
			exportDir =new File(outputDir + File.separator + name);
			if(!exportDir.exists()){
				System.out.println(exportDir.mkdirs());
			}else{
				for(File f: exportDir.listFiles()){
					f.delete();
				}
			}
		}
		Run runjob= new Run(this.page.getProjectName(), outputDir + File.separator,this.page.getProjectID());
		runjob.schedule();
		return true;
	}

	private class RunPage extends PdfExportPage{

		public RunPage(String pageName, String projectName) {
			super(pageName, projectName, PathComposite.DIR_DIALOG);
		}
		
		@Override
		public void createControl(Composite parent) {
			super.createControl(parent);
			this.pathChooser.setText(Platform.getInstanceLocation().getURL().getFile()
					+ OUTPUT + File.separator + getProjectName());
			setPageComplete(checkFinish());
		}
	}
}


