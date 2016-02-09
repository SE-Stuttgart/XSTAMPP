package xstpa.settings;

/*******************************************************************************
 * Copyright (c) 2015 Yannic Sowoidnich
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/


import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import xstampp.Activator;

/**
 * Generate the export preference page.
 * 
 * @author Yannic Sowoidnich
 * 
 */
public class PreferencePageSettings extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {


	public static final String ID ="xstpa.preferences.choosePath"; 
	private FileFieldEditor actsSettingsFieldEditor;


	/**
	 * Constructor using grid layout.
	 */
	public PreferencePageSettings() {
		super(FieldEditorPreferencePage.GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
		this.setDescription("Please set the Path for the ACTS File");
	}

	@Override
	protected void createFieldEditors() {


		this.actsSettingsFieldEditor = new FileFieldEditor(
				"ACTS_Path", "Please select the Path to the ACTS-File",
				this.getFieldEditorParent());
		this.addField(this.actsSettingsFieldEditor);


	}

	@Override
	protected void performDefaults() {

		this.actsSettingsFieldEditor.loadDefault();
		
	}

	@Override
	public boolean performOk() {

		this.actsSettingsFieldEditor.store();

		return super.performOk();
	}

}
