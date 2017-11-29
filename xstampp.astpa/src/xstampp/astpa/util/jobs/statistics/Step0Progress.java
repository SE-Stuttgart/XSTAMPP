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

import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.EntryWithSeverity;
import xstampp.astpa.model.hazacc.Accident;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;

public class Step0Progress extends AbstractProgressSheetCreator {

  private static final String[] titles = new String[] {
      "Hazard", "", "Severity",
      "Accident", "", "Severity",
      "Safety Constraint", "", "Design Requirements", "", "Completion[%]" };
  private Map<Severity, Integer> sc_per_acc;

  public Step0Progress(Workbook wb, DataModelController controller, Map<Severity, Integer> sc_per_acc) {
    super(wb, controller, STEP.STEP_0);
    this.sc_per_acc = sc_per_acc;
  }

  public void createWorkSheet(Sheet sheet) {
    setColumns(titles.length);
    int rowIndex = 0;
    // the header row: centered text in 48pt font
    Row headerRow = sheet.createRow(rowIndex);
    headerRow.setHeightInPoints(12.75f);

    createCells(headerRow, titles, Styles.HEADER_STYLE, sheet);
    Row hazRow;
    for (ITableModel hazModel : getController().getAllHazards()) {
      triggerDefaultStyle();
      hazRow = createRow(sheet);
      createCell(hazRow, 0, hazModel.getIdString());
      createCell(hazRow, 1, hazModel.getTitle());
      createCell(hazRow, 2, ((EntryWithSeverity) hazModel).getSeverity().name());
      createSubRows(sheet, hazRow, new int[] { 0, 1, 2, 10 }, (row) -> {
        return addAccidents(sheet, row, hazModel);
      });
      Float progress = getProgress(hazModel.getId(), 1);
      createCell(hazRow, 10, String.format("%.1f", progress) + "%");
      addProgress(getController().getProjectId(), progress);
    }

    createTotalRow(sheet, titles.length - 1);
    for (int i = 0; i < titles.length; i++) {
      sheet.autoSizeColumn(i);
    }
  }

  /**
   * Adds all accidents that are linked to the given {@link ITableModel}
   * 
   * @return the index of the last added row (equal to row index if no line has been added)
   */
  private int addAccidents(Sheet sheet, Row hazRow, ITableModel hazModel) {
    int index = hazRow.getRowNum();
    Row row = hazRow;
    for (UUID accId : getController().getLinkController().getLinksFor(LinkingType.HAZ_ACC_LINK,
        hazModel.getId())) {
      row = row == null ? createRow(sheet) : row;
      ITableModel accModel = getController().getAccident(accId);
      createCell(row, 3, accModel.getIdString());
      createCell(row, 4, accModel.getTitle());
      if (hazModel instanceof ATableModel && ((ATableModel) hazModel).getSeverity() != null) {
        createCell(row, 5, ((ATableModel) accModel).getSeverity().name());
      }
      createSubRows(sheet, row, new int[] { 3, 4 }, (parentRow) -> {
        return addSafetyConstraints(sheet, parentRow, accId);
      });
      addProgress(hazModel.getId(), getProgress(accId, this.sc_per_acc.get(((ATableModel) accModel).getSeverity())));
      index = row.getRowNum();
      row = null;
    }
    return index;
  }

  /**
   * Adds all safety constraints that are linked to the given {@link Accident}'s id
   * 
   * @return the index of the last added row (equal to row index if no line has been added)
   */
  private int addSafetyConstraints(Sheet sheet, Row accRow, UUID accId) {
    int index = accRow.getRowNum();
    Row row = accRow;
    for (UUID s0Id : getController().getLinkController().getLinksFor(LinkingType.ACC_S0_LINK,
        accId)) {
      row = row == null ? createRow(sheet) : row;
      ITableModel s0Model = getController().getSafetyConstraint(s0Id);
      createCell(row, 6, s0Model.getIdString());
      createCell(row, 7, s0Model.getTitle());
      index = createSubRows(sheet, row, new int[] { 3, 4 }, (parentRow) -> {
        return createDesignRows(sheet, parentRow, s0Model.getId());
      });
      addProgress(accId, getProgress(s0Model.getId(), 1));
      row = null;
    }
    return index;
  }

  private int createDesignRows(Sheet sheet, Row ucaRow, UUID scId) {
    Row row = ucaRow;
    int index = ucaRow.getRowNum();
    for (UUID dr1Id : getController().getLinkController().getLinksFor(LinkingType.DR0_SC_LINK,
        scId)) {
      row = row == null ? createRow(sheet) : row;
      ITableModel designReq = getController().getSdsController().getDesignRequirement(dr1Id,
          ObserverValue.DESIGN_REQUIREMENT);
      createCell(row, 8, designReq.getIdString());
      createCell(row, 9, designReq.getTitle());
      if (designReq.getTitle().isEmpty()) {
        addProgress(scId, 0f);
      } else {
        addProgress(scId, 100f);
      }
      row = null;
    }
    return index;
  }
}
