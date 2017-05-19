/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.ui.menu.file.commands;

import java.util.UUID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;

/**
 * opens a rename dialoge and calls the function {@link ProjectManager#renameProject(UUID, String)}
 * 
 * @author Lukas Balzer
 * @since 1.0
 */
public class CloseProjectEditors extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Object selection = HandlerUtil.getCurrentSelection(event);
    if (selection instanceof IProjectSelection) {
      ((IProjectSelection) selection).closeEditors();
    }
    return null;
  }
}
