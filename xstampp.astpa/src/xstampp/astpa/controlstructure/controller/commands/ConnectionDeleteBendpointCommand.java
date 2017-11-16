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
package xstampp.astpa.controlstructure.controller.commands;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Point;

import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * Command used to delete a bendpoint from a {@link OPMLink} This class is declared final since it
 * has a very specific functionality.
 * 
 * @author vainolo
 */
public final class ConnectionDeleteBendpointCommand extends ControlStructureAbstractCommand {

  public ConnectionDeleteBendpointCommand(UUID rootId, IControlStructureEditorDataModel model,
      String stepID) {
    super(rootId, model, stepID);
  }

  private IConnection link;
  private Point location;

  /**
   * Only execute is link is not null and index is valid.
   */
  @Override
  public boolean canExecute() {
    return (link != null) && (location.x > 0 && location.y > 0);
  }

  /**
   * Remove the bendpoint from the link.
   */
  @Override
  public void execute() {
    getDataModel().getControlStructureController().removeBendPoint(link.getId(), location.x,
        location.y);
  }

  /**
   * Reinsert the bendpoint in the link.
   */
  @Override
  public void undo() {
    getDataModel().getControlStructureController().addBendPoint(link.getId(), location.x,
        location.y);
  }

  /**
   * Set the link from which the bendpoint is removed.
   * 
   * @param link
   *          the link from which the bendpoint is removed.
   */
  public void setLink(IConnection link) {
    this.link = link;
  }

  /**
   * Set the new location of the bendpoint.
   * 
   * @param location
   *          the new location of the bendpoint.
   */
  public void setIndex(int index) {
    this.location = link.getBendPoints().get(index);
  }
}
