/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.ui.causalfactors;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import xstampp.astpa.model.BadReferenceModel;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.CellButtonAdd;
import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

public class CellEditorSafetyConstraint extends GridCellTextEditor {

  private ICausalFactorDataModel causalDataInterface;
  private Link ucaHazLink;
  private ITableModel safetyOption;

  /**
   * 
   * @param gridWrapper
   * @param dataInterface
   * @param causalHazLink
   *          a Link of type {@link ObserverValue#CausalEntryLink_HAZ_LINK}
   */
  public CellEditorSafetyConstraint(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      Link causalHazLink) {
    super(gridWrapper,
        dataInterface.getCausalFactorController().getConstraintTextFor(causalHazLink.getLinkB()),
        causalHazLink.getId());
    setShowDelete(true);
    this.causalDataInterface = dataInterface;
    this.ucaHazLink = causalHazLink;
    Optional<UUID> safetyOption = this.causalDataInterface.getLinkController()
        .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, causalHazLink.getId()).stream().findFirst();
    this.safetyOption = causalDataInterface.getCausalFactorController().getSafetyConstraint(safetyOption.orElse(null));
  }

  @Override
  public String getCurrentText() {
    return this.safetyOption.getText();
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    clearCellButtons();
    if (this.safetyOption.getId() == BadReferenceModel.getBadReference().getId()) {
      setReadOnly(true);
      setShowDelete(false);
      CellButtonAdd addButton = new CellButtonAdd(() -> {
        UUID constraintId = causalDataInterface.getCausalFactorController().addSafetyConstraint("");
        causalDataInterface.getLinkController().addLink(LinkingType.CausalHazLink_SC2_LINK, this.ucaHazLink.getId(),
            constraintId);
      });
      addButton.setToolTip("Add a new Safety Constraint");
      addCellButton(addButton);
      addCellButton(new CellButtonImportConstraint(getGridWrapper().getGrid(), causalDataInterface,
          (id) -> causalDataInterface.getLinkController().addLink(LinkingType.CausalHazLink_SC2_LINK,
              ucaHazLink.getId(), id)));
    } else {
      setReadOnly(false);
      setShowDelete(true);
    }
    super.paint(renderer, gc, item);

  }

  @Override
  public String getToolTip(Point point) {
    if (!(safetyOption instanceof BadReferenceModel)) {
      return safetyOption.getIdString();
    }
    return super.getToolTip(point);
  }

  @Override
  public void updateDataModel(String newText) {
    Optional<UUID> safetyOption = this.causalDataInterface.getLinkController()
        .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, ucaHazLink.getId()).stream().findFirst();
    if (safetyOption.isPresent()) {
      causalDataInterface.getCausalFactorController().setSafetyConstraintText(safetyOption.get(), newText);
    }
  }

  @Override
  public void delete() {
    if (this.safetyOption.getId() != BadReferenceModel.getBadReference().getId()) {
      if (this.causalDataInterface.getCausalFactorController().removeSafetyConstraint(safetyOption.getId())) {
        causalDataInterface.getLinkController().deleteLink(LinkingType.CausalHazLink_SC2_LINK, ucaHazLink.getId(),
            safetyOption.getId());
        this.safetyOption = BadReferenceModel.getBadReference();
      }
    }
  }

  @Override
  protected void editorOpening() {
    causalDataInterface.lockUpdate();
  }

  @Override
  protected void editorClosing() {
    causalDataInterface.releaseLockAndUpdate(new ObserverValue[] {});
  }

}
