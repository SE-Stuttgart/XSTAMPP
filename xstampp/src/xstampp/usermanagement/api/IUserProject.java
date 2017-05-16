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

package xstampp.usermanagement.api;

import java.util.UUID;

import xstampp.model.IDataModel;
import xstampp.usermanagement.UserSystem;

/**
 * The API to access a user entry on the platform.
 * 
 * @author Lukas Balzer - initial implementation and API
 *
 */
public interface IUserProject extends IDataModel {
  
  /**
   * This getter should never return null but instead implementing systems
   * should initialize the user system as an {@link EmptyUserSystem}.
   * 
   * @return the currently used user system which can not be null but only one
   *         of {@link EmptyUserSystem} or {@link UserSystem}
   */
  IUserSystem getUserSystem();

  IUserSystem createUserSystem();
  
  IUserSystem loadUserSystem();

  UUID getExclusiveUserId();

  void setExclusiveUserId(UUID userId);
}
