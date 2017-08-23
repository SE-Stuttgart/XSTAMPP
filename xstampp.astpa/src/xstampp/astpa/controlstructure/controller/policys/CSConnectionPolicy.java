/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.controlstructure.controller.policys;

import java.util.UUID;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import xstampp.astpa.controlstructure.controller.commands.ConnectionCreateCommand;
import xstampp.astpa.controlstructure.controller.commands.ConnectionReconnectCommand;
import xstampp.astpa.controlstructure.controller.editparts.CSAbstractEditPart;
import xstampp.astpa.controlstructure.controller.editparts.CSConnectionEditPart;
import xstampp.astpa.controlstructure.controller.editparts.IControlStructureEditPart;
import xstampp.astpa.controlstructure.figure.IAnchorFigure;
import xstampp.astpa.controlstructure.figure.IControlStructureFigure;
import xstampp.astpa.model.controlstructure.components.CSConnection;
import xstampp.astpa.model.controlstructure.components.ConnectionType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * 
 * 
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 */
public class CSConnectionPolicy extends GraphicalNodeEditPolicy {

  private IControlStructureEditorDataModel dataModel;
  private final String stepID;
  private UUID rootId;

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param model
   *          The DataModel which contains all model classes
   * @param stepId
   *          TODO
   */
  public CSConnectionPolicy(IControlStructureEditorDataModel model,
      String stepId) {
    super();
    this.stepID = stepId;
    this.dataModel = model;
  }

  public UUID getRootId() {
    if (rootId == null) {
      rootId = ((IRectangleComponent) getHost().getViewer().getContents().getModel()).getId();
    }
    return rootId;
  }

  @Override
  protected Command getConnectionCompleteCommand(
      CreateConnectionRequest request) {

    ConnectionCreateCommand cmd = (ConnectionCreateCommand) request
        .getStartCommand();

    ConnectionAnchor targetAnchor = ((CSAbstractEditPart) this.getHost())
        .getTargetConnectionAnchor(request);

    cmd.setTargetModel((IAnchorFigure) targetAnchor);
    return cmd;
  }

  @Override
  protected void removeFeedback(IFigure figure) {
    ((IControlStructureFigure) this.getHostFigure()).disableOffset();
    super.removeFeedback(figure);
  }

  @Override
  protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
    ((IControlStructureFigure) this.getHostFigure()).enableOffset();
    ConnectionCreateCommand cmd = new ConnectionCreateCommand(
        getRootId(), this.dataModel, this.stepID);
    ConnectionAnchor sourceAnchor = ((CSAbstractEditPart) this.getHost())
        .getSourceConnectionAnchor(request);
    cmd.setConnectionType(((ConnectionType) request.getNewObjectType()));

    cmd.setSourceModel((IAnchorFigure) sourceAnchor);

    request.setStartCommand(cmd);
    return cmd;
  }

  @Override
  protected Command getReconnectSourceCommand(ReconnectRequest request) {
    ((IControlStructureFigure) this.getHostFigure()).enableOffset();
    CSConnection conn = (CSConnection) request.getConnectionEditPart()
        .getModel();
    ((CSConnectionEditPart) request.getConnectionEditPart()).getConnectionFigure().setFixed(false);
    ConnectionAnchor sourceAnchor = ((CSAbstractEditPart) this.getHost())
        .getSourceConnectionAnchor(request);
    ConnectionAnchor targetAnchor = ((CSConnectionEditPart) request
        .getConnectionEditPart()).getTargetAnchor();
    ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn,
        getRootId(), this.dataModel, this.stepID);

    ((AbstractConnectionAnchor) sourceAnchor).setOwner(getHost().getFigure());
    cmd.setNewSourceNode((IAnchorFigure) targetAnchor,
        (IAnchorFigure) sourceAnchor);

    return cmd;
  }

  @Override
  protected Command getReconnectTargetCommand(ReconnectRequest request) {
    ((IControlStructureFigure) this.getHostFigure()).enableOffset();
    CSConnection conn = (CSConnection) request.getConnectionEditPart()
        .getModel();
    ((CSConnectionEditPart) request.getConnectionEditPart()).getConnectionFigure().setFixed(false);
    ConnectionAnchor sourceAnchor = ((CSConnectionEditPart) request
        .getConnectionEditPart()).getSourceAnchor();
    ConnectionAnchor targetAnchor = ((CSAbstractEditPart) this.getHost())
        .getTargetConnectionAnchor(request);
    ((AbstractConnectionAnchor) targetAnchor).setOwner(getHost().getFigure());
    ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn,
        getRootId(), this.dataModel, this.stepID);

    cmd.setNewTargetNode((IAnchorFigure) targetAnchor,
        (IAnchorFigure) sourceAnchor);
    return cmd;
  }

  @Override
  public Command getCommand(Request request) {
    if (getHost().canEdit()) {
      return super.getCommand(request);
    }
    return null;
  }

  @Override
  public IControlStructureEditPart getHost() {
    return (IControlStructureEditPart) super.getHost();
  }
}
