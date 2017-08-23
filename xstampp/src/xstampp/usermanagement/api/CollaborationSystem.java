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

import xstampp.usermanagement.ui.settings.SyncShell;

public abstract class CollaborationSystem implements ICollaborationSystem {

  @Override
  public int syncDataWithUser(IUser user) {
    SyncShell syncShell = new SyncShell(user, this);
    syncShell.open();
    return (int) syncShell.getReturnValue();
  }

  @Override
  public boolean syncDataWithUser(List<IUser> users) {
    SyncShell syncShell = new SyncShell(users, this);
    syncShell.open();
    return (boolean) syncShell.getReturnValue();
  }
}
