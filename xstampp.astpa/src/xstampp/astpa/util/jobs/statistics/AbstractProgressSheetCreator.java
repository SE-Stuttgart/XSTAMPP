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
    HEADER_STYLE, DEFAULT_EVEN, DEFAULT_UNEVEN, TOTAL_STYLE
  }

  private DataModelController controller;

  private STEP defaultStep;
  private Styles defaultStyle = Styles.DEFAULT_UNEVEN;

  enum STEP {
    TOTAL("Total"), STEP_1("Step 1"), STEP_2("Step 2"), STEP_3("Step 3"), STEP_3_HAZARD_CENTERED("Step 3"), STEP_4(
        "Step 4"), STEP_4_HAZARD_CENTERED(
            "Step 4");

    private String label;

    STEP(String name) {
      label = name;
    }

    public String getLabel() {
      return label;
    }
  }

  @FunctionalInterface
  interface SubRowCreator {
    int createSubRows(Row row);
  }

  private static Map<STEP, Map<UUID, List<Float>>> progressMap;

  private Integer columns;

  public AbstractProgressSheetCreator(Workbook wb, DataModelController controller, STEP step) {
    this.controller = controller;
    this.factory = new StyleFactory(wb);
    this.defaultStep = step;
  }

  static void initMap() {
    progressMap = new HashMap<>();
    progressMap.put(STEP.TOTAL, new HashMap<>());
    progressMap.put(STEP.STEP_1, new HashMap<>());
    progressMap.put(STEP.STEP_3, new HashMap<>());
    progressMap.put(STEP.STEP_4, new HashMap<>());
    progressMap.put(STEP.STEP_3_HAZARD_CENTERED, new HashMap<>());
    progressMap.put(STEP.STEP_4_HAZARD_CENTERED, new HashMap<>());

  }

  public static Map<STEP, Map<UUID, List<Float>>> getProgressMap() {
    return progressMap;
  }

  protected void createTotalRow(Sheet sheet, int cellIndex, boolean addToTotal) {
    Row footer = createRow(sheet);
    Float progress = getProgress(getController().getProjectId(), 1);
    if (addToTotal) {
      addProgress(getController().getProjectId(), progress, STEP.TOTAL);
    }
    createCell(footer, cellIndex, String.format("%.1f", progress) + "%", Styles.TOTAL_STYLE);
  }

  public void triggerDefaultStyle() {
    this.defaultStyle = defaultStyle == Styles.DEFAULT_EVEN ? Styles.DEFAULT_UNEVEN : Styles.DEFAULT_EVEN;
  }

  /**
   * @param entryId
   *          the id for which this progress value should be added
   * @param progress
   *          a {@link Float} in <code>[0,100]</code>
   */
  void addProgress(UUID entryId, Float progress) {
    addProgress(entryId, progress, defaultStep);
  }

  /**
   * @param entryId
   *          the id for which this progress value should be added
   * @param progress
   *          a {@link Float} in <code>[0,100]</code>
   */
  static void addProgress(UUID entryId, Float progress, STEP step) {
    assert progress >= 0 && progress <= 100;
    if (!progressMap.get(step).containsKey(entryId)) {
      progressMap.get(step).put(entryId, new ArrayList<>());
    }
    progressMap.get(step).get(entryId).add(progress);
  }

  /**
   * 
   * @param entryId
   *          the id for which the total stored progress should be calculated
   * @param constraint
   *          defines the minimum number of progress values that must be stored for the given
   *          entryId if the constraint isn't reached by the stored lists length a zero value is
   *          assumed, must be >= 0
   * @return the progress that is a {@link Float} in <code>[0,100]</code>
   */
  Float getProgress(UUID entryId, int constraint) {
    return getProgress(entryId, constraint, defaultStep);
  }

  /**
   * 
   * @param entryId
   *          the id for which the total stored progress should be calculated
   * @param constraint
   *          defines the minimum number of progress values that must be stored for the given
   *          entryId if the constraint isn't reached by the stored lists length a zero value is
   *          assumed, must be >= 0
   * @param step
   *          one of the enum values stored in {@link STEP}
   * @return the progress that is a {@link Float} in <code>[0,100]</code>
   */
  static Float getProgress(UUID entryId, int constraint, STEP step) {
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
    return createCell(hazRow, i, content, this.defaultStyle);
  }

  void createCells(Row hazRow, int colIndex, String content, int count) {
    for (int i = 0; i < count; i++) {
      createCell(hazRow, i + colIndex, content);
    }
  }

  void createCells(Row row, String[] titles, Styles style, Sheet sheet) {
    int regionStart = -1;
    for (int i = 0; i < titles.length; i++) {
      createCell(row, i, titles[i], style);
      if (titles[i].isEmpty() && regionStart == -1) {
        regionStart = i - 1;
      } else if (!titles[i].isEmpty() && regionStart != -1) {
        int rowNum = row.getRowNum();
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, regionStart, i - 1));
        regionStart = -1;
      }
    }
  }

  Row createRow(Sheet sheet) {
    Row row = sheet.createRow(sheet.getLastRowNum() + 1);
    row.setHeightInPoints(10f);
    row.setHeight((short) -1);
    if (columns != null) {
      createCells(row, 0, null, columns);
    }
    return row;
  }

  public void setColumns(int columns) {
    this.columns = columns;
  }

  int createSubRows(Sheet sheet, Row row, int[] parentCells, SubRowCreator creator) {
    int start = row.getRowNum();
    int index = creator.createSubRows(row);
    mergeRows(sheet, start, index, parentCells);
    return index;
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
        this.styles.put(Styles.DEFAULT_EVEN, style);

        CellStyle unevenStyle = this.wb.createCellStyle();
        unevenStyle.cloneStyleFrom(style);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.styles.put(Styles.DEFAULT_UNEVEN, unevenStyle);

        CellStyle total = this.wb.createCellStyle();
        total.setBorderBottom(BorderStyle.DOUBLE);
        total.setTopBorderColor(IndexedColors.BLACK.getIndex());
        total.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        total.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        this.styles.put(Styles.TOTAL_STYLE, total);
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
