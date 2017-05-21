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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.controller.commands.ComponentChangeLayoutCommand;
import xstampp.astpa.controlstructure.controller.commands.ComponentCreateCommand;
import xstampp.astpa.controlstructure.controller.commands.SwapRelativeCommand;
import xstampp.astpa.controlstructure.controller.editparts.IControlStructureEditPart;
import xstampp.astpa.controlstructure.controller.editparts.IMemberEditPart;
import xstampp.astpa.controlstructure.controller.editparts.IRelativePart;
import xstampp.astpa.controlstructure.controller.editparts.RootEditPart;
import xstampp.astpa.controlstructure.figure.ComponentFigure;
import xstampp.astpa.controlstructure.figure.ConnectionFigure;
import xstampp.astpa.controlstructure.figure.IControlStructureFigure;
import xstampp.astpa.controlstructure.figure.TextFieldFigure;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IComponent;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * CSEditPolicy manages the positioning/creation of the components inside their parents
 * 
 * 
 * @version 1.0
 * @author Lukas Balzer, Aliaksei Babkovich
 * 
 */
public class CSEditPolicy extends XYLayoutEditPolicy {

  private IControlStructureEditorDataModel dataModel;
  private Figure parentFeedback;
  private final String stepID;
  private Border tmpBorder;
  private ConnectionFigure relative;
  private List<IFigure> feedback = new ArrayList<>();
  private UUID rootId;
  /**
   * the offset of the process variables and values
   * 
   * @author Lukas Balzer
   */
  public static final int PROCESS_MODEL_COLUMN = 20;

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param model
   *          The DataModel which contains all model classes
   * @param stepId
   *          TODO
   */
  public CSEditPolicy(IControlStructureEditorDataModel model, String stepId) {
    super();
    this.stepID = stepId;

    this.dataModel = model;
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param rect
   *          the rectangle to translate
   */
  public void getAbsoluteLayout(Translatable rect) {

    this.translateFromAbsoluteToLayoutRelative(rect);

  }

  public UUID getRootId() {
    if (rootId == null) {
      rootId = ((IRectangleComponent) getHost().getViewer().getContents().getModel()).getId();
    }
    return rootId;
  }

  @Override
  protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
    ComponentChangeLayoutCommand command = null;
    if (getHost().canEdit()) {
      command = new ComponentChangeLayoutCommand(getRootId(), this.dataModel, this.stepID);

      IFigure childFigure = ((IControlStructureEditPart) child).getFigure();
      getHost().getRoot();
      command.setModel(child.getModel());
      command.setConstraint((Rectangle) constraint);
    }
    return command;
  }

  @Override
  protected Command getCreateCommand(CreateRequest request) {
    if (request.getType() == RequestConstants.REQ_CREATE && getHost().canEdit()) {
      ComponentCreateCommand command = new ComponentCreateCommand(getRootId(), this.dataModel,
          this.stepID);
      command.setFeedbackLayer(this.getFeedbackLayer());
      // the root Edit Part is the EditPart on which this policy is
      // installed
      IRectangleComponent rootModel = (IRectangleComponent) this.getHost().getModel();

      // the EditPart on shall be created is the newObject which is given
      // by the request,
      // which is send from the ModelCreationFactory
      IRectangleComponent compModel = (IRectangleComponent) request.getNewObject();

      command.setRootModel(rootModel);
      command.setComponentModel(compModel);

      if (request.getNewObject() instanceof IComponent) {
        if (this.relative != null
            && getFeedbackLayer().getChildren().contains(this.relative.getFeedback())) {
          removeFeedback(this.relative.getFeedback());
        }
        this.relative = null;
        Rectangle constraint = (Rectangle) this.getConstraintFor(request);
        // if the components are ment for ProcessModel
        // if ((rootModel.getComponentType() == ComponentType.PROCESS_VARIABLE)
        // || (rootModel.getComponentType() == ComponentType.PROCESS_MODEL)) {
        // command.addConstraint(this.getHost().getParent().getModel());
        // constraint = this.addProcessModelConstraint(constraint,
        // rootModel, compModel);
        // }

        if ((compModel.getComponentType() == ComponentType.PROCESS_VALUE)
            && (rootModel.getComponentType() != ComponentType.PROCESS_VARIABLE)) {
          this.getFeedbackLayer().getChildren().clear();
          this.getFeedbackLayer().repaint();
        }

        if (constraint.x < 0) {
          constraint.x = 0;
          constraint.y = 0;
        }

        if (!constraint.getSize().contains(
            this.getDefaultSizeFor((ComponentType) request.getNewObjectType(), constraint))) {
          constraint.setSize(
              this.getDefaultSizeFor((ComponentType) request.getNewObjectType(), constraint));
        }
        if (command.canExecute() && !(getHost() instanceof RootEditPart)) {
          this.getHostFigure().showFeedback();
        }
        command.setLayout(constraint);

        // this if branch checks whether the command takes place on the root, since the a relation
        // can only exist on the root
        if (getHost() instanceof RootEditPart
            && (compModel.getComponentType() == ComponentType.CONTAINER
                || compModel.getComponentType() == ComponentType.CONTROLACTION)) {
          this.relative = findNearestRelative(constraint.getCenter());

          if (this.relative != null) {
            addFeedback(this.relative.getFeedback());
            command.setRelative(this.relative.getId());
          }
        }
        return command;
      }
    }
    return null;
  }

  /**
   * Finds the nearest relative (in general the nearest connection) if there is no it returns null
   *
   * @author Lukas Balzer
   *
   * @param point
   *          the point which should be used
   * @return the nearest relative or null if no
   */
  private ConnectionFigure findNearestRelative(Point point) {
    Rectangle bounds;
    double distance = Double.MAX_VALUE;
    double newDistance;
    ConnectionFigure relativeFigure = null;
    for (Object connection : getLayer(LayerConstants.CONNECTION_LAYER).getChildren()) {
      if (connection instanceof ConnectionFigure) {
        bounds = ((ConnectionFigure) connection).getBounds();
        newDistance = point.getDistance(bounds.getCenter());
        if (newDistance < distance) {
          relativeFigure = (ConnectionFigure) connection;
          distance = newDistance;
        }
      }
    }
    return relativeFigure;
  }

  @Override
  public void eraseSourceFeedback(Request request) {
    // TODO Auto-generated method stub
    super.eraseSourceFeedback(request);
  }

  @Override
  public void eraseTargetFeedback(Request request) {
    // TODO Auto-generated method stub
    super.eraseTargetFeedback(request);
  }

  @Override
  public IControlStructureEditPart getHost() {
    return (IControlStructureEditPart) super.getHost();
  }

  @Override
  protected IControlStructureFigure getHostFigure() {
    // TODO Auto-generated method stub
    return (IControlStructureFigure) super.getHostFigure();
  }

  private Dimension getDefaultSizeFor(ComponentType type, Rectangle rect) {
    Dimension dim = new Dimension();
    switch (type) {
      case PROCESS_VALUE:
      case TEXTFIELD:
        dim.setWidth(Math.max(TextFieldFigure.TEXTBOX_FIGURE_DEFSIZE.width, rect.width));
        dim.setHeight(Math.max(TextFieldFigure.TEXTBOX_FIGURE_DEFSIZE.height, rect.height));
        return dim;
      default:

        dim.setWidth(Math.max(ComponentFigure.COMPONENT_FIGURE_DEFSIZE.width, rect.width));
        dim.setHeight(Math.max(ComponentFigure.COMPONENT_FIGURE_DEFSIZE.height, rect.height));
        return dim;
    }
  }

  private Rectangle addProcessModelConstraint(Rectangle constraint, IRectangleComponent rootModel,
      IRectangleComponent compModel) {
    constraint.y = ((IControlStructureFigure) this.getHost().getFigure()).getTextField()
        .getPreferredSize().height;

    if (rootModel.getComponentType() == ComponentType.PROCESS_MODEL) {
      constraint.y += CSEditPolicy.PROCESS_MODEL_COLUMN;
      constraint.setWidth(rootModel.getLayout(this.stepID.equals(CSEditor.ID)).width
          - (2 * CSEditPolicy.PROCESS_MODEL_COLUMN));

    } else if (compModel.getComponentType() == ComponentType.PROCESS_VALUE) {
      this.parentFeedback = new Figure();
      this.parentFeedback.setBorder(new LineBorder(ColorConstants.lightBlue));
      Rectangle bounds = rootModel.getLayout(this.stepID.equals(CSEditor.ID)).getCopy();
      this.getHostFigure().translateToAbsolute(bounds);
      this.parentFeedback.setBounds(bounds);
      if (this.getFeedbackLayer().getChildren().size() > 1) {
        this.getFeedbackLayer().getChildren().clear();
        this.getFeedbackLayer().repaint();
      }
      this.addFeedback(this.parentFeedback);

    }
    for (Object constraintChild : this.getHost().getChildren()) {
      constraint.y += ((IControlStructureEditPart) constraintChild).getFigure().getBounds().height;

    }
    constraint.x = CSEditPolicy.PROCESS_MODEL_COLUMN;

    return constraint;
  }

  @Override
  public Command getCommand(Request request) {
    if (!getHost().canEdit()) {
      return null;
    }
    if (RequestConstants.REQ_MOVE_CHILDREN.equals(request.getType())) {
      EditPart part = getHost().getViewer()
          .findObjectAt(((ChangeBoundsRequest) request).getLocation());
      Object connectable = ((ChangeBoundsRequest) request).getEditParts().get(0);
      if (part instanceof IRelativePart && connectable instanceof IMemberEditPart
          && ((IRelativePart) part).getId() != ((IMemberEditPart) connectable).getRelativeId()) {
        IFigure conn = ((IRelativePart) part).getFeedback(((IMemberEditPart) connectable));
        if (!(this.feedback.contains(conn))) {
          addFeedback(conn);
          this.feedback.add(conn);
        }
        return new SwapRelativeCommand(getRootId(), this.dataModel, this.stepID,
            (IRelativePart) part, (IMemberEditPart) connectable);
      }
      for (IFigure figure : this.feedback) {
        if (figure.getParent() == getFeedbackLayer()) {
          removeFeedback(figure);
        }
      }
      this.feedback.clear();

    }
    return super.getCommand(request);
  }
}
