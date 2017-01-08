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

import xstampp.ui.navigation.StepSelector;

/**
 *
 * @author Lukas Balzer
 * @since 1.0
 */
public class OpenStepEditor extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    Object currentSelection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getSelection("astpa.explorer"); //$NON-NLS-1$
    String openWithEditor = event.getParameter("xstampp.command.steps.open"); //$NON-NLS-1$
    // if the currentSelection is a stepSelector than it is transfered in a
    // proper object
    if (!(currentSelection instanceof StepSelector)) {
      return null;
    }
    StepSelector selector = ((StepSelector) currentSelection);
    // if(!selector.getOpenWithPerspective().equals("")){
    //
    // String perspective = selector.getOpenWithPerspective();
    // IPerspectiveDescriptor descriptor=
    // PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(perspective);
    // PlatformUI.getWorkbench().getPerspectiveRegistry().revertPerspective(descriptor);
    // if (descriptor != null) {
    // PlatformUI.getWorkbench().getActiveWorkbenchWindow()
    // .getActivePage().setPerspective(descriptor);
    // }
    //
    // }
    if (openWithEditor != null) {

      selector.openEditor(openWithEditor);

    } else {
      selector.openDefaultEditor();
    }
    return null;
  }

}
