package xstampp.astpa.usermanagement.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

/**
 * 
 * @author Lukas Balzer
 *
 */
public class ResponsibilitySettings implements ISettingsPage {

  private List<ISettingsPage> pages;
  private List<String> pageTitles;
  private String name;
  
  public ResponsibilitySettings() {
    super();
    this.pages = new ArrayList<>();
    this.pageTitles = new ArrayList<>();
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public boolean doAccept() {
    boolean result = true;
    for (ISettingsPage page : pages) {
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
    pages.add(new AccidentResponsibilityPage());
    pageTitles.add(Messages.Accidents);
    pages.add(new HazardsResponsibilityPage());
    pageTitles.add(Messages.Hazards);
    pages.add(new SCResponsibilityPage());
    pageTitles.add(Messages.SafetyConstraints);
    pages.add(new ControlActionsResponsibilityPage());
    pageTitles.add(Messages.ControlActions);
    for (int i= 0; i < this.pages.size(); i++) {
      CTabItem item = new CTabItem(folder, SWT.None);
      item.setText(pageTitles.get(i));
      item.setControl(pages.get(i).createControl(folder, parent, modelId));
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

  @Override
  public String getName() {
    return xstampp.astpa.usermanagement.Messages.ResponsibilitySettings_0;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getId() {
    return "xstampp.astpa.settings.responsibilities";
  }

}
