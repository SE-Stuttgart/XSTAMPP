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
package xstampp.ui.common.contentassist;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class LabelWithAssist extends Composite {

  /**
   * A {@link Label} with an {@link ControlDecoration} attached to it.
   * 
   * @param title
   *          the content of the Label
   * @param tip
   *          the Tooltip that is displayed when havering the {@link ControlDecoration}
   */
  public LabelWithAssist(Composite parent, int style, String title, String tip) {
    super(parent, style);
    this.setLayout(new GridLayout());
    Label text = new Label(this, SWT.NONE);
    text.setText(title);
    FieldDecorationRegistry decRegistry = FieldDecorationRegistry.getDefault();

    FieldDecoration infoField = decRegistry
        .getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION);

    ControlDecoration decoration = new ControlDecoration(text, SWT.TOP | SWT.RIGHT);
    decoration.setImage(infoField.getImage());

    decoration.setDescriptionText(tip);
  }

}
