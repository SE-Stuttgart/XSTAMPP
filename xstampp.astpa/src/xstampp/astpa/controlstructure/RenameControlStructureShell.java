/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.controlstructure;

import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.ui.common.shell.ModalShell;
import xstampp.ui.common.shell.ModalShell.Style;

public class RenameControlStructureShell extends ModalShell {

  private TextInput nameInput;
  private IControlStructureEditorDataModel dataModel;
  private UUID rootId;

  public RenameControlStructureShell(IControlStructureEditorDataModel dataModel, UUID rootId) {
    super("Rename Control Structure", Style.PACKED);
    this.dataModel = dataModel;
    this.rootId = rootId;
    this.dataModel.setActiveRoot(rootId);
  }

  @Override
  protected boolean validate() {
    return !this.nameInput.getText().isEmpty();
  }

  @Override
  protected boolean doAccept() {
    for (IRectangleComponent comp : dataModel.getRoots()) {
      if (comp.getText().equals(nameInput.getText())) {
        invalidate("The name for the control structure must be unique!");
        return false;
      }
    }
    if (dataModel.changeComponentText(rootId, nameInput.getText())) {
      setReturnValue(nameInput.getText());
      return true;
    }
    return false;
  }

  @Override
  protected void createCenter(Shell parent) {
    String text = dataModel.getComponent(rootId).getText();
    this.nameInput = new TextInput(parent, SWT.None, "Name:", text);

  }

}
