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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import messages.Messages;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
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

  private IRectangleComponent component;
  private ICausalFactorDataModel dataInterface;
  private UUID ucaID;

  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param component
   *          the component the add buttons adds causal factors to.
   * @param ucaID
   *          An id of a uca any new causal factor should be linked to, or null if the causal factor
   *          should initially not be linked to any uca.
   */
  public GridCellButtonAddCausalFactor(IRectangleComponent component,
      ICausalFactorDataModel dataInterface, UUID ucaID) {
    super(Messages.AddNewCausalFactor);

    this.component = component;
    this.dataInterface = dataInterface;
    this.ucaID = ucaID;
  }

  @Override
  public String getToolTip(Point point) {
    return xstampp.astpa.messages.Messages.GridCellButtonAddCausalFactor_ToolTip;
  }

  @Override
  public void onMouseDown(MouseEvent e,
      org.eclipse.swt.graphics.Point relativeMouse,
      Rectangle cellBounds) {
    ProjectManager.getLOGGER().info(Messages.AddingNewCausalFactor);
    dataInterface.addCausalFactor(component.getId(), ucaID);
  }
}