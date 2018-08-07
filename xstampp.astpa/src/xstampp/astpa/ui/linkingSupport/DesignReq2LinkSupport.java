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
package xstampp.astpa.ui.linkingSupport;

import java.util.List;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;

public class DesignReq2LinkSupport extends LinkSupport<DataModelController> {

  public DesignReq2LinkSupport(DataModelController dataInterface, LinkingType type) {
    super(dataInterface, type);
  }

  @Override
  protected List<ITableModel> getModels() {
    return getDataInterface().getSdsController().getAllDesignRequirements(ObserverValue.DESIGN_REQUIREMENT_STEP2);
  }

  @Override
  public String getTitle() {
    return "Design Requirement Step 2 Links";
  }

}
