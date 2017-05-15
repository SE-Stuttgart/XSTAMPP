package xstampp.astpa.usermanagement;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.haz.controlaction.UCAHazLink;
import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.DataModelController;
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
    Event event = new Event();
    event.data = 0;
    listener.handleEvent(event);
      DataModelController userController = (DataModelController) ProjectManager
          .getContainerInstance().getDataModel(user.getWorkingProjectId());
      for (ITableModel entry : controller.getAllAccidents()) {
        ITableModel userEntry = userController.getAccident(entry.getId());
        if(user.isResponibleFor(entry.getId()) && userEntry != null) {
          controller.setAccidentTitle(entry.getId(), userEntry.getTitle());
          controller.setAccidentDescription(entry.getId(), userEntry.getDescription());
        }
      }
      event.data = 30;
      listener.handleEvent(event);
      for (ITableModel entry : controller.getAllHazards()) {
        ITableModel userEntry = userController.getHazard(entry.getId());
        if(user.isResponibleFor(entry.getId()) && userEntry != null) {
          controller.setHazardTitle(entry.getId(), userEntry.getTitle());
          controller.setHazardDescription(entry.getId(), userEntry.getDescription());
        }
      }
      event.data = 60;
      listener.handleEvent(event);
      for (IHAZXControlAction entry : controller.getAllControlActionsU()) {
        IHAZXControlAction userEntry = userController.getControlActionU(entry.getId());
        if(user.isResponibleFor(entry.getId()) && userEntry != null) {
          controller.setControlActionTitle(entry.getId(), userEntry.getTitle());
          controller.setControlActionDescription(entry.getId(), userEntry.getDescription());
          for (IUnsafeControlAction uca : entry.getUnsafeControlActions()) {
            IUnsafeControlAction userUca = userEntry.getUnsafeControlAction(uca.getId());
            if(userUca != null) {
              controller.setUcaDescription(uca.getId(), userUca.getDescription());
              controller.removeAllUCAHazardLinks(uca.getId());
              for (UCAHazLink ucaHazLink : userController.getAllUCALinks()) {
                controller.addUCAHazardLink(ucaHazLink.getUnsafeControlActionId(), ucaHazLink.getHazardId());
              }
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
}
