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
import xstampp.astpa.model.hazacc.Hazard;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;

public class Step0ProgressWithHazConstraints extends AbstractProgressSheetCreator {

  private static final String[] titles = new String[] {
      "Accident", "", "Severity",
      "Hazard", "", "Severity",
      "Safety Constraint", "", "Design Requirements", "", "Completion[%]" };
  private Map<Severity, Integer> sc_per_acc;

  public Step0ProgressWithHazConstraints(Workbook wb, DataModelController controller,
      Map<Severity, Integer> sc_per_acc) {
    super(wb, controller, STEP.STEP_1);
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
    for (ITableModel accModel : getController().getAllAccidents()) {
      triggerDefaultStyle();
      hazRow = createRow(sheet);
      createCell(hazRow, 0, accModel.getIdString());
      createCell(hazRow, 1, accModel.getTitle());
      createCell(hazRow, 2, ((EntryWithSeverity) accModel).getSeverity().name());
      createSubRows(sheet, hazRow, new int[] { 0, 1, 2, 10 }, (row) -> {
        return addHazards(sheet, row, accModel);
      });
      Float progress = getProgress(accModel.getId(), 1);
      createCell(hazRow, 10, String.format("%.1f", progress) + "%");
      addProgress(getController().getProjectId(), progress);
    }

    createTotalRow(sheet, titles.length - 1, true);
    for (int i = 0; i < titles.length; i++) {
      sheet.autoSizeColumn(i);
    }
  }

  /**
   * Adds all hazards that are linked to the given {@link ITableModel}
   * 
   * @return the index of the last added row (equal to row index if no line has been added)
   */
  private int addHazards(Sheet sheet, Row accRow, ITableModel accModel) {
    int index = accRow.getRowNum();
    Row row = accRow;
    for (UUID hazId : getController().getLinkController().getLinksFor(LinkingType.HAZ_ACC_LINK,
        accModel.getId())) {
      row = row == null ? createRow(sheet) : row;
      ITableModel haz = getController().getHazard(hazId);
      createCell(row, 3, haz.getIdString());
      createCell(row, 4, haz.getTitle());
      if (accModel instanceof ATableModel && ((ATableModel) accModel).getSeverity() != null) {
        createCell(row, 5, ((ATableModel) haz).getSeverity().name());
      }
      createSubRows(sheet, row, new int[] { 3, 4 }, (parentRow) -> {
        return addSafetyConstraints(sheet, parentRow, hazId);
      });
      addProgress(accModel.getId(), getProgress(hazId, this.sc_per_acc.get(((ATableModel) haz).getSeverity())));
      index = row.getRowNum();
      row = null;
    }
    return index;
  }

  /**
   * Adds all safety constraints that are linked to the given {@link Hazard}'s id
   * 
   * @return the index of the last added row (equal to row index if no line has been added)
   */
  private int addSafetyConstraints(Sheet sheet, Row hazRow, UUID accId) {
    int index = hazRow.getRowNum();
    Row row = hazRow;
    for (UUID s0Id : getController().getLinkController().getLinksFor(LinkingType.HAZ_S0_LINK,
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
