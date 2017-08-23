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
package xstampp.astpa.ui.causalfactors;

import java.util.UUID;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;

import messages.Messages;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.grid.GridCellButton;

/**
 * The add button.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class GridCellButtonAddCausalFactor extends GridCellButton {

  private ICausalComponent component;
  private ICausalFactorDataModel dataInterface;

  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param component
   *          the component the add buttons adds causal factors to.
   */
  public GridCellButtonAddCausalFactor(ICausalComponent component,
      ICausalFactorDataModel dataInterface) {
    super(Messages.AddNewCausalFactor);

    this.component = component;
    this.dataInterface = dataInterface;
  }

  @Override
  public void onMouseDown(MouseEvent e,
      org.eclipse.swt.graphics.Point relativeMouse,
      Rectangle cellBounds) {
    ProjectManager.getLOGGER().info(Messages.AddingNewCausalFactor);
    UUID factorId = dataInterface
        .addCausalFactor(component.getId());
  }
}