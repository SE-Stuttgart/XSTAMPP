package acast.controlstructure.controller.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import acast.controlstructure.CSEditor;


public class OpenNewTool extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String newTool = event.getParameter("xstampp.acast.cseditor.toolconstant");
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(part instanceof CSEditor){
			((CSEditor) part).changeActiveTool(newTool);
		}
		return null;
	}

}
