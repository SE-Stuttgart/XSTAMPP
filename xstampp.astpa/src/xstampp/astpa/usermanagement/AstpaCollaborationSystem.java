package xstampp.astpa.usermanagement;

import java.util.List;
import java.util.UUID;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.haz.controlaction.UCAHazLink;
import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.haz.hazacc.Link;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorUCAEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.UnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.IHAZXControlAction;
import xstampp.ui.common.ProjectManager;
import xstampp.usermanagement.api.ICollaborationSystem;
import xstampp.usermanagement.api.IUser;

public class AstpaCollaborationSystem implements ICollaborationSystem {

  private DataModelController controller;

  public AstpaCollaborationSystem(DataModelController controller) {
    this.controller = controller;
  }

  protected boolean syncDataWithUser(IUser user, Listener listener) {

    List<UUID> responsibilities = controller.getUserSystem().getResponsibilities(user.getUserId());

    DataModelController userController = (DataModelController) ProjectManager.getContainerInstance()
        .getDataModel(user.getWorkingProjectId());
    Event event = new Event();
    event.data = 0;
    listener.handleEvent(event);
    for (ITableModel entry : controller.getAllAccidents()) {
      ITableModel userEntry = userController.getAccident(entry.getId());
      if (responsibilities.contains(entry.getId()) && userEntry != null) {
        controller.setAccidentTitle(entry.getId(), userEntry.getTitle());
        controller.setAccidentDescription(entry.getId(), userEntry.getDescription());
      }
    }
    event.data = 30;
    listener.handleEvent(event);
    for (ITableModel entry : controller.getAllHazards()) {
      ITableModel userEntry = userController.getHazard(entry.getId());
      if (responsibilities.contains(entry.getId()) && userEntry != null) {
        controller.setHazardTitle(entry.getId(), userEntry.getTitle());
        controller.setHazardDescription(entry.getId(), userEntry.getDescription());
      }
    }
    for (ITableModel model : userController.getAllSafetyConstraints()) {
      if (controller.getSafetyConstraint(model.getId()) == null) {
        controller.addSafetyConstraint(model);
      }
    }

    // sync the accident-hazard links
    for (Link ucaHazLink : controller.getAllHazAccLinks()) {
      if (!userController.getAllHazAccLinks().contains(ucaHazLink)) {
        controller.deleteLink(ucaHazLink.getAccidentId(), ucaHazLink.getHazardId());
      }
    }
    for (Link ucaHazLink : userController.getAllHazAccLinks()) {
      if (!controller.getAllHazAccLinks().contains(ucaHazLink)) {
        controller.addLink(ucaHazLink.getAccidentId(), ucaHazLink.getHazardId());
      }
    }

    event.data = 60;
    listener.handleEvent(event);

    // Synchronize all COntrol actions the given user is responsible for
    // furthermore all unsafe control actions and corresponding safety constraints for
    // this control action are synchronized
    for (IHAZXControlAction userCa : userController.getAllControlActionsU()) {

      IHAZXControlAction originalCa = controller.getControlActionU(userCa.getId());
      if (originalCa != null && responsibilities.contains(userCa.getId())) {
        controller.setControlActionTitle(userCa.getId(), userCa.getTitle());
        controller.setControlActionDescription(userCa.getId(), userCa.getDescription());
        // get all changes in the unsafe control actions defined for the current control action
        for (IUnsafeControlAction uca : userCa.getUnsafeControlActions()) {
          // check whether the uca must be created in the original model
          UnsafeControlAction originalUca = (UnsafeControlAction) originalCa
              .getUnsafeControlAction(uca.getId());
          if (originalUca == null) {
            controller.addUnsafeControlAction(userCa.getId(), uca.getDescription(), uca.getType(),
                uca.getId());
          } else {
            controller.setUcaDescription(uca.getId(), uca.getDescription());
            controller.setCorrespondingSafetyConstraint(uca.getId(),
                ((UnsafeControlAction) uca).getCorrespondingSafetyConstraint().getText());
          }
        }
      }
    }

    // Also sync
    for (UCAHazLink ucaHazLink : controller.getAllUCALinks()) {
      if (!userController.getAllUCALinks().contains(ucaHazLink)) {
        controller.removeUCAHazardLink(ucaHazLink.getUnsafeControlActionId(),
            ucaHazLink.getHazardId());
      }
    }
    for (UCAHazLink ucaHazLink : userController.getAllUCALinks()) {
      if (!controller.getAllUCALinks().contains(ucaHazLink)) {
        controller.addUCAHazardLink(ucaHazLink.getUnsafeControlActionId(),
            ucaHazLink.getHazardId());
      }
    }

    for (ICausalComponent component : userController.getCausalComponents()) {
      for (ICausalFactor factor : component.getCausalFactors()) {
        for (ICausalFactorEntry entry : factor.getAllEntries()) {
          try {
            if (responsibilities
                .contains(userController.getControlActionForUca(entry.getUcaLink()).getId())) {
              controller.addCausalUCAEntry(component.getId(), factor.getId(), entry);
              CausalFactorUCAEntryData entryData = new CausalFactorUCAEntryData(entry.getId());
              entryData.setConstraint(entry.getConstraintText());
              entryData.setNote(entry.getNote());
              entryData.setScenarioLinks(entry.getScenarioLinks());
              controller.changeCausalEntry(component.getId(), factor.getId(), entryData);
            }
          } catch (NullPointerException exc) {

          }
        }
      }
    }
    event.data = 100;
    listener.handleEvent(event);
    return false;
  }

  @Override
  public boolean syncDataWithUser(IUser user) {
    SyncShell syncShell = new SyncShell(user, this);
    syncShell.open();
    return (boolean) syncShell.getReturnValue();
  }

  @Override
  public boolean syncDataWithUser(List<IUser> users) {
    SyncShell syncShell = new SyncShell(users, this);
    syncShell.open();
    return (boolean) syncShell.getReturnValue();
  }
}
