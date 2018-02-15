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

import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;

public class Step1Progress extends AbstractProgressSheetCreator {

  private static final String[] titles = new String[] { "Control Actions", "",
      "Unsafe Control Actions", "Severity", "Corresponding Safety Constraint", "", "Design Requirement", "",
      "Completion[%]" };
  private int uca_per_ca;

  public Step1Progress(Workbook wb, DataModelController controller, int uca_per_ca) {
    super(wb, controller, STEP.STEP_1);
    this.uca_per_ca = uca_per_ca;
  }

  public void createWorkSheet(Sheet sheet) {
    setColumns(titles.length);
    int rowIndex = 0;
    // the header row: centered text in 48pt font
    Row headerRow = sheet.createRow(rowIndex);
    headerRow.setHeightInPoints(12.75f);
    createCells(headerRow, titles, Styles.HEADER_STYLE, sheet);
    rowIndex = createCAs(sheet, ++rowIndex);

    createTotalRow(sheet, titles.length - 1, true);
    for (int i = 0; i < titles.length; i++) {
      sheet.autoSizeColumn(i);
    }
    sheet.setColumnWidth(7, 100 * 255);
  }

  private int createCAs(Sheet sheet, int rowIndex) {

    Row row;
    int index = rowIndex;
    for (IControlAction action : getController().getAllControlActions()) {
      triggerDefaultStyle();
      row = createRow(sheet);
      createCell(row, 0, action.getIdString());
      createCell(row, 1, action.getTitle());
      index = createSubRows(sheet, row, new int[] { 0, 1, 8 }, (parentRow) -> {
        return createUCARows(sheet, parentRow, action);
      });
      Float progress = getProgress(action.getId(), this.uca_per_ca);
      addProgress(getController().getProjectId(), progress);
      createCell(row, 8, String.format("%.1f", progress) + "%");
      row = null;
    }

    return index;

  }

  private int createUCARows(Sheet sheet, Row caRow, IControlAction action) {
    Row row = caRow;
    int index = caRow.getRowNum();
    for (IUnsafeControlAction ucaModel : action.getUnsafeControlActions()) {
      row = row == null ? createRow(sheet) : row;
      Float progress;
      if (getController().getLinkController().getLinksFor(LinkingType.UCA_HAZ_LINK, ucaModel.getId()).isEmpty()) {
        progress = 100f;
      } else {
        ITableModel safetyModel = ((ICorrespondingUnsafeControlAction) ucaModel)
            .getCorrespondingSafetyConstraint();
        createCell(row, 2, ucaModel.getIdString());
        createCell(row, 3, ucaModel.getSeverity().name());
        createCell(row, 4, safetyModel.getIdString());
        createCell(row, 5, safetyModel.getText());
        index = createSubRows(sheet, row, new int[] { 4, 5 }, (parentRow) -> {
          return createDesignRows(sheet, parentRow, safetyModel.getId());
        });
        progress = getProgress(safetyModel.getId(), 1);
      }
      addProgress(action.getId(), progress);
      row = null;
    }
    return index;
  }

  private int createDesignRows(Sheet sheet, Row ucaRow, UUID scId) {
    Row row = ucaRow;
    int index = ucaRow.getRowNum();
    for (UUID dr1Id : getController().getLinkController().getLinksFor(LinkingType.DR1_CSC_LINK,
        scId)) {
      row = row == null ? createRow(sheet) : row;
      ITableModel designReq = getController().getSdsController().getDesignRequirement(dr1Id,
          ObserverValue.DESIGN_REQUIREMENT_STEP1);
      createCell(row, 6, designReq.getIdString());
      createCell(row, 7, designReq.getTitle());
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
