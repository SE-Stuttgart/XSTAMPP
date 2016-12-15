/*************************************************************************
 * Copyright (c) 2014-2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************/

package xstampp.astpa.ui.causalScenarios;

import java.util.UUID;

import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.model.AbstractLtlProvider;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridWrapper;

public class DeleteCSAction extends DeleteGridEntryAction<IExtendedDataModel>{
  
  public DeleteCSAction(GridWrapper grid, IExtendedDataModel dataModel, String entryType, String prefix) {
    super(grid, dataModel, entryType, prefix);
  }
  
  @Override
  protected String getIdString(UUID id) {
    String idString = null;
    AbstractLtlProvider refinedRule = getDataModel().getRefinedRule(id);
    if(refinedRule != null){
      idString = getPrefix()+refinedRule.getNumber();
    }
    return idString;
  }
  
  @Override
  protected void removeEntry(UUID id) {
    getDataModel().removeRefinedSafetyRule(false, id);
  }
}
