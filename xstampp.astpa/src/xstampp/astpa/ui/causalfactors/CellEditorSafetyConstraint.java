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
import java.util.function.Consumer;

import org.eclipse.swt.graphics.GC;

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
  private Link causalEntryHazLink;
  private ITableModel scModel;

  /**
   * 
   * @param gridWrapper
   * @param dataInterface
   * @param causalHazLink
   *          a Link of type {@link LinkingType#CausalEntryLink_SC2_LINK}
   */
  public CellEditorSafetyConstraint(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      Link causalEntryHazLink) {
    super(gridWrapper, "", causalEntryHazLink.getId());
    setShowDelete(true);
    this.causalDataInterface = dataInterface;
    this.causalEntryHazLink = causalEntryHazLink;
    Optional<UUID> scOptional = dataInterface.getLinkController()
        .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, causalEntryHazLink.getId()).stream().findFirst();
    this.scModel = causalDataInterface.getSafetyConstraint(scOptional.orElse(null));
    if(scModel instanceof BadReferenceModel && scOptional.isPresent()) {
      causalDataInterface.getLinkController().deleteAllFor(LinkingType.CausalHazLink_SC2_LINK, this.causalEntryHazLink.getId());
    }
    setToolTip(scModel.getIdString());
  }

  @Override
  public String getCurrentText() {
    return scModel.getText();
  }

  @Override
  public String getCurrentTitle() {
    return scModel.getIdString();
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    clearCellButtons();
    if (scModel instanceof BadReferenceModel) {
      setReadOnly(true);
      setShowDelete(false);
      Consumer<UUID> linkAction = (id) -> causalDataInterface.getLinkController()
          .addLink(LinkingType.CausalHazLink_SC2_LINK, causalEntryHazLink.getId(), id);
      CellButtonAdd addButton = new CellButtonAdd(() -> {
        UUID constraintId = causalDataInterface.getCausalFactorController().addSafetyConstraint("");
        linkAction.accept(constraintId);
      });
      addButton.setToolTip("Add a new Safety Constraint");
      addCellButton(addButton);
      addCellButton(new CellButtonImportConstraint(getGridWrapper().getGrid(), causalDataInterface,
          linkAction));
      addCellButton(new CellButtonLinkToConstraint(getGridWrapper().getGrid(), causalDataInterface,
          linkAction));
    } else {
      setReadOnly(false);
      setShowDelete(true);
    }
    super.paint(renderer, gc, item);

  }

  @Override
  public void updateDataModel(String newText) {
    // the change is made in one of the three safety responsible controllers depending on the ID of
    // the safety constraint
    if (!(scModel instanceof BadReferenceModel)) {
      ObserverValue value = ObserverValue.SAFETY_CONSTRAINT;
      switch (scModel.getIdString().charAt(2)) {
      case ('1'):
        value = ObserverValue.UNSAFE_CONTROL_ACTION;
        break;
      case ('2'):
        value = ObserverValue.CAUSAL_FACTOR;
        break;
      }
      causalDataInterface.setModelTitle(scModel, newText, value);
    }
  }

  @Override
  public void delete() {
    if (!(scModel instanceof BadReferenceModel)) {
      this.causalDataInterface.getCausalFactorController()
          .removeSafetyConstraint(scModel.getId());
        causalDataInterface.getLinkController().deleteAllFor(LinkingType.CausalHazLink_SC2_LINK, this.causalEntryHazLink.getId());
      
    }
    this.scModel = BadReferenceModel.getBadReference();
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
