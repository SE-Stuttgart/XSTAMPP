package xstampp.ui.menu.file.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.ui.common.ViewContainer;

/**
 * this handler simply passes the call to {@link ViewContainer#saveAllDataModels()}
 *
 * @author Lukas Balzer
 * @since 1.0
 */
public class SaveAll extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return ViewContainer.getContainerInstance().saveAllDataModels();
	}

}
