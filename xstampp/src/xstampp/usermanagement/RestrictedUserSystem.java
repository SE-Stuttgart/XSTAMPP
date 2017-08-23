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
package xstampp.usermanagement;

import java.util.UUID;

import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.EmptyUserSystem;
import xstampp.usermanagement.api.IUserSystem;

public class RestrictedUserSystem extends EmptyUserSystem implements IUserSystem {

  @Override
  public boolean checkAccess(UUID entryId, AccessRights accessRight) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean checkAccess(AccessRights accessRight) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isResponsible(UUID userId, UUID entryId) {
    return false;
  }

  @Override
  public boolean isResponsible(UUID entryId) {
    return false;
  }

}
