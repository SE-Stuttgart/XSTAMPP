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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A normal user which can be given certain responsibilities to access files.
 * @author Lukas Balzer - initial implementation
 *
 */
public class User extends AbstractUser {
  private List<UUID> responsibilities;

  public User() {
    this("",""); //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  public User(String username, String password) {
    super(username, password);
    this.responsibilities = new ArrayList<>();
  }
  
  /* (non-Javadoc)
   * @see xstampp.usermanagement.roles.IUser#getUserId()
   */
  @Override
  public boolean checkAccess(UUID entryId) {
    return responsibilities.contains(entryId);
  }

  /**
   * @param responsibility the responsibilities to set.
   */
  public void addResponsibility(UUID responsibility) {
    this.responsibilities.add(responsibility);
  }

}
