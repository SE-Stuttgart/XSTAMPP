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

package xstampp.astpa.controlstructure.controller.commands;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.model.controlstructure.ControlStructureController;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 *
 *
 *
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 */
public class ComponentChangeParentCommand extends ControlStructureAbstractCommand {
  private IRectangleComponent comp;
  private UUID oldParentId;
  private UUID newParentId;

  private Rectangle oldLayoutStep0;
  private Rectangle oldLayoutStep2;

  private Rectangle newLayoutStep0;
  private Rectangle newLayoutStep2;
  private ControlStructureController controller;

  /**
   *
   * @author Lukas Balzer
   * @param model
   *          The dataModel which contains all model classes
   * @param stepID
   *          the stepEditor ID
   */
  public ComponentChangeParentCommand(UUID rootId, IControlStructureEditorDataModel model,
      String stepID) {
    super(rootId, model, stepID);
    this.controller = getDataModel().getControlStructureController();
  }

  @Override
  public void execute() {
    controller.changeComponentParent(comp.getId(), oldParentId, newParentId, newLayoutStep0,
        newLayoutStep2);
  }

  public void setNewLocation(Point p) {
    this.newLayoutStep0 = oldLayoutStep0.setLocation(Math.max(0, p.x()), Math.max(0,p.y()));
    this.newLayoutStep2 = oldLayoutStep2.setLocation(Math.max(0, p.x()), Math.max(0,p.y()));
  }

  @Override
  public boolean canExecute() {
    return true;
  }

  @Override
  public void undo() {
    controller.changeComponentParent(comp.getId(), newParentId, oldParentId, oldLayoutStep0,
        oldLayoutStep2);
  }

  /**
   * @param compId
   *          the compId to set
   */
  public void setComp(IRectangleComponent comp) {
    this.comp = comp;
    this.oldLayoutStep0 = comp.getLayout(true);
    this.oldLayoutStep2 = comp.getLayout(false);
  }

  public void setOldParentId(UUID oldParentId) {
    this.oldParentId = oldParentId;
  }

  /**
   * @param newParentId
   *          the newParentId to set
   */
  public void setNewParentId(UUID newParentId) {
    this.newParentId = newParentId;
  }

}
