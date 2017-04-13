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

package xstampp.usermanagement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import xstampp.usermanagement.api.IUser;
import xstampp.util.ColorManager;

/**
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public abstract class ModelShell {

  private IUser selectedUser;
  private String title;
  private String acceptLabel;
  private Label invalidLabel;
  private Button okButton;

  public ModelShell(String name, String acceptLabel) {
    this.title = name;
    this.acceptLabel = acceptLabel;
    this.selectedUser = null;
  }

  public IUser open() {
    if (this.selectedUser == null) {
      final Shell shell = new Shell(Display.getCurrent().getActiveShell(), SWT.CLOSE | SWT.APPLICATION_MODAL);
      GridLayout gridLayout = new GridLayout(2, false);
      shell.setLayout(gridLayout);
      shell.setSize(500, 300);

      Label nameLabel = new Label(shell, SWT.None);
      nameLabel.setText(title);
      nameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
      createCenter(shell);

      this.invalidLabel = new Label(shell, SWT.WRAP);
      invalidLabel.setBackground(ColorManager.registerColor("lightRed", new RGB(100, 0, 0)));
      invalidLabel.setVisible(false);
      invalidLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
      Button cancelButton = new Button(shell, SWT.PUSH);
      cancelButton.setText("Cancel");
      cancelButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          shell.close();
        }
      });
      cancelButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));

      okButton = new Button(shell, SWT.PUSH);
      okButton.setText(acceptLabel);
      okButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          if (doAccept()) {
            shell.close();
          }
        }
      });
      okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
      okButton.setEnabled(false);
      shell.open();
      while (!shell.isDisposed()) {
        if (!Display.getDefault().readAndDispatch()) {
          Display.getDefault().sleep();
        }
      }
    }
    return this.selectedUser;
  }

  protected void invalidate(String error) {
    if (!invalidLabel.isDisposed()) {
      this.invalidLabel.setText(error);
      this.invalidLabel.setVisible(true);
    }
  }

  protected void setUnchecked() {
    if (!invalidLabel.isDisposed()) {
      this.invalidLabel.setVisible(false);
    }
  }

  public void setSelectedUser(IUser selectedUser) {
    this.selectedUser = selectedUser;
  }

  protected void canAccept() {
    if (!okButton.isDisposed()) {
      this.okButton.setEnabled(false);
    }
  }
  public void setTitle(String title) {
    this.title = title;
  }
  
  public void setAcceptLabel(String acceptLabel) {
    this.acceptLabel = acceptLabel;
  }

  protected abstract void validate();

  protected abstract boolean doAccept();

  protected abstract void createCenter(Shell parent);
}
