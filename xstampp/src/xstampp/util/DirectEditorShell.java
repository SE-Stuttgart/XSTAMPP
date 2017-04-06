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
package xstampp.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DirectEditorShell {

  private Shell shell;
  private SelectionListener okListener;
  private String content;

  public DirectEditorShell(Control parent, Rectangle bounds, String initialContent, Rectangle editorBounds) {
    this.shell = new Shell(parent.getShell(), SWT.APPLICATION_MODAL);
    Point absPosition = parent.toDisplay(editorBounds.x - 1, editorBounds.y - 1);
    this.shell.setBounds(new Rectangle(absPosition.x, absPosition.y, editorBounds.width + 2, editorBounds.height + 35));
    this.shell.setLayout(null);
    this.shell.setBackground(null);
    this.shell.setBackgroundMode(SWT.INHERIT_FORCE);
    final Text text = new Text(shell, SWT.WRAP);
    text.setBounds(1, 1, bounds.width + 5, bounds.height);
    text.setText(initialContent);
    this.content = initialContent;
    text.addModifyListener(new ModifyListener() {

      @Override
      public void modifyText(ModifyEvent e) {
        content = text.getText();
        int newHeight = text.computeSize(text.getBounds().width, -1).y;
        text.setSize(text.getBounds().width, newHeight);
      }
    });
    Button ok = new Button(shell, SWT.PUSH);
    ok.setText("Ok");
    ok.setBounds(editorBounds.width - 80, editorBounds.height + 1, 30, 30);
    ok.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        if (okListener != null) {
          okListener.widgetSelected(e);
        }
        shell.close();
      }
    });

    Button cancel = new Button(shell, SWT.PUSH);
    cancel.setText("Cancel");
    cancel.setBounds(editorBounds.width - 50, editorBounds.height + 1, 50, 30);
    cancel.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        shell.close();
      }
    });

  }

  public String getContent() {
    return content;
  }

  public void addOkListener(SelectionListener listener) {
    this.okListener = listener;
  }

  public void open() {
    this.shell.open();

  }

}
