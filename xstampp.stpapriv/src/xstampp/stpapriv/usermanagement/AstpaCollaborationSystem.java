package xstampp.stpapriv.usermanagement;

import java.util.List;
import java.util.UUID;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.astpa.model.controlaction.UCAHazLink;
import xstampp.astpa.model.controlaction.UnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.stpapriv.model.PrivacyController;
import xstampp.ui.common.ProjectManager;
import xstampp.usermanagement.api.CollaborationSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.util.IUndoCallback;
import xstampp.util.service.UndoRedoService;

public class AstpaCollaborationSystem extends CollaborationSystem {

	  private PrivacyController controller;

	  public AstpaCollaborationSystem(PrivacyController controller) {
	    this.controller = controller;
	  }

	  public int syncDataWithUser(IUser user, Listener listener) {
	    ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench()
	        .getService(ISourceProviderService.class);
	    UndoRedoService provider = (UndoRedoService) service
	        .getSourceProvider(UndoRedoService.CAN_REDO);
	    provider.startRecord();
	    List<UUID> responsibilities = controller.getUserSystem().getResponsibilities(user.getUserId());

	    PrivacyController userController = (PrivacyController) ProjectManager.getContainerInstance()
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
	        controller.deleteLink(ucaHazLink.getLinkA(), ucaHazLink.getLinkB());
	      }
	    }
	    for (Link ucaHazLink : userController.getAllHazAccLinks()) {
	      if (!controller.getAllHazAccLinks().contains(ucaHazLink)) {
	        controller.addLink(ucaHazLink.getLinkA(), ucaHazLink.getLinkB());
	      }
	    }

	    event.data = 60;
	    listener.handleEvent(event);

	    // Synchronize all COntrol actions the given user is responsible for
	    // furthermore all unsafe control actions and corresponding safety constraints for
	    // this control action are synchronized
	    for (IControlAction userCa : userController.getAllControlActionsU()) {

	      IControlAction originalCa = controller.getControlActionU(userCa.getId());
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

	    event.data = 100;
	    listener.handleEvent(event);
	    List<IUndoCallback> record = provider.getRecord();
	    return record.size();
	  }
}
