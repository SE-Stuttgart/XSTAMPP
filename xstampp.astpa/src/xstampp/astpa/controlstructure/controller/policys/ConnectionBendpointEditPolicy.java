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
package xstampp.astpa.controlstructure.controller.policys;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import xstampp.astpa.controlstructure.controller.commands.ConnectionCreateBendpointCommand;
import xstampp.astpa.controlstructure.controller.commands.ConnectionDeleteBendpointCommand;
import xstampp.astpa.controlstructure.controller.commands.ConnectionMoveBendpointCommand;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * Policy used by the {@link OPMLink} to manage link bendpoints.
 * 
 * @author vainolo
 *
 */
public class ConnectionBendpointEditPolicy extends BendpointEditPolicy {

  private String stepId;
  private IControlStructureEditorDataModel dataInterface;
  private UUID rootId;

  public ConnectionBendpointEditPolicy(IControlStructureEditorDataModel model, String stepId) {
    this.dataInterface = model;
    this.stepId = stepId;
    // TODO Auto-generated constructor stub
  }

  @Override
  protected Command getCreateBendpointCommand(final BendpointRequest request) {
    ConnectionCreateBendpointCommand command = new ConnectionCreateBendpointCommand(getRootId(),
        this.dataInterface, this.stepId);

    Point p = request.getLocation().getCopy();
    getHostFigure().translateToRelative(p);
    command.setLink((IConnection) request.getSource().getModel());
    command.setLocation(p);
    command.setIndex(request.getIndex());

    return command;
  }

  @Override
  protected Command getMoveBendpointCommand(final BendpointRequest request) {
    ConnectionMoveBendpointCommand command = new ConnectionMoveBendpointCommand(getRootId(),
        this.dataInterface, this.stepId);

    Point p = request.getLocation().getCopy();
    getHostFigure().translateToRelative(p);
    command.setLink((IConnection) request.getSource().getModel());
    command.setLocation(p);
    command.setIndex(request.getIndex());

    return command;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Command getDeleteBendpointCommand(final BendpointRequest request) {
    ConnectionDeleteBendpointCommand command = new ConnectionDeleteBendpointCommand(getRootId(),
        this.dataInterface, this.stepId);

    command.setLink((IConnection) request.getSource().getModel());
    command.setIndex(request.getIndex());
    return command;
  }

  public UUID getRootId() {
    if (rootId == null) {
      rootId = ((IRectangleComponent) getHost().getViewer().getContents().getModel()).getId();
    }
    return rootId;
  }
}
