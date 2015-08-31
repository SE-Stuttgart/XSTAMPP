package xstampp.ui.menu.file.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.editors.STPAEditorInput;
import xstampp.ui.navigation.StepSelector;

/**
 *
 * @author Lukas Balzer
 * @since 1.0
 */
public class OpenStepEditor extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		Object currentSelection = PlatformUI.getWorkbench().
									getActiveWorkbenchWindow().
									getActivePage().getSelection("astpa.explorer");
		String openWithEditor = event.getParameter("xstampp.command.steps.open");
		//if the currentSelection is a stepSelector than it is transfered in a proper object
		if(!(currentSelection instanceof StepSelector )){
			return null;
		}
		StepSelector selector =((StepSelector) currentSelection);
//		if(!selector.getOpenWithPerspective().equals("")){
//			
//			String perspective = selector.getOpenWithPerspective();
//			IPerspectiveDescriptor descriptor= PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(perspective);
//			PlatformUI.getWorkbench().getPerspectiveRegistry().revertPerspective(descriptor);
//			if (descriptor != null) {
//				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
//					.getActivePage().setPerspective(descriptor);
//			}
//		
//		}
		if(openWithEditor != null){

			selector.openEditor(openWithEditor);
			
		}else {
			selector.openDefaultEditor();
		}
		return null;
	}

	
}
