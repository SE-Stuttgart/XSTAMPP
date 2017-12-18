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

import java.util.Map;
import java.util.Observable;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.util.XstamppJob;

public class STPAStatisticsJob extends XstamppJob {

  private DataModelController controller;
  private Workbook workbook;
  private Map<Severity, Integer> sc_per_acc;
  private Map<Severity, Integer> cf_per_uca;
  private int uca_per_ca;

  public STPAStatisticsJob(DataModelController controller) {
    super("Exporting Statistics of " + controller.getProjectName());
    this.controller = controller;
  }

  public void setSc_per_acc(Map<Severity, Integer> sc_per_acc) {
    this.sc_per_acc = sc_per_acc;
  }

  public void setUca_per_ca(int uca_per_ca) {
    this.uca_per_ca = uca_per_ca;
  }

  public void setCf_per_uca(Map<Severity, Integer> cf_per_uca) {
    this.cf_per_uca = cf_per_uca;
  }

  @Override
  protected Observable getModelObserver() {
    return this.controller;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    ImageProvider imageProvider = new ImageProvider(controller);
    Workbook wb = new XSSFWorkbook();
    Sheet ws = wb.createSheet("Total");
    Sheet hazardTotalSheet = wb.createSheet("Hazard Progress");
    AbstractProgressSheetCreator.initMap();
    new Step0Progress(wb, controller, sc_per_acc).createSheet("STPA Step 0");
    new Step1Progress(wb, controller, uca_per_ca).createSheet("STPA Step 1");
    new Step2Progress(wb, controller, cf_per_uca).createSheet("STPA Step 2");
    new Step1HazardProgress(wb, controller).createSheet("STPA Step 1 Hazard Centered");
    new Step2HazardProgress(wb, controller, cf_per_uca).createSheet("STPA Step 2 Hazard Centered");
    new NumbersSheet(wb, controller, sc_per_acc, uca_per_ca, cf_per_uca).createSheet("Definitions");
    new SeveritySheet(wb, controller, null).createSheet("Severity Definitions");
    insertImage(wb, ws, imageProvider.createStepBars(600, 400), 1, 1);
    insertImage(wb, ws, imageProvider.createTotalBar(600, 200), 11, 10);
    insertImage(wb, hazardTotalSheet, imageProvider.createHazardBars(600, 400, 5), 1, 10);
    setWorkbook(wb);
    return Status.OK_STATUS;
  }

  private void insertImage(Workbook wb, Sheet ws, byte[] image, int col, int row) {
    int pictureIdx = wb.addPicture(image, Workbook.PICTURE_TYPE_PNG);
    Drawing<?> drawing = ws.createDrawingPatriarch();
    CreationHelper helper = wb.getCreationHelper();
    ClientAnchor anchor = helper.createClientAnchor();
    anchor.setCol1(col);
    anchor.setRow1(row);

    Picture picture = drawing.createPicture(anchor, pictureIdx);
    picture.resize();
  }

  public Workbook getWorkbook() {
    return workbook;
  }

  public void setWorkbook(Workbook workbook) {
    this.workbook = workbook;
  }
}
