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
package xstampp.astpa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import xstampp.model.ObserverValue;
import xstampp.util.IUndoCallback;

public class TestObserver implements Observer {

  private List<ObserverValue> updatedValues;

  public TestObserver() {
    updatedValues = new ArrayList<>();
  }

  public Observer getCleanObserver() {
    this.updatedValues.clear();
    return this;
  }

  public boolean hasUpdates(List<ObserverValue> updates) {
    return this.updatedValues.containsAll(updates);
  }

  public boolean hasNoUpdates(ObserverValue update) {
    return !this.updatedValues.contains(update);
  }
  public boolean hasNoUpdates() {
    return this.updatedValues.isEmpty();
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg instanceof ObserverValue) {
      this.updatedValues.add((ObserverValue) arg);
    } else if (arg instanceof IUndoCallback) {
      this.updatedValues.add(((IUndoCallback) arg).getChangeConstant());
    }
  }

}
