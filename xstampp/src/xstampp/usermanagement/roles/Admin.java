/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement.roles;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUserSystem;

/**
 * An admin which can access and manipulate all files.
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
@XmlRootElement(name = "admin")
@XmlAccessorType(XmlAccessType.NONE)
public class Admin extends AbstractUser {

  public Admin() {
    this("", ""); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public Admin(String username, String password) {
    super(username, password);
  }

  @Override
  public boolean checkAccess(AccessRights accessRight) {
    return true;
  }

  @Override
  public void addResponsibility(UUID responsibility) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean deleteUser(IUserSystem system) {
    return system.deleteAdmin(getUserId());
  }

}
