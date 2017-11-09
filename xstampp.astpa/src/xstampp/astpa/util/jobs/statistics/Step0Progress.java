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

import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.EntryWithSeverity;
import xstampp.astpa.model.hazacc.Accident;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.util.jobs.statistics.AbstractProgressSheetCreator.STEP;
import xstampp.model.ObserverValue;

public class Step0Progress extends AbstractProgressSheetCreator {

  private static final String[] titles = new String[] { "HazId", "Hazard", "Severity", "AccId",
      "Accident", "Severity",
      "SCId", "Safety Constraint", "Completion[%]" };

  public Step0Progress(Workbook wb, DataModelController controller) {
    super(wb, controller);
  }

  public void createWorkSheet(Sheet sheet) {
    int rowIndex = 0;
    // the header row: centered text in 48pt font
    Row headerRow = sheet.createRow(rowIndex);
    headerRow.setHeightInPoints(12.75f);

    createCells(headerRow, titles, Styles.HEADER_STYLE, sheet);
    Row hazRow;
    for (ITableModel hazModel : getController().getAllHazards()) {
      hazRow = createRow(sheet, ++rowIndex);
      createCell(hazRow, 0, hazModel.getIdString());
      createCell(hazRow, 1, hazModel.getTitle());
      createCell(hazRow, 2, ((EntryWithSeverity) hazModel).getSeverity().name());
      int hazGroupStart = rowIndex;
      rowIndex = addAccidents(sheet, hazRow, rowIndex, hazModel);
      Float progress = getProgress(STEP.STEP_0, hazModel.getId(), 1);
      createCell(hazRow, 8, String.format("%.1f", progress) + "%");
      addProgress(STEP.STEP_0, getController().getProjectId(), progress);
      if (rowIndex > hazGroupStart) {
        sheet.addMergedRegion(new CellRangeAddress(hazGroupStart, rowIndex, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(hazGroupStart, rowIndex, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(hazGroupStart, rowIndex, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(hazGroupStart, rowIndex, 8, 8));
      }
    }

    Row footer = createRow(sheet, ++rowIndex);
    Float progress = getProgress(STEP.STEP_1, getController().getProjectId(), 1);
    createCell(footer, 8, String.format("%.1f", progress) + "%");
    for (int i = 0; i < titles.length; i++) {
      sheet.autoSizeColumn(i);
    }
  }

  /**
   * Adds all accidents that are linked to the given {@link ITableModel}
   * 
   * @return the index of the last added row (equal to row index if no line has been added)
   */
  private int addAccidents(Sheet sheet, Row hazRow, int rowIndex, ITableModel hazModel) {
    int index = rowIndex;
    Row accRow = hazRow;
    for (UUID accId : getController().getLinkController().getLinksFor(ObserverValue.HAZ_ACC_LINK,
        hazModel.getId())) {
      if (accRow == null) {
        accRow = createRow(sheet, ++index);
        createCells(accRow, 0, null, 3);
      }
      ITableModel accModel = getController().getAccident(accId);
      createCell(accRow, 3, accModel.getIdString());
      createCell(accRow, 4, accModel.getTitle());
      if (hazModel instanceof ATableModel && ((ATableModel) hazModel).getSeverity() != null) {
        createCell(accRow, 5, ((ATableModel) accModel).getSeverity().name());
      }
      int accGroupStart = index;
      index = addSafetyConstraints(sheet, accRow, index, accId);
      addProgress(STEP.STEP_0, hazModel.getId(), getProgress(STEP.STEP_0, accId, 1));
      if (index > accGroupStart) {
        sheet.addMergedRegion(new CellRangeAddress(accGroupStart, index, 3, 3));
        sheet.addMergedRegion(new CellRangeAddress(accGroupStart, index, 4, 4));
      }
      accRow = null;
    }
    // if the index didn't change than no accidents have been added and empty cells need to be
    // inserted
    if (accRow != null) {
      createCells(accRow, 3, null, 6);
    }
    return index;
  }

  /**
   * Adds all safety constraints that are linked to the given {@link Accident}'s id
   * 
   * @return the index of the last added row (equal to row index if no line has been added)
   */
  private int addSafetyConstraints(Sheet sheet, Row accRow, int rowIndex, UUID accId) {
    int index = rowIndex;
    Row scRow = accRow;
    for (UUID s0Id : getController().getLinkController().getLinksFor(ObserverValue.ACC_S0_LINK,
        accId)) {
      if (scRow == null) {
        scRow = createRow(sheet, ++index);
        createCells(scRow, 0, null, 6);
      }
      ITableModel s0Model = getController().getSafetyConstraint(s0Id);
      createCell(scRow, 6, s0Model.getIdString());
      createCell(scRow, 7, s0Model.getTitle());
      createCell(scRow, 8, null);
      addProgress(STEP.STEP_0, accId, 100f);
      scRow = null;
    }
    // if the index didn't change than no accidents have been added and empty cells need to be
    // inserted
    if (scRow != null) {
      createCells(scRow, 6, null, 3);
    }
    return index;
  }
}
