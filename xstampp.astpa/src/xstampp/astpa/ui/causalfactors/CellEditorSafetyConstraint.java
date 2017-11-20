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

  /**
   * 
   * @param gridWrapper
   * @param dataInterface
   * @param ucaHazLink
   *          a Link of type {@link ObserverValue#CausalEntryLink_HAZ_LINK}
   */
  public CellEditorSafetyConstraint(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      Link ucaHazLink) {
    super(gridWrapper,
        dataInterface.getCausalFactorController().getConstraintTextFor(ucaHazLink.getLinkB()),
        ucaHazLink.getId());
    this.ucaHazLink = ucaHazLink;
    setShowDelete(true);
    this.causalDataInterface = dataInterface;
  }

  @Override
  public String getCurrentText() {
    Optional<UUID> safetyOption = this.causalDataInterface.getLinkController()
        .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, ucaHazLink.getId()).stream().findFirst();
    UUID constraintId = safetyOption.orElse(null);
    return causalDataInterface.getCausalFactorController().getConstraintTextFor(constraintId);
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    clearCellButtons();
    Optional<UUID> safetyOption = this.causalDataInterface.getLinkController()
        .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, ucaHazLink.getId()).stream().findFirst();
    if (!safetyOption.isPresent()) {
      addCellButton(
          new NewConstraintButton(ucaHazLink, causalDataInterface));
      addCellButton(new CellButtonImportConstraint(getGridWrapper().getGrid(), ucaHazLink, causalDataInterface));
      setReadOnly(true);
      setShowDelete(false);
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
    Optional<UUID> safetyOption = this.causalDataInterface.getLinkController()
        .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, ucaHazLink.getId()).stream().findFirst();
    if (safetyOption.isPresent()) {
      causalDataInterface.getLinkController().deleteLink(LinkingType.CausalHazLink_SC2_LINK, ucaHazLink.getId(),
          safetyOption.get());
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
