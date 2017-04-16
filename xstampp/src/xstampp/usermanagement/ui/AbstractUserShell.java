/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import xstampp.ui.common.ModalShell;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserSystem;

/**
 * A {@link ModalShell} which has a content consisting of two entry fileds for username and
 * password.
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public abstract class AbstractUserShell extends ModalShell {
  private final IUserSystem userSystem;
  private boolean hidePassword;
  private String password;
  private String username;
  private IUser selectedUser;

  /**
   * Constructs a {@link ModalShell} with <code>User</code> as title .
   * 
   * @param userSystem
   *          The user system in which the user wants to login
   * @param hidePassword
   *          whether of not the password input should hide or show characters
   */
  public AbstractUserShell(IUserSystem userSystem, boolean hidePassword) {
    super("User");
    setSize(300, 200);
    this.selectedUser = null;
    this.userSystem = userSystem;
    this.hidePassword = hidePassword;
  }

  @Override
  protected void createCenter(Shell shell) {
    GridData labelData = new GridData(SWT.FILL, SWT.BOTTOM, true, true, 2, 1);

    Label nameLabel = new Label(shell, SWT.None);
    nameLabel.setText("Username");
    nameLabel.setLayoutData(labelData);

    Text nameText = new Text(shell, SWT.None);
    nameText.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent ev) {
        username = ((Text) ev.getSource()).getText();
        canAccept();
      }
    });
    GridData textData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
    nameText.setLayoutData(textData);

    int passwordStyle = SWT.None;
    if (hidePassword) {
      passwordStyle = SWT.PASSWORD;
    }
    Label passwordLabel = new Label(shell, passwordStyle);
    passwordLabel.setText("Password");
    passwordLabel.setLayoutData(labelData);

    Text passwordText = new Text(shell, SWT.None);
    passwordText.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent ev) {
        password = ((Text) ev.getSource()).getText();
        canAccept();
      }
    });
    passwordText.setLayoutData(textData);
  }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }

  /**
   * method which calls {@link #open()} to define the selected user if its' current value is
   * <code>null</code>. This method just returns the selected user when the value by the time of the
   * method call is not <code>null</code>.
   * 
   * @return the user that has been selected in the shell or null if none has been selected.
   */
  public IUser pullUser() {
    if (this.selectedUser == null) {
      open();
    }
    return this.selectedUser;
  }

  @Override
  protected boolean validate() {
    try {
      return !password.isEmpty() && !username.isEmpty();
    } catch (NullPointerException exc) {
      return false;
    }
  }

  public void setSelectedUser(IUser selectedUser) {
    this.selectedUser = selectedUser;
  }

  /**
   * The user system with which this user shell is interacting.
   * 
   * @return the userSystem
   */
  public IUserSystem getUserSystem() {
    return userSystem;
  }
}
