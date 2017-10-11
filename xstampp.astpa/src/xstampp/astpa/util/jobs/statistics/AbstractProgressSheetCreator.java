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

import xstampp.astpa.model.DataModelController;

abstract class AbstractProgressSheetCreator {
  private StyleFactory factory;

  enum Styles {
    HEADER_STYLE, DEFAULT_STYLE
  }
  
  private DataModelController controller;

  enum STEP {
    STEP_1, STEP_2, STEP_3
  }

  private Map<STEP, Map<UUID, List<Float>>> progressMap;

  public AbstractProgressSheetCreator(Workbook wb, DataModelController controller) {
    this.controller = controller;
    this.factory = new StyleFactory(wb);
    this.progressMap = new HashMap<>();
    this.progressMap.put(STEP.STEP_1, new HashMap<>());
    this.progressMap.put(STEP.STEP_2, new HashMap<>());
    this.progressMap.put(STEP.STEP_3, new HashMap<>());
  }



  void addProgress(STEP step, UUID entryId, Float progress) {
    if (!this.progressMap.get(step).containsKey(entryId)) {
      this.progressMap.get(step).put(entryId, new ArrayList<>());
    } 
    this.progressMap.get(step).get(entryId).add(progress);
  }
  
  /**
   * 
   * @param step
   * @param entryId
   * @param constraint must be > 0
   * @return
   */
  Float getProgress(STEP step, UUID entryId, int constraint) {
    if(this.progressMap.get(step).containsKey(entryId)) {
      int size = this.progressMap.get(step).get(entryId).size();
      float factor = 1/(float) Math.max(constraint, size);
      Float total = 0f;
      for (Float progress : this.progressMap.get(step).get(entryId)) {
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

  Row createRow(Sheet sheet, int rowIndex) {
    Row hazRow = sheet.createRow(rowIndex);
    hazRow.setHeightInPoints(10f);
    hazRow.setHeight((short) -1);
    return hazRow;
  }

  Sheet createSheet() {
    Sheet sheet = getFactory().wb.createSheet("STPA Analysis Progress");
    
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
        return getStyle(styleConstant);
      }
    }
  }
  
  DataModelController getController() {
    return controller;
  }
  
  StyleFactory getFactory() {
    return factory;
  }
}
