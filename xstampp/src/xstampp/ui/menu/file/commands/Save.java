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

package xstampp.ui.menu.file.commands;

import java.util.UUID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.ui.common.ProjectManager;
import xstampp.util.STPAPluginUtils;

/**
 * Handler that saves the analysis to the last location on runtime
 * 
 * @author Fabian Toth
 * 
 */
public class Save extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    STPAPluginUtils.executeCommand("org.eclipse.ui.file.save"); //$NON-NLS-1$
    Object doSaveAs = event.getParameter("xstampp.commands.params.doSaveAs"); //$NON-NLS-1$
    
    UUID saveId = ProjectManager.getContainerInstance().getActiveProject();
    if (doSaveAs != null && doSaveAs.equals("TRUE")) { //$NON-NLS-1$
      return ProjectManager.getContainerInstance().saveDataModel(saveId , true, true);
    }
    return ProjectManager.getContainerInstance().saveDataModel(saveId, true, false);
  }
}
