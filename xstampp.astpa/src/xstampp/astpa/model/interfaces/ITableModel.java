/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.astpa.model.interfaces;

import xstampp.model.ITableEntry;
import xstampp.model.NumberedEntry;

/**
 * @author Lukas Balzer
 *
 */
public interface ITableModel
    extends IEntryWithNameId, NumberedEntry, ITableEntry {
  /**
   * getter for the ID uniquely representing the entry e.g.: <i>H-1</i>
   * 
   * @return the id as a string that is the number and an optional literal
   */
  public String getIdString();

}
