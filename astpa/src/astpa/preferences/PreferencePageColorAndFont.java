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
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import astpa.Activator;

/**
 * Generate the color preference page.
 * 
 * @author Sebastian Sieber
 * 
 */
public class PreferencePageColorAndFont extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	private ColorFieldEditor navigationSelectedColor, navigationUnselectedColor, hoverColor, splitterForegroundColor,
		splitterBackgroundColor, splitterFontColor;
	
	private FontFieldEditor navigationTitel, defaultFont;
	
	
	/**
	 * Constructor using grid layout.
	 */
	public PreferencePageColorAndFont() {
		super(FieldEditorPreferencePage.GRID);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.ChangeColorsAnd);
	}
	
	@Override
	protected void createFieldEditors() {
		this.navigationSelectedColor =
			new ColorFieldEditor(IPreferenceConstants.NAVIGATION_ITEM_SELECTED, Messages.SelectedNavItem,
				getFieldEditorParent());
		addField(this.navigationSelectedColor);
		
		this.navigationUnselectedColor =
			new ColorFieldEditor(IPreferenceConstants.NAVIGATION_ITEM_UNSELECTED, Messages.UnselectedNavItem,
				getFieldEditorParent());
		addField(this.navigationUnselectedColor);
		
		this.hoverColor = new ColorFieldEditor(IPreferenceConstants.HOVER_ITEM, Messages.Hover, getFieldEditorParent());
		addField(this.hoverColor);
		
		this.splitterForegroundColor =
			new ColorFieldEditor(IPreferenceConstants.SPLITTER_FOREGROUND, Messages.SplitterForegColor,
				getFieldEditorParent());
		addField(this.splitterForegroundColor);
		
		this.splitterBackgroundColor =
			new ColorFieldEditor(IPreferenceConstants.SPLITTER_FOREGROUND, Messages.SplitterBackgColor,
				getFieldEditorParent());
		addField(this.splitterBackgroundColor);
		
		this.splitterFontColor =
			new ColorFieldEditor(IPreferenceConstants.SPLITTER_FONT, Messages.SplitterFontColor, getFieldEditorParent());
		addField(this.splitterFontColor);
		
		// Fonts
		this.navigationTitel =
			new FontFieldEditor(IPreferenceConstants.NAVIGATION_TITLE_FONT, Messages.TitleFont, getFieldEditorParent());
		addField(this.navigationTitel);
		
		this.defaultFont =
			new FontFieldEditor(IPreferenceConstants.DEFAULT_FONT, Messages.DefaultFont, getFieldEditorParent());
		addField(this.defaultFont);
	}
	
	@Override
	protected void checkState() {
		super.checkState();
	}
	
	@Override
	protected void performDefaults() {
		this.navigationSelectedColor.loadDefault();
		this.navigationUnselectedColor.loadDefault();
		this.hoverColor.loadDefault();
		this.splitterForegroundColor.loadDefault();
		this.splitterBackgroundColor.loadDefault();
		this.splitterFontColor.loadDefault();
		this.navigationTitel.loadDefault();
		this.defaultFont.loadDefault();
		
	}
	
	@Override
	public boolean performOk() {
		this.navigationSelectedColor.store();
		this.navigationUnselectedColor.store();
		this.hoverColor.store();
		this.splitterForegroundColor.store();
		this.splitterBackgroundColor.store();
		this.splitterFontColor.store();
		this.navigationTitel.store();
		this.defaultFont.store();
		
		return super.performOk();
	}
}
