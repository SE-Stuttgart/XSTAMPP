/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.astpa.util.commands;

import java.util.UUID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.astpa.controlstructure.NewControlStructureShell;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;

public class NewControlStructureHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    String idString = event.getParameter("xstampp.dynamicStep.commandParameter.projectId");
    // if the currentSelection is a stepSelector than it is transfered in a
    // proper object
    if (idString != null) {
      UUID id = UUID.fromString(idString);
      if (ProjectManager.getContainerInstance().canAccess(id)) {
        IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(id);

        NewControlStructureShell shell = new NewControlStructureShell(
            (IControlStructureEditorDataModel) dataModel);
        shell.open();
      }
      return null;
    }
    return null;
  }

}
