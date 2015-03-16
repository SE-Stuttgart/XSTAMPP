package xstampp.ui.menu.file.commands;

import java.util.UUID;

import messages.Messages;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.ui.common.ViewContainer;
import xstampp.ui.navigation.IProjectSelection;

public class RenameCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IProjectSelection) {
			UUID projectId = ((IProjectSelection) selection).getProjectId();
			String currentString = ViewContainer.getContainerInstance()
					.getTitle(projectId);

			InputDialog renameDiag = new InputDialog(Display.getCurrent()
					.getActiveShell(), "Rename Project", "New Project Name: ",
					currentString, new ProjectNameValidator(projectId));
			if (renameDiag.open() == Window.OK &&
				ViewContainer.getContainerInstance().renameProject(projectId,
						renameDiag.getValue())){
				MessageDialog.openError(null, Messages.Error, "Project cannot be renamed!");
			}
		}
		return null;
	}

	private class ProjectNameValidator implements IInputValidator {

		private UUID projectId;

		public ProjectNameValidator(UUID projectId) {
			this.projectId = projectId;
		}

		@Override
		public String isValid(String newName) {
			for (UUID id : ViewContainer.getContainerInstance().getProjects()
					.keySet()) {

				if (!this.projectId.equals(id)
						&& newName.equals(ViewContainer.getContainerInstance()
								.getTitle(id))) {
					return "the Project " + newName + " already exists";
				}
			}
			return null;
		}

	}

}
