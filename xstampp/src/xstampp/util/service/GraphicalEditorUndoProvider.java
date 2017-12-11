/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.util.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.ui.ISources;

import xstampp.ui.editors.IGraphicalEditor;
import xstampp.util.IUndoCallback;

public class GraphicalEditorUndoProvider implements IUndoRedoProvider {

  private UndoRedoService undoRedoService;
  private CommandStack commandStack;

  public GraphicalEditorUndoProvider(UndoRedoService undoRedoService, IGraphicalEditor part) {
    this.undoRedoService = undoRedoService;
    this.commandStack = part.getEditDomain().getCommandStack();

  }

  public void setCommandStack(CommandStack commandStack) {
    this.commandStack = commandStack;
  }

  @Override
  public void startRecord() {
    // TODO Auto-generated method stub

  }

  @Override
  public List<IUndoCallback> getRecord() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean pushRecord() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public IUndoCallback push(IUndoCallback callback) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IUndoCallback undo() {
    if (commandStack.canUndo()) {
      commandStack.undo();
    }
    activate();
    return null;
  }

  @Override
  public IUndoCallback redo() {
    if (commandStack.canRedo()) {
      commandStack.redo();
    }
    activate();
    return null;
  }

  @Override
  public IUndoCallback[] getUndoStack() {
    return new IUndoCallback[0];
  }

  @Override
  public IUndoCallback[] getRedoStack() {
    return new IUndoCallback[0];
  }

  @Override
  public Map<String, Boolean> getCurrentState() {
    Map<String, Boolean> values = new HashMap<>();
    values.put(CAN_UNDO, commandStack.canUndo());
    values.put(CAN_REDO, commandStack.canRedo());
    return values;
  }

  @Override
  public void dispose() {

  }

  @Override
  public void activate() {
    undoRedoService.triggerSourceChanged(ISources.WORKBENCH, CAN_UNDO, commandStack.canUndo());
    undoRedoService.triggerSourceChanged(ISources.WORKBENCH, CAN_REDO, commandStack.canRedo());
  }

}
