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
package xstampp.astpa.model.service;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.util.IUndoCallback;

public abstract class UndoTableModelChangeCallback<T> implements IUndoCallback {

  private T dataModel;
  private UUID entryId;
  private UndoTextChange descriptionChange;
  private UndoTextChange titleChange;

  public UndoTableModelChangeCallback(T dataModel, ITableModel model) {
    this(dataModel, model.getId());
  }

  public UndoTableModelChangeCallback(T dataModel, UUID modelId) {
    this.dataModel = dataModel;
    this.entryId = modelId;
    this.descriptionChange = new UndoTextChange();
    this.titleChange = new UndoTextChange();
  }

  public void setDescriptionChange(String oldDescription, String newDescription) {
    this.descriptionChange = new UndoTextChange(oldDescription, newDescription, getChangeConstant());
    this.descriptionChange.setConsumer((text) -> undoDescription(text));
  }

  public void setTitleChange(String oldTitle, String newTitle) {
    this.titleChange = new UndoTextChange(oldTitle, newTitle, getChangeConstant());
    this.titleChange.setConsumer((text) -> undoTitle(text));
  }

  @Override
  public void undo() {
    this.descriptionChange.undo();
    this.titleChange.undo();
  }

  @Override
  public void redo() {
    this.descriptionChange.redo();
    this.titleChange.redo();
  }

  public UUID getEntryId() {
    return entryId;
  }

  /**
   * @return the dataModel
   */
  public T getDataModel() {
    return dataModel;
  }

  protected abstract void undoDescription(String description);

  protected abstract void undoTitle(String title);
}
