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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.projectSettings.ProjectSettingsShell;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;
import xstampp.ui.navigation.StepSelector;

/**
 *
 * @author Lukas Balzer
 */
public class OpenProjectSettings extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    Object currentSelection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getSelection(ProjectExplorer.ID); // $NON-NLS-1$

    // if the currentSelection is a stepSelector than it is transfered in a
    // proper object
    if (!(currentSelection instanceof IProjectSelection)) {
      return null;
    }
    IProjectSelection selector = ((IProjectSelection) currentSelection);
    if (ProjectManager.getContainerInstance().canAccess(selector.getProjectId())) {
      ProjectSettingsShell shell = new ProjectSettingsShell(selector.getProjectId());
      shell.open();
    }
    return null;
  }

}
