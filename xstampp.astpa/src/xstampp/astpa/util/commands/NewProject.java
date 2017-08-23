/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.util.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.astpa.model.DataModelController;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.menu.file.commands.CommandState;

/**
 * Handler which starts the new Analysis Wizards
 * 
 * @author Fabian
 * 
 */
public class NewProject extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    String nameParam = event.getParameter("astpa.new.name"); //$NON-NLS-1$
    String pathParam = event.getParameter("astpa.new.path"); //$NON-NLS-1$
    if ((nameParam == null) || (pathParam == null)) {
      ProjectManager.getLOGGER().debug("failed to initialize new Project");
    }

    if (ProjectManager.getContainerInstance().startUp(DataModelController.class,
        nameParam, pathParam, null) == null) {
      ProjectManager.getLOGGER().debug("failed to start up new Project");
    }

    IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench()
        .getPerspectiveRegistry()
        .findPerspectiveWithId("xstampp.defaultPerspective"); //$NON-NLS-1$
    if (descriptor != null) {
      PlatformUI.getWorkbench().getActiveWorkbenchWindow()
          .getActivePage().setPerspective(descriptor);
    }
    return null;
  }

}
