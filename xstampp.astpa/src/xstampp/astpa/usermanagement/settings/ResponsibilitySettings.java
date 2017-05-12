package xstampp.astpa.usermanagement.settings;

import messages.Messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.projectsettings.ISettingsPage;
import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.api.EmptyUserSystem;
import xstampp.usermanagement.api.IUserProject;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * 
 * @author Lukas Balzer
 *
 */
public class ResponsibilitySettings implements ISettingsPage {

  private Map<ISettingsPage,String> pages;
  public ResponsibilitySettings() {
    super();
    this.pages = new HashMap<>();
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public boolean doAccept() {
    boolean result = true;
    for (ISettingsPage page : pages.keySet()) {
      result &= page.doAccept();
    }
    return result;
  }

  @Override
  public Composite createControl(CTabFolder control, ModalShell parent, UUID modelId) {

    final CTabFolder folder = new CTabFolder(control, SWT.BORDER);
    folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    folder.setSimple(false);
    folder.setMinimizeVisible(false);
    folder.setMaximizeVisible(false);
    folder.setUnselectedCloseVisible(false);
    pages.put(new AccidentResponsibilityPage(), Messages.Accidents);
    pages.put(new SCResponsibilityPage(), Messages.SafetyConstraints);
    pages.put(new ControlActionsResponsibilityPage(), Messages.ControlActions);
    for (Entry<ISettingsPage, String> page : this.pages.entrySet()) {
      CTabItem item = new CTabItem(folder, SWT.None);
      item.setText(page.getValue());
      item.setControl(page.getKey().createControl(folder, parent, modelId));
    }
    folder.setSelection(0);
    return folder;
  }

  @Override
  public boolean isVisible(UUID projectId) {
    IDataModel model = ProjectManager.getContainerInstance().getDataModel(projectId);
    return model instanceof IUserProject
        && !(((IUserProject) model).getUserSystem()instanceof EmptyUserSystem);
  }

}
