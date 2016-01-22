package xstampp.astpa.util.jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.util.STPAPluginUtils;
import xstampp.util.XstamppJob;

/**
 * an export job which run s a complete export in all available formats.
 * the export is managed through the implementation of<code> IJobChangeListener</code>
 * which counts all scheduled jobs and notifys the system when the export is finished
 * 
 * @author Lukas Balzer
 * @since 2.0
 *
 */
public class Run extends XstamppJob{


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
	
	private List<Job> jobList;
	private String dir;
	private boolean isCanceled;
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
		super(name,id);
		this.dir = path;
		this.exportCSVs = true;
		this.exportImages = true;
		this.exportPDFs = true;
		this.exportReport=true;
		this.isCanceled = false;
		this.jobList = new ArrayList<>();
	}

	@Override
	protected void canceling() {
		this.isCanceled = true;
		for(Job job : this.jobList){
			job.cancel();
		}
		super.canceling();
	}
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Main Run Export...", 10);
		String fileName;
		ProjectManager.getContainerInstance().getDataModel(getProjectId()).prepareForExport();
		
		for(int i= 0;i<this.csvMap.length && this.exportCSVs;i+=2){
			List<String> values = new ArrayList<>();
			values.add(this.csvMap[i+1]);
			fileName = this.csvMap[i] +".csv";
			StpaCSVExport job = new StpaCSVExport(getJobName(fileName),this.dir+ CSV_DIR + File.separator + fileName,
										 	';',ProjectManager.getContainerInstance().getDataModel(getProjectId()),values);
			job.showPreview(false);
			if(!addJob(job)){
				return Status.CANCEL_STATUS;
			}
		}
		monitor.worked(1);
		for(int i= 0;i<this.xslMap.length && this.exportImages;i+=2){
			fileName = this.xslMap[i] +".png";
			ExportJob job = new ExportJob(getProjectId(), getJobName(fileName), 
											this.dir+ IMAGE_DIR + File.separator + fileName,  
										 	this.xslMap[i+1], true, false);
			job.showPreview(false);
			if(!addJob(job)){
				return Status.CANCEL_STATUS;
			}
		}
		monitor.worked(1);
		if(this.exportImages){
			String csPath = this.dir+ IMAGE_DIR + File.separator + Messages.ControlStructure +".png";
			String csPMPath = this.dir+ IMAGE_DIR + File.separator + Messages.ControlStructureDiagramWithProcessModel +".png";
			CSExportJob job = new CSExportJob(csPath, 5	, CSEditor.ID, getProjectId(), false,this.decorateCS);
			CSExportJob pmJob = new CSExportJob(csPMPath, 5	, CSEditorWithPM.ID, getProjectId(), false,this.decorateCS);
			
			if(!addJob(job)){
				return Status.CANCEL_STATUS;
			}
			if(!addJob(pmJob)){
				return Status.CANCEL_STATUS;
			}
		}
		monitor.worked(5);
		for(int i= 0;i<this.xslMap.length && this.exportPDFs;i+=2){
			ExportJob pdfJob = new ExportJob(getProjectId(), "Expoting " +this.xslMap[i] +".pdf",
											this.dir + PDF_DIR + File.separator + this.xslMap[i] +".pdf", //$NON-NLS-1$
											this.xslMap[i+1], true, false);
			pdfJob.showPreview(false);
			if(!addJob(pdfJob)){
				return Status.CANCEL_STATUS;
			}
		}
		monitor.worked(8);
		if(this.exportReport){
			ExportJob pdfRepJob = new ExportJob(getProjectId(), getJobName("Final Report"),
					this.dir + getName()+".pdf", //$NON-NLS-1$
					"/fopxsl.xsl", true, false); //$NON-NLS-1$
			pdfRepJob.setCSDirty();
			pdfRepJob.showPreview(false);
			if(!addJob(pdfRepJob)){
				return Status.CANCEL_STATUS;
			}
		}
		monitor.worked(10);
		return Status.OK_STATUS;
	}

	private String getJobName(String file){
		return "Exporting "+ getName() + " - " +file+"...";
	}
	
	private boolean addJob(Job job){
		if(this.isCanceled){
			return false;
		}
		try {
			job.schedule();
			job.join();
			this.jobList.add(job);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
		
	}

	@Override
	public void done(IJobChangeEvent event) {
		if(this.isCanceled){
			ProjectManager.getLOGGER().debug("STPA run export was canceled"); //$NON-NLS-1$
		}else{
			//this command 
			STPAPluginUtils.OpenInFileBrowser(this.dir);
			Display.getDefault().syncExec(new Runnable() {
	
				@Override
				public void run() {
	
					ProjectManager.getContainerInstance().callObserverValue(
							ObserverValue.EXPORT_FINISHED);
					ProjectManager.getContainerInstance()
							.getDataModel(getProjectId()).prepareForSave();
				}
			});
			ProjectManager.getLOGGER().debug("STPA run export finished"); //$NON-NLS-1$
		}
		super.done(event);
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
