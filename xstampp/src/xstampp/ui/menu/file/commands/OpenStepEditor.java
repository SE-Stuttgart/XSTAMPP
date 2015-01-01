package xstampp.ui.menu.file.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.ui.navigation.StepSelector;

public class OpenStepEditor extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		Object currentSelection = PlatformUI.getWorkbench().
									getActiveWorkbenchWindow().
									getActivePage().getSelection("astpa.explorer");
		if (currentSelection instanceof StepSelector) {
			try {
				((StepSelector) currentSelection).getDefaultEditor();
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	
}
