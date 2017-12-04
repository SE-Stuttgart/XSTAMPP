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

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;

public class Step2Progress extends AbstractProgressSheetCreator {

  private static final String[] titles = new String[] {
      "Unsafe Control Actions", "Severity", "Causal Factors", "Hazard", "Safety Constraint", "",
      "Design Requirements", "Completion[%]" };
  private Map<Severity, Integer> cf_per_uca;

  public Step2Progress(Workbook wb, DataModelController controller, Map<Severity, Integer> cf_per_uca) {
    super(wb, controller, STEP.STEP_2);
    this.cf_per_uca = cf_per_uca;
  }

  public void createWorkSheet(Sheet sheet) {
    setColumns(titles.length);
    int rowIndex = 0;
    // the header row: centered text in 48pt font
    Row headerRow = sheet.createRow(rowIndex);
    headerRow.setHeightInPoints(12.75f);

    createCells(headerRow, titles, Styles.HEADER_STYLE, sheet);
    Row ucaRow;
    for (ICorrespondingUnsafeControlAction uca : getController().getControlActionController()
        .getAllUnsafeControlActions()) {
      triggerDefaultStyle();
      ucaRow = createRow(sheet);
      createCell(ucaRow, 0, uca.getIdString());
      createCell(ucaRow, 1, uca.getSeverity().name());
      rowIndex = createSubRows(sheet, ucaRow, new int[] { 0, 1 }, (parentRow) -> {
        return createRows(uca, parentRow, sheet);
      });
      Float progress = getProgress(uca.getId(), this.cf_per_uca.get(uca.getSeverity()));
      addProgress(getController().getProjectId(), progress);
      createCell(ucaRow, titles.length - 1, String.format("%.1f", progress) + "%");
    }
    createTotalRow(sheet, titles.length - 1, true);

    for (int i = 0; i < titles.length; i++) {
      sheet.autoSizeColumn(i);
    }
  }

  private int createRows(ICorrespondingUnsafeControlAction uca, Row parentRow, Sheet sheet) {
    Row row = parentRow;
    int index = parentRow.getRowNum();
    // each unsafe control action is linked to n causal factors, but only once to each one
    for (Link ucaCfLink : getController().getLinkController().getRawLinksFor(LinkingType.UCA_CausalFactor_LINK,
        uca.getId())) {
      row = row == null ? createRow(sheet) : row;
      // The causal entry is defined as link between the linking between uca and causal factor and a
      // component
      ICausalFactor factor = getController().getCausalFactorController().getCausalFactor(ucaCfLink.getLinkB());
      Optional<Link> causalEntryOpt = getController().getLinkController()
          .getRawLinksFor(LinkingType.UcaCfLink_Component_LINK, ucaCfLink.getId()).stream().findFirst();
      if (factor != null && causalEntryOpt.isPresent()) {
        createCell(row, 2, factor.getText());
        index = createSubRows(sheet, row, new int[] { 2 }, (parent) -> {
          return createCausalEntries(sheet, parent, factor, causalEntryOpt.get());
        });
        int requiredEntries = getController().getLinkController().getLinksFor(LinkingType.UCA_HAZ_LINK, uca.getId())
            .size();
        addProgress(uca.getId(), getProgress(factor.getId(), requiredEntries));
      }
      row = null;
    }
    return index;

  }

  private int createCausalEntries(Sheet sheet, Row parentRow, ICausalFactor factor, Link causalEntryLin) {
    Row row = parentRow;
    int index = parentRow.getRowNum();
    // each entry (link between uca-cf and a component is used to get the hazard linking for this
    // causal factor
    for (Link link : getController().getLinkController().getRawLinksFor(LinkingType.CausalEntryLink_HAZ_LINK,
        causalEntryLin.getId())) {
      row = row == null ? createRow(sheet) : row;
      createHazardRow(link, row);
      addProgress(factor.getId(), getProgress(link.getLinkB(), 1));
      row = null;
    }
    return index;

  }

  private void createHazardRow(Link link, Row hazRow) {
    ITableModel hazard = getController().getHazard(link.getLinkB());
    if (hazard != null) {
      Optional<UUID> constraintOpt = getController().getLinkController()
          .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, link.getId()).stream().findFirst();
      ITableModel constraint = getController().getCausalFactorController()
          .getSafetyConstraint(constraintOpt.orElse(null));

      createCell(hazRow, 3, hazard.getIdString());
      if (constraint != null) {
        createCell(hazRow, 4, constraint.getIdString());
        createCell(hazRow, 5, constraint.getTitle());
      }
      Optional<UUID> designOpt = getController().getLinkController()
          .getLinksFor(LinkingType.DR2_CausalSC_LINK, constraintOpt.orElse(null)).stream().findFirst();
      ITableModel requirement = getController().getSdsController().getDesignRequirement(designOpt.orElse(null),
          ObserverValue.DESIGN_REQUIREMENT_STEP2);
      if (requirement != null) {
        addProgress(hazard.getId(), 100f);
        createCell(hazRow, 6, requirement.getIdString());
      } else {
        addProgress(hazard.getId(), 0f);
      }
    }
  }
}
