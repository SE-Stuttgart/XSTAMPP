/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.stpapriv.util.jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import xstampp.model.ObserverValue;
import xstampp.stpapriv.messages.SecMessages;
import xstampp.stpapriv.model.PrivacyController;
import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.wizards.AbstractExportPage;
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
	
	public static final String Extended_DIR="Extended"; //$NON-NLS-1$
	/**
	 * the name for the image export folder
	 * @author Lukas Balzer
	 */
	public static final String EX_IMAGE_DIR=Extended_DIR+File.separator+"images"; //$NON-NLS-1$
	/**
	 * the name for the pdf export folder
	 * @author Lukas Balzer
	 */
	public static final String EX_PDF_DIR=Extended_DIR+File.separator+"pdf"; //$NON-NLS-1$
	/**
	 * the name for the csv export folder
	 * @author Lukas Balzer
	 */
	public static final String EX_CSV_DIR=Extended_DIR+File.separator+"csv"; //$NON-NLS-1$
	
	private List<Job> jobList;
	private static final int CSV_WORK = 1;
	private static final int XSL_WORK = 4;
	private static final int CS_IMG_WORK = 1;
	private static final int REPORT_WORK =8;
	private String dir;
	private boolean isCanceled;
	private String[] xslMap = new String[] {SecMessages.Losses,"/fopAccidents.xsl",//$NON-NLS-1$
											SecMessages.Vulnerabilities,"/fopHazards.xsl",//$NON-NLS-1$
											Messages.CausalFactors,"/fopcausal.xsl",//$NON-NLS-1$
											SecMessages.CorrespondingSecurityConstraints,"/fopCorrespondingSafetyConstraints.xsl",//$NON-NLS-1$
											Messages.DesignRequirements,"/fopDesignRequirements.xsl",//$NON-NLS-1$
											SecMessages.SecurityConstraints,"/fopSafetyConstraints.xsl",//$NON-NLS-1$
											Messages.SystemDescription,"/fopSystemDescription.xsl",//$NON-NLS-1$
											Messages.SystemGoals,"/fopSystemGoals.xsl",//$NON-NLS-1$
											SecMessages.UnsecureControlActionsTable,"/fopuca.xsl"};//$NON-NLS-1$

	private String[] xstpaXslMap = new String[] {Messages.ContextTables,"/fopContextTable.xsl",//$NON-NLS-1$
											SecMessages.RefinedSecurityConstraintsTable,"/fopRefinedConstraints.xsl",//$NON-NLS-1$
											SecMessages.RefinedUnsecureControlActions,"/fopRefinedUnsafeControlActions.xsl",//$NON-NLS-1$
											Messages.LTLFormulasTable,"/fopLTLPropertys.xsl"};//$NON-NLS-1$
	private boolean exportReport;
	private boolean exportImages;
	private boolean exportCSVs;
	private boolean exportPDFs;
	private boolean exportExtendedCSVs;
	private boolean exportExtendedPDFs;
	private boolean exportExtendedIMGs;
	private boolean decorateCS;
							
	private String exportReportFormat;
	private String exportImagesFormat;
	private String exportPDFsFormat;
	private String exportExtendedPDFsFormat;
	private String exportExtendedIMGsFormat;
	private UUID projectID;
	
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
		this.isCanceled = false;
		this.jobList = new ArrayList<>();
	}

	@Override
	protected Observable getModelObserver() {
		return (PrivacyController)ProjectManager.getContainerInstance().getDataModel(projectID);
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
		monitor.beginTask("Main Run Export...", calcWork());
		String fileName;
		ProjectManager.getContainerInstance().getDataModel(getProjectID()).prepareForExport();
		
		for(int i= 0;i<ICSVExportConstants.STEPS.size() && this.exportCSVs;i++){
			
			fileName = ICSVExportConstants.STEPS.get(i) +".csv";
			StpaCSVExport job = new StpaCSVExport(getJobName(fileName),this.dir+ CSV_DIR + File.separator + fileName,
										 	';',ProjectManager.getContainerInstance().getDataModel(getProjectID()),1<<i);
			job.showPreview(false);
			if(!addJob(job)){
				return Status.CANCEL_STATUS;
			}
			monitor.worked(CSV_WORK);
		}
		if(this.exportExtendedCSVs){
			fileName = "Extended STPA Data.csv";
			XCSVExportJob export = new XCSVExportJob(getJobName(fileName),	this.dir+ EX_CSV_DIR + File.separator + fileName,
					';',ProjectManager.getContainerInstance().getDataModel(getProjectID()),XCSVExportJob.REFINED_DATA);
			if(!addJob(export)){
				return Status.CANCEL_STATUS;
			}
			monitor.worked(CSV_WORK);
		}
		for(int i= 0;i<this.xslMap.length && this.exportImages;i+=2){
			fileName = this.xslMap[i] +".png";
			ExportJob job = new ExportJob(getProjectID(), getJobName(fileName), 
											this.dir+ IMAGE_DIR + File.separator + fileName,  
										 	this.xslMap[i+1], true, false);
			job.setPageFormat(exportImagesFormat);
			job.showPreview(false);
			if(!addJob(job)){
				return Status.CANCEL_STATUS;
			}
			monitor.worked(XSL_WORK);
		}
		for(int i= 0;i<this.xstpaXslMap.length && this.exportExtendedIMGs;i+=2){
			fileName = this.xstpaXslMap[i] +".png";
			ExportJob job = new ExportJob(getProjectID(), getJobName(fileName), 
											this.dir+ EX_IMAGE_DIR + File.separator + fileName,  
										 	this.xstpaXslMap[i+1], true, false);
			job.setPageFormat(exportExtendedIMGsFormat);
			job.showPreview(false);
			if(!addJob(job)){
				return Status.CANCEL_STATUS;
			}
			monitor.worked(XSL_WORK);
		}
		if(this.exportImages || this.exportReport){
			String csPath = this.dir+ IMAGE_DIR + File.separator + Messages.ControlStructure +".png";
			String csPMPath = this.dir+ IMAGE_DIR + File.separator + Messages.ControlStructureDiagramWithProcessModel +".png";
			CSExportJob job = new CSExportJob(csPath, 5	, CSEditor.ID, getProjectID(), false,this.decorateCS);
			CSExportJob pmJob = new CSExportJob(csPMPath, 5	, CSEditorWithPM.ID, getProjectID(), false,this.decorateCS);
			
			if(!addJob(job)){
				return Status.CANCEL_STATUS;
			}
			if(!addJob(pmJob)){
				return Status.CANCEL_STATUS;
			}
			monitor.worked(CS_IMG_WORK);
		}
		for(int i= 0;i<this.xslMap.length && this.exportPDFs;i+=2){
			ExportJob pdfJob = new ExportJob(getProjectID(), "Expoting " +this.xslMap[i] +".pdf",
											this.dir + PDF_DIR + File.separator + this.xslMap[i] +".pdf", //$NON-NLS-1$
											this.xslMap[i+1], true, false);
			pdfJob.showPreview(false);
			pdfJob.setPageFormat(exportPDFsFormat);
			if(!addJob(pdfJob)){
				return Status.CANCEL_STATUS;
			}
			monitor.worked(XSL_WORK);
		}
		for(int i= 0;i<this.xstpaXslMap.length && this.exportExtendedPDFs;i+=2){
			ExportJob pdfJob = new ExportJob(getProjectID(), "Expoting " +this.xstpaXslMap[i] +".pdf",
											this.dir + EX_PDF_DIR + File.separator + this.xstpaXslMap[i] +".pdf", //$NON-NLS-1$
											this.xstpaXslMap[i+1], true, false);
			pdfJob.setPageFormat(exportExtendedPDFsFormat);
			pdfJob.showPreview(false);
			if(!addJob(pdfJob)){
				return Status.CANCEL_STATUS;
			}
			monitor.worked(XSL_WORK);
		}
		if(this.exportReport){
			ExportJob pdfRepJob = new ExportJob(getProjectID(), getJobName("Final Report"),
					this.dir + getName()+".pdf", //$NON-NLS-1$
					"/fopxsl.xsl", true, false); //$NON-NLS-1$
			pdfRepJob.setCSDirty();
			pdfRepJob.setPageFormat(exportReportFormat);
			pdfRepJob.showPreview(false);
			if(!addJob(pdfRepJob)){
				return Status.CANCEL_STATUS;
			}
			monitor.worked(REPORT_WORK);
		}
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

	/**
	 * @return the work that has to be done by this job
	 */
	private int calcWork(){
		int totalWork = 0;
		for(int i= 0;i<ICSVExportConstants.STEPS.size() && this.exportCSVs;i+=2){
			totalWork += CSV_WORK;
		}
		if(this.exportExtendedCSVs){
			totalWork += CSV_WORK;
		}
		for(int i= 0;i<this.xslMap.length && this.exportImages;i+=2){
			totalWork += XSL_WORK;
		}
		for(int i= 0;i<this.xstpaXslMap.length && this.exportExtendedIMGs;i+=2){
			totalWork += XSL_WORK;
		}
		if(this.exportImages || this.exportReport){
			totalWork += CS_IMG_WORK;
		}
		for(int i= 0;i<this.xslMap.length && this.exportPDFs;i+=2){
			totalWork += XSL_WORK;
		}
		for(int i= 0;i<this.xstpaXslMap.length && this.exportExtendedPDFs;i+=2){
			totalWork += XSL_WORK;
		}
		if(this.exportReport){
			totalWork += REPORT_WORK;
		}
		return totalWork;
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
							.getDataModel(getProjectID()).prepareForSave();
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
	public void exportPDFs(String format) {
		this.exportPDFs = !format.equals(AbstractExportPage.NON);
		this.exportPDFsFormat = format;
		
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
	public void exportImages(String format) {
		this.exportImages = !format.equals(AbstractExportPage.NON);
		this.exportImagesFormat = format;
	}

	/**
	 * set whether or not the final STPA report should be included in the export
	 * 
	 * @author Lukas Balzer
	 *
	 * @param choice the choice whether or not to export the report
	 */
	public void exportReport(String format) {
		this.exportReport = !format.equals(AbstractExportPage.NON);
		this.exportReportFormat = format;
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

	/**
	 * @param exportExtendedCSVs the exportExtendedCSVs to set
	 */
	public void setExportExtendedCSVs(boolean exportExtendedCSVs) {
		this.exportExtendedCSVs = exportExtendedCSVs;
	}

	/**
	 * @param exportExtendedPDFs the exportExtendedPDFs to set
	 */
	public void setExportExtendedPDFs(String format) {
		this.exportExtendedPDFs = !format.equals(AbstractExportPage.NON);
		this.exportExtendedPDFsFormat = format;
	}
	
	public void setExportExtendedIMGs(String format) {
		this.exportExtendedIMGs = !format.equals(AbstractExportPage.NON);
		this.exportExtendedIMGsFormat = format;
	}

	/**
	 * @return the projectID
	 */
	public UUID getProjectID() {
		return this.projectID;
	}
}
