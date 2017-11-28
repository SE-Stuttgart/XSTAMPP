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
package xstampp.astpa.util.commands;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.ui.StatisticsView;
import xstampp.astpa.util.jobs.statistics.STPAStatisticsJob;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;

public class STPAProjectStateExportCommand extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Object currentSelection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getSelection(ProjectExplorer.ID); // $NON-NLS-1$

    // if the currentSelection is a stepSelector than it is transfered in a
    // proper object
    if (currentSelection instanceof IProjectSelection
        && ((IProjectSelection) currentSelection).getProjectData() instanceof DataModelController) {
      IProjectSelection selector = ((IProjectSelection) currentSelection);
      STPAStatisticsJob job = new STPAStatisticsJob(
          (DataModelController) selector.getProjectData());

      final boolean storeAsExcel = false;
      job.addJobChangeListener(new JobChangeAdapter() {
        @Override
        public void done(IJobChangeEvent event) {
          Workbook wb = ((STPAStatisticsJob) event.getJob()).getWorkbook();
          if (event.getResult().isOK() && !storeAsExcel) {

            PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
              try {
                IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView("xstampp.astpa.view.statistics");
                ((StatisticsView) view).setWorkbook(wb);
              } catch (PartInitException e2) {
                // TODO Auto-generated catch block
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
    return false;
  }

  private void store(Workbook wb) {
    FileDialog diag = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.SAVE);
    diag.setFileName("STPA state");
    diag.setFilterExtensions(new String[] { "*.xls" });
    if (wb instanceof XSSFWorkbook) {
      diag.setFilterExtensions(new String[] { "*.xlsx" });
    }
    String fileName = diag.open();
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
}
