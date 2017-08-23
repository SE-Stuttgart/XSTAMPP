/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.ui.wizards;

import java.io.File;

import messages.Messages;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xstampp.ui.common.ProjectManager;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class NewProjectPage extends AbstractWizardPage implements ModifyListener {

  private final static int DEFAULTMARGIN = 10;
  private final static int DEFAULTHEIGHT = 30;

  private String projectExtension;
  private String[] extensions;
  private String[] descriptions;
  private Text newProjectName;
  private PathComposite pathComposite;
  private Button defaultPathCheckbox;

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param pageName
   *          the page name
   */
  public NewProjectPage(String pageName) {
    super(pageName);
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param pageName
   *          this Pages Name
   * @param title
   *          the title which appears on top of the wizard
   * @param titleImage
   *          the Image which appears on top of the wizard dialog
   */
  public NewProjectPage(String pageName, String title, ImageDescriptor titleImage, String ext) {
    super(pageName, title, titleImage);
    this.projectExtension = ext;
    this.extensions = new String[] { ext };
    this.descriptions = new String[] { new String() };
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param pageName
   *          this Pages Name
   * @param title
   *          the title which appears on top of the wizard
   * @param titleImage
   *          the Image which appears on top of the wizard dialog
   */
  public NewProjectPage(String pageName, String title, ImageDescriptor titleImage, String[] ext,
      String[] descriptions) {
    super(pageName, title, titleImage);

    this.extensions = ext;
    this.descriptions = descriptions;

  }

  @Override
  public void createControl(Composite parent) {
    Composite control = new Composite(parent, SWT.NONE);
    control.setLayout(new FormLayout());

    // -----Create new Project Name Composite---------
    FormData data = new FormData();
    data.top = new FormAttachment(0, NewProjectPage.DEFAULTMARGIN);
    data.left = new FormAttachment(0, NewProjectPage.DEFAULTMARGIN);
    data.right = new FormAttachment(100, NewProjectPage.DEFAULTMARGIN);
    data.height = NewProjectPage.DEFAULTHEIGHT;

    Composite nameComposite = new Composite(control, SWT.None);
    nameComposite.setLayoutData(data);
    nameComposite.setLayout(new GridLayout(2, false));

    Label newProjectLabel = new Label(nameComposite, SWT.NONE);
    newProjectLabel.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));
    newProjectLabel.setText(Messages.NewProject + ": "); //$NON-NLS-1$
    this.newProjectName = new Text(nameComposite, SWT.SINGLE | SWT.BORDER);
    this.newProjectName.setLayoutData(new GridData(300, SWT.DEFAULT));
    this.newProjectName.addModifyListener(this);

    // -----Create Project Path Composite----------
    data = new FormData();
    data.top = new FormAttachment(nameComposite, NewProjectPage.DEFAULTMARGIN);
    data.left = new FormAttachment(0, NewProjectPage.DEFAULTMARGIN);
    data.right = new FormAttachment(100, NewProjectPage.DEFAULTMARGIN);
    data.height = NewProjectPage.DEFAULTHEIGHT;

    this.defaultPathCheckbox = new Button(control, SWT.CHECK);
    this.defaultPathCheckbox.setText(Messages.UseWorkspace);
    this.defaultPathCheckbox.setLayoutData(data);
    this.defaultPathCheckbox.setSelection(true);
    this.defaultPathCheckbox.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        if (NewProjectPage.this.defaultPathCheckbox.getSelection()) {
          // if the default path checkbox is checked the path choice is
          // automatically resettet to
          // the workspace location
          NewProjectPage.this.pathComposite
              .setText(Platform.getInstanceLocation().getURL().getPath());
          NewProjectPage.this.pathComposite.setEnabled(false);
        } else {
          NewProjectPage.this.pathComposite.setEnabled(true);
        }
      }
    });

    data = new FormData();
    data.top = new FormAttachment(this.defaultPathCheckbox, NewProjectPage.DEFAULTMARGIN);
    data.left = new FormAttachment(0, NewProjectPage.DEFAULTMARGIN);
    data.right = new FormAttachment(100, NewProjectPage.DEFAULTMARGIN);
    data.height = NewProjectPage.DEFAULTHEIGHT;

    this.pathComposite = new PathComposite(new String[] { "*." + this.projectExtension }, control, //$NON-NLS-1$
        PathComposite.DIR_DIALOG);
    this.pathComposite.setLayoutData(data);
    this.pathComposite.setText(Platform.getInstanceLocation().getURL().getPath());
    this.pathComposite.getTextWidget().addModifyListener(this);
    this.pathComposite.setEnabled(false);
    // the group component to choose the extension (the type of project is only
    // displayed if there is more than
    // one choice
    if (this.extensions.length > 1) {
      data = new FormData();
      data.top = new FormAttachment(this.pathComposite, NewProjectPage.DEFAULTMARGIN);
      data.left = new FormAttachment(0, NewProjectPage.DEFAULTMARGIN);
      data.right = new FormAttachment(100, NewProjectPage.DEFAULTMARGIN);
      Group extGroup = new Group(control, SWT.SHADOW_IN);
      extGroup.setLayoutData(data);
      extGroup.setLayout(new RowLayout(SWT.VERTICAL));
      for (int i = 0; i < this.extensions.length; i++) {
        final String extName = this.extensions[i];
        Button extChooser = new Button(extGroup, SWT.RADIO);
        extChooser.setText(this.extensions[i] + " - " + this.descriptions[i]); //$NON-NLS-1$
        extChooser.addSelectionListener(new SelectionAdapter() {
          @Override
          public void widgetSelected(SelectionEvent e) {
            NewProjectPage.this.projectExtension = extName;
          }
        });
        // the first item in the group is initially selected
        if (i == 0) {
          extChooser.setSelection(true);
          this.projectExtension = extName;
        }
      }
    }

    this.setPageComplete(false);
    this.setControl(control);
  }

  @Override
  public boolean checkFinish() {
    this.setErrorMessage(null);
    if ((this.newProjectName == null) || this.getNewProjectName().equals("")) { //$NON-NLS-1$
      return false;
    }
    if (!this.defaultPathCheckbox.getSelection()
        && ((this.pathComposite == null) || this.pathComposite.getText().equals(""))) { //$NON-NLS-1$
      return false;
    }
    if (ProjectManager.getContainerInstance().getProjects()
        .containsValue(this.getNewProjectName())) {
      this.setErrorMessage(Messages.ProjectExists);
      return false;
    }
    File path = new File(this.getNewProjectPath());
    if (path.exists()) {
      this.setErrorMessage(Messages.FileExists);
      return false;
    }
    return true;
  }

  /**
   * @return the newProjectName
   */
  public String getNewProjectName() {
    return this.newProjectName.getText();
  }

  /**
   * @return the path where the Data for the new project is stored
   */
  public String getNewProjectPath() {
    return this.pathComposite.getText() + File.separator + this.getNewProjectName() + "." //$NON-NLS-1$
        + this.projectExtension;

  }

  @Override
  public void modifyText(ModifyEvent arg0) {
    this.setPageComplete(this.checkFinish());

  }

}
