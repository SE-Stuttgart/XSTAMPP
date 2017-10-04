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

package xstampp.astpa.util.jobs;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import messages.Messages;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.EntryWithSeverity;
import xstampp.astpa.model.hazacc.Accident;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.model.ObserverValue;
import xstampp.util.STPAPluginUtils;
import xstampp.util.XstamppJob;

public class STPAStatisticsJob extends XstamppJob {

  private StyleFactory defaultFactory;

  private enum Styles {
    HEADER_STYLE, DEFAULT_STYLE
  }

  private static final String[] titles = new String[] { "HazId", "Hazard", "Severity", "AccId", "Accident",
      "SCId", "Safety Constraint","Completion[%]" };
  private DataModelController controller;

  public STPAStatisticsJob(DataModelController controller) {
    super("Exporting Statistics of " + controller.getProjectName());
    this.controller = controller;
  }

  @Override
  protected Observable getModelObserver() {
    // TODO Auto-generated method stub
    return this.controller;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    Workbook wb = new XSSFWorkbook();
    this.defaultFactory = new StyleFactory(wb);
    // Map<String, CellStyle> styles = createStyles(wb);

    Sheet sheet = wb.createSheet("Business Plan");

    // turn off gridlines
    sheet.setDisplayGridlines(false);
    sheet.setPrintGridlines(false);
    sheet.setFitToPage(true);
    sheet.setHorizontallyCenter(true);
    PrintSetup printSetup = sheet.getPrintSetup();
    printSetup.setLandscape(true);

    // the following three statements are required only for HSSF
    sheet.setAutobreaks(true);
    printSetup.setFitHeight((short) 1);
    printSetup.setFitWidth((short) 1);

    // the header row: centered text in 48pt font
    Row headerRow = sheet.createRow(0);
    headerRow.setHeightInPoints(12.75f);

    for (int i = 0; i < titles.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(titles[i]);
      cell.setCellStyle(this.defaultFactory.getStyle(Styles.HEADER_STYLE));
    }

    Row hazRow;
    int rowIndex = 1;
    for (ITableModel hazModel : controller.getAllHazards()) {
      hazRow = createRow(sheet, rowIndex);
      createCell(hazRow, 0, hazModel.getIdString());
      createCell(hazRow, 1, hazModel.getTitle());
      createCell(hazRow, 2, ((EntryWithSeverity)hazModel).getSeverity().name());
      int hazGroupStart = rowIndex;
      rowIndex = addAccidents(sheet, hazRow, rowIndex, hazModel);
      if (rowIndex - hazGroupStart > 1) {
        sheet.addMergedRegion(new CellRangeAddress(hazGroupStart, rowIndex - 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(hazGroupStart, rowIndex - 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(hazGroupStart, rowIndex - 1, 2, 2));
      }
    }

    sheet.setColumnWidth(1, 256 * 33);
    sheet.setColumnWidth(4, 256 * 33);
    sheet.setColumnWidth(6, 256 * 33);
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

  /**
   * Adds all accidents that are linked to the given {@link ITableModel}
   * 
   * @return the index of the next row (index++)
   */
  private int addAccidents(Sheet sheet, Row hazRow, int rowIndex, ITableModel hazModel) {
    int index = rowIndex;
    for (UUID accId : controller.getLinkController().getLinksFor(ObserverValue.HAZ_ACC_LINK,
        hazModel.getId())) {
      if (rowIndex != index) {
        hazRow = createRow(sheet, index);
        createCell(hazRow, 0, null);
        createCell(hazRow, 1, null);
        createCell(hazRow, 2, null);
      }
      ITableModel accModel = controller.getAccident(accId);
      createCell(hazRow, 3, accModel.getIdString());
      createCell(hazRow, 4, accModel.getTitle());
      int accGroupStart = index;
      index = addSavetyConstraints(sheet, hazRow, index, accId);
      // sheet.addMergedRegion(new CellRangeAddress(accGroupStart, rowIndex, 2, 3));
      index++;
    }
    // if the index didn't change than no accidents have been added and empty cells need to be
    // inserted
    if (rowIndex == index) {
      createCell(hazRow, 3, null);
      createCell(hazRow, 4, null);
      createCell(hazRow, 5, null);
      createCell(hazRow, 6, null);
    }
    return index;
  }

  /**
   * Adds all safety constraints that are linked to the given {@link Accident}'s id
   * 
   * @return the index of the next row (index++)
   */
  private int addSavetyConstraints(Sheet sheet, Row hazRow, int rowIndex, UUID accId) {
    int index = rowIndex;
    for (UUID s0Id : controller.getLinkController().getLinksFor(ObserverValue.ACC_S0_LINK,
        accId)) {
      if (rowIndex != index) {
        hazRow = createRow(sheet, index);
        createCell(hazRow, 0, null);
        createCell(hazRow, 1, null);
        createCell(hazRow, 2, null);
        createCell(hazRow, 3, null);
        createCell(hazRow, 4, null);
      }
      ITableModel s0Model = controller.getSafetyConstraint(s0Id);
      createCell(hazRow, 5, s0Model.getIdString());
      createCell(hazRow, 6, s0Model.getTitle());
      index++;
    }
    // if the index didn't change than no accidents have been added and empty cells need to be
    // inserted
    if (rowIndex == index) {
      createCell(hazRow, 5, null);
      createCell(hazRow, 6, null);
    }
    return index;
  }

  private Cell createCell(Row hazRow, int i, String content, Styles style) {
    Cell cell = hazRow.createCell(i);
    cell.setCellValue(content);
    cell.setCellStyle(this.defaultFactory.getStyle(style));
    return cell;
  }

  private Cell createCell(Row hazRow, int i, String content) {
    return createCell(hazRow, i, content, Styles.DEFAULT_STYLE);
  }

  private Row createRow(Sheet sheet, int rowIndex) {
    Row hazRow = sheet.createRow(rowIndex);
    hazRow.setHeightInPoints(10f);
    hazRow.setHeight((short) -1);
    return hazRow;
  }

  private class StyleFactory {

    private Map<Styles, CellStyle> styles;
    private Workbook wb;

    public StyleFactory(Workbook wb) {
      this.wb = wb;
      this.styles = new HashMap<>();
    }

    private CellStyle getStyle(Styles styleConstant) {
      if (this.styles.containsKey(styleConstant)) {
        return this.styles.get(styleConstant);
      } else {
        CellStyle style = this.wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderBottom(BorderStyle.MEDIUM);
        this.styles.put(Styles.HEADER_STYLE, style);

        style = this.wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        this.styles.put(Styles.DEFAULT_STYLE, style);
        return getStyle(styleConstant);
      }
    }
  }
}
