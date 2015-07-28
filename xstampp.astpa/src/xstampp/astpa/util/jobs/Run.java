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

import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;

public class Run extends Job implements IJobChangeListener{

	private UUID projectID;
	private String dir;
	private int counter=0;
	private String[] xslMap = new String[] {"Accidents","/fopAccidents.xsl",
											"Hazzard","/fopHazards.xsl",
											"CausalFactors","/fopcausal.xsl",
											"CorresponsingSafetyConstraints","/fopCorrespondingSafetyConstraints.xsl",
											"DesignRequironments","/fopDesignRequironments.xsl",
											"SafetyConstraints","/fopSafetyConstraints.xsl",
											"SystemDecription","/fopSystemDescription.xsl",
											"SystemGoals","/fopSystemGoals.xsl",
											"UnsafeControlActionsTable","/fopuca.xsl"};
	
	private String[] csvMap = new String[] {"Accidents",ICSVExportConstants.ACCIDENT,
											"Hazzard",ICSVExportConstants.HAZARD,
											"CausalFactors",ICSVExportConstants.CAUSAL_FACTOR,
											"CorresponsingSafetyConstraints",ICSVExportConstants.CORRESPONDING_SAFETY_CONSTRAINTS,
											"DesignRequironments",ICSVExportConstants.DESIGN_REQUIREMENT,
											"SafetyConstraints",ICSVExportConstants.SAFETY_CONSTRAINT,
											"SystemDecription",ICSVExportConstants.PROJECT_DESCRIPTION,
											"SystemGoals",ICSVExportConstants.SYSTEM_GOAL,
											"UnsafeControlActionsTable",ICSVExportConstants.UNSAFE_CONTROL_ACTION};
							

	public Run(String name,String path,UUID id) {
		super(name);
		this.dir = path;
		this.projectID = id;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.ExportPdf, IProgressMonitor.UNKNOWN);
		ProjectManager.getContainerInstance()
		.getDataModel(this.projectID).prepareForExport();
		for(int i= 0;i<this.xslMap.length;i+=2){

			ExportJob job = new ExportJob(this.projectID, getName(), 
											this.dir+ "images" + File.separator + this.xslMap[i] +".png", 
										 	this.xslMap[i+1], true, false);
			job.showPreview(false);
			job.addJobChangeListener(this);
			job.schedule();
			ExportJob pdfJob = new ExportJob(this.projectID, getName(),
											this.dir + "pdf" + File.separator + this.xslMap[i] +".pdf",
											this.xslMap[i+1], true, false);
			pdfJob.showPreview(false);
			pdfJob.addJobChangeListener(this);
			pdfJob.schedule();
		}
		
		ExportJob pdfRepJob = new ExportJob(this.projectID, getName(),
				this.dir + "pdf" + File.separator + getName()+".pdf",
				"/fopxsl.xsl", true, false);
		pdfRepJob.setCSDirty();
		pdfRepJob.setCSImagePath(this.dir + "images");
		pdfRepJob.showPreview(false);
		pdfRepJob.addJobChangeListener(this);
		pdfRepJob.schedule();
		
		for(int i= 0;i<this.csvMap.length;i+=2){
			List<String> values = new ArrayList<>();
			values.add(this.csvMap[i+1]);
			StpaCSVExport job = new StpaCSVExport(getName(),this.dir+ "csv" + File.separator + this.csvMap[i] +".csv", 
										 	';',ProjectManager.getContainerInstance().getDataModel(this.projectID),values);
			job.showPreview(false);
			job.addJobChangeListener(this);
			job.schedule();
		}
		
		System.out.println("export done");
		return Status.OK_STATUS;
	}

	@Override
	public void aboutToRun(IJobChangeEvent event) {
		
	}

	@Override
	public void awake(IJobChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void done(IJobChangeEvent event) {
		this.counter--;
		if(this.counter == 0){
			try {
				Runtime.getRuntime().exec("explorer.exe /select,"+this.dir);
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {

						ProjectManager.getContainerInstance().callObserverValue(
								ObserverValue.EXPORT_FINISHED);
						ProjectManager.getContainerInstance()
								.getDataModel(Run.this.projectID).prepareForSave();
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void running(IJobChangeEvent event) {
	
	}

	@Override
	public void scheduled(IJobChangeEvent event) {
		this.counter++;
	}

	@Override
	public void sleeping(IJobChangeEvent event) {
		
	}

}
