/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.ui.workbench.contributions;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ButtonContribution extends ControlContribution {

  private Button button;
  private int style;
  private int size;

  /**
   * {@link ButtonContribution#ButtonContribution(String, int, int)}
   */
  public ButtonContribution(String id) {
    this(id, SWT.PUSH);
  }

  /**
   * {@link ButtonContribution#ButtonContribution(String, int, int)}
   */
  public ButtonContribution(String id, int style) {
    this(id, style, 30);
  }

  /**
   * 
   * @param id
   *          {@link #setId(String)}
   * @param style
   *          the style constant for the button
   * @param size
   *          the width of the contribution
   * 
   * @see Button
   */
  public ButtonContribution(String id, int style, int size) {
    super(id);
    this.style = style;
    this.size = size;
  }

  @Override
  protected Control createControl(Composite parent) {
    button = new Button(parent, this.style);
    return button;
  }

  @Override
  public int computeWidth(Control control) {
    return size;
  }

  public Button getButtonControl() {
    return button;
  }

  public void setEnabled(boolean enabled) {
    if (this.button != null) {
      this.button.setEnabled(enabled);
    }
  }

}
