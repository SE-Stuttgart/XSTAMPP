/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package astpa.preferences;

import messages.Messages;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import astpa.Activator;

/**
 * Generate the export preference page.
 * 
 * @author Sebastian Sieber
 * 
 */
public class PreferencePageExport extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	private StringFieldEditor companyNameFieldEditor;
	private FileFieldEditor companyLogoFieldEditor;
	private ColorFieldEditor companyColorBackgroundFieldEditor;
	private ColorFieldEditor companyColorFontFieldEditor;
	
	
	/**
	 * Constructor using grid layout.
	 */
	public PreferencePageExport() {
		super(FieldEditorPreferencePage.GRID);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
		this.setDescription(Messages.ChangeExportValues);
	}
	
	@Override
	protected void createFieldEditors() {
		this.companyNameFieldEditor =
			new StringFieldEditor(IPreferenceConstants.COMPANY_NAME, Messages.Company, this.getFieldEditorParent());
		this.addField(this.companyNameFieldEditor);
		
		this.companyLogoFieldEditor =
			new FileFieldEditor(IPreferenceConstants.COMPANY_LOGO, Messages.Logo, this.getFieldEditorParent());
		this.addField(this.companyLogoFieldEditor);
		
		this.companyColorBackgroundFieldEditor =
			new ColorFieldEditor(IPreferenceConstants.COMPANY_BACKGROUND_COLOR, Messages.BackgroundColor,
				this.getFieldEditorParent());
		this.addField(this.companyColorBackgroundFieldEditor);
		
		this.companyColorFontFieldEditor =
			new ColorFieldEditor(IPreferenceConstants.COMPANY_FONT_COLOR, Messages.FontColor,
				this.getFieldEditorParent());
		this.addField(this.companyColorFontFieldEditor);
	}
	
	@Override
	protected void performDefaults() {
		this.companyColorBackgroundFieldEditor.loadDefault();
		this.companyColorFontFieldEditor.loadDefault();
		this.companyLogoFieldEditor.loadDefault();
		this.companyNameFieldEditor.loadDefault();
	}
	
	@Override
	public boolean performOk() {
		this.companyNameFieldEditor.store();
		this.companyLogoFieldEditor.store();
		this.companyColorBackgroundFieldEditor.store();
		this.companyColorFontFieldEditor.store();
		return super.performOk();
	}
	
}
