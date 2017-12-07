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
package xstampp.astpa.ui.causalfactors;

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.linking.Link;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;

public class CellEditorFactorNote extends GridCellTextEditor {

  private ICausalFactorDataModel dataInterface;
  private Link link;

  public CellEditorFactorNote(GridWrapper gridWrapper, ICausalFactorDataModel dataInterface, Link link) {
    super(gridWrapper, link.getNote(), link.getId());
    this.dataInterface = dataInterface;
    this.link = link;
    setDefaultText("Note/Rational...");
  }

  @Override
  public void updateDataModel(String newText) {
    this.dataInterface.getLinkController().changeLinkNote(link, newText);
  }

  @Override
  public void delete() {
    updateDataModel("");
  }

  @Override
  protected void editorOpening() {
    dataInterface.lockUpdate();
  }

  @Override
  protected void editorClosing() {
    dataInterface.releaseLockAndUpdate(new ObserverValue[] {});
  }
}
