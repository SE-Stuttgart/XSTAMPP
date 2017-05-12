/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement;

import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserSystem;
import xstampp.usermanagement.io.SaveUserJob;
import xstampp.usermanagement.roles.AbstractUser;
import xstampp.usermanagement.roles.Admin;
import xstampp.usermanagement.roles.User;
import xstampp.usermanagement.ui.ChangeUserShell;
import xstampp.usermanagement.ui.CreateUserShell;
import xstampp.usermanagement.ui.LoginShell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An admin which can access and manipulate all files.
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
@XmlRootElement(name = "userSystem", namespace = "userSystem")
@XmlAccessorType(XmlAccessType.NONE)
public class UserSystem extends Observable implements IUserSystem {

  @XmlAttribute(name = "systemId", required = true)
  private UUID systemId;

  @XmlElementWrapper(name = "userRegistry")
  @XmlElement(name = "user")
  private List<User> userRegistry;

  @XmlElementWrapper(name = "adminRegistry")
  @XmlElement(name = "admin")
  private List<Admin> adminRegistry;

  private IUser currentUser;
  private String projectName;

  public UserSystem() {
    this.userRegistry = new ArrayList<>();
    this.adminRegistry = new ArrayList<>();
  }

  /**
   * Constructs a new UserSystem.
   * 
   * @param admin
   *          the initial user which has to be an administrator
   * @param projectName
   *          the name of the project which is than used as name to create the database dile
   */
  public UserSystem(Admin admin, String projectName) {
    this();
    adminRegistry.add(admin);
    this.projectName = projectName;
    this.systemId = UUID.randomUUID();
    this.currentUser = admin;
    save();
  }

  @Override
  public boolean assignResponsibility(IUser user, UUID responsibility) {
    Map<UUID, IUser> responsibilityMap = new HashMap<>();
    responsibilityMap.put(responsibility, user);
    return assignResponsibility(responsibilityMap);
  }

  @Override
  public boolean assignResponsibility(Map<UUID, IUser> responsibilityMap) {
    if (checkAccess(AccessRights.ADMIN)) {
      for (Entry<UUID, IUser> entry : responsibilityMap.entrySet()) {
        if (entry.getValue() instanceof AbstractUser) {
          ((AbstractUser) entry.getValue()).addResponsibility(entry.getKey());
        }
      }
      save();
      return true;
    }
    return false;
  }

  @Override
  public boolean checkAccess(UUID entryId, AccessRights accessLevel) {
    return this.getCurrentUser().checkAccess(entryId, accessLevel);
  }

  @Override
  public boolean checkAccess(AccessRights accessRight) {
    if (this.getCurrentUser() != null) {
      return getCurrentUser().checkAccess(accessRight);
    }
    return false;
  }

  @Override
  public List<IUser> getRegistry() {
    List<IUser> registry = new ArrayList<>();
    registry.addAll(userRegistry);
    registry.addAll(adminRegistry);
    return registry;
  }

  @Override
  public UUID getSystemId() {
    return systemId;
  }

  @Override
  public IUser getCurrentUser() {
    if (this.currentUser == null) {
      this.currentUser = new LoginShell(this, true).pullUser();
      setChanged();
      notifyObservers(NOTIFY_LOGIN);
    }
    return currentUser;
  }

  @Override
  public boolean grantAccessTo(IUser user, AccessRights right) {
    if (getCurrentUser() instanceof Admin) {
      if (user instanceof User) {
        ((User) user).giveAccessLevel(right);
      }
    }
    return false;
  }

  @Override
  public boolean canCreateUser() {
    return checkAccess(AccessRights.CREATE);
  }

  @Override
  public boolean createUser() {
    boolean result = false;
    if (getCurrentUser() instanceof Admin) {
      CreateUserShell create = new CreateUserShell(this);
      User user = (User) create.pullUser();
      if (user != null && this.userRegistry.add(user)) {
        result = true;
        save();
      }
    }
    return result;
  }

  @Override
  public boolean canDeleteUser(UUID userId) {
    return checkAccess(AccessRights.CREATE) && !this.currentUser.getUserId().equals(userId);
  }

  @Override
  public boolean deleteUser(UUID userId) {
    if (!this.currentUser.getUserId().equals(userId)) {
      for (int i = 0; i < userRegistry.size(); i++) {
        User user = this.userRegistry.get(i);
        if (user.getUserId().equals(userId) && this.userRegistry.remove(user)) {
          save();
          return true;
        }
      }
    }
    return false;
  }

  public void editUser(IUser user) {
    new ChangeUserShell(this, user).open();
    save();
  }

  private void save() {
    SaveUserJob saveJob = new SaveUserJob(this, projectName);
    saveJob.schedule();
    setChanged();
    notifyObservers();
  }

  @Override
  public UUID getCurrentUserId() {
    if (currentUser == null) {
      return null;
    }
    return currentUser.getUserId();
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }
}
