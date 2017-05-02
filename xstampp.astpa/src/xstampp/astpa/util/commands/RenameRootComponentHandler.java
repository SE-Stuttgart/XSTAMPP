package xstampp.astpa.util.commands;

import java.util.UUID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.astpa.controlstructure.RenameControlStructureShell;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;

public class RenameRootComponentHandler extends AbstractHandler {
  public static final String ROOT_ID = "xstampp.astpa.cs.rootId";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    String idString = event.getParameter("xstampp.dynamicStep.commandParameter.projectId");
    String rootIdString = event.getParameter(ROOT_ID);
    // if the currentSelection is a stepSelector than it is transfered in a
    // proper object
    if (idString != null && rootIdString != null) {
      UUID id = UUID.fromString(idString);
      UUID rootId = UUID.fromString(rootIdString);
      if (ProjectManager.getContainerInstance().canAccess(id)) {
        IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(id);
        RenameControlStructureShell shell = new RenameControlStructureShell((IControlStructureEditorDataModel) dataModel, rootId);
        shell.open();
        return shell.getReturnValue();
      }
    }
    return null;
  }

}