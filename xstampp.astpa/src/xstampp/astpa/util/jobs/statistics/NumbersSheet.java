/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.util.jobs.statistics;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.Severity;

public class NumbersSheet extends AbstractProgressSheetCreator {

  private List<Severity> severities = Arrays.asList(Severity.S0, Severity.S1, Severity.S2, Severity.S3);
  private Map<Severity, Integer> sc_per_acc;
  private int uca_per_ca;
  private Map<Severity, Integer> cf_per_uca;

  public NumbersSheet(Workbook wb, DataModelController controller, Map<Severity, Integer> sc_per_acc, int uca_per_ca,
      Map<Severity, Integer> cf_per_uca) {
    super(wb, controller, null);
    this.sc_per_acc = sc_per_acc;
    this.uca_per_ca = uca_per_ca;
    this.cf_per_uca = cf_per_uca;
    // TODO Auto-generated constructor stub
  }

  @Override
  protected void createWorkSheet(Sheet sheet) {
    Row row = createRow(sheet);
    createCells(row, new String[] { "Definitions", "", "", "" }, Styles.HEADER_STYLE, sheet);
    row = createRow(sheet);
    createCell(row, 0, "Title", Styles.HEADER_STYLE);
    createCell(row, 1, "Description", Styles.HEADER_STYLE);
    createCell(row, 2, "Severity", Styles.HEADER_STYLE);
    createCell(row, 3, "Value", Styles.HEADER_STYLE);
    row = createRow(sheet);
    createCell(row, 0, "Safety Constraints per Accident");
    createCell(row, 1,
        "The amount of safety constraints required to fully satisfy the definition of done for an Accident");
    createSubRows(sheet, row, new int[] { 0, 1 }, (parent) -> {
      return createSeverityRows(parent, sc_per_acc, sheet);
    });
    row = createRow(sheet);
    createCell(row, 0, "Causal Factors per Unsafe Control Action");
    createCell(row, 1,
        "The amount of Causal Factors that should be analysed for each Unsafe Control Action");
    createSubRows(sheet, row, new int[] { 0, 1 }, (parent) -> {
      return createSeverityRows(parent, cf_per_uca, sheet);
    });
    row = createRow(sheet);
    createCell(row, 0, "Unsafe Control Actions per Control Action");
    createCell(row, 1,
        "Minimal number of Unsafe Control Action that must be defined per Control Action in order to consider the Control Action done");
    createCell(row, 2, "-");
    createCell(row, 3, "" + uca_per_ca);

    sheet.autoSizeColumn(0);
    sheet.autoSizeColumn(1);
    sheet.autoSizeColumn(2);
    sheet.autoSizeColumn(3);
  }

  private int createSeverityRows(Row row, Map<Severity, Integer> map, Sheet sheet) {
    int index = row.getRowNum();
    for (Severity severity : severities) {
      row = row == null ? createRow(sheet) : row;
      createCell(row, 2, severity.name());
      createCell(row, 3, map.get(severity).toString());
      index = row.getRowNum();
      row = null;
    }
    return index;
  }

}
