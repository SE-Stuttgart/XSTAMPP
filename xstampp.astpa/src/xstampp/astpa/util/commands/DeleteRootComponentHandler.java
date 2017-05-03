package xstampp.astpa.util.commands;

import java.util.UUID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.controlstructure.RenameControlStructureShell;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.ProjectExplorer;

public class DeleteRootComponentHandler extends AbstractHandler {
  public static final String ROOT_ID = "xstampp.astpa.cs.rootId";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    String idString = event.getParameter("xstampp.dynamicStep.commandParameter.projectId");
    String rootIdString = event.getParameter(ROOT_ID);
    // if the currentSelection is a stepSelector than it is transfered in a
    // proper object
    try {
      UUID id = UUID.fromString(idString);
      UUID rootId = UUID.fromString(rootIdString);
      if (ProjectManager.getContainerInstance().canAccess(id)) {
        IControlStructureEditorDataModel dataModel = (IControlStructureEditorDataModel) ProjectManager
            .getContainerInstance().getDataModel(id);
        IRectangleComponent component = dataModel.getComponent(rootId);
        String title = "Delete Control Structure " + component.getText();
        String message = "Do you really want to delete the Control Structure " + component.getText()
            + "?\n" + "Note that this will delete all entries and is not undoable!";

        if (MessageDialog.openConfirm(
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title, message)
            && dataModel.removeComponent(rootId)) {
          IViewPart navi = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
              .findView("astpa.explorer"); //$NON-NLS-1$
          if (navi != null) {
            ((ProjectExplorer) navi).updateProjects();
          }
        }
      }
    } catch (Exception exc) {
      ProjectManager.getLOGGER().error(exc);
    }
    return null;
  }

}