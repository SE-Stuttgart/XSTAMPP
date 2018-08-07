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

import java.util.ArrayList;
import java.util.List;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkingType;

public class StepConstraintsLinkSupport extends LinkSupport<DataModelController> {

  public StepConstraintsLinkSupport(DataModelController dataInterface, LinkingType type) {
    super(dataInterface, type);
  }

  @Override
  protected List<ITableModel> getModels() {
    List<ITableModel> list = new ArrayList<>();
    list.addAll(getDataInterface().getAllSafetyConstraints());
    list.addAll(getDataInterface().getCorrespondingSafetyConstraints());
    list.addAll(getDataInterface().getCausalFactorController().getSafetyConstraints());
    list.addAll(getDataInterface().getExtendedDataController().getSafetyConstraints(false, true, false));
    return list;
  }

  @Override
  public String getTitle() {
    return "Safety Constraint Links";
  }

}
