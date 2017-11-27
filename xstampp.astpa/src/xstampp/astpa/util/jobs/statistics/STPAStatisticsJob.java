/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributor:
 * Lukas Balzer - initial code contribution
 *******************************************************************************/

package xstampp.astpa.util.jobs.statistics;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.model.DataModelController;
import xstampp.util.XstamppJob;

public class STPAStatisticsJob extends XstamppJob {

  private DataModelController controller;

  public STPAStatisticsJob(DataModelController controller) {
    super("Exporting Statistics of " + controller.getProjectName());
    this.controller = controller;
  }

  @Override
  protected Observable getModelObserver() {
    return this.controller;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    Workbook wb = new XSSFWorkbook();
    AbstractProgressSheetCreator.initMap();
    new Step0Progress(wb, controller).createSheet("STPA Step 0");
    new Step1Progress(wb, controller).createSheet("STPA Step 1");
    new Step1HazardProgress(wb, controller).createSheet("STPA Step 1 Hazard Centered");
    new Step2Progress(wb, controller).createSheet("STPA Step 2");
    new Step2HazardProgress(wb, controller).createSheet("STPA Step 2 Hazard Centered");
    // Write the output to a file
    
    PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

      @Override
      public void run() {
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
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          } catch (IOException e1) {
            e1.printStackTrace();
          }
          File file = new File(fileName);
          if (file.exists() && file.canWrite()) {
            if (Desktop.isDesktopSupported()) {
              try {
                Desktop.getDesktop().open(file);
              } catch (IOException e) {

              }
            }
          }
        }
      }
    });
    return Status.OK_STATUS;
  }
}
