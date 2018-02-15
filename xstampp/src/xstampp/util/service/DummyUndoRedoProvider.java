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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xstampp.util.IUndoCallback;

class DummyUndoRedoProvider implements IUndoRedoProvider {

  @Override
  public void startRecord() {
  }

  @Override
  public List<IUndoCallback> getRecord() {
    return new ArrayList<>();
  }

  @Override
  public boolean pushRecord() {
    return false;
  }

  @Override
  public IUndoCallback push(IUndoCallback callback) {
    return null;
  }

  @Override
  public IUndoCallback undo() {
    return null;
  }

  @Override
  public IUndoCallback redo() {
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
    values.put(CAN_UNDO, false);
    values.put(CAN_REDO, false);
    return values;
  }

  @Override
  public void dispose() {
  }

  @Override
  public void activate() {
  }

}
