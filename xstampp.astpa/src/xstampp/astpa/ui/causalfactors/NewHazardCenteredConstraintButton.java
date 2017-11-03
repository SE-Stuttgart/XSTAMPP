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
package xstampp.astpa.ui.causalfactors;

import java.util.UUID;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.ui.common.grid.CellButton;
import xstampp.ui.common.grid.GridWrapper;

public class NewHazardCenteredConstraintButton extends CellButton {

  private UUID componentId;
  private UUID factorId;
  private UUID entryId;
  private ICausalFactorDataModel datamodel;

  public NewHazardCenteredConstraintButton(UUID componentId, UUID factorId, UUID entryId,
      ICausalFactorDataModel datamodel) {
    super(new Rectangle(
        -1, -1,
        GridWrapper.getAddButton16().getBounds().width,
        GridWrapper.getAddButton16().getBounds().height),
        GridWrapper.getAddButton16());
    this.componentId = componentId;
    this.factorId = factorId;
    this.entryId = entryId;
    this.datamodel = datamodel;
  }

  @Override
  public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
    CausalFactorEntryData data = new CausalFactorEntryData(entryId);
    data.setConstraint(new String());
    UUID randomUUID = UUID.randomUUID();
    
    datamodel.lockUpdate();
    datamodel.changeCausalEntry(componentId, factorId, data);
    datamodel.releaseLockAndUpdate(null);
  }
}
