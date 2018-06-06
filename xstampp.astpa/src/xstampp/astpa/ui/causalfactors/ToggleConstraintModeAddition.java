package xstampp.astpa.ui.causalfactors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import xstampp.astpa.Activator;
import xstampp.astpa.messages.Messages;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.CellButton;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.shell.ModalShell;
import xstampp.util.DefaultSelectionAdapter;

interface ModeAddition {
  public default void paint() {
    // do nothing by default
  }
}

class ToggleConstraintModeAddition implements ModeAddition {

  private static final Image ONE_MANY_TOOGLE = Activator
      .getImageDescriptor("/icons/buttons/grid/ToggleOne_ManySCs_2.png").createImage(); //$NON-NLS-1$

  private static final Image MANY_ONE_TOOGLE = Activator
      .getImageDescriptor("/icons/buttons/grid/ToggleMany_OneSCs_2.png").createImage(); //$NON-NLS-1$

  private ICausalFactorDataModel dataInterface;
  private Link factorComponentLink;
  private GridCellTextEditor parentEditor;

  public ToggleConstraintModeAddition(GridCellTextEditor parentEditor, Link factorComponentLink,
      ICausalFactorDataModel dataInterface) {
    this.parentEditor = parentEditor;
    this.factorComponentLink = factorComponentLink;
    this.dataInterface = dataInterface;
  }

  public void paint() {
    if (!dataInterface.isUseScenarios()) {
      if (!dataInterface.getLinkController().isLinked(LinkingType.CausalEntryLink_SC2_LINK,
          factorComponentLink.getId())) {
        parentEditor.addCellButton(new CellButton(ONE_MANY_TOOGLE, () -> switchToSingleConstraint(),
            Messages.CellEditorCausalEntry_UseOneSCToolTip));
      } else {
        parentEditor.addCellButton(new CellButton(MANY_ONE_TOOGLE, () -> switchToHazardConstraints(),
            Messages.CellEditorCausalEntry_UseMultipleSCToolTip));
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
        .getRawLinksFor(LinkingType.CausalEntryLink_SC2_LINK, factorComponentLink.getId()).stream().findFirst();
    if (causalEntryS2Optional.isPresent()) {
      List<String> choiceList = new ArrayList<>();
      List<UUID> hazardIds = new ArrayList<>();
      Link ucaCFLink = dataInterface.getLinkController().getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
          factorComponentLink.getLinkA());
      for (Link entryHazLink : dataInterface.getLinkController().getRawLinksFor(LinkingType.UCA_HAZ_LINK,
          ucaCFLink.getLinkA())) {
        ITableModel hazard = dataInterface.getHazard(entryHazLink.getLinkB());
        if (hazard == null) {
          hazard = dataInterface.getHazard(entryHazLink.getLinkA());
        }
        if (hazard != null) {
          choiceList.add(hazard.getIdString());
          hazardIds.add(hazard.getId());
        }
      }
      SwitchToHazardSCModal modal = new SwitchToHazardSCModal(choiceList);
      if (modal.open()) {
        for (Integer index : modal.choice) {
          Optional<UUID> sc2Option = dataInterface.getLinkController()
              .getLinksFor(LinkingType.CausalEntryLink_SC2_LINK, factorComponentLink.getId()).stream().findFirst();
          UUID constraint = null;
          if (sc2Option.isPresent()) {
            String initialText = dataInterface.getCausalFactorController().getConstraintTextFor(sc2Option.get());
            constraint = dataInterface.getCausalFactorController().addSafetyConstraint(initialText);
          }
          String note = dataInterface.getLinkController()
              .getLinkObjectFor(LinkingType.UcaCfLink_Component_LINK, factorComponentLink.getId()).getNote();
          UUID causalHazLink = dataInterface.getLinkController().addLink(LinkingType.CausalEntryLink_HAZ_LINK,
              factorComponentLink.getId(), hazardIds.get(index), note);
          dataInterface.getLinkController().changeLinkNote(causalHazLink, LinkingType.CausalEntryLink_HAZ_LINK, note);
          dataInterface.getLinkController().addLink(LinkingType.CausalHazLink_SC2_LINK, causalHazLink,
              constraint, causalEntryS2Optional.get().getNote());

        }
        dataInterface.lockUpdate();
        for (Link link : dataInterface.getLinkController().getRawLinksFor(LinkingType.CausalEntryLink_SC2_LINK,
            factorComponentLink.getId())) {
          dataInterface.getLinkController().deleteLink(link.getLinkType(), link.getId());
          dataInterface.getCausalFactorController().removeSafetyConstraint(link.getLinkB());
        }
        for (UUID hazId : dataInterface.getLinkController().getLinksFor(LinkingType.UCA_HAZ_LINK,
            ucaCFLink.getLinkA())) {
          ITableModel hazard = dataInterface.getHazard(hazId);
          if (hazard != null) {
            dataInterface.getLinkController().addLink(LinkingType.CausalEntryLink_HAZ_LINK,
                factorComponentLink.getId(), hazId, false);
          }
        }
        dataInterface.releaseLockAndUpdate(new ObserverValue[] { ObserverValue.CAUSAL_FACTOR });
      }
    }
  }

  /**
   * This function changes the definition of one safety constraint for each hazard linked to the uca
   * entry to one single safety constraint for the causal factor.
   * <p>
   * calling this opens a popup asking the user to select one of the linked hazards<br>
   * the safety constraint, design hint and note/rational is than used as new single constraint
   */
  private void switchToSingleConstraint() {
    if (!dataInterface.getLinkController().isLinked(LinkingType.CausalEntryLink_SC2_LINK,
        factorComponentLink.getId())) {
      List<String> choiceList = new ArrayList<>();
      List<Link> causalHazLinks = new ArrayList<>();
      for (Link entryHazLink : dataInterface.getLinkController().getRawLinksFor(LinkingType.CausalEntryLink_HAZ_LINK,
          factorComponentLink.getId())) {
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
        dataInterface.getLinkController().addLink(LinkingType.CausalEntryLink_SC2_LINK, factorComponentLink.getId(),
            constraint, note);

        for (Link entryHazLink : dataInterface.getLinkController().getRawLinksFor(LinkingType.CausalEntryLink_HAZ_LINK,
            factorComponentLink.getId())) {
          for (Link link : dataInterface.getLinkController().getRawLinksFor(LinkingType.CausalHazLink_SC2_LINK,
              entryHazLink.getId())) {
            dataInterface.getLinkController().deleteLink(link.getLinkType(), link.getId());
            dataInterface.getCausalFactorController().removeSafetyConstraint(link.getLinkB());
          }
          dataInterface.getLinkController().deleteLink(entryHazLink.getLinkType(), entryHazLink.getId());
        }
      }
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
      if (button != null) {
        button.setSelection(true);
      }
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
        button.setSelection(false);
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
    }

  }
}
