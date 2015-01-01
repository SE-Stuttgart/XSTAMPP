package xstampp.ui.menu.file.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.ui.common.ViewContainer;

public class SaveAll extends AbstractHandler {

	public SaveAll() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return ViewContainer.getContainerInstance().saveAllDataModels();
	}

}
