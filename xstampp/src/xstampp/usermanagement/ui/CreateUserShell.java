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

import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserSystem;
import xstampp.usermanagement.roles.User;

/**
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public class CreateUserShell extends LoginShell {

  public CreateUserShell(IUserSystem userSystem) {
    super(userSystem, false);
    setTitle("Create User");
    setAcceptLabel("Create");
  }


  @Override
  protected boolean doAccept() {
    for (IUser user : getUserSystem().getRegistry()) {
      if (user.getUsername().equals(getUsername())) {
        invalidate("Username already exists!");
        return false;
      }
    }
    setSelectedUser(new User(getUsername(), getPassword(),AccessRights.WRITE));
    return true;
  }

}
