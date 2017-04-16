package xstampp.ui.common.projectSettings;

import java.util.UUID;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Shell;

import xstampp.ui.common.ModalShell;
import xstampp.ui.common.ProjectManager;
import xstampp.usermanagement.ui.UserManagementPage;

public class ProjectSettingsShell extends ModalShell {

  private UUID projectId;

  public ProjectSettingsShell(String name, String acceptLabel, UUID model) {
    super(name, acceptLabel);
    this.projectId = model;
    setSize(400, 400);
  }

  public ProjectSettingsShell(UUID model) {
    super("Project Settings");
    this.projectId = model;
    setSize(600, 400);
  }

  @Override
  protected boolean validate() {
    return false;
  }

  @Override
  protected boolean doAccept() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  protected void createCenter(Shell parent) {

    final CTabFolder folder = new CTabFolder(parent, SWT.BORDER);
    folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    folder.setSimple(false);
    folder.setMinimizeVisible(false);
    folder.setMaximizeVisible(false);
    folder.setUnselectedCloseVisible(false);

    IExtension configurationElement = ProjectManager.getContainerInstance()
        .getConfigurationFor(projectId).getDeclaringExtension();
    for (IConfigurationElement element : configurationElement.getConfigurationElements()) {
      if (element.getName().equals("SettingsPage")) {
        try {
          ISettingsPage page = (ISettingsPage) element.createExecutableExtension("class");
          CTabItem item = new CTabItem(folder, SWT.None);
          item.setText(element.getAttribute("name"));
          item.setControl(page.createControl(folder, this, projectId));
        } catch (CoreException exc) {
          exc.printStackTrace();
        }
      }
    }
    ISettingsPage page = new UserManagementPage();
    CTabItem item = new CTabItem(folder, SWT.None);
    item.setText("Users");
    item.setControl(page.createControl(folder, this, projectId));

  }

}
