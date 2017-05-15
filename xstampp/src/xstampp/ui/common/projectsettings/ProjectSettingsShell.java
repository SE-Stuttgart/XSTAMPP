package xstampp.ui.common.projectsettings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Shell;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.Messages;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.ui.settings.CollaborationSettings;
import xstampp.usermanagement.ui.settings.UserManagementPage;

/**
 * The project settings shell is a {@link ModalShell} which displays all available settings in the
 * project. All The settings can be displayed on multiple pages that can be registered in every
 * <code>xstampp.extension.steppedProcess</code> as SettingsPage implementing the
 * {@link ISettingsPage} interface.
 * 
 * <p>
 * Additionally the settings page contains the user management for a {@link IUserProject}
 * 
 * @author Lukas Balzer
 *
 */
public class ProjectSettingsShell extends ModalShell {

  private UUID projectId;
  private List<ISettingsPage> pages;

  /**
   * Creates a {@link ModalShell} with a pre defined title:<br>
   * <code>Project Settings for [project
   * title]</code>.
   * 
   * @param projectId
   *          the unique identifier of the project with which it is registered in the
   *          {@link ProjectManager}
   */
  public ProjectSettingsShell(UUID projectId) {
    super(Messages.ProjectSettingsShell_Title
        + ProjectManager.getContainerInstance().getTitle(projectId), APPLYABLE);
    this.projectId = projectId;
    this.pages = new ArrayList<>();
    setSize(600, 400);
  }

  @Override
  protected boolean validate() {
    for (ISettingsPage page : pages) {
      if (!page.validate()) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected boolean doAccept() {
    boolean result = true;
    for (ISettingsPage page : pages) {
      result &= page.doAccept();
    }
    return result;
  }

  @Override
  protected void createCenter(Shell parent) {

    final CTabFolder folder = new CTabFolder(parent, SWT.BORDER | SWT.V_SCROLL);
    folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    folder.setSimple(false);
    folder.setMinimizeVisible(false);
    folder.setMaximizeVisible(false);
    folder.setUnselectedCloseVisible(false);

    IExtension configurationElement = ProjectManager.getContainerInstance()
        .getConfigurationFor(projectId).getDeclaringExtension();
    for (IConfigurationElement element : configurationElement.getConfigurationElements()) {
      if (element.getName().equals("SettingsPage")) { //$NON-NLS-1$
        try {
          ISettingsPage page = (ISettingsPage) element.createExecutableExtension("class"); //$NON-NLS-1$
          if (page.isVisible(projectId)) {
            CTabItem item = new CTabItem(folder, SWT.None);
            item.setText(element.getAttribute("name")); //$NON-NLS-1$
            item.setControl(page.createControl(folder, this, projectId));
            this.pages.add(page);
          }
        } catch (CoreException exc) {
          exc.printStackTrace();
        }
      }
    }
    if (ProjectManager.getContainerInstance().getDataModel(projectId) instanceof IUserProject) {
      ISettingsPage page = new UserManagementPage();
      this.pages.add(page);
      CTabItem item = new CTabItem(folder, SWT.None);
      item.setText(Messages.ProjectSettingsShell_UserManagementTitle);
      item.setControl(page.createControl(folder, this, projectId));

      page = new CollaborationSettings();
      if (page.isVisible(projectId)) {
        this.pages.add(page);
        item = new CTabItem(folder, SWT.None);
        item.setText(Messages.ProjectSettingsShell_Collaboration);
        item.setControl(page.createControl(folder, this, projectId));
      }
    }

  }

}
