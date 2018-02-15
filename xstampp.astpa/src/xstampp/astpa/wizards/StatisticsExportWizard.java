/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.wizards;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.Activator;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.util.jobs.statistics.STPAStatisticsJob;
import xstampp.astpa.wizards.pages.StatisticsExportPage;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;

public class StatisticsExportWizard extends Wizard implements INewWizard {

  private StatisticsExportPage page;

  public StatisticsExportWizard() {
    super();
    page = new StatisticsExportPage("Export Statistics", Activator.PLUGIN_ID);
    addPage(page);
  }

  @Override
  public boolean performFinish() {
    IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(this.page.getProjectID());
    // if the currentSelection is a stepSelector than it is transfered in a
    // proper object
    if (dataModel instanceof DataModelController) {
      STPAStatisticsJob job = new STPAStatisticsJob((DataModelController) dataModel);
      job.setCf_per_uca(this.page.getCf_per_uca());
      job.setUca_per_ca(page.getUca_per_ca());
      job.setSc_per_acc(page.getSc_per_acc());
      job.addJobChangeListener(new JobChangeAdapter() {
        @Override
        public void done(IJobChangeEvent event) {
          Workbook wb = ((STPAStatisticsJob) event.getJob()).getWorkbook();
          if (event.getResult().isOK() && !page.isUseExport()) {

            PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
              try {
                IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("xstampp.astpa.view.statistics");
                ((StatisticsView) view).setWorkbook(wb);
              } catch (PartInitException e2) {
                e2.printStackTrace();
              }
            });

          } else if (event.getResult().isOK()) {
            PlatformUI.getWorkbench().getDisplay().asyncExec(() -> store(wb));
          }
        }
      });

      job.schedule();
    }
    return true;
  }

  private void store(Workbook wb) {
    String fileName = this.page.getExportPath();
    if (fileName != null) {
      try (FileOutputStream out = new FileOutputStream(fileName);) {
        wb.write(out);
        out.close();
        wb.close();
      } catch (FileNotFoundException e2) {
        e2.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      File file = new File(fileName);
      if (file.exists() && file.canWrite()) {
        if (Desktop.isDesktopSupported()) {
          try {
            Desktop.getDesktop().open(file);
          } catch (IOException e3) {

          }
        }
      }
    }
  }

  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    // TODO Auto-generated method stub

  }

}
