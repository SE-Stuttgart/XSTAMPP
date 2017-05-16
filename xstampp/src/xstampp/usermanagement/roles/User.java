/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xstampp.usermanagement.api.AccessRights;

/**
 * A normal user which can be given certain responsibilities to access files.
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.NONE)
public class User extends AbstractUser {

  @XmlAttribute(name = "accessLevel")
  private List<AccessRights> accessLevel;

  public User() {
    this("", "", AccessRights.READ_ONLY); //$NON-NLS-1$ //$NON-NLS-2$
    this.accessLevel = new ArrayList<>();
  }

  public User(String username, String password, AccessRights accessRights) {
    this(username, password, new AccessRights[] { accessRights });
  }

  /**
   * Constructs a new user with the given name and password that has the accessRights which are
   * given plus a general {@link AccessRights#ACCESS}.
   * 
   * @param username
   *          The name of the user
   * @param password
   *          the password which is used to authorize the user
   * @param accessRights
   *          the initial access rights that the user possesses these can be changed using
   *          {@link #giveAccessLevel(AccessRights)}
   */
  public User(String username, String password, AccessRights[] accessRights) {
    super(username, password);
    this.accessLevel = new ArrayList<>();
    for (AccessRights accessRight : accessRights) {
      this.accessLevel.add(accessRight);
    }
  }

  @Override
  public boolean checkAccess(AccessRights accessRight) {
    return this.accessLevel.contains(accessRight);
  }

  public void giveAccessLevel(AccessRights accessLevel) {
    this.accessLevel.add(accessLevel);
  }
}
