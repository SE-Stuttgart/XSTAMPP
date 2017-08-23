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

import java.util.UUID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;
import xstampp.ui.navigation.ProjectExplorer;
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
        .getSelection(ProjectExplorer.ID); // $NON-NLS-1$
    String openWithEditor = event.getParameter("xstampp.command.steps.open"); //$NON-NLS-1$
    String selectionIdString = event.getParameter(StandartEditorPart.SELECTED_ENTRY); // $NON-NLS-1$
    UUID selectionId = null;
    if (selectionIdString != null) {
      try {
        selectionId = UUID.fromString(selectionIdString);
      } catch (IllegalArgumentException exc) {
        exc.printStackTrace();
      }
    }
    // if the currentSelection is a stepSelector than it is transfered in a
    // proper object
    if (currentSelection instanceof StepSelector) {
      StepSelector selector = ((StepSelector) currentSelection);
      if (ProjectManager.getContainerInstance().canAccess(selector.getProjectId())) {
        if (openWithEditor != null) {
          selector.openEditor(openWithEditor, selectionId);
        } else {
          selector.openDefaultEditor();
        }
      }
    }
    return null;
  }

}
