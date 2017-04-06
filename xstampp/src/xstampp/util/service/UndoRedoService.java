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
import java.util.Map;
import java.util.Stack;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

import xstampp.util.IUndoCallback;

public class UndoRedoService extends AbstractSourceProvider {
  
  /**
   * The variable id of the canUndo
   * 
   * @author Lukas Balzer
   */
  public static final String CAN_UNDO = "xstampp.util.service.canUndo"; //$NON-NLS-1$
  
  /**
   * The variable id of the canRedo
   * 
   * @author Lukas Balzer
   */
  public static final String CAN_REDO = "xstampp.util.service.canRedo"; //$NON-NLS-1$
  
  private static final int LIMIT = 15;
  private Stack<IUndoCallback> undoStack;
  private Stack<IUndoCallback> redoStack;
  private boolean canUndo;
  private boolean canRedo;
  private boolean lock;

  public UndoRedoService() {
    this.undoStack = new Stack<>();
    this.redoStack = new Stack<>();
    this.canRedo = false;
    this.canUndo = false;
    this.lock = false;
  }

  public IUndoCallback push(IUndoCallback callback) {
    if(!this.lock) {
      this.canUndo = true;
      fireSourceChanged(ISources.WORKBENCH, CAN_UNDO, true);
      if(this.undoStack.size() + 1 > LIMIT) {
        this.undoStack.removeElementAt(0);
      }
      return this.undoStack.push(callback);
    }
    return null;
  }

  private IUndoCallback _pushRedo(IUndoCallback callback) {
    this.canRedo = true;
    fireSourceChanged(ISources.WORKBENCH, CAN_REDO, this.canRedo);
    if(this.undoStack.size() + 1 > LIMIT) {
      this.redoStack.removeElementAt(0);
    }
    return this.redoStack.push(callback);
  }

  public IUndoCallback undo() {
    this.lock = true;
    IUndoCallback result = null;
    this.canUndo = !this.undoStack.isEmpty();
    if (this.canUndo) {
      IUndoCallback pop = this.undoStack.pop();
      this.canUndo = !this.undoStack.isEmpty();
      pop.undo();
      result = this._pushRedo(pop);
    }
    fireSourceChanged(ISources.WORKBENCH, CAN_UNDO, canUndo);
    this.lock = false;
    return result;
  }

  public IUndoCallback redo() {
    IUndoCallback result = null;
    this.canRedo = !this.redoStack.isEmpty();
    if (this.canRedo) {
      IUndoCallback pop = this.redoStack.pop();
      this.canRedo = !this.redoStack.isEmpty();
      pop.redo();
      result = pop;
    }
    fireSourceChanged(ISources.WORKBENCH, CAN_REDO, canRedo);
    this.lock = false;
    return result;
  }

  @Override
  public void dispose() {
    this.undoStack.clear();
    this.redoStack.clear();
  }

  @Override
  public Map<String, Boolean> getCurrentState() {
    Map<String, Boolean> values = new HashMap<>();
    values.put(CAN_UNDO, this.canUndo);
    values.put(CAN_REDO, this.canRedo);
    return values;
  }

  @Override
  public String[] getProvidedSourceNames() {
    return new String[] { CAN_UNDO, CAN_REDO };
  }
}
