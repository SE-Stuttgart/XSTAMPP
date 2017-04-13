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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserSystem;
import xstampp.usermanagement.roles.Admin;

/**
 * An admin which can access and manipulate all files.
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public class UserSystem implements IUserSystem {
  private UUID systemId;
  private List<IUser> registry;
  private IUser currentUser;

  private UserSystem() {
    this.registry = new ArrayList<>();
  }

  /* (non-Javadoc)
   * @see xstampp.usermanagement.IUserSystem#getUserSystem()
   */
  @Override
  public IUserSystem getUserSystem() {
    CreateAdmin create = new CreateAdmin(null);
    create.setTitle("Create initial User");
    IUser admin = create.open();
    UserSystem system = null;
    if (admin != null) {
      system = new UserSystem();
      system.systemId = UUID.randomUUID();
      system.registry.add(admin);
    }
    return system;
  }

  /* (non-Javadoc)
   * @see xstampp.usermanagement.IUserSystem#createUser()
   */
  @Override
  public boolean createUser() {
    boolean result = false;
    if (currentUser instanceof Admin) {
      CreateUser create = new CreateUser(this);
      IUser user = create.open();
      if (user != null) {
        result = this.registry.add(user);
      }
    }
    return result;
  }

  /* (non-Javadoc)
   * @see xstampp.usermanagement.IUserSystem#canAccess(java.util.UUID)
   */
  @Override
  public boolean canAccess(UUID entryId) {
    return new LoginShell(this, true).open().checkAccess(entryId);
  }
  
  public List<IUser> getRegistry() {
    return registry;
  }

  /* (non-Javadoc)
   * @see xstampp.usermanagement.IUserSystem#canAccess(java.util.UUID)
   */
  @Override
  public UUID getSystemId() {
    return systemId;
  }
  
  public IUser getCurrentUser() {
    return currentUser;
  }
}
