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

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.EntryWithSeverity;
import xstampp.astpa.model.hazacc.Accident;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;

public class Step0Progress extends AbstractProgressSheetCreator {

  private static final String[] titles = new String[] {
      "Hazard", "", "Severity",
      "Accident", "", "Severity",
      "Safety Constraint", "", "Design Requirements", "", "Completion[%]" };

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
      triggerDefaultStyle();
      hazRow = createRow(sheet, ++rowIndex, titles.length);
      createCell(hazRow, 0, hazModel.getIdString());
      createCell(hazRow, 1, hazModel.getTitle());
      createCell(hazRow, 2, ((EntryWithSeverity) hazModel).getSeverity().name());
      int hazGroupStart = rowIndex;
      rowIndex = addAccidents(sheet, hazRow, rowIndex, hazModel);
      Float progress = getProgress(STEP.STEP_0, hazModel.getId(), 1);
      createCell(hazRow, 10, String.format("%.1f", progress) + "%");
      addProgress(STEP.STEP_0, getController().getProjectId(), progress);
      mergeRows(sheet, hazGroupStart, rowIndex, new int[] { 0, 1, 2, 9 });
    }

    createTotalRow(sheet, rowIndex, STEP.STEP_0);
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
    Row row = hazRow;
    for (UUID accId : getController().getLinkController().getLinksFor(LinkingType.HAZ_ACC_LINK,
        hazModel.getId())) {
      row = row == null ? createRow(sheet, ++index, titles.length) : row;
      ITableModel accModel = getController().getAccident(accId);
      createCell(row, 3, accModel.getIdString());
      createCell(row, 4, accModel.getTitle());
      if (hazModel instanceof ATableModel && ((ATableModel) hazModel).getSeverity() != null) {
        createCell(row, 5, ((ATableModel) accModel).getSeverity().name());
      }
      int accGroupStart = index;
      index = addSafetyConstraints(sheet, row, index, accId);
      addProgress(STEP.STEP_0, hazModel.getId(), getProgress(STEP.STEP_0, accId, 1));
      mergeRows(sheet, accGroupStart, rowIndex, new int[] { 3, 4 });
      row = null;
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
    Row row = accRow;
    for (UUID s0Id : getController().getLinkController().getLinksFor(LinkingType.ACC_S0_LINK,
        accId)) {
      row = row == null ? createRow(sheet, ++index, titles.length) : row;
      ITableModel s0Model = getController().getSafetyConstraint(s0Id);
      createCell(row, 6, s0Model.getIdString());
      createCell(row, 7, s0Model.getTitle());
      int groupStart = index;
      index = createDesignRows(sheet, row, index, s0Model.getId());
      addProgress(STEP.STEP_0, accId, getProgress(STEP.STEP_0, s0Model.getId(), 1));
      mergeRows(sheet, groupStart, rowIndex, new int[] { 3, 4 });
      row = null;
    }
    return index;
  }

  private int createDesignRows(Sheet sheet, Row ucaRow, int rowIndex, UUID scId) {
    Row row = ucaRow;
    int index = rowIndex;
    for (UUID dr1Id : getController().getLinkController().getLinksFor(LinkingType.DR0_SC_LINK,
        scId)) {
      row = row == null ? createRow(sheet, ++index, titles.length) : row;
      ITableModel designReq = getController().getSdsController().getDesignRequirement(dr1Id,
          ObserverValue.DESIGN_REQUIREMENT);
      createCell(row, 8, designReq.getIdString());
      createCell(row, 9, designReq.getTitle());
      if (designReq.getTitle().isEmpty()) {
        addProgress(STEP.STEP_0, scId, 0f);
      } else {
        addProgress(STEP.STEP_0, scId, 100f);
      }
      row = null;
    }
    return index;
  }
}
