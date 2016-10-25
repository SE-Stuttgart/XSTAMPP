package xstampp.ui.menu.file.commands;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.navigation.AbstractSelector;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.util.STPAPluginUtils;

public class OpenPathHandler extends AbstractHandler {

  public static final String FOLDER_PATH = "xstampp.commandParameter.openFolder"; //$NON-NLS-1$

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    String path = event.getParameter(FOLDER_PATH);
    if (path == null) {
      return null;
    }
    if (path.equals("instance")) { //$NON-NLS-1$
      STPAPluginUtils.OpenInFileBrowser(new File(Platform.getInstanceLocation().getURL().getFile()).getAbsolutePath());
    }
    ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
    if (path.equals("output") && selection instanceof IProjectSelection) { //$NON-NLS-1$
      String outputFolder = Platform.getInstanceLocation().getURL().getFile() + File.separator;
      outputFolder += ((AbstractSelector) selection).getProjectOutput();
      STPAPluginUtils.OpenInFileBrowser(new File(outputFolder).getAbsolutePath());
    }
    if (path.equals("log")) { //$NON-NLS-1$
      STPAPluginUtils.OpenInFileBrowser(Platform.getLogFileLocation().toFile().getAbsolutePath());
    }

    return null;
  }

}
