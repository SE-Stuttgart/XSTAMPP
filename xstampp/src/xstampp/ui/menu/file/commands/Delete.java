package xstampp.ui.menu.file.commands;

import java.util.UUID;

import messages.Messages;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.ui.common.ViewContainer;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectSelector;

public class Delete extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection instanceof ProjectSelector) {
			UUID projectId = ((IProjectSelection) currentSelection)
					.getProjectId();
			String projName = ViewContainer.getContainerInstance().getTitle(
					projectId);

			if (MessageDialog.openConfirm(
					Display.getCurrent().getActiveShell(),
					Messages.DeleteProject,
					String.format(Messages.DeleteProjectConfirmMsg, projName))
					&& !ViewContainer.getContainerInstance().removeProjectData(
							projectId)) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						Messages.Error, Messages.DeleteFailedMsg);
			}
		}
		return null;
	}

}
