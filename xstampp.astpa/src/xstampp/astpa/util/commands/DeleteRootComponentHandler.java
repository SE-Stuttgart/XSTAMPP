/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.astpa.util.commands;

import java.util.UUID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.messages.Messages;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.ProjectExplorer;

/**
 * 
 * @author Lukas Balzer - Initial implementation
 *
 */
public class DeleteRootComponentHandler extends AbstractHandler {
  public static final String ROOT_ID = "xstampp.astpa.cs.rootId"; //$NON-NLS-1$

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    String idString = event.getParameter("xstampp.dynamicStep.commandParameter.projectId"); //$NON-NLS-1$
    String rootIdString = event.getParameter(ROOT_ID);
    // if the currentSelection is a stepSelector than it is transfered in a
    // proper object
    try {
      UUID id = UUID.fromString(idString);
      UUID rootId = UUID.fromString(rootIdString);
      if (ProjectManager.getContainerInstance().canAccess(id)) {
        IControlStructureEditorDataModel dataModel = (IControlStructureEditorDataModel) ProjectManager
            .getContainerInstance().getDataModel(id);
        dataModel.setActiveRoot(rootId);
        IRectangleComponent component = dataModel.getComponent(rootId);
        String title = String.format(Messages.DeleteControlStructureTitle, component.getText());
        String message = String.format(Messages.DeleteControlStructureQuestion,
            component.getText());

        if (MessageDialog.openConfirm(
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title, message)
            && dataModel.removeComponent(rootId)) {
          IViewPart navi = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
              .findView("astpa.explorer"); //$NON-NLS-1$
          if (navi != null) {
            ((ProjectExplorer) navi).updateProjects();
          }
        }
      }
    } catch (Exception exc) {
      exc.printStackTrace();
    }
    return null;
  }

}