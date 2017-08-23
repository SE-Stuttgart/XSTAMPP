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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ComboContribiution extends ControlContribution {

  private Combo combo;
  private int style;
  private int size;

  /**
   * 
   * @param id
   *          {@link #setId(String)}
   * @param style
   *          the SWT style constants for the combo control
   * 
   * @see Combo
   */
  public ComboContribiution(String id, int style) {
    this(id, style, 100);
  }

  /**
   * 
   * @param id
   *          {@link #setId(String)}
   * @param style
   *          the SWT style constants for the combo control
   * @param size
   *          {@link #computeWidth(Control)}
   * 
   * @see Combo
   */
  public ComboContribiution(String id, int style, int size) {
    super(id);

    this.style = style;
    this.size = size;
  }

  @Override
  protected Control createControl(Composite parent) {
    combo = new Combo(parent, style);
    return combo;
  }

  @Override
  public int computeWidth(Control control) {
    return this.size;
  }

  public Combo getComboControl() {
    return combo;
  }

  public void setEnabled(boolean enabled) {
    if (this.combo != null) {
      this.combo.setEnabled(enabled);
    }
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
