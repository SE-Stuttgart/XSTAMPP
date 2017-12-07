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

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.CellButton;
import xstampp.ui.common.grid.GridWrapper;

public class NewConstraintButton extends CellButton {

  private ICausalFactorDataModel datamodel;
  private Link ucaHazLink;

  /**
   * @param ucaHazLink
   *          a Link of type {@link ObserverValue#CausalEntryLink_HAZ_LINK}
   * @param datamodel
   */
  public NewConstraintButton(Link ucaHazLink, ICausalFactorDataModel datamodel) {
    super(new Rectangle(
        -1, -1,
        GridWrapper.getAddButton16().getBounds().width,
        GridWrapper.getAddButton16().getBounds().height),
        GridWrapper.getAddButton16());
    this.ucaHazLink = ucaHazLink;
    this.datamodel = datamodel;
    setToolTip("Add a new Safety Constraint");
  }

  @Override
  public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
    UUID constraintId = this.datamodel.getCausalFactorController().addSafetyConstraint("");
    this.datamodel.getLinkController().addLink(LinkingType.CausalHazLink_SC2_LINK, this.ucaHazLink.getId(),
        constraintId);
  }
}
