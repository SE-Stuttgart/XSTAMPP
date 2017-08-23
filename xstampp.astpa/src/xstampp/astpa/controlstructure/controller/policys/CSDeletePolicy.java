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

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import xstampp.astpa.controlstructure.controller.commands.DeleteCommand;
import xstampp.astpa.controlstructure.controller.editparts.IControlStructureEditPart;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * @author Aliaksei Babkovich
 * @version 1.0
 */
public class CSDeletePolicy extends ComponentEditPolicy {

  private IControlStructureEditorDataModel dataModel;
  private final String stepID;
  private UUID rootId;

  /**
   * 
   * @author Lukas
   * 
   * @param model
   *          The DataModel which contains all model classes
   * @param stepId
   *          TODO
   */
  public CSDeletePolicy(IControlStructureEditorDataModel model, String stepId) {
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
  protected Command createDeleteCommand(GroupRequest deleteRequest) {
    DeleteCommand command = new DeleteCommand(getRootId(), this.dataModel, this.stepID);
    command.setModel((IRectangleComponent) this.getHost().getModel());

    command.setParentModel(this.getHost().getParent().getModel());
    return command;

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
