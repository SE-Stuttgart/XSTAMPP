/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.util.jobs;

import java.io.File;
import java.util.Observable;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.util.ExportPackage;
import xstampp.util.JAXBExportJob;

/**
 * Eclipse job that handles the export, by extending the JaxbExportJob defined in xstampp with the
 * additional creation of a temporary image of the control structure and the control structure with
 * process model therefore the model is assumed to be an IControlStructureDataModel
 * <p/>
 * <i>possible substitutes should also be aware that the function</i> {@link #canExport()}<br>
 * <i>should be overwritten as well to prevent class cast a exception</i>
 * 
 * @author Fabian Toth,Lukas Balzer
 * @since 2.0
 * 
 * @see IControlStructureDataModel
 * 
 */
public class ExportJob extends JAXBExportJob {

  private final boolean decorate;
  private String imgPath;
  private boolean isCsDirty;
  private File csPath;
  private File csPmPath;
  private UUID projectId;

  /**
   * the xslfoTransormer is beeing related to the xsl which describes the pdf export
   */

  /**
   * Constructor of the export job
   * 
   * @author Fabian Toth, Lukas Balzer
   * @param projectId
   *          the project which
   * @param name
   *          the name of the job
   * @param filePath
   *          the path to the pdf file
   * @param xslName
   *          the name of the file in which the xsl file is stored which should be used
   * @param asOne
   *          true if all content shall be exported on a single page
   * @param decorate
   *          if the control structure components should be pictured with colored borders and image
   *          labels
   */
  public ExportJob(UUID projectId, ExportPackage data, boolean asOne, boolean decorate) {
    super(data);
    this.projectId = projectId;
    this.decorate = decorate;
    this.isCsDirty = false;
  }

  public void setCSImagePath(String path) {
    File pathFile = new File(path);
    if (pathFile.exists()) {
      this.imgPath = path;
    }
  }

  /**
   * @return The class with which the jaxb instance is created
   */
  protected JAXBContext getModelContent() throws JAXBException {
    return JAXBContext.newInstance(getExportData().getDataModelClazz());
  }

  public void setCSDirty() {
    this.isCsDirty = true;
  }

  protected boolean canExport() {
    return ((DataModelController) getModel()).getExportInfo() == null;

  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);

    while (canExport()) {
      if (monitor.isCanceled()) {
        return Status.CANCEL_STATUS;
      }
    }
    this.imgPath = new File(this.getFilePath()).getParent();
    if (this.isCsDirty) {
      this.csPath = new File(this.imgPath + File.separator + getThread().getId() + "cs.png");
      this.csPmPath = new File(this.imgPath + File.separator + getThread().getId() + "cspm.png");
      CSExportJob csExport = new CSExportJob(this.csPath.getPath(), CSEditor.ID, projectId, 10,
          this.decorate);
      csExport.setConstraint(getPageFormat());
      CSExportJob csPmExport = new CSExportJob(this.csPmPath.getPath(), CSEditorWithPM.ID,
          projectId, 10, this.decorate);
      csPmExport.setConstraint(getPageFormat());

      csExport.getPrintableRoot();
      csPmExport.getPrintableRoot();

      ((IControlStructureEditorDataModel) getModel()).setCSImagePath(this.csPath.getPath());
      ((IControlStructureEditorDataModel) getModel()).setCSPMImagePath(this.csPmPath.getPath());
    }

    return super.run(monitor);
  }

  @Override
  protected Object getModel() {
    return ProjectManager.getContainerInstance().getDataModel(projectId);
  }

  @Override
  protected Observable getModelObserver() {
    return (Observable) getModel();
  }

  public UUID getProjectId() {
    return this.projectId;
  }

  @Override
  protected void canceling() {
    if (this.csPath != null) {
      ExportJob.this.csPath.deleteOnExit();
    }
    if (this.csPmPath != null) {
      this.csPmPath.deleteOnExit();
    }
    super.canceling();
  }

  @Override
  public void done(IJobChangeEvent event) {
    if (this.csPath != null && this.csPath.exists()) {
      this.csPath.delete();
    }
    if (this.csPmPath != null && this.csPmPath.exists()) {
      this.csPmPath.delete();
    }
    super.done(event);
  }
}
