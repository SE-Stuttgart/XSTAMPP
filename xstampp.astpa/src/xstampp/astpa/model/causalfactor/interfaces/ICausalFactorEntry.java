/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.causalfactor.interfaces;

import java.util.List;
import java.util.UUID;

public interface ICausalFactorEntry {

  /**
   * @return the hazardIds
   */
  public List<UUID> getHazardIds();

  /**
   * @return the note
   */
  public String getNote();

  public UUID getConstraintId();

  public UUID getId();

  /**
   * @return the ucaLink
   */
  public UUID getUcaLink();

  /**
   * @return the scenarioLinks
   */
  public List<UUID> getScenarioLinks();
}
