/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package xstampp.usermanagement.api;

import java.util.UUID;

/**
 * The API to access he user system and create one on the platform.
 * 
 * @author Lukas Balzer - initial implementation and API
 *
 */
public interface IUserSystem {

  IUserSystem getUserSystem();

  boolean createUser();

  public UUID getSystemId();

  boolean canAccess(UUID entryId);

}