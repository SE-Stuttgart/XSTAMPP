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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class ExpandHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Object currentSelection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getSelection("astpa.explorer"); //$NON-NLS-1$
    IWorkbenchPart part = HandlerUtil.getActivePart(event);
    String shouldExpand = event.getParameter("xstampp.expand"); //$NON-NLS-1$
    if (shouldExpand == null) {
      return null;
    }
    boolean exp = Boolean.parseBoolean(shouldExpand);
    if (currentSelection instanceof IProjectSelection) {
      ((IProjectSelection) currentSelection).expandTree(exp, true);
    } else if (part instanceof ProjectExplorer) {
      ((ProjectExplorer) part).expandTree(exp);
    }
    return null;
  }

}
