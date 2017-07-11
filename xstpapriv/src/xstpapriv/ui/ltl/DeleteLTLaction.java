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
package xstpapriv.ui.ltl;

import java.util.UUID;

import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridWrapper;

public class DeleteLTLaction extends DeleteGridEntryAction<IExtendedDataModel> {

  private IExtendedDataModel dataModel2;

  public DeleteLTLaction(GridWrapper grid, IExtendedDataModel dataModel, String entryType, String prefix) {
    super(grid, dataModel, entryType, prefix);
  }

  @Override
  protected String getIdString(UUID id) {
    return getPrefix()+getDataModel().getRefinedScenario(id).getNumber();
  }

  @Override
  protected void removeEntry(UUID id) {
    getDataModel().removeRefinedSafetyRule(ScenarioType.CUSTOM_LTL, false, id);
  }

}
