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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

/**
 * a simple contribution adding a seperator to the action bar it is added to
 * 
 * @author Lukas Balzer
 * @since 1.0
 */
public class SeperatorContribution extends WorkbenchWindowControlContribution {

  @Override
  protected Control createControl(Composite parent) {
    // Create a composite to place the label in
    Composite comp = new Composite(parent, SWT.NONE);
    comp.setLayout(new FillLayout());
    @SuppressWarnings("unused")
    Label sep = new Label(comp, SWT.SEPARATOR);

    return null;
  }

}
