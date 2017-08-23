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

public interface IHazardModel extends ILinkModel {

  /**
   * Getter for all existing hazards
   * 
   * @author Jarkko Heidenwag
   * 
   * @return All hazards
   */
  List<ITableModel> getAllHazards();

  /**
   * Get a hazard by its id.
   * 
   * @author Jarkko Heidenwag, Patrick Wickenhaeuser
   * 
   * @param hazardId
   *          the id of the hazard.
   * @return the requested hazard.
   */
  ITableModel getHazard(UUID hazardId);

  /**
   * getter for a sorted list containing all hazards
   * for the given list of ids
   * 
   * @param ids
   *          an a array of ids of hazard entries in the dataModel
   * @return a sorted list containing with hazard models or or an empty list
   *         if ids is an empty array
   *         or no hazards could be found
   */
  List<ITableModel> getHazards(List<UUID> ids);
}
