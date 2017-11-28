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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;

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
import xstampp.util.XstamppJob;

public class STPAStatisticsJob extends XstamppJob {

  private DataModelController controller;
  private Workbook workbook;

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
    Sheet ws = wb.createSheet("sheet0");
    AbstractProgressSheetCreator.initMap();
    new Step0Progress(wb, controller).createSheet("STPA Step 0");
    new Step1Progress(wb, controller).createSheet("STPA Step 1");
    new Step1HazardProgress(wb, controller).createSheet("STPA Step 1 Hazard Centered");
    new Step2Progress(wb, controller).createSheet("STPA Step 2");
    new Step2HazardProgress(wb, controller).createSheet("STPA Step 2 Hazard Centered");
    // PaletteData palette = new PaletteData(1, 1, 1);
    // ImageData data = new ImageData(600, 400, 8, palette);
    // Image img = new Image(null, data);
    BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, 600, 400);
    graphics.setColor(Color.BLACK);
    graphics.drawPolyline(new int[] { 50, 50, 450 }, new int[] { 50, 350, 350 }, 3);
    try (ByteArrayOutputStream baps = new ByteArrayOutputStream();) {
      ImageIO.write(image, "png", baps);
      int pictureIdx = wb.addPicture(baps.toByteArray(), Workbook.PICTURE_TYPE_PNG);
      Drawing<?> drawing = ws.createDrawingPatriarch();
      CreationHelper helper = wb.getCreationHelper();
      ClientAnchor anchor = helper.createClientAnchor();
      anchor.setCol1(1);
      anchor.setRow1(1);

      Picture picture = drawing.createPicture(anchor, pictureIdx);
      picture.resize();
    } catch (IOException e) {
      e.printStackTrace();
    }
    setWorkbook(wb);
    return Status.OK_STATUS;
  }

  public Workbook getWorkbook() {
    return workbook;
  }

  public void setWorkbook(Workbook workbook) {
    this.workbook = workbook;
  }
}
