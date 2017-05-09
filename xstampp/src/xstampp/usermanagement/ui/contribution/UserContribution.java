/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement.ui.contribution;

import java.util.UUID;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
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
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.api.IProjectSelection;
import xstampp.usermanagement.api.EmptyUserSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;

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
    implements ISelectionListener {

  private UUID projectId;
  private IUserProject dataModel;
  private Listener listener;
  private boolean labelVisible;
  private boolean loginVisible;
  private boolean loginEnabled;
  private String accountName;

  public UserContribution() {
    this.labelVisible = false;
    this.loginVisible = true;
    this.loginEnabled = true;
  }

  @Override
  protected Control createControl(Composite parent) {
    Composite control = new Composite(parent, SWT.NONE);
    control.setLayout(new FormLayout());
    FormData data = new FormData();
    data.left = new FormAttachment(0);
    data.right = new FormAttachment(100);
    data.width = 100;
    data.height = 20;

    getWorkbenchWindow().getSelectionService().addSelectionListener(this);
    final Button loginContribution = new Button(control, SWT.PUSH);
    loginContribution.setLayoutData(data);
    loginContribution.setText("Login");
    loginContribution.setVisible(loginVisible);
    loginContribution.setEnabled(loginEnabled);
    loginContribution.setImage(Activator.getImage("/icons/usermanagement/users.png")); //$NON-NLS-1$

    final CLabel label = new CLabel(control, SWT.NONE);
    label.setLayoutData(data);
    label.setVisible(labelVisible);
    label.setImage(Activator.getImage("/icons/usermanagement/users.png")); //$NON-NLS-1$
    listener = new Listener() {

      @Override
      public void handleEvent(Event event) {
        loginContribution.setVisible(loginVisible);
        if (accountName != null) {
          label.setVisible(true);
          label.setText(accountName);
          loginContribution.setVisible(false);
        } else {
          label.setVisible(false);
          loginContribution.setVisible(true);
        }
      }
    };
    parent.getParent().setRedraw(true);
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
    if (model instanceof IUserProject) {
      this.dataModel = (IUserProject) model;
      IUserSystem userSystem = dataModel.getUserSystem();
      UUID userId = userSystem.getCurrentUserId();
      accountName = null;
      if (userId != null) {
        for (IUser user : userSystem.getRegistry()) {
          if (user.getUserId().equals(userId)) {
            accountName = user.getUsername();
            break;
          }
        }
      } else if (userSystem instanceof EmptyUserSystem) {
        loginVisible = true;
      } else {
        loginVisible = true;
      }
      this.loginEnabled = true;
    } else {
      this.loginEnabled = false;
    }
    listener.handleEvent(null);
  }

}
