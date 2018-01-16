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
package xstampp.astpa.util.commands;

import java.util.UUID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;

public class PopulateHazTestCommand extends AbstractHandler {

  private DataModelController dataModel;

  public PopulateHazTestCommand() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Object selection = HandlerUtil.getCurrentSelection(event);
    if (selection instanceof IProjectSelection) {
      UUID projectId = ((IProjectSelection) selection).getProjectId();
      setDataModel(ProjectManager.getContainerInstance().getDataModel(projectId));

      getDataModel().lockUpdate();
      populate();
      getDataModel().releaseLockAndUpdate();
    }
    return null;
  }

  private void populate() {
    UUID caId = dataModel.addControlAction("test", "test");
    for (int i = 0; i < 1000; i++) {
      getDataModel()
          .addUnsafeControlAction(caId, "", UnsafeControlActionType.GIVEN_INCORRECTLY); //$NON-NLS-1$
      getDataModel()
          .addUnsafeControlAction(caId, "", UnsafeControlActionType.NOT_GIVEN); //$NON-NLS-1$
      getDataModel()
          .addUnsafeControlAction(caId, "", UnsafeControlActionType.STOPPED_TOO_SOON); //$NON-NLS-1$
      getDataModel()
          .addUnsafeControlAction(caId, "", UnsafeControlActionType.WRONG_TIMING); //$NON-NLS-1$
    }
    IRectangleComponent root = dataModel.getRoot();
    UUID compId = dataModel.addComponent(root.getId(), new Rectangle(), "test", ComponentType.CONTROLLER, -1);
    for (int i = 0; i < 100; i++) {
      dataModel.addCausalFactor(compId);
    }
  }

  public DataModelController getDataModel() {
    return dataModel;
  }

  public void setDataModel(IDataModel dataModel) {
    this.dataModel = (DataModelController) dataModel;
  }
}
