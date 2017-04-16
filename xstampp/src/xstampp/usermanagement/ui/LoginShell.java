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

package xstampp.usermanagement.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserSystem;

/**
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public class LoginShell extends ModalShell {
  private final IUserSystem userSystem;
  private boolean hidePassword;
  private String password;
  private String username;

  public LoginShell(IUserSystem userSystem, boolean hidePassword) {
    super("Login", "Login");
    setSize(300, 200);
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
      public void modifyText(ModifyEvent e) {
        username = ((Text) e.getSource()).getText();
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
      public void modifyText(ModifyEvent e) {
        password = ((Text) e.getSource()).getText();
        canAccept();
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
          break;
        }
      }
    }
    invalidate("Input invalid!");
    return false;
  }

  @Override
  protected boolean validate() {
    try {
      return !password.isEmpty() && !username.isEmpty();
    } catch (NullPointerException exc) {
      return false;
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
  public IUserSystem getUserSystem() {
    return userSystem;
  }
}
