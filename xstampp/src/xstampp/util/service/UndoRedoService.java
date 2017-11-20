/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.util.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.editors.StandartEditorPart;
import xstampp.util.IUndoCallback;

public class UndoRedoService extends AbstractSourceProvider implements IPartListener {

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

  private Map<String, Stack<IUndoCallback>> undoStacksToEditorIds;
  private Map<String, Stack<IUndoCallback>> redoStacksToEditorIds;
  private static final int LIMIT = 50;
  private Stack<IUndoCallback> undoStack;
  private Stack<IUndoCallback> redoStack;
  private boolean canUndo;
  private boolean canRedo;
  private boolean lock;
  private UndoStackedCommands recordList;
  private boolean recording;

  public UndoRedoService() {
    this.undoStacksToEditorIds = new HashMap<>();
    this.redoStacksToEditorIds = new HashMap<>();
    this.undoStack = new Stack<>();
    this.redoStack = new Stack<>();
    this.canRedo = false;
    this.canUndo = false;
    this.lock = false;
  }

  /**
   * this initializes a new record list in which all following {@link IUndoCallback}'s will be
   * included in one {@link IUndoCallback}.
   * <br>
   * The {@link IUndoCallback}s that are pushed onto the stack with
   * {@link UndoRedoService#push(IUndoCallback)} are recorded until either
   * {@link UndoRedoService#getRecord()} or {@link UndoRedoService#pushRecord()} is called.
   */
  public void startRecord() {
    if (!this.recording) {
      this.recording = true;
      this.recordList = new UndoStackedCommands(this);
    }
  }

  /**
   * returns the recordList that includes all {@link IUndoCallback}'s since the last call of
   * {@link #startRecord()}, sets the record list of this service to null when finished.
   * 
   * @return the recordList that includes all {@link IUndoCallback}'s since the last call of
   *         {@link #startRecord()} or null if {@link #startRecord()} hasn't been called since the
   *         last call of this method
   */
  public List<IUndoCallback> getRecord() {

    List<IUndoCallback> recordList = null;
    if (recording) {
      recordList = this.recordList.getRecordList();
      this.recording = false;
      this.recordList = null;
      this.undoStack.removeAll(recordList);
    }
    return recordList;
  }

  /**
   * If this method is called after a call to {@link UndoRedoService#startRecord()} but before a
   * {@link UndoRedoService#getRecord()} call than a {@link IUndoCallback} containing all
   * {@link IUndoCallback}s pushed since the call of startRecord is pushed onto the undo stack.
   * <p>
   * The callback will undo/redo all included callbacks which enables undo of group commands
   * 
   * @return <b style="color:blue">true</b> if the service was recording and a callback has been
   *         pushed<br>
   *         <b style="color:blue">false</b> otherwise
   */
  public boolean pushRecord() {
    if (recording) {
      this.recording = false;
      push(this.recordList);
      this.recordList = null;
    }
    return recording;
  }

  public IUndoCallback push(IUndoCallback callback) {
    if (!this.lock && recording) {
      this.recordList.add(callback);
    } else if (!this.lock) {
      this.canUndo = true;
      if (this.undoStack.size() + 1 > LIMIT) {
        this.undoStack.removeElementAt(0);
      }
      
      IUndoCallback push = this.undoStack.push(callback);
      fireSourceChanged(ISources.WORKBENCH, CAN_UNDO, true);
      return push;
    }
    return null;
  }

  private IUndoCallback _pushRedo(IUndoCallback callback) {
    this.canRedo = true;
    if (this.undoStack.size() + 1 > LIMIT) {
      this.redoStack.removeElementAt(0);
    }
    IUndoCallback push = this.redoStack.push(callback);
    fireSourceChanged(ISources.WORKBENCH, CAN_REDO, this.canRedo);
    return push;
  }

  /**
   * This method locks the undo stack for its runtime which prevents any callbacks to be pushed to
   * the undo stack. Than it calls {@link IUndoCallback#undo()} in the callback at the top of the
   * stack and pushes it afterwards onto the redo stack. Finally it fires a property change and
   * releases the lock.
   * 
   * @return the {@link IUndoCallback} that is pushed onto the redo stack
   */
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

  /**
   * This method calls {@link IUndoCallback#redo()} in the callback at the top of the redo
   * stack. Finally it fires a property change and
   * releases the undo lock if one is set.
   * 
   * @return the {@link IUndoCallback} that is redone
   */
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

  public IUndoCallback[] getUndoStack() {
    return undoStack.toArray(new IUndoCallback[0]);
  }

  public IUndoCallback[] getRedoStack() {
    return redoStack.toArray(new IUndoCallback[0]);
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

  @Override
  public void partActivated(IWorkbenchPart part) {
    if (part instanceof StandartEditorPart) {
      String editorId = ((StandartEditorPart) part).getId();
      this.undoStacksToEditorIds.putIfAbsent(editorId, new Stack<>());
      this.redoStacksToEditorIds.putIfAbsent(editorId, new Stack<>());
      this.undoStack = this.undoStacksToEditorIds.get(editorId);
      this.redoStack = this.redoStacksToEditorIds.get(editorId);

      fireSourceChanged(ISources.WORKBENCH, CAN_REDO, !this.redoStack.isEmpty());
      fireSourceChanged(ISources.WORKBENCH, CAN_UNDO, !this.undoStack.isEmpty());
    }
  }

  @Override
  public void partBroughtToTop(IWorkbenchPart part) {
    // TODO Auto-generated method stub

  }

  @Override
  public void partClosed(IWorkbenchPart part) {
    // TODO Auto-generated method stub

  }

  @Override
  public void partDeactivated(IWorkbenchPart part) {
    fireSourceChanged(ISources.WORKBENCH, CAN_REDO, false);
    fireSourceChanged(ISources.WORKBENCH, CAN_UNDO, false);
  }

  @Override
  public void partOpened(IWorkbenchPart part) {
    // TODO Auto-generated method stub

  }
}
