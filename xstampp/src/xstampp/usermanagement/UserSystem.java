/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement;

import java.util.ArrayList;
import java.util.Arrays;
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

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

import xstampp.model.IEntryWithId;
import xstampp.ui.common.ProjectManager;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserSystem;
import xstampp.usermanagement.io.SaveUserJob;
import xstampp.usermanagement.roles.Admin;
import xstampp.usermanagement.roles.User;
import xstampp.usermanagement.ui.ChangeUserShell;
import xstampp.usermanagement.ui.CreateAdminShell;
import xstampp.usermanagement.ui.CreateUserShell;
import xstampp.usermanagement.ui.LoginShell;

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

  @XmlElementWrapper(name = "responsibilities")
  @XmlElement(name = "responsibility")
  private ResponsibilityController responsibilities;

  private IUser currentUser;
  private String systemName;
  private UUID exclusiveUser;

  /**
   * This constructs an empty user system and initializes the user registry and responsibilities
   * map.
   */
  public UserSystem() {
    this.userRegistry = new ArrayList<>();
    this.adminRegistry = new ArrayList<>();
    this.responsibilities = new ResponsibilityController();
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
    this.systemName = projectName + ".user"; //$NON-NLS-1$
    this.systemId = UUID.randomUUID();
    this.currentUser = admin;
    save();

  }

  @Override
  public boolean assignResponsibility(UUID responsibility) {
    return assignResponsibility(currentUser.getUserId(), responsibility);
  }

  @Override
  public boolean assignResponsibility(UUID user, UUID responsibility) {
    return assignResponsibility(user, responsibility);
  }

  @Override
  public boolean assignResponsibility(IUser user, IEntryWithId responsibility) {
    Map<IEntryWithId, List<IUser>> responsibilityMap = new HashMap<>();
    responsibilityMap.put(responsibility, Arrays.asList(user));
    return assignResponsibilities(responsibilityMap);
  }

  @Override
  public boolean assignResponsibilities(Map<IEntryWithId, List<IUser>> responsibilityMap) {
    boolean changed = false;
    if (checkAccess(AccessRights.ADMIN)) {
      for (Entry<IEntryWithId, List<IUser>> entry : responsibilityMap.entrySet()) {
        UUID entryId = entry.getKey().getId();
        // remove all responsibilities that are not included in the given mapping
        List<IUser> currentResp = getResponsibilities(entry.getKey());
        currentResp.removeAll(entry.getValue());
        for (IUser user : currentResp) {
          changed |= this.responsibilities.remove(new Responsibility(user.getUserId(), entry.getKey().getId()));
        }
        for (IUser userId : entry.getValue()) {
          changed |= this.responsibilities.add(userId.getUserId(), entryId);
        }
      }
      if (changed) {
        save();
      }
    }
    return changed;
  }

  @Override
  public List<UUID> getResponsibilities(UUID userId) {
    List<UUID> returnList = new ArrayList<>();
    for (Responsibility entry : responsibilities) {
      if (entry.getUserId().equals(userId)) {
        returnList.add(entry.getEntryId());
      }
    }
    return returnList;
  }

  @Override
  public List<IUser> getResponsibilities(IEntryWithId entry) {
    List<UUID> idList = new ArrayList<>();
    for (Responsibility resp : responsibilities) {
      if (resp.getEntryId().equals(entry.getId())) {
        idList.add(resp.getUserId());
      }
    }
    List<IUser> returnList = new ArrayList<>();
    for (User user : userRegistry) {
      if (idList.contains(user.getUserId())) {
        returnList.add(user);
      }
    }
    return returnList;
  }

  @Override
  public boolean isResponsible(UUID userId, UUID entryId) {
    return this.responsibilities.contains(new Responsibility(userId, entryId));
  }

  @Override
  public boolean isResponsible(UUID entryId) {
    if (getCurrentUser() != null) {
      return isResponsible(currentUser.getUserId(), entryId);
    }
    return false;
  }

  @Override
  public boolean checkAccess(UUID entryId, AccessRights accessLevel) {
    boolean returnBool = true;
    if (!(getCurrentUser() instanceof Admin)) {
      returnBool = isResponsible(entryId);
      returnBool &= this.getCurrentUser().checkAccess(accessLevel);
    }
    return returnBool;
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
      LoginShell shell = new LoginShell(this, true);
      shell.open();
      this.currentUser = (IUser) shell.getReturnValue();
      if (this.currentUser != null) {
        setChanged();
        notifyObservers(NOTIFY_LOGIN);
      }
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
        setChanged();
        notifyObservers(NOTIFY_USER);
      }
    }
    return result;
  }

  public boolean createAdmin() {
    boolean result = false;
    if (getCurrentUser() instanceof Admin) {
      CreateAdminShell create = new CreateAdminShell(this);
      Admin user = (Admin) create.pullUser();
      if (user != null && this.adminRegistry.add(user)) {
        result = true;
        save();
        setChanged();
        notifyObservers(NOTIFY_USER);
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
    if (canDeleteUser(userId)) {
      for (int i = 0; i < userRegistry.size(); i++) {
        User user = this.userRegistry.get(i);
        if (user.getUserId().equals(userId) && this.userRegistry.remove(user)) {
          save();
          setChanged();
          notifyObservers(NOTIFY_USER);
          return true;
        }
      }
    }
    return false;
  }

  public void logout() {
    this.currentUser = null;
    setChanged();
    notifyObservers(NOTIFY_LOGOUT);
  }

  public void editUser(IUser user) {
    new ChangeUserShell(this, user).open();
  }

  /**
   * This method changes the existing password of the user if given the correct current password.
   * The password is not verified against any measure but just set when the oldPassword given is
   * correct.
   * 
   * @param newPassword
   *          the new password which should be set for this user
   * @return whether the password could be changed
   */
  public boolean setPassword(UUID userId, String newPassword) {
    if (currentUser.checkAccess(AccessRights.ADMIN)) {
      for (User user : userRegistry) {
        if (user.getUserId().equals(userId)) {
          user.setPassword(newPassword);
          save();
          return true;
        }
      }
    }
    return false;
  }

  private void save() {
    SaveUserJob saveJob = new SaveUserJob(this, systemName);
    saveJob.addJobChangeListener(new JobChangeAdapter() {
      @Override
      public void done(IJobChangeEvent event) {
        if (event.getResult().isOK()) {
          ProjectManager.getLOGGER().debug("User System has been updated"); //$NON-NLS-1$
        }
      }
    });
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

  public void setSystemName(String projectName) {
    this.systemName = projectName;
  }

  @Override
  public String getSystemName() {
    return systemName;
  }

  public boolean assignWorkProject(UUID userId, UUID projectId) {
    if (currentUser.checkAccess(AccessRights.ADMIN)) {
      for (User user : userRegistry) {
        if (user.getUserId().equals(userId) && user.setWorkingProjectId(projectId)) {
          save();
          return true;
        }
      }
    }
    return false;
  }

  /**
   * This returns either <b>null</b> or an id if this user systems' instance can only used by one
   * specific user.
   */
  public UUID getExclusiveUser() {
    return exclusiveUser;
  }

  public void setExclusiveUser(UUID exclusiveUser) {
    this.exclusiveUser = exclusiveUser;
  }

  public void refresh(UserSystem update) {
    responsibilities = update.responsibilities;
  }
}
