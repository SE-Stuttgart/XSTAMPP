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

import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.roles.AbstractUser;

/**
 * Creates a {@link LoginShell} which is used to create a user.
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public class ChangeUserShell extends AbstractUserShell {

  private IUser user;

  /**
   * Constructs the UserShell and changes the title to <code>Create User</code> and the ok/accept
   * button's label to <code>Create</code>.
   * 
   * @param userSystem
   *          the user system in which the user should be created
   */
  public ChangeUserShell(UserSystem userSystem, IUser user) {
    super(userSystem, true);
    this.user = user;
    setTitle("Change User");
    setAcceptLabel("Change");
    setSelectedUser(user);
    setUserLabelStyle(SWT.READ_ONLY);
  }

  @Override
  public void open() {
    if (getUserSystem().getCurrentUserId().equals(user.getUserId())
        || getUserSystem().checkAccess(AccessRights.ADMIN)) {
      super.open();
    }
  }

  @Override
  protected boolean doAccept() {
    if (getUserSystem().getCurrentUserId().equals(user.getUserId())
        || getUserSystem().checkAccess(AccessRights.ADMIN) && user instanceof AbstractUser) {
      getUserSystem().setPassword(user.getUserId(),getPassword());
    }
    return true;
  }

  @Override
  protected boolean validate() {
    return !getPassword().isEmpty();
  }
}
