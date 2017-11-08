package xstampp.usermanagement.api;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.ui.navigation.IProjectSelection;
import xstampp.util.STPAPluginUtils;

public class LoginHandler extends AbstractHandler {
  private static final String _COMMAND = "xstampp.command.openProjectSettings"; //$NON-NLS-1$
  private static final String _PARAM = "xstampp.commandParameter.project.settings"; //$NON-NLS-1$
  private static final String _PAGE = "xstampp.settings.users"; //$NON-NLS-1$

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    if (selection instanceof IProjectSelection
        && ((IProjectSelection) selection).getProjectData() instanceof IUserProject) {
      IUserSystem system = ((IUserProject) ((IProjectSelection) selection).getProjectData())
          .getUserSystem();
      Map<String, String> values = new HashMap<>();
      values.put(_PARAM, _PAGE);

      if (system != null) {
        system.getCurrentUser();
      } else {
        STPAPluginUtils.executeParaCommand(_COMMAND, values);
      }
    }

    return null;
  }

}
