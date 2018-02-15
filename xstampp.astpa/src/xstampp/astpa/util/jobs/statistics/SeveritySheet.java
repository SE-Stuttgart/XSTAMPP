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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.Severity;

public class SeveritySheet extends AbstractProgressSheetCreator {

  public SeveritySheet(Workbook wb, DataModelController controller, STEP step) {
    super(wb, controller, step);
  }

  @Override
  protected void createWorkSheet(Sheet sheet) {
    Row row = createRow(sheet);
    createCells(row, new String[] { "Severity Definitions", "" }, Styles.HEADER_STYLE, sheet);
    createCell(row, 0, "Severity", Styles.HEADER_STYLE);
    createCell(row, 1, "Description", Styles.HEADER_STYLE);

    List<Severity> severities = Arrays.asList(Severity.S0, Severity.S1, Severity.S2, Severity.S3);
    for (Severity severity : severities) {
      row = createRow(sheet);
      createCell(row, 0, severity.name());
      createCell(row, 1, severity.getDescription());
      triggerDefaultStyle();
    }
    sheet.autoSizeColumn(0);
    sheet.autoSizeColumn(1);
  }

}
