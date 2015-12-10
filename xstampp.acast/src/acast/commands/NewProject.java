package acast.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.menu.file.commands.CommandState;
import acast.controller.Controller;


public class NewProject extends AbstractHandler{
	
	

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		String nameParam = event.getParameter("acast.new.name"); //$NON-NLS-1$
		String pathParam = event.getParameter("acast.new.path"); //$NON-NLS-1$
		if ((nameParam == null) || (pathParam == null)) {
			return null;
		}
		
		ProjectManager.getContainerInstance().startUp(Controller.class,
				nameParam, pathParam);

		// Enable the save entries in the menu
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getService(ISourceProviderService.class);
		CommandState saveStateService = (CommandState) sourceProviderService
				.getSourceProvider(CommandState.SAVE_STATE);

		IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench()
				.getPerspectiveRegistry()
				.findPerspectiveWithId("xstampp.defaultPerspective"); //$NON-NLS-1$
		if (descriptor != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().setPerspective(descriptor);
		}
		return null;
	}

}
