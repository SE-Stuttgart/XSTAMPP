/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.ui.CommonTableView;
import xstampp.preferences.IPreferenceConstants;

/**
 *
 * @author Lukas Balzer
 * @since 2.0.0
 *
 */
public class StpaPreferencePage extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage {

  private BooleanFieldEditor showNrColinTables;

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
    showNrColinTables = new BooleanFieldEditor(CommonTableView.COMMON_TABLE_VIEW_SHOW_NUMBER_COLUMN,
        "Show the row numbers in the Hazard, Accident, System Goals, Design Requirements and safety Constraints Views",
        this.getFieldEditorParent());
    addField(showNrColinTables);
  }

  @Override
  protected void checkState() {
    super.checkState();
  }

  @Override
  protected void performDefaults() {
    this.showNrColinTables.loadDefault();

  }

  @Override
  public boolean performOk() {
    this.showNrColinTables.store();
    return super.performOk();
  }
}
