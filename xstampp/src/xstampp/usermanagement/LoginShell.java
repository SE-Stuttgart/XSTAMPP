/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import xstampp.usermanagement.api.IUser;

/**
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public class LoginShell extends ModelShell {
  private final UserSystem userSystem;
  private boolean hidePassword;
  private String password;
  private String username;

  public LoginShell(UserSystem userSystem, boolean hidePassword) {
    super("Login","Login");
    this.userSystem = userSystem;
    this.hidePassword = hidePassword;
    setSelectedUser(userSystem.getCurrentUser());
  }

  @Override
  protected void createCenter(Shell shell) {
    GridData labelData = new GridData(SWT.LEFT, SWT.BOTTOM, true, true, 2, 1);

    Label nameLabel = new Label(shell, SWT.None);
    nameLabel.setText("Username");
    nameLabel.setLayoutData(labelData);

    Text nameText = new Text(shell, SWT.PASSWORD);
    nameText.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {
        username = (String) e.data;
        setUnchecked();
      }
    });
    GridData textData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
    nameText.setLayoutData(textData);

    int passwordStyle = SWT.None;
    if (hidePassword) {
      passwordStyle =SWT.PASSWORD;
    }
    Label passwordLabel = new Label(shell, passwordStyle);
    passwordLabel.setText("Password");
    passwordLabel.setLayoutData(labelData);

    Text passwordText = new Text(shell, SWT.None);
    passwordText.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {
        password = (String) e.data;
        setUnchecked();
      }
    });
    passwordText.setLayoutData(textData);
  }

  @Override
  protected boolean doAccept() {
    for (IUser user : getUserSystem().getRegistry()) {
      if (user.getUsername().equals(username)) {
        if (user.getPassword().equals(password)) {
          setSelectedUser(user);
          return true;
        } else {
          invalidate("Password or username invalid!");
        }
      }
    }
    return false;
  }

  @Override
  protected void validate() {
    if(!password.isEmpty() && !username.isEmpty()) {
      validate();
    }
  }
  
  public String getPassword() {
    return password;
  }
  
  public String getUsername() {
    return username;
  }

  /**
   * @return the userSystem
   */
  public UserSystem getUserSystem() {
    return userSystem;
  }
}
