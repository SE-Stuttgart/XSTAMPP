/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributor: Lukas Balzer - initial code contribution
 *******************************************************************************/

package xstampp.astpa.util.jobs.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import xstampp.astpa.model.DataModelController;

abstract class AbstractProgressSheetCreator {
  private StyleFactory factory;

  enum Styles {
    HEADER_STYLE, DEFAULT_STYLE
  }

  private DataModelController controller;

  enum STEP {
    STEP_0, STEP_1, STEP_2
  }

  private static Map<STEP, Map<UUID, List<Float>>> progressMap;

  public AbstractProgressSheetCreator(Workbook wb, DataModelController controller) {
    this.controller = controller;
    this.factory = new StyleFactory(wb);
  }

  static void initMap() {
    if (progressMap == null) {
      progressMap = new HashMap<>();
      progressMap.put(STEP.STEP_0, new HashMap<>());
      progressMap.put(STEP.STEP_1, new HashMap<>());
      progressMap.put(STEP.STEP_2, new HashMap<>());
    }
  }

  /**
   * 
   * @param step
   *          one of the enum values stored in {@link STEP}
   * @param entryId
   *          the id for which this progress value should be added
   * @param progress
   *          a {@link Float} in <code>[0,100]</code>
   */
  void addProgress(STEP step, UUID entryId, Float progress) {
    assert progress >= 0 && progress <= 100;
    if (!progressMap.get(step).containsKey(entryId)) {
      progressMap.get(step).put(entryId, new ArrayList<>());
    }
    progressMap.get(step).get(entryId).add(progress);
  }

  /**
   * 
   * @param step
   *          one of the enum values stored in {@link STEP}
   * @param entryId
   *          the id for which the total stored progress should be calculated
   * @param constraint
   *          defines the minimum number of progress values that must be stored for the given
   *          entryId if the constraint isn't reached by the stored lists length a zero value is
   *          assumed, must be >= 0
   * @return the progress that is a {@link Float} in <code>[0,100]</code>
   */
  Float getProgress(STEP step, UUID entryId, int constraint) {
    assert constraint >= 0;
    if (progressMap.get(step).containsKey(entryId)) {
      int size = progressMap.get(step).get(entryId).size();
      float factor = 1 / (float) Math.max(constraint, size);
      Float total = 0f;
      for (Float progress : progressMap.get(step).get(entryId)) {
        total += factor * progress;
      }
      return total;
    }
    return 0f;
  }

  Cell createCell(Row hazRow, int i, String content, Styles style) {
    Cell cell = hazRow.createCell(i);
    cell.setCellValue(content);
    cell.setCellStyle(this.factory.getStyle(style));
    return cell;
  }

  Cell createCell(Row hazRow, int i, String content) {
    return createCell(hazRow, i, content, Styles.DEFAULT_STYLE);
  }

  void createCells(Row hazRow, int colIndex, String content, int count) {
    for (int i = 0; i < count; i++) {
      createCell(hazRow, i + colIndex, content);
    }
  }

  void createCells(Row row, int rowIndex, String[] titles, Styles style) {
    for (int i = 0; i < titles.length; i++) {
      createCell(row, i, titles[i], style);
    }
  }

  Row createRow(Sheet sheet, int rowIndex) {
    Row hazRow = sheet.createRow(rowIndex);
    hazRow.setHeightInPoints(10f);
    hazRow.setHeight((short) -1);
    return hazRow;
  }

  public final Sheet createSheet(String name) {
    Sheet sheet = getFactory().wb.createSheet(name);

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

    createWorkSheet(sheet);
    return sheet;
  }

  class StyleFactory {

    private Map<Styles, CellStyle> styles;
    Workbook wb;

    private StyleFactory(Workbook wb) {
      this.wb = wb;
      this.styles = new HashMap<>();
    }

    CellStyle getStyle(Styles styleConstant) {
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
        return this.styles.get(styleConstant);
      }
    }
  }

  DataModelController getController() {
    return controller;
  }

  StyleFactory getFactory() {
    return factory;
  }

  protected void mergeRows(Sheet sheet, int start, int end, int[] cells) {
    if (end > start) {
      for (int i : cells) {
        sheet.addMergedRegion(new CellRangeAddress(start, end, i, i));
      }
    }
  }

  protected abstract void createWorkSheet(Sheet sheet);
}
