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

/**
 * The general access rights of the xstampp user system.
 * 
 * @author Lukas Balzer
 *
 */
public enum AccessRights {
  
  /**
   * The enum constant describing that a user has general rights to 
   * Create whatever entities in the system.
   */
  CREATE,
  
  /**
   * AN enum constant meaning that the user has general write rights on
   * the system this may be useful if the user is restricted to a few responsibilities
   * in which he can than manipulate the content.
   */
  WRITE,
  
  /**
   * This is a general enum constant granting read_only rights to a user
   * in general all write access is denied.
   */
  READ_ONLY,
  
  /**
   * The basic right to access a project, a user that does not hold this is not able to see
   * or manipulate any data in the project.
   */
  ACCESS
}
