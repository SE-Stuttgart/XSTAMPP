/*************************************************************************
 * Copyright (c) 2014-2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************/

package xstampp.astpa.ui.unsafecontrolaction;

import java.util.UUID;

import xstampp.astpa.model.interfaces.IUnsafeControlActionDataModel;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridWrapper;

public class DeleteUcaAction extends DeleteGridEntryAction<IUnsafeControlActionDataModel>{

  public DeleteUcaAction(GridWrapper grid, IUnsafeControlActionDataModel dataModel, String entryType, String prefix) {
    super(grid, dataModel, entryType, prefix);
  }

  @Override
  protected String getIdString(UUID id) {
    String idString = null;
    if(getDataModel().getUCANumber(id) >= 0){
       idString = getPrefix()+getDataModel()
          .getUCANumber(id);
    }
    return idString;
  }

  @Override
  protected void removeEntry(UUID id) {
    getDataModel().removeUnsafeControlAction(id);
  }
  
}
