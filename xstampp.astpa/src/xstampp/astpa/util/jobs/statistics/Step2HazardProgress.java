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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.EntryWithSeverity;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;

public class Step2HazardProgress extends AbstractProgressSheetCreator {

  private static final String[] titles = new String[] { "Hazards", "", "Severity",
      "Unsafe Control Actions", "Severity", "Causal Factors", "Safety Constraints",
      "Design Requirements", "Completion[%]" };
  private Map<Severity, Integer> cf_per_uca;

  public Step2HazardProgress(Workbook wb, DataModelController controller, Map<Severity, Integer> cf_per_uca) {
    super(wb, controller, STEP.STEP_2_HAZARD_CENTERED);
    this.cf_per_uca = cf_per_uca;
  }

  public void createWorkSheet(Sheet sheet) {
    setColumns(titles.length);
    // the header row: centered text in 48pt font
    Row headerRow = sheet.createRow(0);
    headerRow.setHeightInPoints(12.75f);
    createCells(headerRow, titles, Styles.HEADER_STYLE, sheet);
    Row hazRow;
    for (ITableModel hazModel : getController().getAllHazards()) {
      triggerDefaultStyle();
      hazRow = createRow(sheet);
      createCell(hazRow, 0, hazModel.getIdString());
      createCell(hazRow, 1, hazModel.getTitle());
      createCell(hazRow, 2, ((EntryWithSeverity) hazModel).getSeverity().name());
      createSubRows(sheet, hazRow, new int[] { 0, 1, 2, 8 }, (parentRow) -> {
        return createUCAs(sheet, parentRow, hazModel);
      });
      Float progress = getProgress(hazModel.getId(), 1);
      createCell(hazRow, 8, String.format("%.1f", progress) + "%");
      addProgress(getController().getProjectId(), progress);
    }
    createTotalRow(sheet, titles.length - 1);

    for (int i = 0; i < titles.length; i++) {
      sheet.autoSizeColumn(i);
    }
  }

  private int createUCAs(Sheet sheet, Row hazRow, ITableModel hazModel) {
    int rowIndex = hazRow.getRowNum();
    Row row = hazRow;
    SortedMap<IUnsafeControlAction, List<Link>> hazardBasedMap = getController().getCausalFactorController()
        .getHazardBasedMap(hazModel, getController().getLinkController(), getController().getControlActionController());
    for (IUnsafeControlAction uca : hazardBasedMap.keySet()) {
      row = row == null ? createRow(sheet) : row;
      createCell(row, 3, uca.getIdString());
      createCell(row, 4, uca.getSeverity().name());
      rowIndex = createSubRows(sheet, row, new int[] { 3, 4 }, (parentRow) -> {
        return createCFs(sheet, parentRow, hazardBasedMap.get(uca));
      });

      Float progress = getProgress(uca.getId(), this.cf_per_uca.get(uca.getSeverity()));
      addProgress(hazModel.getId(), progress);
      row = null;
    }
    return rowIndex;
  }

  private int createCFs(Sheet sheet, Row hazRow, List<Link> list) {
    Row row = hazRow;
    int index = hazRow.getRowNum();
    // iterate over all LinkingType.CausalEntryLink_HAZ_LINK
    for (Link causalHazLink : list) {
      Link causalEntryLink = getController().getLinkController().getLinkObjectFor(LinkingType.UcaCfLink_Component_LINK,
          causalHazLink.getLinkA());
      Link ucaCfLink = getController().getLinkController().getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
          causalEntryLink.getLinkA());
      ICausalFactor factor = getController().getCausalFactorController().getCausalFactor(ucaCfLink.getLinkB());
      row = row == null ? createRow(sheet) : row;
      Optional<UUID> sc2Optional = getController().getLinkController()
          .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, causalHazLink.getId()).stream().findFirst();
      String constraint = getController().getCausalFactorController().getConstraintTextFor(sc2Optional.orElse(null));
      Optional<UUID> designOptional = getController().getLinkController()
          .getLinksFor(LinkingType.DR2_CausalSC_LINK, sc2Optional.orElse(null)).stream().findFirst();
      ITableModel requirement = getController().getSdsController().getDesignRequirement(designOptional.orElse(null),
          ObserverValue.DESIGN_REQUIREMENT_STEP2);
      String designRequirement = "";
      if (requirement != null) {
        designRequirement = requirement.getIdString();
        addProgress(ucaCfLink.getLinkA(), 100f);
      } else {
        addProgress(ucaCfLink.getLinkA(), 0f);
      }
      createCell(row, 5, factor.getText());
      createCell(row, 6, constraint);
      createCell(row, 7, designRequirement);
      index = row.getRowNum();
      row = null;
    }
    return index;
  }

}
