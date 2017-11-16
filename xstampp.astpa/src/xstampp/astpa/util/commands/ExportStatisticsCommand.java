package xstampp.astpa.util.commands;

import java.io.File;
import java.util.UUID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.util.jobs.ExportStatisticsJob;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.STPAEditorInput;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.util.STPAPluginUtils;

public class ExportStatisticsCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		UUID projectId = null;
		Object currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection == null) {
			IEditorInput activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor().getEditorInput();
			if (activeEditor instanceof STPAEditorInput) {
				projectId = ((STPAEditorInput) activeEditor).getProjectID();
			}
		} else if (currentSelection instanceof IProjectSelection) {
			projectId = ((IProjectSelection) currentSelection).getProjectId();
		}
		String force = event.getParameter("xstampp.astpa.commandParameter.statistics.force");
		boolean forceGeneration = false;
		if (force != null && force.toLowerCase().equals("true")) {
			forceGeneration = true;
		}
		if (projectId == null && ProjectManager.getContainerInstance().getDataModel(projectId) instanceof DataModelController) {
			String title = ProjectManager.getContainerInstance().getTitle(projectId);
			DataModelController dataModel = (DataModelController) ProjectManager.getContainerInstance().getDataModel(projectId);
			File file = new File(
					Platform.getInstanceLocation().getURL().getPath() + File.separator + "Statistics" + title + "index.html");
			if ( !file.exists() || forceGeneration) {
				file.mkdirs();
				ExportStatisticsJob job = new ExportStatisticsJob("Export Statistics", dataModel, file);
				job.schedule();
			}
			STPAPluginUtils.OpenInFileBrowser(file.getAbsolutePath());

		}
		return null;
	}

}
