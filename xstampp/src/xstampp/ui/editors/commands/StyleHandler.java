package xstampp.ui.editors.commands;

import java.lang.reflect.Field;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.editors.interfaces.ITextEditor;

public class StyleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object activeEditor = PlatformUI.getWorkbench().
				getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(activeEditor instanceof ITextEditor){
			String parameterStyle= event.getParameter("xstampp.commandParameter.style");
			((ITextEditor) activeEditor).setStyle(parameterStyle);
		}
		return null;
	}

}
