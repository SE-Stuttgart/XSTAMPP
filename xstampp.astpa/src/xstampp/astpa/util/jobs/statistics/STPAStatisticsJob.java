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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
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
import xstampp.astpa.model.interfaces.Severity;
import xstampp.astpa.util.jobs.statistics.AbstractProgressSheetCreator.STEP;
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
    Workbook wb = new XSSFWorkbook();
    Sheet ws = wb.createSheet("Total");
    AbstractProgressSheetCreator.initMap();
    new Step0Progress(wb, controller, sc_per_acc).createSheet("STPA Step 0");
    new Step1Progress(wb, controller, uca_per_ca).createSheet("STPA Step 1");
    new Step1HazardProgress(wb, controller).createSheet("STPA Step 1 Hazard Centered");
    new Step2Progress(wb, controller, cf_per_uca).createSheet("STPA Step 2");
    new Step2HazardProgress(wb, controller, cf_per_uca).createSheet("STPA Step 2 Hazard Centered");
    insertImage(wb, ws, createBars(600, 400), 1, 1);
    insertImage(wb, ws, createTotalBar(600, 200), 11, 10);
    setWorkbook(wb);
    return Status.OK_STATUS;
  }

  private BufferedImage createTotalBar(int width, int height) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, width, height);
    graphics.setColor(Color.BLACK);
    Rectangle rect = new Rectangle(50, 50, width - 100, height - 100);
    drawHeading(graphics, "Total Progress", rect);
    graphics.drawPolyline(new int[] { rect.x, rect.x, rect.x + rect.width },
        new int[] { rect.y, rect.y + rect.height, rect.y + rect.height }, 3);
    float progress = (AbstractProgressSheetCreator.getProgress(controller.getProjectId(), 3, STEP.TOTAL));

    String progressString = String.format("%.1f", progress) + "%";
    int stringWidth = graphics.getFontMetrics().stringWidth(progressString);
    int barHeight = (int) (rect.height * 0.8);
    int barWidth = (int) (rect.width * progress / 100);
    int barY = rect.y + (int) (rect.height * 0.1);
    graphics.setColor(Color.gray);
    graphics.fillRect(rect.x, barY, barWidth, barHeight);
    int stringY = (int) (barY + barHeight * 0.5 - graphics.getFontMetrics().getHeight() / 2);
    int stringX = rect.x + barWidth;
    if (stringWidth < barWidth) {
      graphics.setColor(Color.WHITE);
      stringX = rect.x;
    }
    graphics.drawString(progressString, stringX, stringY);

    return image;
  }

  private BufferedImage createBars(int width, int height) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, width, height);
    graphics.setColor(Color.BLACK);

    Rectangle rect = new Rectangle(50, 50, width - 100, height - 100);
    drawHeading(graphics, "per Step Progress", rect);
    graphics.drawPolyline(new int[] { rect.x, rect.x, rect.x + rect.width },
        new int[] { rect.y, rect.y + rect.height, rect.y + rect.height }, 3);
    graphics.drawString("100%", 5, 50);
    graphics.drawLine(rect.x, rect.y, rect.x - 5, rect.y);
    graphics.drawString("  0%", 5, 50 + rect.height);
    graphics.drawLine(rect.x, rect.y + rect.height, rect.x - 5, rect.y + rect.height);
    Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
    graphics.setStroke(dashed);
    for (float factor = 0; factor < 1; factor += 0.25) {
      int lineY = (int) (rect.y + (rect.height * factor));
      graphics.drawLine(rect.x, lineY, rect.x + rect.width, lineY);
    }
    drawBar(graphics, rect, STEP.STEP_0, 1, 30);
    drawBar(graphics, rect, STEP.STEP_1, 2, 30);
    drawBar(graphics, rect, STEP.STEP_2, 3, 30);
    return image;
  }

  private void drawBar(Graphics2D graphics, Rectangle rect, STEP step, int nr, int barWidth) {
    int stepBarPosition = rect.x + nr * rect.width / 4;
    float progress = (AbstractProgressSheetCreator.getProgress(controller.getProjectId(), 1, step)) / 100;
    int heightOffset = (int) (rect.height * (1 - progress));
    Color color = progress < 0.75 ? Color.yellow : Color.green;
    color = progress < 0.5 ? Color.orange : color;
    color = progress < 0.25 ? Color.red : color;
    Color colorBefore = graphics.getColor();
    graphics.setColor(color);
    graphics.fillRect(stepBarPosition - barWidth / 2, rect.y + heightOffset, barWidth,
        rect.height - heightOffset);
    graphics.setColor(colorBefore);

    int fontHeight = graphics.getFontMetrics().getHeight();
    int labelWidth = graphics.getFontMetrics().stringWidth(step.getLabel());
    graphics.drawString(step.getLabel(), stepBarPosition - labelWidth / 2,
        rect.y + rect.height + fontHeight);

  }

  private void insertImage(Workbook wb, Sheet ws, BufferedImage image, int col, int row) {
    try (ByteArrayOutputStream baps = new ByteArrayOutputStream();) {
      ImageIO.write(image, "png", baps);
      int pictureIdx = wb.addPicture(baps.toByteArray(), Workbook.PICTURE_TYPE_PNG);
      Drawing<?> drawing = ws.createDrawingPatriarch();
      CreationHelper helper = wb.getCreationHelper();
      ClientAnchor anchor = helper.createClientAnchor();
      anchor.setCol1(col);
      anchor.setRow1(row);

      Picture picture = drawing.createPicture(anchor, pictureIdx);
      picture.resize();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void drawHeading(Graphics2D graphics, String text, Rectangle rect) {
    double width = rect.getWidth() / 2;
    width -= graphics.getFontMetrics().stringWidth(text) / 2;
    Font font = graphics.getFont();
    graphics.setFont(new Font("Arial", Font.BOLD, 12));
    graphics.drawString(text, (int) width, 30);
    graphics.setFont(font);
  }

  public Workbook getWorkbook() {
    return workbook;
  }

  public void setWorkbook(Workbook workbook) {
    this.workbook = workbook;
  }
}
