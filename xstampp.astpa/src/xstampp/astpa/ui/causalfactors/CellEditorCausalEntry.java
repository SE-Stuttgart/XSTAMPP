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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import xstampp.astpa.Activator;
import xstampp.astpa.messages.Messages;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.CellButton;
import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;
import xstampp.ui.common.shell.ModalShell;
import xstampp.util.DefaultSelectionAdapter;

public class CellEditorCausalEntry extends GridCellTextEditor {

  private ICausalFactorDataModel dataInterface;
  private Link ucaCFLink;

  private static final Image ONE_MANY_TOOGLE = Activator
      .getImageDescriptor("/icons/buttons/grid/ToggleOne_ManySCs_2.png").createImage(); //$NON-NLS-1$

  private static final Image MANY_ONE_TOOGLE = Activator
      .getImageDescriptor("/icons/buttons/grid/ToggleMany_OneSCs_2.png").createImage(); //$NON-NLS-1$

  /**
   * 
   * @param gridWrapper
   * @param dataInterface
   * @param ucaCFLink
   *          a Link of type {@link ObserverValue#UCA_CausalFactor_LINK}
   * @param uca
   * @param entryId
   *          the id of a Link of type {@link ObserverValue#UcaCfLink_Component_LINK}
   */
  public CellEditorCausalEntry(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface,
      Link ucaCFLink, IUnsafeControlAction uca, UUID entryId) {
    super(gridWrapper, getUcaText(uca), entryId);
    this.ucaCFLink = ucaCFLink;
    this.dataInterface = dataInterface;
  }

  private static String getUcaText(IUnsafeControlAction uca) {
    // add the uca id + description in a read only cell with an delete button
    return uca.getTitle() + "\n" //$NON-NLS-1$
        + uca.getDescription();
  }

  private void switchToSingleConstraint() {
    if (!dataInterface.getLinkController().isLinked(LinkingType.CausalEntryLink_SC2_LINK, getEntryId())) {
      List<String> choiceList = new ArrayList<>();
      List<UUID> causalHazLinkIds = new ArrayList<>();
      for (Link uuid : dataInterface.getLinkController().getRawLinksFor(LinkingType.CausalEntryLink_HAZ_LINK,
          getEntryId())) {
        ITableModel hazard = dataInterface.getHazard(uuid.getLinkB());
        if (hazard != null) {
          choiceList.add(hazard.getIdString());
          causalHazLinkIds.add(uuid.getId());
        }
      }
      choiceList.add(Messages.CellEditorCausalEntry_None);
      causalHazLinkIds.add(null);
      SwitchToSingleSCModal modal = new SwitchToSingleSCModal(choiceList);
      if (modal.open()) {
        Optional<UUID> sc2Option = dataInterface.getLinkController()
            .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, causalHazLinkIds.get(modal.choice)).stream().findFirst();
        UUID constraint = null;
        if (sc2Option.isPresent()) {
          String initialText = dataInterface.getCausalFactorController().getConstraintTextFor(sc2Option.get());
          constraint = dataInterface.getCausalFactorController().addSafetyConstraint(initialText);
        }
        dataInterface.getLinkController().addLink(LinkingType.CausalEntryLink_SC2_LINK, getEntryId(),
            constraint);
      }
    }
  }

  private void switchToHazardConstraints() {
    if (dataInterface.getLinkController().isLinked(LinkingType.CausalEntryLink_SC2_LINK, getEntryId())) {
      for (Link link : dataInterface.getLinkController().getRawLinksFor(LinkingType.CausalEntryLink_SC2_LINK,
          getEntryId())) {
        dataInterface.lockUpdate();
        dataInterface.getLinkController().deleteLink(link.getLinkType(), link.getId());
        dataInterface.getCausalFactorController().removeSafetyConstraint(link.getLinkB());
        dataInterface.releaseLockAndUpdate(new ObserverValue[] { ObserverValue.CAUSAL_FACTOR });
      }
    }
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    setReadOnly(true);
    setShowDelete(true);
    if (!dataInterface.isUseScenarios()) {
      if (!dataInterface.getLinkController().isLinked(LinkingType.CausalEntryLink_SC2_LINK, getEntryId())) {
        addCellButton(new CellButton(ONE_MANY_TOOGLE, () -> switchToSingleConstraint(),
            Messages.CellEditorCausalEntry_UseOneSCToolTip));
      } else {
        addCellButton(new CellButton(MANY_ONE_TOOGLE, () -> switchToHazardConstraints(),
            Messages.CellEditorCausalEntry_UseMultipleSCToolTip));
      }
    }
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
      this.dataInterface.getLinkController().changeLink(this.ucaCFLink, null, this.ucaCFLink.getLinkB());
    }
  }

  private class SwitchToSingleSCModal extends ModalShell {

    private List<String> choiceList;
    private int choice;

    public SwitchToSingleSCModal(List<String> choiceList) {
      super(Messages.CellEditorCausalEntry_UseOneSCShellTitle);
      this.choiceList = choiceList;
      setSize(500, 200);
    }

    @Override
    protected boolean validate() {
      return true;
    }

    @Override
    protected boolean doAccept() {
      return true;
    }

    @Override
    protected void createCenter(Shell parent) {
      Label label = new Label(parent, SWT.WRAP);
      label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
      label.setText(
          Messages.CellEditorCausalEntry_UseOneSCShellMsg);
      Group group = new Group(parent, SWT.NONE);
      group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
      group.setLayout(new GridLayout());
      Button button = null;
      for (int i = 0; i < choiceList.size(); i++) {
        button = new Button(group, SWT.RADIO);
        button.setText(choiceList.get(i));
        final int index = i;
        button.addSelectionListener(new DefaultSelectionAdapter((e) -> choice = index));
      }
      button.setSelection(true);
    }

  }
}
