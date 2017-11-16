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

package xstampp.astpa.controlstructure.controller.editparts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IClippingStrategy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import xstampp.astpa.controlstructure.controller.policys.CSConnectionDeleteEditPolicy;
import xstampp.astpa.controlstructure.figure.CSAnchor;
import xstampp.astpa.controlstructure.figure.CSFlyAnchor;
import xstampp.astpa.controlstructure.figure.ConnectionFigure;
import xstampp.astpa.controlstructure.figure.IAnchorFigure;
import xstampp.astpa.controlstructure.figure.IControlStructureFigure;
import xstampp.astpa.model.controlstructure.components.CSConnection;
import xstampp.astpa.model.controlstructure.interfaces.IAnchor;
import xstampp.astpa.model.controlstructure.interfaces.IComponent;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUserProject;

/**
 * 
 * Editpart for connections.
 * 
 * @author Aliaksei Babkovich
 * @version 1.0
 */
public class CSConnectionEditPart extends AbstractConnectionEditPart implements IRelativePart {

  private static final float DASH = 4;
  private IAnchorFigure targetAnchor;
  private IAnchorFigure sourceAnchor;
  private IControlStructureEditorDataModel dataModel;
  private final UUID ownID;
  private final String stepId;
  private List<IMemberEditPart> members;
  private IPreferenceStore store;

  /**
   * This constructor is used to load a connection EditPart from a given model
   * 
   * @author Lukas Balzer
   * 
   * @param model
   *          The DataModel which contains all model classes
   * @param source
   *          the source anchor which is given from the Anchor Model
   * @param target
   *          the target anchor which is given from the Anchor Model
   * @param id
   *          the UUID which is given from the model
   * @param stepId
   *          this steps id
   */
  public CSConnectionEditPart(IControlStructureEditorDataModel model, IAnchorFigure source,
      IAnchorFigure target, UUID id, String stepId) {
    super();
    this.members = new ArrayList<>();
    this.stepId = stepId;
    this.registerAccessibility();
    this.activate();
    this.dataModel = model;
    this.ownID = id;
    this.sourceAnchor = source;
    this.targetAnchor = target;

  }

  @Override
  protected IFigure createFigure() {
    ConnectionFigure connection = new ConnectionFigure(getId());
    setFigure(connection);
    connection.setPreferenceStore(this.store);
    connection.addMouseMotionListener(this);
    connection.setLayoutManager(new XYLayout());
    connection.setLineWidth(1);
    connection.setTolerance(2);
    connection.setLineStyle(SWT.LINE_CUSTOM);
    switch (((CSConnection) this.getModel()).getConnectionType()) {
    case ARROW_SIMPLE: {
      connection.setLineStyle(SWT.LINE_SOLID);
      break;
    }
    case ARROW_DASHED: {
      connection.setLineDashOffset(CSConnectionEditPart.DASH);
      connection.setLineDash(new float[] { CSConnectionEditPart.DASH });
      break;
    }
    default:
      return null;
    }
    return connection;
  }

  @Override
  public void refresh() {
    this.getConnectionFigure().setFixed(true);
    Connection connection = getConnectionFigure();
    List<Point> modelConstraint = this.dataModel.getControlStructureController()
        .getBendPoints(getId());
    List<AbsoluteBendpoint> figureConstraint = new ArrayList<AbsoluteBendpoint>();
    for (Point p : modelConstraint) {
      figureConstraint.add(new AbsoluteBendpoint(p));
    }
    connection.setRoutingConstraint(figureConstraint);
    super.refresh();
    this.refreshChildren();
    this.getViewer().getControl().redraw();
    for (Object child : this.getChildren()) {
      ((IControlStructureEditPart) child).refresh();
    }
  }

  @Override
  protected List<IRectangleComponent> getModelChildren() {
    return new ArrayList<>();
  }

  @Override
  protected void createEditPolicies() {
    this.installEditPolicy(EditPolicy.CONNECTION_ROLE,
        new CSConnectionDeleteEditPolicy(this.dataModel, this.stepId));
    this.installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
        new ConnectionEndpointEditPolicy());
    // installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
    // new ConnectionBendpointEditPolicy(this.dataModel, this.stepId));
  }

  @Override
  public IFigure getFeedback() {
    return null;
  }

  @Override
  public IFigure getFeedback(IMemberEditPart member, Color color) {
    return this.getConnectionFigure().getFeedback(member, color);
  }

  @Override
  protected ConnectionAnchor getTargetConnectionAnchor() {
    IAnchor target = this.dataModel.getConnection(this.ownID).getTargetAnchor();
    boolean isCurrentlyFlying = this.targetAnchor instanceof CSFlyAnchor;
    IComponent targetOwner = this.dataModel.getComponent(target.getOwnerId());
    IControlStructureEditPart owner = (IControlStructureEditPart) getViewer().getEditPartRegistry()
        .get(targetOwner);
    // if the status of the anchor changes from/to flying than the
    // figure has to be reinitialized
    if (isCurrentlyFlying && !target.isFlying()) {
      this.targetAnchor = new CSAnchor(owner.getFigure(), target);
    } else if (!isCurrentlyFlying && target.isFlying()) {
      this.targetAnchor = new CSFlyAnchor(owner.getFigure(), this.sourceAnchor, target);
    } else {
      this.targetAnchor.updateAnchor(target, owner);
    }
    this.getFigure().revalidate();
    return this.targetAnchor;
  }

  @Override
  protected ConnectionAnchor getSourceConnectionAnchor() {
    IAnchor source = this.dataModel.getConnection(this.ownID).getSourceAnchor();
    boolean isCurrentlyFlying = this.sourceAnchor instanceof CSFlyAnchor;
    IComponent sourceOwner = this.dataModel.getComponent(source.getOwnerId());
    IControlStructureEditPart owner = (IControlStructureEditPart) getViewer().getEditPartRegistry()
        .get(sourceOwner);
    // if the status of the anchor changes from/to flying than the
    // figure has to be reinitialized
    if (isCurrentlyFlying && !source.isFlying()) {
      this.sourceAnchor = new CSAnchor(owner.getFigure(), source);
    } else if (!isCurrentlyFlying && source.isFlying()) {
      this.sourceAnchor = new CSFlyAnchor(owner.getFigure(), this.targetAnchor, source);
    } else {
      this.sourceAnchor.updateAnchor(source, owner);
    }
    this.getFigure().revalidate();
    return this.sourceAnchor;
  }

  /**
   * This getter is called to get the Target Anchor the connection is momentarily connected with
   * 
   * @author Lukas Balzer
   * 
   * @return The Anchor at which the connection ends
   */
  public ConnectionAnchor getTargetAnchor() {
    return this.targetAnchor;
  }

  /**
   * This getter is called to get the Source Anchor the connection is momentarily connected with
   * 
   * @author Lukas Balzer
   * 
   * @return The Anchor at which the connection starts
   */
  public ConnectionAnchor getSourceAnchor() {
    return this.sourceAnchor;
  }

  @Override
  public UUID getId() {
    return ((IConnection) this.getModel()).getId();
  }

  @Override
  public void translateToRoot(Translatable t) {
    // does nothing by default

  }

  @Override
  public void mouseDragged(MouseEvent me) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseEntered(MouseEvent me) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseExited(MouseEvent me) {
    // getConnectionFigure().disableFeedback();

  }

  @Override
  public void mouseHover(MouseEvent me) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseMoved(MouseEvent me) {
    // TODO Auto-generated method stub

  }

  @Override
  public ConnectionFigure getConnectionFigure() {
    // TODO Auto-generated method stub
    return (ConnectionFigure) super.getConnectionFigure();
  }

  @Override
  public void eraseFeedback() {
    getConnectionFigure().eraseFeedback();
  }

  @Override
  public void addMember(IMemberEditPart member) {
    this.members.add(member);
  }

  @Override
  public List<IMemberEditPart> getMembers() {
    return this.members;
  }

  @Override
  public void removeMember(IMemberEditPart member) {
    this.members.remove(member);

  }

  @Override
  public void updateFeedback() {
    getConnectionFigure().updateFeedback();

  }

  @Override
  public void setPreferenceStore(IPreferenceStore store) {
    this.store = store;
    if (getFigure() != null) {
      ((IControlStructureFigure) getFigure()).setPreferenceStore(store);
      ((IAnchorFigure) getSourceConnectionAnchor()).setPreferenceStore(store);
      ((IAnchorFigure) getTargetConnectionAnchor()).setPreferenceStore(store);
    }
  }

  @Override
  public void refreshModel() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean canEdit() {
    if (dataModel instanceof IUserProject) {
      return ((IUserProject) dataModel).getUserSystem().checkAccess(AccessRights.ADMIN);
    }
    return true;
  }

}

class RectangleClipping implements IClippingStrategy {
  private Rectangle clippingRect;

  public RectangleClipping(Rectangle rect) {
    this.clippingRect = rect;
  }

  @Override
  public Rectangle[] getClip(IFigure childFigure) {
    return new Rectangle[] { this.clippingRect };
  }

}
