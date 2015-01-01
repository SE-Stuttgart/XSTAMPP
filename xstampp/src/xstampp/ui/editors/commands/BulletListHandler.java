package xstampp.ui.editors.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.editors.interfaces.ITextEditor;

public class BulletListHandler extends AbstractHandler {

	public BulletListHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object activeEditor = PlatformUI.getWorkbench().
				getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		String bulletType= event.getParameter("xstampp.commandParameter.bulletlist");
		if(activeEditor instanceof ITextEditor && bulletType != null){
			((ITextEditor) activeEditor).setBullet(bulletType);
		}
		return null;
	}

}
