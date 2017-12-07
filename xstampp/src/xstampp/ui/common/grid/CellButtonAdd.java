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
package xstampp.ui.common.grid;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class CellButtonAdd extends CellButton {

  private ButtonAction action;

  public CellButtonAdd(ButtonAction action) {
    super(new Rectangle(
        -1, -1,
        GridWrapper.getAddButton16().getBounds().width,
        GridWrapper.getAddButton16().getBounds().height),
        GridWrapper.getAddButton16());
    this.action = action;
  }

  @Override
  public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
    this.action.clickAction();
  }
}
