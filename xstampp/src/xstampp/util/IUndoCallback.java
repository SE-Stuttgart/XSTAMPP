/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.util;

import xstampp.model.ObserverValue;
import xstampp.util.service.UndoRedoService;

/**
 * This interface is used in the {@link UndoRedoService} of the xstampp platform. it is used to fill
 * a command stack in by pushing through the {@link UndoRedoService#push(IUndoCallback)} method.
 *
 */
public interface IUndoCallback {

  /**
   * This method is called to undo all actions performed by the event that constructed this object.
   * Note that during the call of undo in the {@link UndoRedoService} no new {@link IUndoCallback}
   * is added to the stack.
   */
  void undo();

  /**
   * This method is called to redo the actions performed by the event that constructed this object.
   * Note that during the call of redo in the {@link UndoRedoService} no new {@link IUndoCallback}
   * is added to the stack.
   */
  void redo();

  /**
   * 
   * @return the {@link ObserverValue} that can be used to notify Observers/Clients/... about
   *         changes in the data model.
   */
  ObserverValue getChangeConstant();
}
