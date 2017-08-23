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

package xstampp.astpa.controlstructure.controller.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.controller.editparts.IMemberEditPart;
import xstampp.astpa.controlstructure.controller.editparts.IRelativePart;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * This class contains methods for connection deleting command
 * 
 * @author Aliaksei Babkovich
 * @version 1.0
 */
public class ConnectionDeleteCommand extends ControlStructureAbstractCommand {

  private UUID connectionId;
  private List<UUID> memberIDs;

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param model
   *          The DataModel which contains all model classes
   * @param stepId
   *          TODO
   */
  public ConnectionDeleteCommand(UUID rootId,
      IControlStructureEditorDataModel model, String stepID) {
    super(rootId, model, stepID);
    this.memberIDs = new ArrayList<>();
  }

  /**
   * 
   * 
   * @author Lukas Balzer, Aliaksei Babkovich
   * 
   * @param structureEditPart
   *          the ConnectionModel which shall be removed
   * 
   */
  public void setLink(IRelativePart structureEditPart) {
    this.connectionId = structureEditPart.getId();
    for (IMemberEditPart part : structureEditPart.getMembers()) {
      this.memberIDs.add(part.getId());
    }

  }

  @Override
  public boolean canExecute() {
    if (this.connectionId == null) {
      return false;
    }
    return this.getStepID().equals(CSEditor.ID);
  }

  @Override
  public void execute() {
    super.execute();
    this.getDataModel().removeConnection(this.connectionId);
    for (UUID memberId : this.memberIDs) {
      this.getDataModel().setRelativeOfComponent(memberId, null);
    }
  }

  @Override
  public boolean canUndo() {
    if (this.connectionId == null) {
      return false;
    }
    return true;
  }

  @Override
  public void undo() {
    this.getDataModel().recoverConnection(this.connectionId);
    for (UUID memberId : this.memberIDs) {
      this.getDataModel().setRelativeOfComponent(memberId, this.connectionId);
    }
  }

}
