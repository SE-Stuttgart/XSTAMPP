package xstampp.util;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;

public class RunExportCommand extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object selection =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ProjectExplorer.ID).getViewSite()
				.getSelectionProvider().getSelection();
		if(selection instanceof IProjectSelection){
			String command = ProjectManager.getContainerInstance().
									getConfigurationFor(((IProjectSelection) selection).getProjectId()).
									getAttribute("runCommand");
									
			STPAPluginUtils.executeCommand(command);
		}
		return null;
	}

}
