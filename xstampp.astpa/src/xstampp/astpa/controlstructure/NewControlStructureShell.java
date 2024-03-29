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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import xstampp.astpa.messages.Messages;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUserProject;

public class NewControlStructureShell extends ModalShell {

  private TextInput nameInput;
  private IControlStructureEditorDataModel dataModel;

  public NewControlStructureShell(IControlStructureEditorDataModel dataModel) {
    super(Messages.ControlStructure_New, Style.PACKED);
    this.dataModel = dataModel;
  }

  @Override
  public boolean open() {
    if (!(dataModel instanceof IUserProject)
        || ((IUserProject) dataModel).getUserSystem().checkAccess(AccessRights.ADMIN)) {
      return super.open();
    } else {
      MessageDialog.openInformation(Display.getCurrent().getActiveShell(),
          xstampp.astpa.usermanagement.Messages.NoAccessRights_title,
          xstampp.astpa.usermanagement.Messages.NoAccessRights_AdminNeeded);
      return false;
    }
  }

  @Override
  protected boolean validate() {
    return !this.nameInput.getText().isEmpty();
  }

  @Override
  protected boolean doAccept() {
    for (IRectangleComponent comp : dataModel.getRoots()) {
      if (comp.getText().equals(nameInput.getText())) {
        invalidate(Messages.ControlStructure_NameMustBeUnique);
        return false;
      }
    }
    dataModel.setRoot(new Rectangle(), this.nameInput.getText());
    return true;
  }

  @Override
  protected void createCenter(Shell parent) {
    this.nameInput = new TextInput(parent, SWT.None, "");

  }

}
