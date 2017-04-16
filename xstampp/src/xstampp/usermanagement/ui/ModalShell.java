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

package xstampp.usermanagement.ui;

import xstampp.usermanagement.api.IUser;

/**
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public abstract class ModalShell extends xstampp.ui.common.ModalShell {

  private IUser selectedUser;

  public ModalShell(String name, String acceptLabel) {
    super(name, acceptLabel);
    this.selectedUser = null;
  }

  public IUser pullUser() {
    if (this.selectedUser == null) {
      open();
    }
    return this.selectedUser;
  }

  public void setSelectedUser(IUser selectedUser) {
    this.selectedUser = selectedUser;
  }
}
