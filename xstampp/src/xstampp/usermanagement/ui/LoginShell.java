/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement.ui;

import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserSystem;

/**
 * A {@link ModalShell} which has a content consisting of two entry fileds for username and
 * password.
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public class LoginShell extends AbstractUserShell {

  /**
   * Constructs a {@link ModalShell} with <code>Login</code> as title
   * and label for the ok/accept button.
   * @param userSystem The user system in which the user wants to login
   * @param hidePassword whether of not the password input should hide or show characters 
   */
  public LoginShell(IUserSystem userSystem, boolean hidePassword) {
    super(userSystem, true);
    setTitle("Login");
    setAcceptLabel("Login");
  }

  @Override
  protected boolean doAccept() {
    for (IUser user : getUserSystem().getRegistry()) {
      if (user.getUsername().equals(getUsername())) {
        if (user.verifyPassword(getPassword())) {
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
}
