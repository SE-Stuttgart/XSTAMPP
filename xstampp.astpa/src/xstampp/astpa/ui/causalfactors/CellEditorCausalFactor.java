/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.ui.causalfactors;

import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.graphics.GC;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

public class CellEditorCausalFactor extends GridCellTextEditor {

  private UUID factorId;
  private ICausalFactorDataModel dataInterface;
  private ModeAddition addition;

  /**
   * This constructs an editor {@link GridCellTextEditor} for editing a Causal Factor.
   * The causal factor is determined by the given factorId and the cell also allows the user to edit
   * the Text of the causal factor or delete it.
   * 
   * @param gridWrapper
   *          the wrapper that contains the {@link Grid} displayed in the editor
   * @param dataInterface
   *          the {@link DataModelController} responsible for storing the data
   * @param factorId
   *          the {@link UUID} of the causal factor
   *          <p>
   *          NOTE: this must be a valid ID otherwise the {@link GridCellTextEditor} will not
   *          function as expected!
   */
  public CellEditorCausalFactor(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      UUID factorId) {
    super(gridWrapper, dataInterface.getCausalFactorController().getCausalFactor(factorId).getText(), factorId);
    setReadOnly(false);
    setShowDelete(true);
    addition = new ModeAddition() {
    };
    this.dataInterface = dataInterface;
    this.factorId = factorId;
    setToolTip(dataInterface.getCausalFactorController().getCausalFactor(factorId).getIdString());
  }

  /**
   * same as
   * {@link CellEditorCausalFactor#CellEditorCausalFactor(GridWrapper, ICausalFactorDataModel, UUID)}
   * but the cell does also contain an additional button for switching between a single safety
   * constraint for the causal factor and one for each related hazard.
   * 
   * @param factorComponentLink
   *          a {@link Link} of type {@link LinkingType#UcaCfLink_Component_LINK} for which the
   *          safety constraint can be set.
   */
  public CellEditorCausalFactor(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      UUID factorId, Link factorComponentLink) {
    super(gridWrapper, dataInterface.getCausalFactorController().getCausalFactor(factorId).getText(), factorId);
    setReadOnly(false);
    setShowDelete(true);
    addition = new ToggleConstraintModeAddition(this, factorComponentLink, dataInterface);
    this.dataInterface = dataInterface;
    this.factorId = factorId;
    setToolTip(dataInterface.getCausalFactorController().getCausalFactor(factorId).getIdString());
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    clearCellButtons();
    addition.paint();
    super.paint(renderer, gc, item);
  }

  @Override
  public void updateDataModel(String newText) {
    dataInterface.getCausalFactorController().setCausalFactorText(factorId, newText);
  }

  @Override
  protected void editorOpening() {
    dataInterface.lockUpdate();
  }

  @Override
  protected void editorClosing() {
    dataInterface.releaseLockAndUpdate(new ObserverValue[] {});
  }

  @Override
  public void delete() {
    if (MessageDialog.openConfirm(null, "Delete Causal Factor?",
        "Do you really want to delete this Causal Factor\n"
            + "and all its child entries?")) {
      for (Link link : dataInterface.getLinkController().getRawLinksFor(LinkingType.UCA_CausalFactor_LINK, factorId)) {
        dataInterface.getLinkController().deleteAllFor(LinkingType.UcaCfLink_Component_LINK, link.getId());
      }
      for (UUID uuid : dataInterface.getLinkController().getLinksFor(LinkingType.UCA_CausalFactor_LINK, factorId)) {
        dataInterface.getLinkController().deleteAllFor(LinkingType.UcaCfLink_Component_LINK, uuid);
      }
      dataInterface.getLinkController().deleteAllFor(LinkingType.UCA_CausalFactor_LINK, factorId);
      dataInterface.getCausalFactorController().removeCausalFactor(factorId);
    }
  }

}
