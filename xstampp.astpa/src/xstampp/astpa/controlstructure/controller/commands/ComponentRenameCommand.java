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

import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * ComponentRenameCommand is a Command which is used to rename a component
 * directly
 * 
 * 
 * @version 1.0
 * @author Lukas Balzer
 * 
 */
public class ComponentRenameCommand extends ControlStructureAbstractCommand {

  private String oldName, newName;
  private IRectangleComponent model;
  private Rectangle oldLayout, newLayout;

  /**
   * This constructor creates a Command which provides methods to manipulate a
   * text and if wished to undo the change later
   * 
   * @author Lukas Balzer
   * 
   * @param name
   *          The text which shall be changed
   * @param stepID
   *          the stepEditor ID
   * @param model
   *          The DataModel which contains all model classes
   */
  public ComponentRenameCommand(String name, UUID rootId,
      IControlStructureEditorDataModel model, String stepID) {
    super(rootId, model, stepID);
    this.oldName = name;
  }

  @Override
  public void execute() {
    super.execute();
    this.getDataModel().changeComponentText(this.model.getId(),
        this.newName);
  }

  @Override
  public void undo() {
    this.getDataModel().changeComponentText(this.model.getId(),
        this.oldName);
    this.getDataModel().changeComponentLayout(this.model.getId(),
        this.oldLayout, this.getStepID().equals(CSEditor.ID));

  }

  /**
   * this method is actually used to perform a rename
   * 
   * @author Lukas Balzer
   * 
   * @param newName
   *          the text to which the "oldName" is set
   * 
   */
  public void setNewName(String newName) {
    this.newName = newName;

  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param newLayout
   *          this rectangle only differs from the oldLayout if the text has
   *          goes over its bounds it
   */
  public void setNewLayout(Rectangle newLayout) {

    newLayout.x = this.oldLayout.x;
    newLayout.y = this.oldLayout.y;
    if (this.oldLayout.height >= newLayout.height) {
      this.newLayout = this.oldLayout;
    } else {
      this.newLayout = newLayout;
    }
  }

  /**
   * 
   * 
   * @author Lukas Balzer
   * 
   * @param model
   *          The model which stored the text
   */
  public void setModel(IRectangleComponent model) {
    this.model = model;
    this.oldLayout = model.getLayout(this.getStepID().equals(CSEditor.ID));
  }

}
