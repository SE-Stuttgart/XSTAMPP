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
package xstampp.astpa.model.causalfactor.interfaces;

import java.util.List;
import java.util.UUID;

public class CausalFactorUCAEntryData extends CausalFactorEntryData {

  public CausalFactorUCAEntryData(UUID id) {
    super(id);
    this.scenariosChanged = false;
  }

  private List<UUID> scenarioLinks;
  private boolean scenariosChanged;

  @Override
  public List<UUID> getScenarioLinks() {
    return scenarioLinks;
  }

  /**
   * @param scenarioLinks
   *          the scenarioLinks to set
   */
  public void setScenarioLinks(List<UUID> scenarioLinks) {
    this.scenarioLinks = scenarioLinks;
    this.scenariosChanged = true;
  }

  @Override
  public boolean scenariosChanged() {
    return scenariosChanged;
  }

}