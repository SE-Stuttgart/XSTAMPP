package xstampp.astpa.controlstructure.controller.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.controlstructure.CSAbstractEditor;
import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.controller.editparts.CSAbstractEditPart;

public class CopyComponentsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(part instanceof CSAbstractEditor){
			return ((CSAbstractEditor) part).copy();
		}
		return false;
	}

}
