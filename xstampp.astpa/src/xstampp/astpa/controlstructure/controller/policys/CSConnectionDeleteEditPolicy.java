/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.controlstructure.controller.policys;

import java.util.UUID;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import xstampp.astpa.controlstructure.controller.commands.ConnectionDeleteCommand;
import xstampp.astpa.controlstructure.controller.editparts.IControlStructureEditPart;
import xstampp.astpa.controlstructure.controller.editparts.IRelativePart;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * This class contains policy for removing connection
 * 
 * @author Lukas Balzer, Aliaksei Babkovich
 * @version 1.0
 */
public class CSConnectionDeleteEditPolicy extends ConnectionEditPolicy
    implements IControlStructurePolicy {

  private final IControlStructureEditorDataModel dataModel;
  private final String stepId;

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param model
   *          The DataModel which contains all model classes
   * @param stepId
   *          this steps id
   */
  public CSConnectionDeleteEditPolicy(IControlStructureEditorDataModel model, String stepId) {
    super();
    this.stepId = stepId;
    this.dataModel = model;
  }

  @Override
  protected Command getDeleteCommand(GroupRequest arg0) {
    UUID rootId = ((IRectangleComponent) getHost().getViewer().getContents().getModel()).getId();
    ConnectionDeleteCommand command = new ConnectionDeleteCommand(rootId, this.dataModel,
        this.stepId);
    command.setLink((IRelativePart) this.getHost());
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
