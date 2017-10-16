/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer- initial API and implementation
 ******************************************************************************/
package xstampp.ui.common.grid;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import xstampp.util.ColorManager;

public class GridTextEditorProvider {
  private String returnString;

  /**
   * 
   * @param text
   *          the initial text of the editor
   * @param textSize
   *          the size of the text input
   * @param size
   *          the overall
   * @return
   */
  public String open(String text, final Rectangle size) {
    final Shell shell = new Shell(Display.getCurrent().getActiveShell(),
        SWT.APPLICATION_MODAL | SWT.RESIZE);
    returnString = null;
    final GridLayout layout = new GridLayout();
    shell.setLayout(layout);

    shell.setLocation(size.x - (layout.marginWidth + layout.horizontalSpacing), size.y);
    final Text textField = new Text(shell, SWT.WRAP | SWT.V_SCROLL);
    textField.setLocation(new Point(0, 0));
    if(text == null) {
      textField.setText("");
    } else {
      textField.setText(text);
    }
    final GridData layoutData = new GridData(size.width, size.height);
    layoutData.horizontalAlignment = SWT.FILL;
    layoutData.verticalAlignment = SWT.FILL;
    layoutData.grabExcessHorizontalSpace = true;
    layoutData.grabExcessVerticalSpace = true;
    textField.setLayoutData(layoutData);

    Composite buttonComp = new Composite(shell, SWT.None);
    buttonComp.setLayout(new GridLayout(2, true));
    buttonComp.setLayoutData(new GridData(SWT.END, SWT.BOTTOM, false, false));
    GridData data = new GridData(70, 30);
    data.horizontalAlignment = SWT.RIGHT;
    data.grabExcessHorizontalSpace = false;
    data.grabExcessVerticalSpace = false;
    data.horizontalAlignment = SWT.RIGHT;
    Button okButton = new Button(buttonComp, SWT.PUSH);
    okButton.setForeground(ColorManager.COLOR_WHITE);
    okButton.setBackground(ColorManager.COLOR_GREEN);
    okButton.setText("Save");
    okButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent ev) {
        returnString = textField.getText();
        shell.close();
      }
    });
    okButton.setLayoutData(data);

    Button cancelButton = new Button(buttonComp, SWT.PUSH);
    cancelButton.setText("Cancel");
    cancelButton.setSize(20, 70);
    cancelButton.setForeground(ColorManager.COLOR_WHITE);
    cancelButton.setBackground(ColorManager.COLOR_RED);
    cancelButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent ev) {
        shell.close();
      }
    });
    cancelButton.setLayoutData(data);
    shell.pack();
    shell.addControlListener(new ControlAdapter() {
      @Override
      public void controlResized(ControlEvent e) {
        shell.layout();
      }
    });
    shell.open();
    while (!shell.isDisposed()) {
      if (!Display.getDefault().readAndDispatch()) {
        Display.getDefault().sleep();
      }
    }
    return returnString;

  }
}
