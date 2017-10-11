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
    
    new Step0Progress(wb, controller).createStep1Sheet();

    // Write the output to a file

    String fileName = "state.xls";
    if (wb instanceof XSSFWorkbook) {
      fileName += "x";
    }
    try (FileOutputStream out = new FileOutputStream(fileName);) {
      wb.write(out);
      out.close();
      wb.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    File file = new File(fileName);
    if (file.exists() && file.canWrite()) {
      if (Desktop.isDesktopSupported()) {
        try {
          Desktop.getDesktop().open(file);
        } catch (IOException e) {
          return Status.CANCEL_STATUS;
        }
      }
    }
    return Status.OK_STATUS;
  }
}
