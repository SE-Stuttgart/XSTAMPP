package xstampp.ui.menu.file.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.editors.StandartEditorPart;
import xstampp.util.STPAPluginUtils;

public class SelectAllHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    if (editor != null && editor instanceof StandartEditorPart) {
      ((StandartEditorPart) editor).selectAll();
    }
    STPAPluginUtils.executeCommand("org.eclipse.ui.edit.selectAll"); //$NON-NLS-1$
    return null;
  }

}
