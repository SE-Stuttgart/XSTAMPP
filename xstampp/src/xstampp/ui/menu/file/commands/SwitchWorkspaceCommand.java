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
package xstampp.ui.menu.file.commands;

import java.util.prefs.Preferences;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.common.ProjectManager;
import xstampp.util.ChooseWorkLocation;

/**
 * this handler sets {@link IPreferenceConstants#WS_REMEMBER} and restarts the
 * Platform causing the system to ask for the prefered workspace on startup
 * 
 * @author Lukas Balzer
 * @since 1.0
 */
public class SwitchWorkspaceCommand extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    if (ProjectManager.getContainerInstance().checkCloseApplication()) {
      Preferences.userNodeForPackage(ChooseWorkLocation.class).putBoolean(IPreferenceConstants.WS_REMEMBER, false);
      PlatformUI.getWorkbench().restart();
      return true;
    }
    return false;
  }

}
