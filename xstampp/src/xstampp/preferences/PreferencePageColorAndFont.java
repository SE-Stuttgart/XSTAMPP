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
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import xstampp.Activator;

/**
 * Generate the color preference page.
 * 
 * @author Sebastian Sieber
 * 
 */
public class PreferencePageColorAndFont extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  private ColorFieldEditor navigationSelectedColor;

  private FontFieldEditor navigationTitel, defaultFont;

  private BooleanFieldEditor useNavigationColor;

  private RadioGroupFieldEditor extensionSortChooser;

  private RadioGroupFieldEditor nameSortChooser;

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
    this.useNavigationColor = new BooleanFieldEditor(IPreferenceConstants.USE_NAVIGATION_COLORS,
        Messages.PreferencePageColorAndFont_DoYouWantToHighlight_selected_step_items, this.getFieldEditorParent());
    this.addField(this.useNavigationColor);

    this.navigationSelectedColor = new ColorFieldEditor(IPreferenceConstants.NAVIGATION_ITEM_SELECTED,
        Messages.SelectedNavItem, this.getFieldEditorParent());
    this.addField(this.navigationSelectedColor);

    this.extensionSortChooser = new RadioGroupFieldEditor(IPreferenceConstants.NAVIGATION_EXTENSION_SORT,
        Messages.PreferencePageColorAndFont_SortExplorerByExtension, 3,
        new String[][] { { Messages.PreferencePageColorAndFont_From_a_to_z, "1" }, //$NON-NLS-1$
            { Messages.PreferencePageColorAndFont_From_z_to_a, "-1" }, //$NON-NLS-1$
            { Messages.PreferencePageColorAndFont_DontSortExplorer, "0" } //$NON-NLS-1$
        }, this.getFieldEditorParent(), true);
    this.addField(this.extensionSortChooser);

    this.nameSortChooser = new RadioGroupFieldEditor(IPreferenceConstants.NAVIGATION_NAME_SORT,
        Messages.PreferencePageColorAndFont_SortExplorerByFileName, 3,
        new String[][] { { Messages.PreferencePageColorAndFont_From_a_to_z, "1" }, //$NON-NLS-1$
            { Messages.PreferencePageColorAndFont_From_z_to_a, "-1" }, //$NON-NLS-1$
            { Messages.PreferencePageColorAndFont_DontSortExplorer, "0" } //$NON-NLS-1$
        }, this.getFieldEditorParent(), true);
    this.addField(this.nameSortChooser);

    // Fonts
    this.defaultFont = new FontFieldEditor(IPreferenceConstants.DEFAULT_FONT, Messages.DefaultNavigationFont,
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
    this.defaultFont.loadDefault();
    this.extensionSortChooser.loadDefault();
    this.nameSortChooser.loadDefault();

  }

  @Override
  public boolean performOk() {
    this.navigationSelectedColor.store();
    this.defaultFont.store();
    this.extensionSortChooser.store();
    this.nameSortChooser.store();
    return super.performOk();
  }
}
