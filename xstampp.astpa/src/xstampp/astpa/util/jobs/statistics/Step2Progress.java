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

import java.util.Optional;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;

public class Step2Progress extends AbstractProgressSheetCreator {

  private static final String[] titles = new String[] {
      "Unsafe Control Actions", "Severity", "Causal Factors", "Hazard", "Safety Constraint",
      "Design Requirements", "Completion[%]" };

  public Step2Progress(Workbook wb, DataModelController controller) {
    super(wb, controller, STEP.STEP_2);
  }

  public void createWorkSheet(Sheet sheet) {
    int rowIndex = 0;
    // the header row: centered text in 48pt font
    Row headerRow = sheet.createRow(rowIndex);
    headerRow.setHeightInPoints(12.75f);

    createCells(headerRow, titles, Styles.HEADER_STYLE, sheet);
    Row ucaRow;
    for (ICorrespondingUnsafeControlAction uca : getController().getControlActionController()
        .getAllUnsafeControlActions()) {
      ucaRow = createRow(sheet, titles.length);
      createCell(ucaRow, 0, uca.getIdString());
      createCell(ucaRow, 1, uca.getSeverity().name());
      rowIndex = createSubRows(sheet, ucaRow, new int[] { 0, 1 }, (parentRow) -> {
        return createRows(uca, parentRow, sheet);
      });
      // TODO create constant to constraint causal factors per uca
      Float progress = getProgress(uca.getId(), 1);
      addProgress(getController().getProjectId(), progress);
      createCell(ucaRow, titles.length - 1, String.format("%.1f", progress) + "%");
    }
    createTotalRow(sheet, titles.length - 1);

    for (int i = 0; i < titles.length; i++) {
      sheet.autoSizeColumn(i);
    }
  }

  private int createRows(ICorrespondingUnsafeControlAction uca, Row parentRow, Sheet sheet) {
    Row row = parentRow;
    int index = parentRow.getRowNum();
    for (Link ucaCfLink : getController().getLinkController().getRawLinksFor(LinkingType.UCA_CausalFactor_LINK,
        uca.getId())) {
      row = row == null ? createRow(sheet, titles.length) : row;
      ICausalFactor factor = getController().getCausalFactorController().getCausalFactor(ucaCfLink.getLinkB());
      Optional<Link> causalEntryOpt = getController().getLinkController()
          .getRawLinksFor(LinkingType.UcaCfLink_Component_LINK, ucaCfLink.getId()).stream().findFirst();
      if (factor != null && causalEntryOpt.isPresent()) {
        createCell(row, 2, factor.getText());
        index = createSubRows(sheet, row, new int[] { 2 }, (parent) -> {
          return createCFs(sheet, parent, factor, causalEntryOpt.get());
        });
      }
      row = null;
    }
    return index;

  }

  private int createCFs(Sheet sheet, Row parentRow, ICausalFactor factor, Link causalEntryLin) {
    Row row = parentRow;
    int index = parentRow.getRowNum();
    for (Link link : getController().getLinkController().getRawLinksFor(LinkingType.CausalEntryLink_HAZ_LINK,
        causalEntryLin.getId())) {
      row = row == null ? createRow(sheet, titles.length) : row;
      createHazardRow(link, row);
      addProgress(factor.getId(), getProgress(factor.getId(), 1));
      row = null;
    }
    return index;

  }

  private void createHazardRow(Link link, Row hazRow) {
    ITableModel hazard = getController().getHazard(link.getLinkB());
    if (hazard != null) {
      Optional<UUID> constraintOpt = getController().getLinkController()
          .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, link.getId()).stream().findFirst();
      String constraint = getController().getCausalFactorController()
          .getConstraintTextFor(constraintOpt.orElse(null));

      createCell(hazRow, 5, hazard.getIdString());
      createCell(hazRow, 6, constraint);
      Optional<UUID> designOpt = getController().getLinkController()
          .getLinksFor(LinkingType.DR2_CausalSC_LINK, constraintOpt.orElse(null)).stream().findFirst();
      ITableModel requirement = getController().getSdsController().getDesignRequirement(designOpt.orElse(null),
          ObserverValue.DESIGN_REQUIREMENT_STEP2);
      if (requirement != null) {
        addProgress(hazard.getId(), 100f);
        createCell(hazRow, 7, requirement.getIdString());
      } else {
        addProgress(hazard.getId(), 0f);
      }
    }
  }
}
