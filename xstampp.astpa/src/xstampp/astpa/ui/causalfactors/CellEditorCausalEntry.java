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

  /**
   * This function changes the definition of one safety constraint for each hazard linked to the uca
   * entry to one single safety constraint for the uca.
   * <p>
   * calling this opens a popup asking the user to select one of the linked hazards<br>
   * the safety constraint, design hint and note/rational is than used as new single constraint
   */
  private void switchToSingleConstraint() {
    if (!dataInterface.getLinkController().isLinked(LinkingType.CausalEntryLink_SC2_LINK, getEntryId())) {
      List<String> choiceList = new ArrayList<>();
      List<Link> causalHazLinks = new ArrayList<>();
      for (Link entryHazLink : dataInterface.getLinkController().getRawLinksFor(LinkingType.CausalEntryLink_HAZ_LINK,
          getEntryId())) {
        ITableModel hazard = dataInterface.getHazard(entryHazLink.getLinkB());
        if (hazard != null) {
          choiceList.add(hazard.getIdString());
          causalHazLinks.add(entryHazLink);
        }
      }
      choiceList.add(Messages.CellEditorCausalEntry_None);
      SwitchToSingleSCModal modal = new SwitchToSingleSCModal(choiceList);
      if (modal.open()) {
        UUID constraint = null;
        String note = null;
        if (modal.choice < causalHazLinks.size()) {
          Optional<UUID> sc2Option = dataInterface.getLinkController()
              .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, causalHazLinks.get(modal.choice).getId()).stream()
              .findFirst();
          // The note of the LinkingType.CausalEntryLink_HAZ_LINK is set as new note/rational for
          // the ucaCfLink
          dataInterface.getLinkController().changeLinkNote(ucaCFLink, causalHazLinks.get(modal.choice).getNote());

          if (sc2Option.isPresent()) {
            // the LinkingType.CausalHazLink_SC2_LINK's note is the design hint which is copied to
            // the new LinkingType.CausalEntryLink_SC2_LINK
            Link linkObjectFor = dataInterface.getLinkController()
                .getLinkObjectFor(LinkingType.CausalHazLink_SC2_LINK, causalHazLinks.get(modal.choice).getId(),
                    sc2Option.get());
            note = linkObjectFor.getNote();
            String initialText = dataInterface.getCausalFactorController().getConstraintTextFor(sc2Option.get());
            constraint = dataInterface.getCausalFactorController().addSafetyConstraint(initialText);
          }
        }
        dataInterface.getLinkController().addLink(LinkingType.CausalEntryLink_SC2_LINK, getEntryId(),
            constraint, note);

        for (Link entryHazLink : dataInterface.getLinkController().getRawLinksFor(LinkingType.CausalEntryLink_HAZ_LINK,
            getEntryId())) {
          for (Link link : dataInterface.getLinkController().getRawLinksFor(LinkingType.CausalHazLink_SC2_LINK,
              entryHazLink.getId())) {
            dataInterface.lockUpdate();
            dataInterface.getLinkController().deleteLink(link.getLinkType(), link.getId());
            dataInterface.getCausalFactorController().removeSafetyConstraint(link.getLinkB());
            dataInterface.releaseLockAndUpdate(new ObserverValue[] { ObserverValue.CAUSAL_FACTOR });

          }
        }
      }
    }
  }

  /**
   * This function changes the definition from one single safety constraint for the uca entry to one
   * safety constraint for each hazard linked to the uca
   * <p>
   * calling this opens a popup asking the user to select all linked hazards for which the existing
   * safety constraint, design hint and note/rational should be used as safety constraint
   */
  private void switchToHazardConstraints() {
    Optional<Link> causalEntryS2Optional = dataInterface.getLinkController()
        .getRawLinksFor(LinkingType.CausalEntryLink_SC2_LINK, getEntryId()).stream().findFirst();
    if (causalEntryS2Optional.isPresent()) {
      List<String> choiceList = new ArrayList<>();
      List<UUID> hazardIds = new ArrayList<>();
      for (Link entryHazLink : dataInterface.getLinkController().getRawLinksFor(LinkingType.UCA_HAZ_LINK,
          ucaCFLink.getLinkA())) {
        ITableModel hazard = dataInterface.getHazard(entryHazLink.getLinkB());
        if (hazard != null) {
          choiceList.add(hazard.getIdString());
          hazardIds.add(hazard.getId());
        }
      }
      SwitchToHazardSCModal modal = new SwitchToHazardSCModal(choiceList);
      if (modal.open()) {
        for (Integer index : modal.choice) {
          Optional<UUID> sc2Option = dataInterface.getLinkController()
              .getLinksFor(LinkingType.CausalEntryLink_SC2_LINK, getEntryId()).stream().findFirst();
          UUID constraint = null;
          if (sc2Option.isPresent()) {
            String initialText = dataInterface.getCausalFactorController().getConstraintTextFor(sc2Option.get());
            constraint = dataInterface.getCausalFactorController().addSafetyConstraint(initialText);
          }
          String note = dataInterface.getLinkController()
              .getLinkObjectFor(LinkingType.UcaCfLink_Component_LINK, getEntryId()).getNote();
          UUID causalHazLink = dataInterface.getLinkController().addLink(LinkingType.CausalEntryLink_HAZ_LINK,
              getEntryId(), hazardIds.get(index), note);
          dataInterface.getLinkController().changeLinkNote(causalHazLink, LinkingType.CausalEntryLink_HAZ_LINK, note);
          dataInterface.getLinkController().addLink(LinkingType.CausalHazLink_SC2_LINK, causalHazLink,
              constraint, causalEntryS2Optional.get().getNote());

        }
        for (Link link : dataInterface.getLinkController().getRawLinksFor(LinkingType.CausalEntryLink_SC2_LINK,
            getEntryId())) {
          dataInterface.lockUpdate();
          dataInterface.getLinkController().deleteLink(link.getLinkType(), link.getId());
          dataInterface.getCausalFactorController().removeSafetyConstraint(link.getLinkB());
          dataInterface.releaseLockAndUpdate(new ObserverValue[] { ObserverValue.CAUSAL_FACTOR });
        }
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
      choice = choiceList.size() - 1;
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

  private class SwitchToHazardSCModal extends ModalShell {

    private List<String> choiceList;
    private List<Integer> choice;

    public SwitchToHazardSCModal(List<String> choiceList) {
      super("Switch to use one safety constraint for each hazard linked to the respective UCA");
      this.choiceList = choiceList;
      this.choice = new ArrayList<>();
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
      label.setText("Switch to use one safety constraint for each hazard linked to the respective UCA");
      Group group = new Group(parent, SWT.NONE);
      group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
      group.setLayout(new GridLayout());
      Button button = null;
      for (int i = 0; i < choiceList.size(); i++) {
        button = new Button(group, SWT.CHECK);
        button.setText(choiceList.get(i));
        final int index = i;
        button.addSelectionListener(new DefaultSelectionAdapter((e) -> {
          if (((Button) e.getSource()).getSelection()) {
            choice.add(index);
          } else {
            choice.remove((Integer) index);
          }
        }));
      }
      button.setSelection(false);
    }

  }
}
