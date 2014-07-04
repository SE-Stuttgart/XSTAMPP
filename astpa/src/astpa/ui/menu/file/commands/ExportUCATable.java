package astpa.ui.menu.file.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import astpa.ui.common.ViewContainer;
import astpa.ui.common.ViewContainer.ExportConstants;

public class ExportUCATable extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ViewContainer viewContainer =
				(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.findView(ViewContainer.ID);
			return viewContainer.openExportWizard(ExportConstants.UCA_TABLE);
	}

}
