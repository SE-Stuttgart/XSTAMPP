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

import xstampp.ui.common.ProjectManager;
import xstampp.util.STPAPluginUtils;

/**
 * this handler simply passes the call to
 * {@link ProjectManager#saveAllDataModels()}
 *
 * @author Lukas Balzer
 * @since 1.0
 */
public class SaveAll extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    STPAPluginUtils.executeCommand("org.eclipse.ui.file.saveAll"); //$NON-NLS-1$
    return ProjectManager.getContainerInstance().saveAllDataModels();
  }

}
