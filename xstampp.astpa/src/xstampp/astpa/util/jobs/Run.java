package xstampp.astpa.util.jobs;

import java.io.File;
import java.util.UUID;




import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;

public class Run extends Job implements IJobChangeListener{

	private UUID projectID;
	private String dir;
	private String[] xslMap = new String[] {"Accidents","/fopAccidents.xsl",
											"Hazzard","/fopHazards.xsl",
											"CausalFactors","/fopcausal.xsl",
											"CorresponsingSafetyConstraints","/fopCorrespondingSafetyConstraints.xsl",
											"DesignRequironments","/fopDesignRequironments.xsl",
											"SafetyConstraints","/fopSafetyConstraints.xsl",
											"SystemDecription","/fopSystemDescription.xsl",
											"SystemGoals","/fopSystemGoals.xsl",
											"UnsafeControlActionsTable","/fopuca.xsl"};
							

	public Run(String name,String path,UUID id) {
		super(name);
		this.dir = path;
		this.projectID = id;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.ExportPdf, IProgressMonitor.UNKNOWN);
		for(int i= 0;i<xslMap.length;i+=2){

			ExportJob job = new ExportJob(this.projectID, getName(), 
											this.dir+ "images" + File.separator + this.xslMap[i] +".png", 
										 	this.xslMap[i+1], true, false);
			job.showPreview(false);
			job.schedule();
			ExportJob pdfJob = new ExportJob(this.projectID, getName(),
											this.dir + "pdf" + File.separator + this.xslMap[i] +".pdf",
											this.xslMap[i+1], true, false);
			pdfJob.showPreview(false);
			pdfJob.schedule();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void running(IJobChangeEvent event) {
		try {
			event.getJob().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void scheduled(IJobChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sleeping(IJobChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
