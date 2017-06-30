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

import messages.Messages;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.shell.ModalShell;
import xstampp.ui.common.shell.ModalShell.Style;
import xstampp.ui.navigation.IProjectSelection;

/**
 * opens a rename dialoge and calls the function {@link ProjectManager#renameProject(UUID, String)}
 * 
 * @author Lukas Balzer
 * @since 1.0
 */
public class RenameCommand extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Object selection = HandlerUtil.getCurrentSelection(event);
    if (selection instanceof IProjectSelection) {
      UUID projectId = ((IProjectSelection) selection).getProjectId();

      ProjectRenameShell renameDiag = new ProjectRenameShell(Messages.RenameCommand_RenameProject,
          projectId);
      renameDiag.open();
    }
    return null;
  }

  class ProjectRenameShell extends ModalShell {

    private TextInput textInput;
    private UUID projectId;

    public ProjectRenameShell(String title, UUID projectId) {
      super(title,Style.PACKED);
      this.projectId = projectId;
    }

    @Override
    protected boolean validate() {
      return !textInput.getText().isEmpty();
    }

    @Override
    protected boolean doAccept() {
      for (UUID id : ProjectManager.getContainerInstance().getProjects().keySet()) {

        if (!this.projectId.equals(id)
            && textInput.getText().equals(ProjectManager.getContainerInstance().getTitle(id))) {
          invalidate(Messages.RenameCommand_TheProject + textInput.getText()
              + Messages.RenameCommand_alreadyExists);
          return false;
        }
      }
      if (!ProjectManager.getContainerInstance().renameProject(projectId, textInput.getText())) {
        invalidate(Messages.RenameCommand_CannotRenameProject);
        return false;
      }
      return true;
    }

    @Override
    protected void createCenter(Shell parent) {
      String currentString = ProjectManager.getContainerInstance().getTitle(projectId);
      textInput = new TextInput(parent, SWT.NONE, "Project Name:", currentString);
    }

  }

}
