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

import java.util.List;
import java.util.Map;

import xstampp.util.IUndoCallback;

public interface IUndoRedoProvider {
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

  /**
   * this initializes a new record list in which all following {@link IUndoCallback}'s will be
   * included in one {@link IUndoCallback}.
   * <br>
   * The {@link IUndoCallback}s that are pushed onto the stack with
   * {@link UndoRedoService#push(IUndoCallback)} are recorded until either
   * {@link UndoRedoService#getRecord()} or {@link UndoRedoService#pushRecord()} is called.
   */
  void startRecord();

  /**
   * returns the recordList that includes all {@link IUndoCallback}'s since the last call of
   * {@link #startRecord()}, sets the record list of this service to null when finished.
   * 
   * @return the recordList that includes all {@link IUndoCallback}'s since the last call of
   *         {@link #startRecord()} or null if {@link #startRecord()} hasn't been called since the
   *         last call of this method
   */
  List<IUndoCallback> getRecord();

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
  boolean pushRecord();

  IUndoCallback push(IUndoCallback callback);

  /**
   * This method locks the undo stack for its runtime which prevents any callbacks to be pushed to
   * the undo stack. Than it calls {@link IUndoCallback#undo()} in the callback at the top of the
   * stack and pushes it afterwards onto the redo stack. Finally it fires a property change and
   * releases the lock.
   * 
   * @return the {@link IUndoCallback} that is pushed onto the redo stack
   */
  IUndoCallback undo();

  /**
   * This method calls {@link IUndoCallback#redo()} in the callback at the top of the redo
   * stack. Finally it fires a property change and
   * releases the undo lock if one is set.
   * 
   * @return the {@link IUndoCallback} that is redone
   */
  IUndoCallback redo();

  IUndoCallback[] getUndoStack();

  IUndoCallback[] getRedoStack();

  Map<String, Boolean> getCurrentState();

  void dispose();

  void activate();

}
