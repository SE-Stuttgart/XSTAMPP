/*******************************************************************************
 * Copyright (c) 2013, 2015 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import xstampp.astpa.Activator;

/**
*
* @author Lukas Balzer
* @since 2.0.0
*
*/
public class StpaPreferencePage  extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage {

	public StpaPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
	}

	@Override
	public void init(IWorkbench workbench) {

		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("A-STPA Preferences");
		
	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		
	}

}
