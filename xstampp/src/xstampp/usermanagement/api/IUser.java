/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement.api;

import java.util.List;
import java.util.UUID;

/**
 * The API to access a user entry on the platform.
 * 
 * @author Lukas Balzer - initial implementation and API
 *
 */
public interface IUser {

  UUID getUserId();

  String getUsername();

  /**
   * This method changes the existing username of the user if given the correct current password.
   * 
   * @param password
   *          the password that is currently set and which has to be correctly given to change the
   *          username
   * @param username
   *          the new username which should be set for this user
   * @return whether the username could be changed
   */
  boolean setUsername(String password, String username);

  boolean verifyPassword(String password);

  /**
   * checks whether the user is allowed to access the given resource
   * 
   * @param entryId
   *          the id of the entry that shall be manipulated
   * @param accessLevel
   *          The level the Access takes place e.g. {@link AccessRights#CREATE},
   *          {@link AccessRights#WRITE}
   *
   * @return if the user is allowed to access.
   */
  public boolean checkAccess(UUID entryId, AccessRights accessLevel);

  /**
   * checks whether the user is allowed to access the given access level
   *
   * @param accessRight
   *          The level the Access takes place e.g. {@link AccessRights#CREATE},
   *          {@link AccessRights#WRITE}
   *
   * @return if the user is allowed to access.
   */
  public boolean checkAccess(AccessRights accessRight);

  /**
   * Checks whether or not this user is responsible for the entry listed in the project by the given
   * id.
   * 
   * @param responsibility
   *          a UUID for an entry in the project
   * @return whether the referenced entry is registered as responsibility of this user
   */
  boolean isResponibleFor(UUID responsibility);

  List<UUID> getResponsibilities();

  /**
   * 
   * @return an id for a project stored in the current workspace.
   */
  UUID getWorkingProjectId();

}