/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.ui.causalfactors;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridWrapper;

public class DeleteCFAction extends DeleteGridEntryAction<ICausalFactorDataModel> {

  public DeleteCFAction(GridWrapper grid, ICausalFactorDataModel dataModel, String entryType, String prefix) {
    super(grid, dataModel, entryType, prefix);
  }

  @Override
  protected String getIdString(UUID id) {
    return new String();
  }

  @Override
  protected void removeEntry(UUID id) {
    getDataModel().removeCausalFactor(null,id);
  }

}
