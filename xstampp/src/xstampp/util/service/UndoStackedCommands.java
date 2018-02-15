/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.util.service;

import java.util.ArrayList;
import java.util.List;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoStackedCommands implements IUndoCallback {

  private List<IUndoCallback> recordList;
  private ObserverValue updateValue;
  private IUndoRedoProvider undoRedoService;

  public UndoStackedCommands(IUndoRedoProvider undoRedoService) {
    this.undoRedoService = undoRedoService;
    this.recordList = new ArrayList<>();
    this.updateValue = ObserverValue.UNSAVED_CHANGES;
  }

  public boolean add(IUndoCallback callback) {
    this.updateValue = callback.getChangeConstant();
    return this.recordList.add(callback);
  }

  public List<IUndoCallback> getRecordList() {
    return new ArrayList<>(this.recordList);
  }

  @Override
  public void undo() {
    
    this.recordList.forEach((callback) -> callback.undo());
  }

  @Override
  public void redo() {
    this.undoRedoService.startRecord();
    this.recordList.forEach((callback) -> callback.redo());
    this.undoRedoService.pushRecord();
  }

  @Override
  public ObserverValue getChangeConstant() {
    return this.updateValue;
  }

}
