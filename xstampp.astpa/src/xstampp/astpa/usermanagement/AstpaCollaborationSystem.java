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

import java.util.ArrayList;
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
import xstampp.astpa.model.hazacc.HazAccController;
import xstampp.astpa.model.sds.SDSController;
import xstampp.ui.common.ProjectManager;
import xstampp.usermanagement.api.CollaborationSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.util.IUndoCallback;
import xstampp.util.service.UndoRedoService;

public class AstpaCollaborationSystem extends CollaborationSystem {

  private DataModelController controller;

  /**
   * Constructs a collaboration system that contains methods to synchronize the given
   * <i>controller</i>
   * with an other one given in one of the provided methods
   * 
   * @param controller
   *          the {@link DataModelController} that should receive the data from an other controller
   */
  public AstpaCollaborationSystem(DataModelController controller) {
    this.controller = controller;
  }

  /**
   * 
   * @param userController
   *          the {@link DataModelController} from which the data will be retrieved
   * @return the number of differences synchronized between the controller given in the constructor
   *         and the controller given in this method
   */
  public int syncData(DataModelController userController) {
    // if no listener is given than a default listener is created
    Listener listener = (e) -> {
    };
    // since the entire DataModelController should be synchronized responsibilities aren't needed
    // and thus are given as trivial accepting list
    @SuppressWarnings("serial")
    List<UUID> responsibilities = new ArrayList<UUID>() {

      @Override
      public boolean contains(Object o) {
        return true;
      }
    };
    return syncDataWithUser(userController, listener, responsibilities);
  }

  /**
   * 
   * @param userController
   *          the {@link DataModelController} from which the data will be retrieved
   * @return the number of differences synchronized between the controller given in the constructor
   *         and the controller given in this method
   */
  public int syncDataWithUser(DataModelController userController, Listener listener, List<UUID> responsibilities) {
    ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench()
        .getService(ISourceProviderService.class);
    UndoRedoService provider = (UndoRedoService) service
        .getSourceProvider(UndoRedoService.CAN_REDO);
    provider.startRecord();
    Event event = new Event();
    event.data = 0;
    listener.handleEvent(event);

    ((HazAccController) controller.getHazAccController())
        .syncContent((HazAccController) userController.getHazAccController());

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

  public int syncDataWithUser(IUser user, Listener listener) {

    List<UUID> responsibilities = controller.getUserSystem().getResponsibilities(user.getUserId());

    DataModelController userController = (DataModelController) ProjectManager.getContainerInstance()
        .getDataModel(user.getWorkingProjectId());
    return syncDataWithUser(userController, listener, responsibilities);
  }
}
