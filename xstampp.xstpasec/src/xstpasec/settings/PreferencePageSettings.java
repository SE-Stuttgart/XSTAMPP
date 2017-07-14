package xstpasec.settings;

/*******************************************************************************
 * Copyright (c) 2015, 2016 Yannic Sowoidnich
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/


import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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


	public static final String ID ="xstpasec.preferences.choosePath"; 
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
				"ACTS_Path", "Path to the ACTS-File:",
				this.getFieldEditorParent()){
			@Override
			protected void createControl(Composite parent) {
				super.createControl(parent);
				Label help = new Label(parent, SWT.None);
				help.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
				help.setForeground(new Color(null, 100, 0, 20));
				help.setText("if the value is empty the default acts file stored in this installation will be used");
			}
		};
		actsSettingsFieldEditor.setErrorMessage("");
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
