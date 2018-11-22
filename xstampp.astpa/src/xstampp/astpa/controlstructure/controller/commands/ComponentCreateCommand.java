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

import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.controller.policys.CSEditPolicy;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * This class is command that uses ComponentCreationFactory class and memorize useful information
 * and creates aktuator component. Used in palette.
 * 
 * @version 1.0
 * @author Aliaksei Babkovich, Lukas Balzer
 * 
 */

public class ComponentCreateCommand extends ControlStructureAbstractCommand {

  private IRectangleComponent parentModel;
  private IRectangleComponent compModel;
  private UUID componentId;
  private UUID parentId;
  private Rectangle layout;
  private Rectangle parentLayout;
  private Rectangle oldParentLayout;
  private UUID relativeId;

  /**
   * This Constructor sets the Model of the Component and of it's root to null so the Component has
   * no root when this command is called
   * 
   * @author Lukas Balzer, Aliaksei Babkovich
   * @param model
   *          The dataModel which contains all model classes
   * @param stepID
   *          the stepEditor ID
   * 
   */
  public ComponentCreateCommand(UUID rootId, IControlStructureEditorDataModel model,
      String stepID) {
    super(rootId, model, stepID);
    this.parentLayout = new Rectangle();
    this.oldParentLayout = new Rectangle();
    this.compModel = null;
    this.parentModel = null;

  }

  /**
   * 
   * @author Lukas Balzer, Aliaksei Babkovich
   * 
   * @param csModel
   *          The Model of the Component which shall be set
   */
  public void setComponentModel(IRectangleComponent csModel) {

    this.compModel = csModel;
  }

  /**
   * 
   * 
   * @author Lukas Balzer, Aliaksei Babkovich
   * 
   * @param csModel
   *          The Model of the Root, which shall be set as the parent of the Component
   */
  public void setRootModel(IRectangleComponent csModel) {
    this.parentModel = csModel;
    this.parentLayout = csModel.getLayout(this.getStepID().equals(CSEditor.ID)).getCopy();
    this.oldParentLayout = this.parentLayout.getCopy();
    this.parentId = csModel.getId();
  }

  /**
   * this adds a component to the map, which needs to be refreshed after execution
   * 
   * @author Lukas Balzer
   * 
   * @param model
   *          the layout constraint
   */
  public void addConstraint(Object model) {
  }

  /**
   * 
   * 
   * @author Lukas Balzer, Aliaksei Babkovich
   * 
   * @param rect
   *          the rectangle which stores the new Layout
   */
  public void setLayout(Rectangle rect) {
    if (this.compModel == null) {
      return;
    }
    if (this.parentModel.getComponentType() == ComponentType.PROCESS_VARIABLE) {
      this.parentLayout.setHeight(this.oldParentLayout.height + rect.height);
      this.parentLayout.setWidth(
          Math.max(this.oldParentLayout.width, rect.width + CSEditPolicy.PROCESS_MODEL_COLUMN));

    }
    this.layout = rect;
  }

  @Override
  public boolean canExecute() {
    if ((this.parentModel == null) || (this.compModel == null)) {
      return false;
    } else if (this.parentModel.getComponentType() == ComponentType.TEXTFIELD) {
      return false;
    }
    switch (this.compModel.getComponentType()) {
    case PROCESS_MODEL: {
      return this.parentModel.getComponentType() == ComponentType.CONTROLLER;
    }
    case PROCESS_VARIABLE: {
      return this.parentModel.getComponentType() == ComponentType.PROCESS_MODEL;
    }
    case PROCESS_VALUE: {
      return this.parentModel.getComponentType() == ComponentType.PROCESS_VARIABLE;
    }
    default: {
      return true;
    }
    }

  }

  /**
   * add new figure to root figure as child
   */
  @Override
  public void execute() {
    super.execute();
    this.deleteFeedback();
    UUID link = compModel.getControlActionLink();
    this.componentId = this.getDataModel().addComponent(link, this.parentModel.getId(), this.layout,
        this.compModel.getText(), this.compModel.getComponentType(), -1);
    // the following branch adds the functionality if inheriting the relation from a container
    // component
    if (this.parentModel.getComponentType() == ComponentType.CONTAINER) {
      this.getDataModel().setRelativeOfComponent(this.componentId, this.parentModel.getRelative());
    } //
    else if (this.relativeId != null) {
      this.getDataModel().setRelativeOfComponent(this.componentId, this.relativeId);
    }
  }

  @Override
  public boolean canUndo() {
    if ((this.parentModel == null) || (this.compModel == null)) {
      return false;
    }
    return true;

  }

  @Override
  public void undo() {
    this.getDataModel().removeComponent(this.componentId);
    this.getDataModel().changeComponentLayout(this.parentId, this.oldParentLayout,
        this.getStepID().equals(CSEditor.ID));

  }

  @Override
  public void redo() {
    this.getDataModel().recoverComponent(this.parentId, this.componentId);
  }

  /**
   * adds a IRelativePart which is set as the components relative part, NOTE: that only components
   * of the type CONTAINER and CONTROLACTION can have a relative, for all other components the
   * relative will be ignored
   * 
   * @param relativeID
   *          the uuid of a component which should be set as relative
   */
  public void setRelative(UUID relativeID) {
    this.relativeId = relativeID;
  }
}
