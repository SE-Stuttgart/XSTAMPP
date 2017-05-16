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

import xstampp.usermanagement.Messages;
import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.roles.Admin;

/**
 * A {@link LoginShell} that creates an administrator.
 * @author Lukas Balzer - initial implementation
 *
 */
public class CreateAdminShell extends AbstractUserShell {

  /**
   * Creates an instance with a title and an custom accept label.
   * @param userSystem the user system for which the administrator is created.
   */
  public CreateAdminShell(UserSystem userSystem) {
    super(userSystem, true);
    setTitle(Messages.CreateAdminShell_CreateAdmin);
    setAcceptLabel(Messages.AbstractUserShell_Create);
  }


  @Override
  protected boolean doAccept() {
    for (IUser user : getUserSystem().getRegistry()) {
      if (user.getUsername().equals(getUsername())) {
        invalidate(Messages.CreateUserShell_UsernameExists);
        return false;
      }
    }
    setSelectedUser(new Admin(getUsername(), getPassword()));
    return true;
  }

}
