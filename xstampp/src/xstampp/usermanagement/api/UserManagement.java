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

import java.io.File;
import java.util.UUID;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.io.RefreshRunner;
import xstampp.usermanagement.io.UserSystemLoader;
import xstampp.usermanagement.roles.Admin;
import xstampp.usermanagement.ui.CreateAdminShell;

public class UserManagement {
  private static UserManagement instance;
  private UserSystemLoader loader;

  private UserManagement() {
    loader = new UserSystemLoader();
  }

  /**
   * The get instance method of the singleton pattern. On the initial call this method also creates
   * the instance for this runtime.
   * 
   * @return an instance of {@link UserManagement}
   */
  public static UserManagement getInstance() {
    if (instance == null) {
      instance = new UserManagement();
    }
    return instance;
  }

  /**
   * Opens up a shell to create a new administrator for a new user system which initiates with the
   * administrator as initial user and which is stored in a file called
   * 
   * <br>
   * <code>[projectName].user</code>.
   * 
   * @param projectName
   *          the name of the project for which the user database should be created.
   * @return The new {@link IUserSystem} with one user
   */
  public IUserSystem createUserSystem(String projectName) {
    CreateAdminShell create = new CreateAdminShell(new UserSystem());
    IUser admin = create.pullUser();
    IUserSystem system = new EmptyUserSystem();
    if (admin != null && admin instanceof Admin) {
      system = new UserSystem((Admin) admin, projectName);
    }
    return system;
  }

  /**
   * This method tries to load a user database in <code>[project.projectName].user</code>. If the
   * database file cannot be found automatically a {@link FileDialog} is opened to search manually
   * for the file.
   * 
   * @param systemName
   *          the project which is using the user system which should be loaded.
   * @param systemId
   *          the id of the system which should be loaded.
   * @return either the {@link IUserSystem} which was found on the file system and has the same
   *         systemId as the given one or a restricted instance which prevents any access to the
   *         project by the unauthorized user.
   */
  public IUserSystem loadSystem(String systemName, UUID systemId) {
    return loadSystem(systemName, systemId, null);
  }

  /**
   * This method tries to load a user database in <code>[project.projectName].user</code>. If the
   * database file cannot be found automatically a {@link FileDialog} is opened to search manually
   * for the file.
   * 
   * @param systemName
   *          the project which is using the user system which should be loaded.
   * @param systemId
   *          the id of the system which should be loaded.
   * @param exclusiveUser
   *          TODO
   * @return either the {@link IUserSystem} which was found on the file system and has the same
   *         systemId as the given one or a restricted instance which prevents any access to the
   *         project by the unauthorized user.
   */
  public IUserSystem loadSystem(String systemName, UUID systemId, UUID exclusiveUser) {
    String wsUrl = Platform.getInstanceLocation().getURL().getPath();
    String fileName = systemName;
    File file = new File(wsUrl, fileName);
    // validate the file
    if (file == null || !file.exists()) {
      FileDialog diag = new FileDialog(Display.getDefault().getActiveShell());
      diag.setFilterPath(wsUrl);
      diag.setText("Please select the User Database or conntact the System administrator");
      diag.setFilterExtensions(new String[] { "*.user" });
      String filePath = diag.open();
      if (filePath != null) {
        file = new File(filePath);
      }
    }
    IUserSystem system = new EmptyUserSystem(false);
    try {
      system = loader.loadSystem(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (systemId.equals(system.getSystemId()) && system instanceof UserSystem) {
      ((UserSystem) system).setSystemName(file.getName());
      ((UserSystem) system).setExclusiveUser(exclusiveUser);
      RefreshRunner runner = new RefreshRunner(file, ((UserSystem) system));
      runner.start();
      return system;
    }
    MessageDialog.openError(Display.getDefault().getActiveShell(), "User database error!",
        "The User database for the project " + systemName + " could not "
            + "be read its either broken or corrupt\nplease conntact the system administrator!");

    return system;
  }

  /**
   * Opens a {@link FileDialog} and lets the user select an existing {@link IUserSystem} that should
   * be used for this project.
   * 
   * @return The loaded {@link IUserSystem} or an {@link EmptyUserSystem} if the chosen file doesn't
   *         contain a valid system.
   */
  public IUserSystem loadExistingSystem() {
    // validate the file
    FileDialog diag = new FileDialog(Display.getDefault().getActiveShell());

    String wsUrl = Platform.getInstanceLocation().getURL().getFile();
    diag.setFilterNames(null);
    diag.setFilterPath(wsUrl);
    diag.setText("Please select the User Database which should be used by the project");
    diag.setFilterExtensions(new String[] { "*.user" });
    String filePath = diag.open();
    if (filePath != null) {
      File file = new File(filePath);
      IUserSystem system = new EmptyUserSystem();
      try {
        system = loader.loadSystem(file);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      if (system instanceof UserSystem) {
        ((UserSystem) system).setSystemName(file.getName());
        return system;
      }
      MessageDialog.openError(Display.getDefault().getActiveShell(), "User database error!",
          "The User database could not "
              + "be read its either broken or corrupt or incompatible with the system!");
    }
    return new EmptyUserSystem();
  }

}
