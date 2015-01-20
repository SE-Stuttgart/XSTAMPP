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

package xstampp.preferences;

import messages.Messages;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import xstampp.Activator;

/**
 * Generate the color preference page.
 * 
 * @author Sebastian Sieber
 * 
 */
public class PreferencePageColorAndFont extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	private ColorFieldEditor navigationSelectedColor,
			navigationUnselectedColor, hoverColor, splitterForegroundColor,
			splitterBackgroundColor, CSFontColor;

	private FontFieldEditor navigationTitel, defaultFont;

	private BooleanFieldEditor useNavigationColor;

	/**
	 * Constructor using grid layout.
	 */
	public PreferencePageColorAndFont() {
		super(FieldEditorPreferencePage.GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
		this.setDescription(Messages.ChangeColorsAnd);
	}

	@Override
	protected void createFieldEditors() {
		this.useNavigationColor=new BooleanFieldEditor(IPreferenceConstants.USE_NAVIGATION_COLORS,
				"Highlight selected step items?", this.getFieldEditorParent());
		this.addField(useNavigationColor);
		this.navigationSelectedColor = new ColorFieldEditor(
				IPreferenceConstants.NAVIGATION_ITEM_SELECTED,
				Messages.SelectedNavItem, this.getFieldEditorParent());
		this.addField(this.navigationSelectedColor);
		

		// Fonts
		this.navigationTitel = new FontFieldEditor(
				IPreferenceConstants.NAVIGATION_TITLE_FONT, Messages.TitleFont,
				this.getFieldEditorParent());
		this.addField(this.navigationTitel);

		this.defaultFont = new FontFieldEditor(
				IPreferenceConstants.DEFAULT_FONT, Messages.DefaultFont,
				this.getFieldEditorParent());
		this.addField(this.defaultFont);
	}

	@Override
	protected void checkState() {
		super.checkState();
	}

	@Override
	protected void performDefaults() {
		this.navigationSelectedColor.loadDefault();
		this.navigationTitel.loadDefault();
		this.defaultFont.loadDefault();

	}

	@Override
	public boolean performOk() {
		this.navigationSelectedColor.store();
		this.navigationTitel.store();
		this.defaultFont.store();

		return super.performOk();
	}
}
