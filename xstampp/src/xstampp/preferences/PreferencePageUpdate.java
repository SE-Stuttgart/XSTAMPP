/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import xstampp.Activator;

/**
 * Generate the update preference page.
 * 
 * @author Sebastian Sieber
 * 
 */
public class PreferencePageUpdate extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  private StringFieldEditor updateLinkFieldEditor;

  /**
   * Constructor using grid layout.
   */
  public PreferencePageUpdate() {
    super(FieldEditorPreferencePage.GRID);
  }

  @Override
  public void init(IWorkbench workbench) {
    this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
    this.setDescription("Change the URL to update the application");//$NON-NLS-1$
  }

  @Override
  protected void createFieldEditors() {
    this.updateLinkFieldEditor = new StringFieldEditor(IPreferenceConstants.UPDATE_LINK, "URL:", //$NON-NLS-1$
        this.getFieldEditorParent());
    this.addField(this.updateLinkFieldEditor);

  }

  @Override
  protected void performDefaults() {
    this.updateLinkFieldEditor.loadDefault();
  }

  @Override
  public boolean performOk() {
    this.updateLinkFieldEditor.store();
    return super.performOk();
  }

}
