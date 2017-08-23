/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.preferences;

import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xstampp.astpa.messages.Messages;
import xstampp.astpa.model.DataModelController;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.projectsettings.ISettingsPage;
import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUserProject;

public class UCAHeaderSettings implements ISettingsPage {

  private UUID modelId;
  private String[] changedHeaders = null;

  private static String[] defaultUCAHeaders = new String[] { Messages.NotGiven,
      Messages.GivenIncorrectly, Messages.WrongTiming, Messages.StoppedTooSoon };

  @Override
  public Composite createControl(CTabFolder control, ModalShell parent, UUID modelId) {
    Composite content = new Composite(control, SWT.NONE);
    GridLayout contentLayout = new GridLayout();
    contentLayout.marginLeft = 5;
    contentLayout.marginRight = contentLayout.marginLeft;
    contentLayout.marginHeight = contentLayout.marginLeft;
    contentLayout.marginBottom = contentLayout.marginLeft;
    content.setLayout(contentLayout);
    Group customHeaderGroup = new Group(content, SWT.None);
    customHeaderGroup
        .setText(Messages.UCAHeaderSettings_CustomHeadersGroupTitle);
    customHeaderGroup.setToolTipText(
        Messages.UCAHeaderSettings_CustomHeadersGroupToolTip);
    customHeaderGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    customHeaderGroup.setLayout(new GridLayout());
    this.modelId = modelId;
    DataModelController dataModel = (DataModelController) ProjectManager.getContainerInstance()
        .getDataModel(modelId);
    String[] headers = dataModel.getControlActionController().getUCAHeaders();
    for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
      new TextCell(customHeaderGroup, headers, columnIndex);
    }
    return content;

  }

  @Override
  public boolean isVisible(UUID projectId) {
    if (ProjectManager.getContainerInstance().getDataModel(projectId) instanceof IUserProject) {
      return ((IUserProject) ProjectManager.getContainerInstance().getDataModel(projectId))
          .getUserSystem().checkAccess(AccessRights.ADMIN);
    }
    return true;
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public boolean doAccept() {
    DataModelController dataModel = (DataModelController) ProjectManager.getContainerInstance()
        .getDataModel(modelId);
    if (changedHeaders != null) {
      dataModel.getControlActionController().setUCACustomHeaders(changedHeaders);
    }
    return true;
  }

  @Override
  public String getName() {
    return Messages.UnsafeControlActionsTable;
  }

  @Override
  public void setName(String name) {
    // cannot be changed

  }

  @Override
  public String getId() {
    return "xstampp.astpa.settings.ucaTable"; //$NON-NLS-1$
  }

  class TextCell extends Composite {

    public TextCell(Composite parent, final String[] headers, final int columnIndex) {
      super(parent, SWT.None);
      setLayout(new GridLayout(3, false));
      setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      Label columnLabel = new Label(this, SWT.None);
      columnLabel.setText(Messages.UCAHeaderSettings_Column + (columnIndex + 1));
      final Text headerText = new Text(this, SWT.MULTI | SWT.WRAP);
      headerText.setText(headers[columnIndex]);
      headerText.setTextLimit(70);
      headerText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      Button setDefaultButton = new Button(this, SWT.PUSH);
      setDefaultButton.setText(Messages.UCAHeaderSettings_Default);
      setDefaultButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          if (UCAHeaderSettings.this.changedHeaders == null) {
            UCAHeaderSettings.this.changedHeaders = new String[4];
            for (int i = 0; i < 4; i++) {
              UCAHeaderSettings.this.changedHeaders[i] = headers[i];
            }
          }
          UCAHeaderSettings.this.changedHeaders[columnIndex] = defaultUCAHeaders[columnIndex];

          headerText.setText(defaultUCAHeaders[columnIndex]);
        }
      });
      headerText.addModifyListener(new ModifyListener() {

        @Override
        public void modifyText(ModifyEvent e) {
          String text = ((Text) e.widget).getText();
          if (UCAHeaderSettings.this.changedHeaders == null) {
            UCAHeaderSettings.this.changedHeaders = new String[4];
            for (int i = 0; i < 4; i++) {
              UCAHeaderSettings.this.changedHeaders[i] = headers[i];
            }
          }
          UCAHeaderSettings.this.changedHeaders[columnIndex] = text;

        }
      });
    }

  }

}
