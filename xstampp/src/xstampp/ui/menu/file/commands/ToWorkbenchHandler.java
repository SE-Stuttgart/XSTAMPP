package xstampp.ui.menu.file.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;

/**
 * this handler is only called the first time when the user starts in the welcome screen this handler is called to switch between 
 * welcome perspective and the default perspective
 * 
 * @author Lukas Balzer
 * @since 1.0
 */
public class ToWorkbenchHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench()
				.getPerspectiveRegistry()
				.findPerspectiveWithId("xstampp.defaultPerspective");//$NON-NLS-1$
		if (descriptor != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().setPerspective(descriptor);
		}
		return null;
	}

}
