/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer- initial API and implementation
 ******************************************************************************/
package xstampp.usermanagement.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;

public class DefaultUser implements IUser {

  @Override
  public UUID getUserId() {
    return null;
  }

  @Override
  public String getUsername() {
    return "default";
  }

  @Override
  public boolean setUsername(String password, String username) {
    return false;
  }

  @Override
  public boolean checkAccess(AccessRights accessRight) {
    return false;
  }

  @Override
  public boolean verifyPassword(String password) {
    return true;
  }

  @Override
  public UUID getWorkingProjectId() {
    return null;
  }

  @Override
  public List<UUID> getResponsibilities() {
    // TODO Auto-generated method stub
    return new ArrayList<>();
  }

}
