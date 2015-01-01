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
		Object currentSelection = PlatformUI.getWorkbench().
				getActiveWorkbenchWindow().
				getActivePage().getSelection("astpa.explorer");
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		String shouldExpand = event.getParameter("xstampp.expand");
		if (shouldExpand == null) {
			return null;
		}
		boolean exp = Boolean.parseBoolean(shouldExpand);
		if (currentSelection instanceof IProjectSelection) {
			((IProjectSelection) currentSelection).expandTree(exp);
		} else if (part instanceof ProjectExplorer) {
			((ProjectExplorer) part).expandTree(exp);
		}
		return null;
	}

}
