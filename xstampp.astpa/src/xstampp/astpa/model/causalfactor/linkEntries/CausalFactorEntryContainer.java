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
package xstampp.astpa.model.causalfactor.linkEntries;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;

public class CausalFactorEntryContainer implements ICausalFactorEntry {
  final ICausalFactorEntry entry;
  
  public CausalFactorEntryContainer(ICausalFactorEntry entry) {
    this.entry = entry;
  }

  @Override
  public List<UUID> getHazardIds() {
    return entry.getHazardIds();
  }

  @Override
  public String getNote() {
    return entry.getNote();
  }

  @Override
  public UUID getId() {
    return entry.getId();
  }

  @Override
  public UUID getUcaLink() {
    return entry.getUcaLink();
  }

  @Override
  public List<UUID> getScenarioLinks() {
    return entry.getScenarioLinks();
  }
  @Override
  public String getConstraintText() {
    return entry.getConstraintText();
  }

}
