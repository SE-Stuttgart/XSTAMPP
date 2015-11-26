package xstampp.ui.menu.file.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.ui.common.ProjectManager;
import xstampp.util.STPAPluginUtils;

/**
 * this handler simply passes the call to {@link ProjectManager#saveAllDataModels()}
 *
 * @author Lukas Balzer
 * @since 1.0
 */
public class SaveAll extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		STPAPluginUtils.executeCommand("org.eclipse.ui.file.saveAll");
		return ProjectManager.getContainerInstance().saveAllDataModels();
	}

}
