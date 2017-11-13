/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.usermanagement.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import messages.Messages;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.projectsettings.ISettingsPage;
import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.api.EmptyUserSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;

/**
 * 
 * @author Lukas Balzer
 *
 */
public class ResponsibilitySettings implements ISettingsPage {

  private List<EntryResponsibilitiesPage<?>> pages;
  private List<String> pageTitles;
  private IUserProject dataModel;

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
    if (getDataModel() != null) {
      Map<UUID, List<UUID>> map = new HashMap<>();
      for (EntryResponsibilitiesPage<?> page : pages) {
        map.putAll(page.getResult());
      }
      getDataModel().getUserSystem().assignResponsibilities(map);
    }
    return true;
  }

  @Override
  public Composite createControl(CTabFolder control, ModalShell parent, UUID modelId) {

    if (!setDataModel(ProjectManager.getContainerInstance().getDataModel(modelId))) {
      return new Composite(control, SWT.None);
    }
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
    for (int i = 0; i < this.pages.size(); i++) {
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
        && !(((IUserProject) model).getUserSystem() instanceof EmptyUserSystem);
  }

  @Override
  public String getName() {
    return xstampp.astpa.usermanagement.Messages.ResponsibilitySettings_0;
  }

  @Override
  public void setName(String name) {
    // fixed name
  }

  @Override
  public String getId() {
    return "xstampp.astpa.settings.responsibilities";
  }

  /**
   * @return the dataModel
   */
  public IUserProject getDataModel() {
    return dataModel;
  }

  /**
   * @param dataModel
   *          the dataModel to set
   */
  public boolean setDataModel(IDataModel dataModel) {
    if (dataModel instanceof IUserProject) {
      this.dataModel = (IUserProject) dataModel;
      return true;
    }
    return false;
  }

}
