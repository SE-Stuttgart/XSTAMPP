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
package xstampp.model;

public interface IEntryFilter<T> {

  /**
   * checks a model on whether it is accepted by the filter or not
   *  
   * @param model a model/entry of the generic type T 
   * @return true if the model can/should be used false otherwise
   */
  boolean check(T model);
}
