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
package xstampp.astpa.wizards;

import java.io.File;
import java.io.IOException;

import messages.Messages;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import xstampp.astpa.Activator;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.util.jobs.ExportJob;
import xstampp.astpa.util.jobs.StpaCSVExport;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.wizards.CSVExportPage;
import xstampp.ui.wizards.IExportPage;
import xstampp.util.ExportPackage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public abstract class AbstractExportWizard extends Wizard implements
    IExportWizard {

  private final IPreferenceStore store = Activator.getDefault()
      .getPreferenceStore();
  private IExportPage exportPage;
  private String[] viewId;
  private Object exportAddition;

  private enum Error {
    OK, CANT_OVERWRITE, EXIT, CANT_FIND;
  }

  /**
   * @author Lukas Balzer
   */
  public AbstractExportWizard() {
    this(new String[] { "" }); //$NON-NLS-1$

  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param viewId
   *          the id of ther view for which the export shall be executed
   */
  public AbstractExportWizard(String viewId) {
    this(new String[] { viewId });
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param viewId
   *          the id of ther view for which the export shall be executed
   */
  public AbstractExportWizard(String[] viewId) {
    super();
    this.setHelpAvailable(true);
    this.viewId = viewId;
    this.exportAddition = null;
  }

  protected boolean performCSVExport(int data) {

    String filePath = this.exportPage.getExportPath();
    try {
      if (this.checkError(this.checkPath(filePath))) {
        IDataModel model = ProjectManager.getContainerInstance()
            .getDataModel(this.exportPage.getProjectID());
        StpaCSVExport export = new StpaCSVExport("Export CSV", filePath,
            ((CSVExportPage) this.exportPage).getSeperator(),
            model, data);
        export.schedule();
      } else {
        return false;
      }
    } catch (IOException e) {
      MessageDialog.openWarning(this.getShell(), Messages.Warning,
          Messages.ChooseTheDestination);
      return false;
    }
    return true;
  }

  protected boolean performXSLExport(String fopName, String jobMessage,
      boolean forceCSDeco) {
    return performXSLExport(fopName, forceCSDeco, "", true);
  }

  protected boolean performXSLExport(String fopName, boolean forceCSDeco,
      String pdfTitle, boolean buildCSImage) {
    String jobMessage = String.format(Messages.Exporting_Format, pdfTitle);
    String filePath = this.getExportPage().getExportPath();
    if ((filePath != null) && !filePath.isEmpty()) {
      File newPDF = new File(filePath);
      if (newPDF.isFile()
          && !MessageDialog.openConfirm(this.getShell(),
              Messages.Warning, String.format(
                  Messages.DoYouReallyWantToOverwriteTheContentAt,
                  newPDF.getName()))) {
        return false;
      }

      try {
        newPDF.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }

      if (!newPDF.canWrite()) {
        MessageDialog.openWarning(this.getShell(), Messages.Warning,
            Messages.CantOverride);
        return false;
      }
      String mimeType = ProjectManager.getContainerInstance().getMimeConstant(filePath);
      if (mimeType == null) {
        return false;
      }
      ExportPackage data = new ExportPackage(jobMessage, filePath, fopName, getExportModel());
      ExportJob exportJob = new ExportJob(this.getExportPage().getProjectID(),
          data, true, forceCSDeco);
      exportJob.setPdfTitle(pdfTitle);
      exportJob.setTextSize(getExportPage().getContentSize());
      exportJob.setTableHeadSize(getExportPage().getHeadSize());
      exportJob.setTitleSize(getExportPage().getTitleSize());
      if (getExportPage().getPageFormat() != null) {
        exportJob.setPageFormat(getExportPage().getPageFormat());
      }
      exportJob.addJobChangeListener(new ExportJobChangeAdapter());
      if (buildCSImage) {
        exportJob.setCSDirty();
      }
      exportJob.setExportAddition(exportAddition);
      ProjectManager.getContainerInstance().getDataModel(this.getExportPage().getProjectID())
          .prepareForExport();
      exportJob.schedule();
    } else {
      MessageDialog.openWarning(this.getShell(), Messages.Warning,
          Messages.ChooseTheDestination);
      return false;
    }
    return true;
  }

  protected Class<?> getExportModel() {
    return DataModelController.class;
  }

  /**
   * Parse string to RGB Object
   * 
   * @author Sebastian Sieber
   * 
   * @param rgbString
   *          String
   * @return RGB
   */
  protected RGB toRGB(String rgbString) {
    int rgbRed = Integer.parseInt(rgbString.substring(
        rgbString.indexOf("{") + 1, rgbString.indexOf(",")).trim()); //$NON-NLS-1$ //$NON-NLS-2$
    int rgbGreen = Integer.parseInt(rgbString.substring(
        rgbString.indexOf(",") + 1, rgbString.lastIndexOf(",")).trim()); //$NON-NLS-1$ //$NON-NLS-2$
    int rgbBlue = Integer.parseInt(rgbString.substring(
        rgbString.lastIndexOf(",") + 1, rgbString.indexOf("}")).trim()); //$NON-NLS-1$ //$NON-NLS-2$
    RGB rgbCompanyColor = new RGB(rgbRed, rgbGreen, rgbBlue);
    return rgbCompanyColor;
  }

  protected Error checkPath(String filePath) throws IOException {
    if ((filePath == null) || filePath.isEmpty()) {
      return Error.CANT_FIND;
    }
    File file = new File(filePath);
    if (!file.exists() && file.createNewFile()) {
      return Error.OK;
    }
    if (MessageDialog.openQuestion(
        this.getShell(),
        Messages.Warning,
        String.format(Messages.DoYouReallyWantToOverwriteTheContentAt,
            file.getName()))) {
      if (!file.delete()) {
        return Error.CANT_OVERWRITE;
      }
      file.createNewFile();
    } else {
      return Error.EXIT;
    }
    return Error.OK;

  }

  protected boolean checkError(Error err) {
    switch (err) {
    case CANT_FIND:
      MessageDialog.openWarning(this.getShell(), Messages.Warning,
          Messages.ChooseTheDestination);
      break;
    case CANT_OVERWRITE:

      MessageDialog.openWarning(this.getShell(), Messages.Warning,
          Messages.CantOverride);
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
   * @param exportPage
   *          the exportPage to set
   */
  public void setExportPage(IExportPage exportPage) {
    this.exportPage = exportPage;
    setWindowTitle(exportPage.getTitle());
  }

  protected void setExportedViews(String[] ids) {
    this.viewId = ids;
  }

  protected String[] getExportedViews() {
    return this.viewId;
  }

  /**
   * @param exportAddition
   *          the exportAddition to set
   */
  public void setExportAddition(Object exportAddition) {
    this.exportAddition = exportAddition;
  }

  protected class ExportJobChangeAdapter extends JobChangeAdapter {
    public ExportJobChangeAdapter() {
      // TODO Auto-generated constructor stub
    }

    @Override
    public void scheduled(IJobChangeEvent event) {

      super.scheduled(event);
    }

    @Override
    public void aboutToRun(IJobChangeEvent event) {
      super.aboutToRun(event);
    }

    @Override
    public void done(final IJobChangeEvent event) {
      if (event.getResult().isOK()) {
        Display.getDefault().syncExec(new Runnable() {

          @Override
          public void run() {

            ProjectManager.getContainerInstance().callObserverValue(
                ObserverValue.EXPORT_FINISHED);
            ProjectManager.getContainerInstance()
                .getDataModel(((ExportJob) event.getJob()).getProjectId())
                .prepareForSave();
          }
        });
        super.done(event);
      }
    }

  }
}