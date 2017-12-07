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

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

public class CellEditorSafetyConstraint extends GridCellTextEditor {

  private ICausalFactorDataModel causalDataInterface;
  private Link ucaHazLink;
  private Optional<ITableModel> safetyOption;

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
    this.safetyOption = Optional
        .ofNullable(causalDataInterface.getCausalFactorController().getSafetyConstraint(safetyOption.orElse(null)));
    if (safetyOption.isPresent() && !this.safetyOption.isPresent()) {
      dataInterface.getLinkController().deleteLink(LinkingType.CausalHazLink_SC2_LINK, causalHazLink.getId(),
          safetyOption.get());
    }
  }

  @Override
  public String getCurrentText() {
    return safetyOption.isPresent() ? safetyOption.get().getText() : "";
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    clearCellButtons();
    if (!safetyOption.isPresent()) {
      setReadOnly(true);
      setShowDelete(false);
      addCellButton(
          new NewConstraintButton(ucaHazLink, causalDataInterface));
      addCellButton(new CellButtonImportConstraint(getGridWrapper().getGrid(), ucaHazLink, causalDataInterface));
    } else {
      setReadOnly(false);
      setShowDelete(true);
    }
    super.paint(renderer, gc, item);

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
    if (safetyOption.isPresent()) {
      if (this.causalDataInterface.getCausalFactorController().removeSafetyConstraint(safetyOption.get().getId())) {
        causalDataInterface.getLinkController().deleteLink(LinkingType.CausalHazLink_SC2_LINK, ucaHazLink.getId(),
            safetyOption.get().getId());
        this.safetyOption = Optional.empty();
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
