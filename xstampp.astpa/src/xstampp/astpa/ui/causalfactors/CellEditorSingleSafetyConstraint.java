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
import xstampp.ui.common.grid.CellButtonAdd;
import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

public class CellEditorSingleSafetyConstraint extends GridCellTextEditor {

  private ICausalFactorDataModel causalDataInterface;
  private Link causalEntrySc2Link;
  private Optional<ITableModel> safetyOption;

  /**
   * 
   * @param gridWrapper
   * @param dataInterface
   * @param causalHazLink
   *          a Link of type {@link LinkingType#CausalEntryLink_SC2_LINK}
   */
  public CellEditorSingleSafetyConstraint(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      Link causalEntrySc2Link) {
    super(gridWrapper, "", causalEntrySc2Link.getId());
    setShowDelete(true);
    this.causalDataInterface = dataInterface;
    this.causalEntrySc2Link = causalEntrySc2Link;
    this.safetyOption = Optional
        .ofNullable(causalDataInterface.getCausalFactorController().getSafetyConstraint(causalEntrySc2Link.getLinkB()));
    if (safetyOption.isPresent() && !this.safetyOption.isPresent()) {
      dataInterface.getLinkController().changeLink(causalEntrySc2Link, causalEntrySc2Link.getLinkA(), null);
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
      CellButtonAdd addButton = new CellButtonAdd(() -> {
        UUID constraintId = causalDataInterface.getCausalFactorController().addSafetyConstraint("");
        causalDataInterface.getLinkController().addLink(LinkingType.CausalEntryLink_SC2_LINK,
            causalEntrySc2Link.getId(),
            constraintId);
      });
      addButton.setToolTip("Add a new Safety Constraint");
      addCellButton(addButton);
      addCellButton(
          new CellButtonImportConstraint(getGridWrapper().getGrid(), causalDataInterface, (id) -> causalDataInterface
              .getLinkController().changeLink(causalEntrySc2Link, causalEntrySc2Link.getLinkA(), id)));
    } else {
      setReadOnly(false);
      setShowDelete(true);
    }
    super.paint(renderer, gc, item);

  }

  @Override
  public void updateDataModel(String newText) {
    causalDataInterface.getCausalFactorController().setSafetyConstraintText(causalEntrySc2Link.getLinkB(), newText);
  }

  @Override
  public void delete() {
    if (safetyOption.isPresent()) {
      if (this.causalDataInterface.getCausalFactorController().removeSafetyConstraint(safetyOption.get().getId())) {
        causalDataInterface.getLinkController().changeLink(this.causalEntrySc2Link, this.causalEntrySc2Link.getLinkB(),
            null);
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
