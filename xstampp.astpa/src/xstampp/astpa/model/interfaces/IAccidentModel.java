/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.interfaces;

import java.util.List;
import java.util.UUID;

public interface IAccidentModel extends ILinkModel {

  /**
   * Getter for all existing accidents
   * 
   * @author Jarkko Heidenwag
   * 
   * @return All accidents
   */
  List<ITableModel> getAllAccidents();

  /**
   * Get an accident by it's ID.
   * 
   * @author Jarkko Heidenwag, Patrick Wickenhaeuser
   * @param accidentId
   *          the ID of the accident.
   * 
   * @return the accident.
   */
  ITableModel getAccident(UUID accidentId);
}
