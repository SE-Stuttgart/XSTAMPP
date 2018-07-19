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

public class Step2ConstraintsLinkSupport extends LinkSupport<DataModelController> {

  public Step2ConstraintsLinkSupport(DataModelController dataInterface, LinkingType type) {
    super(dataInterface, type);
  }

  @Override
  protected List<ITableModel> getModels() {
    return getDataInterface().getCausalFactorController().getSafetyConstraints();
  }

  @Override
  protected String getLiteral() {
    return "S2.";
  }

  @Override
  public String getTitle() {
    return "Causal Safety Constraint Links";
  }
}
