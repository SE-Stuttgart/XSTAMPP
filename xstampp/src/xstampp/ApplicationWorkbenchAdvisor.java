/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.services.ISourceProviderService;

import messages.Messages;
import xstampp.ui.common.ProjectManager;
import xstampp.util.LoadWorkspace;
import xstampp.util.service.UndoRedoService;

/**
 * Configures the workbench.
 * 
 * @author Patrick Wickenhaeuser, Sebastian Sieber
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

  /**
   * The perspective's ID.
   */
  private static final String PERSPECTIVE_ID = "astpa.welcome.perspective"; //$NON-NLS-1$

  @Override
  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
      IWorkbenchWindowConfigurer configurer) {
    return new ApplicationWorkbenchWindowAdvisor(configurer);

  }

  @Override
  public String getInitialWindowPerspectiveId() {
    return ApplicationWorkbenchAdvisor.PERSPECTIVE_ID;
  }

  @Override
  public void postStartup() {
    Job loadWs = new LoadWorkspace(Messages.ApplicationWorkbenchAdvisor_Load_Projects);
    loadWs.schedule();

    ISourceProviderService service = (ISourceProviderService) PlatformUI.getWorkbench()
        .getService(ISourceProviderService.class);
    if (service == null) {
      ProjectManager.getLOGGER().error(
          "Undo Redo Service could not be started please restart XSTAMPP for a proper functionallity of this service");
    } else {
      UndoRedoService sourceProvider = (UndoRedoService) service.getSourceProvider(UndoRedoService.CAN_REDO);
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(sourceProvider);
    }
  }

}
