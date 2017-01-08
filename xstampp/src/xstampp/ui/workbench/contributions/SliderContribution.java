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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Slider;

public class SliderContribution extends ControlContribution {

  private Slider slider;
  private int style;
  private int size;

  public SliderContribution(String id, int style, int size) {
    super(id);
    this.style = style;
    this.size = size;
  }

  @Override
  protected Control createControl(Composite parent) {
    slider = new Slider(parent, style);
    return slider;
  }

  @Override
  public int computeWidth(Control control) {
    return size;
  }

  public Slider getSliderControl() {
    return slider;
  }

  public void setEnabled(boolean enabled) {
    if (this.slider != null) {
      this.slider.setEnabled(enabled);
    }
  }
}
