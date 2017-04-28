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
package xstampp.util;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.ProjectExplorer;
import xstampp.ui.navigation.api.IProjectSelection;

public class RunExportCommand extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ProjectExplorer.ID);
    if (part != null) {
      Object selection = part.getViewSite().getSelectionProvider().getSelection();

      if (selection instanceof IProjectSelection) {
        String command = ProjectManager.getContainerInstance()
            .getConfigurationFor(((IProjectSelection) selection).getProjectId()).getAttribute("runCommand");

        STPAPluginUtils.executeCommand(command);
      }
    }
    return null;
  }

}
