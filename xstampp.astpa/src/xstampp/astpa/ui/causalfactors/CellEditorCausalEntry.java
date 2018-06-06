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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.GC;

import xstampp.astpa.messages.Messages;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

public class CellEditorCausalEntry extends GridCellTextEditor {

  private ICausalFactorDataModel dataInterface;
  private List<Link> factorComponentLinkList;
  private ModeAddition addition;

  /**
   * 
   * @param gridWrapper
   * @param dataInterface
   * @param ucaCFLink
   *          a Link of type {@link ObserverValue#UcaCfLink_Component_LINK}
   * @param uca
   * @param entryId
   *          the id of a Link of type {@link ObserverValue#UcaCfLink_Component_LINK}
   */
  public CellEditorCausalEntry(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      List<Link> factorComponentLinks, IUnsafeControlAction uca, UUID entryId) {
    super(gridWrapper, getUcaText(uca), entryId);
    addition = new ModeAddition() {
    };
    this.factorComponentLinkList = factorComponentLinks;
    this.dataInterface = dataInterface;
  }

  /**
   * 
   * @param gridWrapper
   * @param dataInterface
   * @param factorComponentLink
   *          a Link of type {@link ObserverValue#UcaCfLink_Component_LINK}
   * @param uca
   * @param entryId
   *          the id of a Link of type {@link ObserverValue#UcaCfLink_Component_LINK}
   */
  public CellEditorCausalEntry(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      Link factorComponentLink, IUnsafeControlAction uca, UUID entryId) {
    super(gridWrapper, getUcaText(uca), entryId);
    addition = new ToggleConstraintModeAddition(this, factorComponentLink, dataInterface);
    this.factorComponentLinkList = Arrays.asList(new Link[] { factorComponentLink });
    this.dataInterface = dataInterface;
  }

  private static String getUcaText(IUnsafeControlAction uca) {
    // add the uca id + description in a read only cell with an delete button
    return uca.getTitle() + "\n" //$NON-NLS-1$
        + uca.getDescription();
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    clearCellButtons();
    addition.paint();
    super.paint(renderer, gc, item);
  }

  @Override
  public void updateDataModel(String newText) {
    // Cannot be modified
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
    if (MessageDialog.openConfirm(null, Messages.CellEditorCausalEntry_ConfirmDeleteUCA,
        Messages.CellEditorCausalEntry_ConfirmDeleteUCAMsg)) {
      for (Link factorComponentLink : factorComponentLinkList) {
        Link ucaCFLink = this.dataInterface.getLinkController().getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
            factorComponentLink.getLinkA());
        this.dataInterface.getLinkController().changeLink(ucaCFLink, null, ucaCFLink.getLinkB());
        this.dataInterface.getLinkController().deleteAllFor(LinkingType.CausalEntryLink_HAZ_LINK,
            factorComponentLink.getId());
      }
    }
  }

}
