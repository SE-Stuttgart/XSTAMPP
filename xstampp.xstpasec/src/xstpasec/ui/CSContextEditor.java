/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstpasec.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import xstampp.astpa.controlstructure.CSEditorWithPM;

public class CSContextEditor extends CSEditorWithPM {

  public static final String ID = "xstpasec.editor.context";

  public CSContextEditor() {
    super();
  }

  @Override
  public void createPartControl(Composite parent) {
    SashForm form = new SashForm(parent, SWT.VERTICAL);
    super.createPartControl(form);
    new View(getModelInterface()).createPartControl(form);
    form.setWeights(new int[] { 3, 1 });
  }

  @Override
  public String getTitle() {
    return "Context Table";
  }


}
