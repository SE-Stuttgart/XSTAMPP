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
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.EntryWithSeverity;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.util.jobs.statistics.AbstractProgressSheetCreator.STEP;
import xstampp.model.ObserverValue;

public class Step1HazardProgress extends AbstractProgressSheetCreator {

  private static final String[] titles = new String[] {
      "Hazards", "", "Severity",
      "Control Actions", "", "Unsafe Control Actions", "Severity",
      "Correcponding Safety Constraint", "Design Requirements", "", "Completion[%]" };

  public Step1HazardProgress(Workbook wb, DataModelController controller) {
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
      rowIndex = createCAs(sheet, hazRow, rowIndex, hazModel);
      Float progress = getProgress(STEP.STEP_1_HAZARD_CENTERED, hazModel.getId(), 1);
      createCell(hazRow, 10, String.format("%.1f", progress) + "%");
      addProgress(STEP.STEP_1_HAZARD_CENTERED, getController().getProjectId(), progress);
      mergeRows(sheet, hazGroupStart, rowIndex, new int[] { 0, 1, 2, 10 });
    }

    createTotalRow(sheet, rowIndex, STEP.STEP_1_HAZARD_CENTERED);
    for (int i = 0; i < titles.length; i++) {
      sheet.autoSizeColumn(i);
    }
    sheet.setColumnWidth(7, 100 * 255);
  }

  private int createCAs(Sheet sheet, Row hazRow, int rowIndex, ITableModel hazModel) {
    TreeMap<ITableModel, List<UUID>> caMap = new TreeMap<>();
    for (UUID uuid : getController().getLinkController().getLinksFor(LinkingType.UCA_HAZ_LINK,
        hazModel.getId())) {
      ITableModel actionForUca = getController().getControlActionForUca(uuid);
      if (!caMap.containsKey(actionForUca)) {
        caMap.put(actionForUca, new ArrayList<>());
      }
      caMap.get(actionForUca).add(uuid);
    }

    Row row = hazRow;
    int index = rowIndex;
    for (Entry<ITableModel, List<UUID>> entry : caMap.entrySet()) {
      row = row == null ? createRow(sheet, ++index, titles.length) : row;
      ITableModel caModel = entry.getKey();
      createCell(row, 3, caModel.getIdString());
      createCell(row, 4, caModel.getTitle());
      int caGroupStart = index;
      index = createUCARows(sheet, row, index, caModel, entry.getValue());
      Float progress = getProgress(STEP.STEP_1_HAZARD_CENTERED, caModel.getId(), 1);
      addProgress(STEP.STEP_1_HAZARD_CENTERED, hazModel.getId(), progress);
      mergeRows(sheet, caGroupStart, index, new int[] { 3, 4 });
      row = null;
    }

    return index;

  }

  private int createUCARows(Sheet sheet, Row caRow, int rowIndex, ITableModel caModel,
      List<UUID> value) {
    Row row = caRow;
    int index = rowIndex;
    for (UUID ucaId : value) {
      row = row == null ? createRow(sheet, ++index, titles.length) : row;
      IUnsafeControlAction ucaModel = ((IControlAction) caModel).getUnsafeControlAction(ucaId);
      ITableModel safetyModel = ((ICorrespondingUnsafeControlAction) ucaModel)
          .getCorrespondingSafetyConstraint();
      createCell(row, 5, ucaModel.getIdString());
      createCell(row, 6, ucaModel.getSeverity().name());
      createCell(row, 7, safetyModel.getText());
      int groupStart = index;
      index = createDesignRows(sheet, row, index, safetyModel.getId());
      addProgress(STEP.STEP_1_HAZARD_CENTERED, caModel.getId(), getProgress(STEP.STEP_1_HAZARD_CENTERED, safetyModel.getId(), 1));
      mergeRows(sheet, groupStart, rowIndex, new int[] { 3, 4 });
      row = null;
    }
    return index;
  }

  private int createDesignRows(Sheet sheet, Row ucaRow, int rowIndex, UUID scId) {
    Row row = ucaRow;
    int index = rowIndex;
    for (UUID dr1Id : getController().getLinkController().getLinksFor(LinkingType.DR1_CSC_LINK,
        scId)) {
      row = row == null ? createRow(sheet, ++index, titles.length) : row;
      ITableModel designReq = getController().getSdsController().getDesignRequirement(dr1Id,
          ObserverValue.DESIGN_REQUIREMENT);
      createCell(row, 8, designReq.getIdString());
      createCell(row, 8, designReq.getTitle());
      if (designReq.getTitle().isEmpty()) {
        addProgress(STEP.STEP_1_HAZARD_CENTERED, scId, 0f);
      } else {
        addProgress(STEP.STEP_1_HAZARD_CENTERED, scId, 100f);
      }
      row = null;
    }
    return index;
  }

}
