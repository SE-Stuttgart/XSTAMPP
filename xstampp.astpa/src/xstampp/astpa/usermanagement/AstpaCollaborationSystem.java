/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.usermanagement;

import java.util.List;
import java.util.UUID;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.CausalFactorController;
import xstampp.astpa.model.controlaction.UnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.sds.SDSController;
import xstampp.ui.common.ProjectManager;
import xstampp.usermanagement.api.CollaborationSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.util.IUndoCallback;
import xstampp.util.service.UndoRedoService;

public class AstpaCollaborationSystem extends CollaborationSystem {

  private DataModelController controller;

  public AstpaCollaborationSystem(DataModelController controller) {
    this.controller = controller;
  }

  public int syncDataWithUser(IUser user, Listener listener) {
    ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench()
        .getService(ISourceProviderService.class);
    UndoRedoService provider = (UndoRedoService) service
        .getSourceProvider(UndoRedoService.CAN_REDO);
    provider.startRecord();
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
    ((SDSController) controller.getSdsController()).syncContent((SDSController) userController.getSdsController());

    event.data = 60;
    listener.handleEvent(event);

    // Synchronize all Control actions the given user is responsible for
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

    ((CausalFactorController) controller.getCausalFactorController())
        .syncContent((CausalFactorController) userController.getCausalFactorController());
    controller.getLinkController().syncContent(userController.getLinkController());

    event.data = 100;
    listener.handleEvent(event);
    List<IUndoCallback> record = provider.getRecord();
    return record.size();
  }
}
