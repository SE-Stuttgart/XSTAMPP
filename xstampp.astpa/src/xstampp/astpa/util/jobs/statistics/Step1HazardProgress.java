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
import xstampp.model.ObserverValue;

public class Step1HazardProgress extends AbstractProgressSheetCreator {

  private static final String[] titles = new String[] {
      "Hazards", "", "Severity",
      "Control Actions", "", "Unsafe Control Actions", "Severity",
      "Correcponding Safety Constraint", "Design Requirements", "", "Completion[%]" };

  public Step1HazardProgress(Workbook wb, DataModelController controller) {
    super(wb, controller, STEP.STEP_1_HAZARD_CENTERED);
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
      rowIndex = createSubRows(sheet, hazRow, new int[] { 0, 1, 2, 10 }, (parentRow) -> {
        return createCAs(sheet, parentRow, hazModel);
      });
      Float progress = getProgress(hazModel.getId(), 1);
      createCell(hazRow, 10, String.format("%.1f", progress) + "%");
      addProgress(getController().getProjectId(), progress);
    }

    createTotalRow(sheet, titles.length - 1);
    for (int i = 0; i < titles.length; i++) {
      sheet.autoSizeColumn(i);
    }
    sheet.setColumnWidth(7, 100 * 255);
  }

  private int createCAs(Sheet sheet, Row hazRow, ITableModel hazModel) {
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
    int index = hazRow.getRowNum();
    for (Entry<ITableModel, List<UUID>> entry : caMap.entrySet()) {
      row = row == null ? createRow(sheet) : row;
      ITableModel caModel = entry.getKey();
      createCell(row, 3, caModel.getIdString());
      createCell(row, 4, caModel.getTitle());
      index = createSubRows(sheet, row, new int[] { 3, 4 }, (parentRow) -> {
        return createUCARows(sheet, parentRow, caModel, entry.getValue());
      });
      Float progress = getProgress(caModel.getId(), 1);
      addProgress(hazModel.getId(), progress);
      row = null;
    }

    return index;

  }

  private int createUCARows(Sheet sheet, Row caRow, ITableModel caModel,
      List<UUID> value) {
    Row row = caRow;
    int index = caRow.getRowNum();
    for (UUID ucaId : value) {
      row = row == null ? createRow(sheet) : row;
      IUnsafeControlAction ucaModel = ((IControlAction) caModel).getUnsafeControlAction(ucaId);
      ITableModel safetyModel = ((ICorrespondingUnsafeControlAction) ucaModel)
          .getCorrespondingSafetyConstraint();
      createCell(row, 5, ucaModel.getIdString());
      createCell(row, 6, ucaModel.getSeverity().name());
      createCell(row, 7, safetyModel.getText());
      index = createSubRows(sheet, row, new int[] { 3, 4 }, (parentRow) -> {
        return createDesignRows(sheet, parentRow, safetyModel.getId());
      });
      addProgress(caModel.getId(), getProgress(safetyModel.getId(), 1));
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
          ObserverValue.DESIGN_REQUIREMENT);
      createCell(row, 8, designReq.getIdString());
      createCell(row, 8, designReq.getTitle());
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
