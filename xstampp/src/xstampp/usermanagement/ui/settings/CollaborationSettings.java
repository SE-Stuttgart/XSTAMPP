/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer- initial API and implementation
 ******************************************************************************/
package xstampp.usermanagement.ui.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.projectsettings.ISettingsPage;
import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.Messages;
import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.ICollaborationSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;

public class CollaborationSettings implements ISettingsPage, Observer {

  private IUserProject dataModel;
  private Listener listener;
  ICollaborationSystem system;
  private String name;

  @Override
  public Composite createControl(final CTabFolder control, final ModalShell parent, UUID modelId) {
    if (!setDataModel(ProjectManager.getContainerInstance().getDataModel(modelId))) {
      return new Composite(control, SWT.None);
    }
    if (getDataModel().getUserSystem() instanceof UserSystem) {
      ((UserSystem) getDataModel().getUserSystem()).addObserver(this);
    } else {
      getDataModel().addObserver(this);
    }
    final ScrolledComposite content = new ScrolledComposite(control, SWT.V_SCROLL);
    content.setExpandVertical(true);
    content.setMinHeight(1000);
    final Composite tableComposite = new Composite(content, SWT.NONE);
    content.setContent(tableComposite);
    // Expand both horizontally and vertically
    content.setExpandHorizontal(true);
    content.setExpandVertical(true);
    tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    tableComposite.setLayout(new GridLayout());
    listener = new Listener() {
      List<Composite> listEntries = new ArrayList<>();

      @Override
      public void handleEvent(Event event) {
        for (Composite comp : listEntries) {
          comp.dispose();
        }
        final UserSystem userSystem = (UserSystem) getDataModel().getUserSystem();
        boolean evenRow = true;
        for (final IUser user : userSystem.getRegistry()) {
          if (user.checkAccess(AccessRights.ACCESS) && !user.checkAccess(AccessRights.ADMIN)) {
            final Composite composite = new Composite(tableComposite, SWT.None);
            composite.setLayout(new GridLayout(2, false));
            composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            int bgConstant = SWT.COLOR_WHITE;
            if (evenRow) {
              bgConstant = SWT.COLOR_LIST_BACKGROUND;
            }
            evenRow = !evenRow;
            composite.setBackground(Display.getCurrent().getSystemColor(bgConstant));
            Text userName = new Text(composite, SWT.READ_ONLY);
            userName.setLayoutData(new GridData(150, SWT.DEFAULT));
            userName.setText(user.getUsername());
            userName.setBackground(Display.getCurrent().getSystemColor(bgConstant));
            final Button createBtn = new Button(composite, SWT.PUSH);
            IDataModel model = ProjectManager.getContainerInstance()
                .getDataModel(user.getWorkingProjectId());
            if (model == null) {
              userSystem.assignWorkProject(user.getUserId(), null);
            }

            if (user.getWorkingProjectId() == null) {
              // if the user has no current working project a working copy of the current project
              // can be
              // assigned
              createBtn.setText(Messages.CollaborationSettings_CreateWorkingCopy);
              createBtn.setToolTipText(Messages.CollaborationSettings_WorkingCopyToolTip);
              final UUID originalId = getDataModel().getProjectId();
              createBtn.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                  IDataModel model = ProjectManager.getContainerInstance().getDataModel(originalId);
                  if (model != null && user.getWorkingProjectId() == null) {
                    String projectName = model.getProjectName();
                    projectName += "_" + user.getUsername(); //$NON-NLS-1$
                    UUID uuid = ProjectManager.getContainerInstance().startUp(model.getClass(),
                        projectName, originalId);

                    userSystem.assignWorkProject(user.getUserId(), uuid);
                    IDataModel newModel = ProjectManager.getContainerInstance()
                        .getDataModel(user.getWorkingProjectId());
                    ((IUserProject) newModel).setExclusiveUserId(user.getUserId());
                    createBtn.setText(String.format(Messages.CollaborationSettings_PullChanges,
                        newModel.getProjectName()));
                    composite.layout();
                    createBtn.addSelectionListener(new SelectionAdapter() {
                      boolean initialCall = true;

                      @Override
                      public void widgetSelected(SelectionEvent event) {
                        if (initialCall) {
                          // This listener is called directly after it is added so
                          initialCall = false;
                        } else {
                          system.syncDataWithUser(user);
                        }
                      }
                    });
                  }
                }
              });
            } else {
              createBtn.setText(String.format(Messages.CollaborationSettings_PullChanges,
                  model.getProjectName()));
              createBtn.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                  system.syncDataWithUser(user);
                }
              });
            }
            listEntries.add(composite);
          }
        }
        tableComposite.layout();
        content.setMinHeight(tableComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        content.layout();
        content.changed(new Control[] { tableComposite });
      }
    };
    listener.handleEvent(null);
    return content;
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public boolean doAccept() {
    return true;
  }

  @Override
  public boolean isVisible(UUID projectId) {
    IDataModel model = ProjectManager.getContainerInstance().getDataModel(projectId);
    boolean isVisible = model instanceof IUserProject;
    isVisible &= (((IUserProject) model).getUserSystem() instanceof UserSystem);
    isVisible &= ((IUserProject) model).getUserSystem().checkAccess(AccessRights.ADMIN);
    system = model.getAdapter(ICollaborationSystem.class);
    isVisible &= system != null;
    return isVisible;
  }

  public IUserProject getDataModel() {
    return dataModel;
  }

  /**
   * Sets the data model if it is of type {@link IUserProject}.
   * 
   * @param dataModel
   *          the dataModel to set
   * @return if the data model could be casted to {@link IUserProject}
   */
  public boolean setDataModel(IDataModel dataModel) {
    if (dataModel instanceof IUserProject) {
      this.dataModel = (IUserProject) dataModel;
      return true;
    }
    return false;
  }

  @Override
  public void update(Observable model, Object arg) {
    try {
      if (arg != null
          && (arg.equals(IUserSystem.NOTIFY_LOGIN) || arg.equals(IUserSystem.NOTIFY_USER))) {
        listener.handleEvent(null);

        if (getDataModel().getUserSystem() instanceof UserSystem) {
          ((UserSystem) getDataModel().getUserSystem()).addObserver(this);
        }
      }
    } catch (Exception exc) {
      model.deleteObserver(this);
    }
  }

  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getId() {
    return "xstampp.settings.collaboration";
  }
}
