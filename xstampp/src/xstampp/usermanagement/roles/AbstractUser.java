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

package xstampp.usermanagement.roles;

import xstampp.usermanagement.api.IUser;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;


/**
 * An abstract class which defines the basis af all users in thre system.
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public abstract class AbstractUser implements IUser {

  @XmlAttribute(name = "userId", required = true)
  private UUID userId;

  @XmlAttribute(name = "username", required = true)
  private String username;

  @XmlAttribute(name = "password", required = true)
  private String password;

  /**
   * Creates a user with a username and a password and an id which can and
   * should not be changed later.
   */
  public AbstractUser(String username, String password) {
    this.username = username;
    this.password = password;
    this.userId = UUID.randomUUID();
  }

  public AbstractUser() {
    this("", ""); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /*
   * (non-Javadoc)
   * 
   * @see xstampp.usermanagement.roles.IUser#getUserId()
   */
  @Override
  public UUID getUserId() {
    return userId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see xstampp.usermanagement.roles.IUser#getUsername()
   */
  @Override
  public String getUsername() {
    return username;
  }

  /*
   * (non-Javadoc)
   * 
   * @see xstampp.usermanagement.roles.IUser#getPassword()
   */
  @Override
  public String getPassword() {
    return password;
  }

  /*
   * (non-Javadoc)
   * 
   * @see xstampp.usermanagement.roles.IUser#setUsername(java.lang.String,
   * java.lang.String)
   */
  @Override
  public boolean setUsername(String password, String username) {
    if (password.equals(this.password)) {
      this.username = username;
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see xstampp.usermanagement.roles.IUser#setPassword(java.lang.String,
   * java.lang.String)
   */
  @Override
  public boolean setPassword(String oldPassword, String newPassword) {
    if (oldPassword.equals(this.password)) {
      this.password = newPassword;
      return true;
    }
    return false;
  }
}
