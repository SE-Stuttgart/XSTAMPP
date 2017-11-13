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
package xstampp.usermanagement.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Every {@link IUserProject} can by default assign this as its user system so
 * that Unnecessary null checks are prevented.
 * 
 * @author Lukas Balzer
 *
 */
public class EmptyUserSystem implements IUserSystem {

  private boolean defaultAccess;

  /**
   * calls {@link EmptyUserSystem#EmptyUserSystem(boolean)} with <b style="color:blue;">true</b>
   */
  public EmptyUserSystem() {
    this(true);
  }

  /**
   * Constructs an {@link EmptyUserSystem} which can be accessed by everyone or by no one depending
   * on the given boolean value
   * 
   * @param defaultAccess
   *          whether this user system grants access to its content or not
   */
  public EmptyUserSystem(boolean defaultAccess) {
    this.defaultAccess = defaultAccess;
  }

  @Override
  public boolean createUser() {
    return false;
  }

  @Override
  public boolean grantAccessTo(IUser user, AccessRights right) {
    return false;
  }

  @Override
  public UUID getSystemId() {
    return null;
  }

  /**
   * The empty user system gives access by default.
   */
  @Override
  public boolean checkAccess(UUID entryId, AccessRights accessRight) {
    return defaultAccess;
  }

  @Override
  public boolean checkAccess(AccessRights accessRight) {
    return defaultAccess;
  }

  @Override
  public List<IUser> getRegistry() {
    return new ArrayList<IUser>();
  }

  @Override
  public boolean deleteUser(UUID userId) {
    return false;
  }

  @Override
  public boolean canDeleteUser(UUID userId) {
    return false;
  }

  @Override
  public boolean canCreateUser() {
    return false;
  }

  @Override
  public UUID getCurrentUserId() {
    return null;
  }

  @Override
  public IUser getCurrentUser() {
    return null;
  }

  @Override
  public boolean assignResponsibility(UUID user, UUID responsibility) {
    return false;
  }

  @Override
  public boolean assignResponsibilities(Map<UUID, List<UUID>> responsibilityMap) {
    return false;
  }

  @Override
  public boolean createAdmin() {
    return false;
  }

  @Override
  public String getSystemName() {
    return null;
  }

  @Override
  public List<UUID> getResponsibilities(UUID userId) {
    return null;
  }

  @Override
  public boolean isResponsible(UUID userId, UUID entryId) {
    return true;
  }

  @Override
  public boolean isResponsible(UUID entryId) {
    return true;
  }

  @Override
  public boolean assignResponsibility(UUID responsibility) {
    return false;
  }

}
