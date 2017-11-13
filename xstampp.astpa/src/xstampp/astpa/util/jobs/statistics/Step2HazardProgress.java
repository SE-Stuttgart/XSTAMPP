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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.EntryWithSeverity;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class Step2HazardProgress extends AbstractProgressSheetCreator {

  private static final String[] titles = new String[] { "Hazards", "", "Severity",
      "Unsafe Control Actions", "Severity", "Causal Factors", "Safety Constraints",
      "Completion[%]" };

  public Step2HazardProgress(Workbook wb, DataModelController controller) {
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
      rowIndex = createCFs(sheet, hazRow, rowIndex, hazModel);
      Float progress = getProgress(STEP.STEP_2, hazModel.getId(), 1);
      createCell(hazRow, 7, String.format("%.1f", progress) + "%");
      addProgress(STEP.STEP_2, getController().getProjectId(), progress);
      mergeRows(sheet, hazGroupStart, rowIndex, new int[] { 0, 1, 2, 8 });
    }
    Row footer = createRow(sheet, ++rowIndex);
    Float progress = getProgress(STEP.STEP_2, getController().getProjectId(), 1);
    createCell(footer, 7, String.format("%.1f", progress) + "%");

    for (int i = 0; i < titles.length; i++) {
      sheet.autoSizeColumn(i);
    }
  }

  private int createCFs(Sheet sheet, Row hazRow, int rowIndex, ITableModel hazModel) {
    Map<UUID, ICorrespondingUnsafeControlAction> ucaMap = new HashMap<>();
    for (ICorrespondingUnsafeControlAction uca : getController().getControlActionController()
        .getAllUnsafeControlActions()) {
      ucaMap.put(uca.getId(), uca);
    }
    TreeMap<ICorrespondingUnsafeControlAction, Map<ICausalFactorEntry, ICausalFactor>> cftoCfEntryToUCAMap = new TreeMap<>();
    // In the below block a map containing the causal factors mapped to the uca entries for
    // every occurring uca
//    for (IRectangleComponent controlStructureComponent : getController()
//        .getControlStructureController().getRoot().getChildren()) {
//      ICausalComponent causalComponent = getController().getCausalFactorController()
//          .getCausalComponent(controlStructureComponent);
//      // if a component is registered in the causalFactorController for the given control structure
//      // component than this can contain a list of causal factors
//      if (causalComponent != null) {
//        for (ICausalFactor factor : causalComponent.getCausalFactors()) {
//          for (ICausalFactorEntry entry : factor.getAllEntries()) {
//            if (entry.getUcaLink() != null) {
//              ICorrespondingUnsafeControlAction uca = ucaMap.get(entry.getUcaLink());
//              Optional<UUID> of = Optional.of(hazModel.getId());
//              if (getController().getLinkController().isLinked(ObserverValue.UCA_HAZ_LINK,
//                  uca.getId(), of)) {
//                if (!cftoCfEntryToUCAMap.containsKey(uca)) {
//                  cftoCfEntryToUCAMap.put(uca, new HashMap<>());
//                }
//                cftoCfEntryToUCAMap.get(uca).put(entry, factor);
//              }
//            }
//          }
//        }
//      }
//    }

    Row row = hazRow;
    int index = rowIndex;
    for (Entry<ICorrespondingUnsafeControlAction, Map<ICausalFactorEntry, ICausalFactor>> ucaEntry : cftoCfEntryToUCAMap
        .entrySet()) {
      if (row == null) {
        row = createRow(sheet, ++index);
        createCells(row, 0, null, 3);
      }
      createCell(row, 3, ucaEntry.getKey().getIdString());
      createCell(row, 4, ucaEntry.getKey().getSeverity().name());
      int ucaStart = index;
      index = createCausalFactorRows(sheet, row, index, ucaEntry.getValue());
      mergeRows(sheet, ucaStart, rowIndex, new int[] { 3, 4 });
      row = null;
    }
    return index;

  }

  private int createCausalFactorRows(Sheet sheet, Row ucaRow, int rowIndex,
      Map<ICausalFactorEntry, ICausalFactor> map) {
    Row row = ucaRow;
    int index = rowIndex;
    for (Entry<ICausalFactorEntry, ICausalFactor> entry : map.entrySet()) {
      if (row == null) {
        row = createRow(sheet, ++index);
        createCells(row, 0, null, 5);
      }
      createCell(row, 5, entry.getValue().getText());
      String constraint = getController().getCausalFactorController()
          .getConstraintTextFor(entry.getKey().getConstraintId());
      createCell(row, 6, constraint);
      row = null;
    }
    return index;
  }

}
