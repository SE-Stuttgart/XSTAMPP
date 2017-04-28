/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.ui.menu.file.commands;

import java.util.UUID;

import messages.Messages;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.ProjectSelector;
import xstampp.ui.navigation.api.IProjectSelection;

/**
 * This handler manages the deletfor all projects and plugins on that platform
 * by calling {@link ProjectManager#removeProjectData(UUID)} with the current
 * project selection
 * 
 * @author Lukas Balzer
 * @since 1.0
 */
public class Delete extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Object currentSelection = HandlerUtil.getCurrentSelection(event);
    if (currentSelection instanceof ProjectSelector) {
      UUID projectId = ((IProjectSelection) currentSelection).getProjectId();
      String projName = ProjectManager.getContainerInstance().getTitle(projectId);

      if (MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), Messages.DeleteProject,
          String.format(Messages.DeleteProjectConfirmMsg, projName))
          && !ProjectManager.getContainerInstance().removeProjectData(projectId)) {
        MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.Error, Messages.DeleteFailedMsg);
      }
    }
    return null;
  }

}
