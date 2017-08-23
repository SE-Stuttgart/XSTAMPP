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

import java.util.List;

import org.eclipse.swt.widgets.Listener;

/**
 * 
 * @author Lukas Balzer
 *
 */
public interface ICollaborationSystem {

  /**
   * This method gets the given users' copy of the project calling
   * {@link IUser#getWorkingProjectId()}<br>
   * If one ha been defined for the current project than its' data for which the given user is
   * responsible is merged into the current project.
   * 
   * @param user
   *          The user which should provide the necessary projectId and the responsibilities which
   *          define the entries that are going to be synchronized
   * @return if the given {@link IUser#getWorkingProjectId()} did provide a valid project Id and the
   *         current project data were successfully merged
   */
  int syncDataWithUser(IUser user);

  boolean syncDataWithUser(List<IUser> users);

  int syncDataWithUser(IUser user, Listener listener);
}
