/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software
 * Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.linking;

import xstampp.astpa.model.EntryWithSeverity;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class UndoChangeSeverity implements IUndoCallback {

  private EntryWithSeverity entry;
  private Severity oldSeverity;
  private ObserverValue changeConstant;
  private Severity newSeverity;

  public UndoChangeSeverity(EntryWithSeverity entry, Severity oldSeverity, ObserverValue changeConstant) {
    this.entry = entry;
    this.oldSeverity = oldSeverity;
    this.changeConstant = changeConstant;
    this.newSeverity = entry.getSeverity();
  }

  @Override
  public void redo() {
    entry.setSeverity(newSeverity);
  }

  @Override
  public ObserverValue getChangeConstant() {
    return changeConstant;
  }

  @Override
  public void undo() {
    entry.setSeverity(oldSeverity);
  }
}
