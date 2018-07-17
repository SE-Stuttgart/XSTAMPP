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

import java.util.UUID;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.controlstructure.figure.CSAnchor;
import xstampp.astpa.controlstructure.figure.CSFlyAnchor;
import xstampp.astpa.controlstructure.figure.IAnchorFigure;
import xstampp.astpa.controlstructure.figure.IControlStructureFigure;
import xstampp.astpa.model.controlstructure.components.Anchor;
import xstampp.astpa.model.controlstructure.components.CSConnection;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * 
 * 
 * @author Aliaksei Babkovich
 * @version 1.0
 */
public class ConnectionReconnectCommand extends ControlStructureAbstractCommand {

  private UUID connId;
  private Anchor oldSourceAnchorModel;
  private Anchor oldTargetAnchorModel;
  private Anchor newSourceAnchorModel;
  private Anchor newTargetAnchorModel;

  /**
   * 
   * 
   * @author Lukas Balzer, Aliaksei Babkovich
   * 
   * @param conn
   *          The Connection to manipulate
   * @param model
   *          The DataModel which contains all model classes
   * @param stepID
   *          the stepEditor ID
   */
  public ConnectionReconnectCommand(CSConnection conn, UUID rootId,
      IControlStructureEditorDataModel model, String stepID) {
    super(rootId, model, stepID);
    if (conn == null) {
      throw new IllegalArgumentException();
    }
    this.connId = conn.getId();
    this.oldSourceAnchorModel = conn.getSourceAnchor();
    this.oldTargetAnchorModel = conn.getTargetAnchor();

  }

  @Override
  public boolean canExecute() {

    if (this.newSourceAnchorModel != null) {
      return this.checkSourceReconnection();
    } else if (this.newTargetAnchorModel != null) {
      return this.checkTargetReconnection();
    }
    return this.getStepID().equals(CSEditor.ID);
  }

  private boolean checkSourceReconnection() {
    if (this.newSourceAnchorModel == null) {
      return false;
    } else if (this.newSourceAnchorModel.equals(this.oldTargetAnchorModel)) {
      return false;
    } else if (this.oldTargetAnchorModel.isFlying() && this.newSourceAnchorModel.isFlying()) {
      return false;
    }
    return true;
  }

  private boolean checkTargetReconnection() {
    if (this.newTargetAnchorModel == null) {
      return false;
    } else if (this.oldSourceAnchorModel.equals(this.newTargetAnchorModel)) {
      return false;
    } else if (this.oldSourceAnchorModel.isFlying() && this.newTargetAnchorModel.isFlying()) {
      return false;
    }
    return true;
  }

  /**
   * This function should only be called when reconnecting the sourceAnchor,
   * it sets the targetAnchorFgiure so it can be referenced in case that
   * source is flying
   * 
   * 
   * @author Lukas Balzer, Aliaksei Babkovich
   * 
   * @param target
   *          The old TargetAnchor
   * @param source
   *          the new Source the connection should be get
   * 
   */
  public void setNewSourceNode(IAnchorFigure target, IAnchorFigure source) {

    UUID id = ((IControlStructureFigure) source.getOwner()).getId();
    int x = source.getAnchorFactor().x;
    int y = source.getAnchorFactor().y;
    boolean flys = source instanceof CSFlyAnchor;
    // a flying Anchor has a relation to its source
    if (flys && target instanceof CSAnchor) {
      ((CSFlyAnchor) source).setRelation((CSAnchor) target);
      x = ((CSFlyAnchor) source).getAnchorPosition().x;
      y = ((CSFlyAnchor) source).getAnchorPosition().y;
    }
    this.newSourceAnchorModel = new Anchor(flys, x, y, id);
    this.newTargetAnchorModel = null;
  }

  /**
   * This function should only be called when reconnecting the targetAnchor,
   * it sets the sourceAnchorFgiure so it can be referenced in case that
   * target is flying
   * 
   * @author Lukas Balzer, Aliaksei Babkovich
   * @param target
   *          the target Anchor of this Connection
   * @param source
   *          The old SourceAnchor
   */
  public void setNewTargetNode(IAnchorFigure target, IAnchorFigure source) {

    UUID id = ((IControlStructureFigure) target.getOwner()).getId();
    int x = target.getAnchorFactor().x;
    ;
    int y = target.getAnchorFactor().y;
    boolean flys = target instanceof CSFlyAnchor;

    // a flying Anchor has a relation to its source
    if (flys && source instanceof CSAnchor) {
      ((CSFlyAnchor) target).setRelation((CSAnchor) source);
      x = ((CSFlyAnchor) target).getAnchorPosition().x;
      y = ((CSFlyAnchor) target).getAnchorPosition().y;
    }
    this.newSourceAnchorModel = null;
    this.newTargetAnchorModel = new Anchor(flys, x, y, id);
  }

  @Override
  public void execute() {
    super.execute();
    if (this.newSourceAnchorModel != null) {
      this.getDataModel().changeConnectionSource(this.connId,
          this.newSourceAnchorModel, getStepID().equals(CSEditorWithPM.ID));
    } else if (this.newTargetAnchorModel != null) {
      this.getDataModel().changeConnectionTarget(this.connId,
          this.newTargetAnchorModel, getStepID().equals(CSEditorWithPM.ID));
    } else {
      throw new IllegalStateException("Should not happen"); //$NON-NLS-1$
    }
  }

  @Override
  public void undo() {
    this.getDataModel().changeConnectionSource(this.connId,
        this.oldSourceAnchorModel, getStepID().equals(CSEditorWithPM.ID));
    this.getDataModel().changeConnectionTarget(this.connId,
        this.oldTargetAnchorModel, getStepID().equals(CSEditorWithPM.ID));
  }

}
