package xstampp.astpa.util.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;

/**
 * an export job which run s a complete export in all available formats.
 * the export is managed through the implementation of<code> IJobChangeListener</code>
 * which counts all scheduled jobs and notifys the system when the export is finished
 * 
 * @author Lukas Balzer
 * @since 2.0
 *
 */
public class Run extends Job implements IJobChangeListener{


	/**
	 * the name for the image export folder
	 * @author Lukas Balzer
	 */
	public static final String IMAGE_DIR="images"; //$NON-NLS-1$
	/**
	 * the name for the pdf export folder
	 * @author Lukas Balzer
	 */
	public static final String PDF_DIR="pdf"; //$NON-NLS-1$
	/**
	 * the name for the csv export folder
	 * @author Lukas Balzer
	 */
	public static final String CSV_DIR="csv"; //$NON-NLS-1$
	
	private UUID projectID;
	private String dir;
	private int counter=0;
	private String[] xslMap = new String[] {Messages.Accidents,"/fopAccidents.xsl",//$NON-NLS-1$
											Messages.Hazards,"/fopHazards.xsl",//$NON-NLS-1$
											Messages.CausalFactors,"/fopcausal.xsl",//$NON-NLS-1$
											Messages.CorrespondingSafetyConstraints,"/fopCorrespondingSafetyConstraints.xsl",//$NON-NLS-1$
											Messages.DesignRequirements,"/fopDesignRequirements.xsl",//$NON-NLS-1$
											Messages.SafetyConstraints,"/fopSafetyConstraints.xsl",//$NON-NLS-1$
											Messages.SystemDescription,"/fopSystemDescription.xsl",//$NON-NLS-1$
											Messages.SystemGoals,"/fopSystemGoals.xsl",//$NON-NLS-1$
											Messages.UnsafeControlActionsTable,"/fopuca.xsl"};//$NON-NLS-1$
	
	private String[] csvMap = new String[] {Messages.Accidents,ICSVExportConstants.ACCIDENT,
											Messages.Hazards,ICSVExportConstants.HAZARD,
											Messages.CausalFactors,ICSVExportConstants.CAUSAL_FACTOR,
											Messages.CorrespondingSafetyConstraints,ICSVExportConstants.CORRESPONDING_SAFETY_CONSTRAINTS,
											Messages.DesignRequirements,ICSVExportConstants.DESIGN_REQUIREMENT,
											Messages.SafetyConstraints,ICSVExportConstants.SAFETY_CONSTRAINT,
											Messages.SystemDescription,ICSVExportConstants.PROJECT_DESCRIPTION,
											Messages.SystemGoals,ICSVExportConstants.SYSTEM_GOAL,
											Messages.UnsafeControlActionsTable,ICSVExportConstants.UNSAFE_CONTROL_ACTION};
	private boolean exportReport;
	private boolean exportImages;
	private boolean exportCSVs;
	private boolean exportPDFs;
	private boolean decorateCS;
							

	/**
	 * this constructor creates a run export job which 
	 * registeres it selfe as it's job listener to guarantie that
	 * done() is called at least one time 
	 *  
	 * @author Lukas Balzer
	 *
	 * @param name the name of the job
	 * @param path the path which should be used
	 * @param id the project id
	 */
	public Run(String name,String path,UUID id) {
		super(name);
		this.dir = path;
		this.projectID = id;
		this.exportCSVs = true;
		this.exportImages = true;
		this.exportPDFs = true;
		this.exportReport=true;
		this.addJobChangeListener(this);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.ExportPdf, IProgressMonitor.UNKNOWN);
		ProjectManager.getContainerInstance()
		.getDataModel(this.projectID).prepareForExport();
		
		monitor.setTaskName(Messages.ExportingCSV);
		for(int i= 0;i<this.csvMap.length && this.exportCSVs;i+=2){
			List<String> values = new ArrayList<>();
			values.add(this.csvMap[i+1]);
			StpaCSVExport job = new StpaCSVExport(getName(),this.dir+ CSV_DIR + File.separator + this.csvMap[i] +".csv",//$NON-NLS-1$
										 	';',ProjectManager.getContainerInstance().getDataModel(this.projectID),values);
			job.showPreview(false);
			job.addJobChangeListener(this);
			job.schedule();
		}

		monitor.setTaskName(Messages.ExportImage);
		for(int i= 0;i<this.xslMap.length && this.exportImages;i+=2){

			ExportJob job = new ExportJob(this.projectID, getName(), 
											this.dir+ IMAGE_DIR + File.separator + this.xslMap[i] +".png",  //$NON-NLS-1$
										 	this.xslMap[i+1], true, false);
			job.showPreview(false);
			job.addJobChangeListener(this);
			job.schedule();
		}
		if(this.exportImages){
			String csPath = this.dir+ IMAGE_DIR + File.separator + Messages.ControlStructure +".png";
			String csPMPath = this.dir+ IMAGE_DIR + File.separator + Messages.ControlStructureDiagramWithProcessModel +".png";
			CSExportJob job = new CSExportJob(csPath, 5	, CSEditor.ID, this.projectID, false,this.decorateCS);
			CSExportJob pmJob = new CSExportJob(csPMPath, 5	, CSEditorWithPM.ID, this.projectID, false,this.decorateCS);
			
			job.addJobChangeListener(this);
			pmJob.addJobChangeListener(this);
			job.schedule();
			pmJob.schedule();
		}
		monitor.setTaskName(Messages.ExportingPdf);
		for(int i= 0;i<this.xslMap.length && this.exportPDFs;i+=2){
			ExportJob pdfJob = new ExportJob(this.projectID, getName(),
											this.dir + PDF_DIR + File.separator + this.xslMap[i] +".pdf", //$NON-NLS-1$
											this.xslMap[i+1], true, false);
			pdfJob.showPreview(false);
			pdfJob.addJobChangeListener(this);
			pdfJob.schedule();
			try {
				pdfJob.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(this.exportReport){
			ExportJob pdfRepJob = new ExportJob(this.projectID, getName(),
					this.dir + getName()+".pdf", //$NON-NLS-1$
					"/fopxsl.xsl", true, false); //$NON-NLS-1$
			pdfRepJob.setCSDirty();
			pdfRepJob.showPreview(false);
			pdfRepJob.addJobChangeListener(this);
			pdfRepJob.schedule();
			try {
				pdfRepJob.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return Status.OK_STATUS;
	}

	@Override
	public void aboutToRun(IJobChangeEvent event) {
		//do nothing
	}

	@Override
	public void awake(IJobChangeEvent event) {
		// do nothing
	}

	@Override
	public void done(IJobChangeEvent event) {
		
		this.counter--;
		if(this.counter == 0){
			//this command 
			OpenInFileBrowser(this.dir);
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {

					ProjectManager.getContainerInstance().callObserverValue(
							ObserverValue.EXPORT_FINISHED);
					ProjectManager.getContainerInstance()
							.getDataModel(Run.this.projectID).prepareForSave();
				}
			});
			ProjectManager.getLOGGER().debug("STPA run export finished"); //$NON-NLS-1$
		}
	}

	@Override
	public void running(IJobChangeEvent event) {
		//this listener needs only to handle the scheduled() and the done() methode
	}

	@Override
	public void scheduled(IJobChangeEvent event) {
		this.counter++;
	}

	@Override
	public void sleeping(IJobChangeEvent event) {
		//this listener needs only to handle the scheduled() and the done() methode
	}
	
	
    private static void OpenInMacFileBrowser(String path)
    {
        Boolean openInsidesOfFolder = false;
        // try mac
        String macPath = path.replace("\\", "/"); // mac finder doesn't like backward slashes
        File pathfile = new File(macPath);
        // if path requested is a folder, automatically open insides of that folder
        if (pathfile.isDirectory()) 
        {
            openInsidesOfFolder = true;
        }

        if (!macPath.startsWith("\""))
        {
            macPath = "\"" + macPath;
        }
        if (!macPath.endsWith("\""))
        {
            macPath = macPath + "\"";
        }
        String arguments = (openInsidesOfFolder ? "" : "-R ") + macPath;
        try
        {
        	Runtime.getRuntime().exec("open"+ arguments);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void OpenInWinFileBrowser(String path)
    {
        Boolean openInsidesOfFolder = false;

        // try windows
        String winPath = path.replace("/", "\\"); // windows explorer doesn't like forward slashes
        File pathfile = new File(winPath);
        // if path requested is a folder, automatically open insides of that folder
        if (pathfile.isDirectory()) 
        {
            openInsidesOfFolder = true;
        }
        try
        {
        	Runtime.getRuntime().exec("explorer.exe "+ (openInsidesOfFolder ? "/root," : "/select,") + winPath);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void OpenInFileBrowser(String path)
    {
    	String osName= System.getProperty("os.name").toLowerCase();
    	if(osName.startsWith("win")){
    		OpenInWinFileBrowser(path);
    	}else if(osName.startsWith("mac")){
    		OpenInMacFileBrowser(path);
    	}
    }

	/**
	 * set whether or not the pdf files should be included in the export
	 * 
	 * @author Lukas Balzer
	 *
	 * @param choice the choice whether or not to export the pdf files
	 */
	public void exportPDFs(boolean choice) {
		this.exportPDFs = choice;
		
	}

	/**
	 * set whether or not the csv data should be included in the export
	 * 
	 * @author Lukas Balzer
	 *
	 * @param choice the choice whether or not to export the data
	 */
	public void exportCSVs(boolean choice) {
		this.exportCSVs = choice;
	}

	/**
	 * set whether or not the images should be included in the export
	 * 
	 * @author Lukas Balzer
	 *
	 * @param choice the choice whether or not to export the images
	 */
	public void exportImages(boolean choice) {
		this.exportImages = choice;
	}

	/**
	 * set whether or not the final STPA report should be included in the export
	 * 
	 * @author Lukas Balzer
	 *
	 * @param choice the choice whether or not to export the report
	 */
	public void exportReport(boolean choice) {
		this.exportReport = choice;
	}

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param decoChoice whether or not to activate the decoration on
	 *     		control structure components
	 */
	public void setcsDecoration(boolean decoChoice) {
		this.decorateCS = decoChoice;
	}
}
