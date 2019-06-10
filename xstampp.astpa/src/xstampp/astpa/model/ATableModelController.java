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
package xstampp.astpa.model;

import java.util.Observable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import xstampp.astpa.model.interfaces.ITableModel;  
import xstampp.astpa.model.service.UndoTextChange;
import xstampp.model.ObserverValue;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class ATableModelController extends Observable {

  protected boolean isExclusiveUserFile;
  
  public ATableModelController() {
  }

  private boolean setModelText(ITableModel model, String input, ObserverValue value, boolean changeTitle) {
    if (!(model instanceof BadReferenceModel)) {
      boolean changed;
      String oldText;
      String newText = input.replaceAll("\r", "");
      if (changeTitle) {
        changed = !newText.equals(((ATableModel) model).getTitle());
        oldText = ((ATableModel) model).setTitle(newText);
      } else {
        changed = !newText.equals(((ATableModel) model).getDescription());
        oldText = ((ATableModel) model).setDescription(newText);
      }
      if (changed) {
        UndoTextChange textChange = new UndoTextChange(oldText, newText, value);
        textChange.setConsumer((text) -> setModelText(model, text, value, changeTitle));
        setChanged();
        notifyObservers(textChange);
        return true;
      }
    }
    return false;
  }

  public boolean setModelTitle(ITableModel model, String newText, ObserverValue value) {
    return setModelText(model, newText, value, true);
  }

  public boolean setModelDescription(ITableModel model, String newText, ObserverValue value) {
    return setModelText(model, newText, value, false);
  }

  public boolean isExclusiveUserFile() {
    return isExclusiveUserFile;
  }

  public void setExclusiveUserFile(boolean isExclusiveUserFile) {
    this.isExclusiveUserFile = isExclusiveUserFile;
  }
}
