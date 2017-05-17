/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement.ui.contribution;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import xstampp.Activator;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.usermanagement.Messages;
import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;
import xstampp.util.ColorManager;
import xstampp.util.STPAPluginUtils;

/**
 * A toolbar {@link WorkbenchWindowControlContribution} which displays depending on the selected
 * {@link IProjectSelection} in the workbench one of three contents.
 * <ol>
 * <li><i>Login Button</i> - to log into an {@link IUserProject} that contains a user system
 * <li><i>Login Label</i> - shows the current login of the selected {@link IUserProject}'s user
 * system
 * <li><i>'Create User' Button</i> - to create a new user system in the selected
 * {@link IUserProject}
 * </ol>
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public class UserContribution extends WorkbenchWindowControlContribution
    implements ISelectionListener, Observer {

  private static final String ADMIN_COLOR = "Admin_Color"; //$NON-NLS-1$
  private static final String USER_COLOR = "User_Color"; //$NON-NLS-1$
  private static final String READONLY_COLOR = "ReadOnly_Color"; //$NON-NLS-1$
  private static final String _COMMAND = "xstampp.command.openProjectSettings"; //$NON-NLS-1$
  private static final String _PARAM = "xstampp.commandParameter.project.settings"; //$NON-NLS-1$
  private static final String _PAGE = "xstampp.settings.users"; //$NON-NLS-1$
  private UUID projectId;
  private Listener listener;
  private IUserProject userProject;
  private UserSystem userSystem;

  public UserContribution() {
    ColorManager.registerColor(USER_COLOR, new RGB(116, 214, 137));
    ColorManager.registerColor(ADMIN_COLOR, new RGB(122, 195, 251));
    ColorManager.registerColor(READONLY_COLOR, new RGB(181, 205, 180));
  }

  @Override
  protected Control createControl(Composite parent) {
    final Composite control = new Composite(parent, SWT.NONE);
    control.setLayout(new FormLayout());
    FormData data = new FormData();
    data.left = new FormAttachment(0);
    data.right = new FormAttachment(100);
    data.width = 100;
    data.height = 20;

    getWorkbenchWindow().getSelectionService().addSelectionListener(this);

    final Button newBtn = new Button(control, SWT.PUSH);
    newBtn.setLayoutData(data);
    newBtn.setToolTipText(Messages.UserContribution_OpenUserSettingsTip);
    newBtn.setVisible(false);
    newBtn.setEnabled(false);
    newBtn.setImage(Activator.getImage("/icons/usermanagement/users.png")); //$NON-NLS-1$
    newBtn.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent event) {
        Map<String, String> values = new HashMap<>();
        values.put(_PARAM, _PAGE);
        STPAPluginUtils.executeParaCommand(_COMMAND, values);
      }
    });

    final Button loginContribution = new Button(control, SWT.PUSH);
    loginContribution.setLayoutData(data);
    loginContribution.setText(Messages.UserContribution_LoginLabel);
    loginContribution.setVisible(false);
    loginContribution.setImage(Activator.getImage("/icons/usermanagement/users.png")); //$NON-NLS-1$
    loginContribution.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent event) {
        userProject.getUserSystem().getCurrentUser();
      }
    });

    final Button label = new Button(control, SWT.NONE);
    label.setLayoutData(data);
    label.setVisible(false);
    label.setImage(Activator.getImage("/icons/usermanagement/users.png")); //$NON-NLS-1$
    label.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent event) {
        Map<String, String> values = new HashMap<>();
        values.put(_PARAM, _PAGE);
        STPAPluginUtils.executeParaCommand(_COMMAND, values);
      }
    });
    listener = new Listener() {

      @Override
      public void handleEvent(Event event) {
        try {
          LoginEvent loginEvent = (LoginEvent) event;
          label.setToolTipText(loginEvent.toolTip);
          newBtn.setEnabled(!loginEvent.isDiasbled);
          newBtn.setVisible(!(loginEvent.loginVisible || loginEvent.labelVisible));
          loginContribution.setVisible(loginEvent.loginVisible);
          label.setVisible(loginEvent.labelVisible);
          label.setText(loginEvent.accountName);
          label.setBackground(ColorManager.color(loginEvent.userColorConstant));
        } catch (Exception exc) {
          // just skip the event in case of an exception
        }
      }
    };
    parent.getParent().setRedraw(true);

    ISelection selection = getWorkbenchWindow().getSelectionService().getSelection();
    selectionChanged(null, selection);
    return control;
  }

  @Override
  public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    this.setVisible(selection instanceof IProjectSelection);
    if (selection instanceof IProjectSelection
        && !((IProjectSelection) selection).getProjectId().equals(projectId)) {
      projectId = ((IProjectSelection) selection).getProjectId();
      getWorkbenchWindow().getShell().getDisplay().asyncExec(new Runnable() {

        @Override
        public void run() {
          refresh(ProjectManager.getContainerInstance().getDataModel(projectId));
        }
      });

    }
  }

  private void refresh(IDataModel model) {
    LoginEvent event = new LoginEvent();
    if (model instanceof IUserProject) {
      event.isDiasbled = false;
      if (userProject != null) {
        userProject.deleteObserver(this);
      }
      userProject = (IUserProject) model;
      userProject.addObserver(this);
      IUserSystem userSystem = userProject.getUserSystem();
      UUID userId = userSystem.getCurrentUserId();
      if (userSystem instanceof UserSystem) {
        event.loginVisible = true;
        if (this.userSystem != null) {
          this.userSystem.deleteObserver(this);
        }
        this.userSystem = ((UserSystem) userSystem);
        this.userSystem.addObserver(this);
      }
      if (userId != null) {
        IUser user = userSystem.getCurrentUser();
        event.accountName = user.getUsername();
        if (user.checkAccess(AccessRights.ADMIN)) {
          event.userColorConstant = ADMIN_COLOR;
          event.toolTip = Messages.UserContribution_LoggedAsAdministrator;
        } else if (user.checkAccess(AccessRights.WRITE)) {
          event.toolTip = Messages.UserContribution_LoggedAsUser;
        } else {
          event.toolTip = Messages.UserContribution_ReadOnlyAccsessToolTip;
          event.userColorConstant = READONLY_COLOR;
        }

        event.toolTip += "\n\n" + Messages.UserContribution_OpenUserSettingsTip;
        event.labelVisible = true;
        event.loginVisible = false;
      }
    }
    listener.handleEvent(event);
  }

  @Override
  public void dispose() {
    if (this.userSystem != null) {
      this.userSystem.deleteObserver(this);
    }
    if (userProject != null) {
      userProject.deleteObserver(this);
    }
    super.dispose();

  }

  class LoginEvent extends Event {
    private boolean labelVisible;
    private boolean loginVisible;
    private String accountName;
    private String toolTip;
    private String userColorConstant;
    private boolean isDiasbled;

    private LoginEvent() {
      this.isDiasbled = true;
      this.labelVisible = false;
      this.loginVisible = false;
      accountName = ""; //$NON-NLS-1$
      toolTip = ""; //$NON-NLS-1$
      userColorConstant = USER_COLOR;
    }
  }

  @Override
  public void update(Observable arg0, Object arg1) {
    if (arg1 != null
        && (arg1.equals(IUserSystem.NOTIFY_LOGIN) || arg1.equals(ObserverValue.UserSystem))) {
      refresh(userProject);
    }
  }

}
