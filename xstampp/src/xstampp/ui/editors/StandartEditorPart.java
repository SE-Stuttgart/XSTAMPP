/*************************************************************************
 * Copyright (c) 2014-2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************/

package xstampp.ui.editors;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.interfaces.IEditorBase;
import xstampp.util.STPAPluginUtils;

/**
 * The Standard Editor Part for an STPA Project
 *
 * @author Lukas Balzer
 * @since version 2.0.0
 * @see EditorPart
 *
 */
public abstract class StandartEditorPart extends EditorPart implements IEditorBase, IPartListener {

  private UUID projectID;
  private EnumSet<ObserverValue> updateValues;
  public static final String SELECTED_ENTRY = "xstampp.ui.editor.selection.id";

  protected static final RGB PARENT_BACKGROUND_COLOR = new RGB(215, 240, 255);

  @Override
  public void doSave(IProgressMonitor monitor) {
  }

  @Override
  public void doSaveAs() {
    Map<String, String> values = new HashMap<>();
    values.put("saveAsProjectId", this.projectID.toString()); //$NON-NLS-1$
    STPAPluginUtils.executeParaCommand("astpa.saveas", values); //$NON-NLS-1$
  }

  @Override
  public void init(IEditorSite site, IEditorInput input) throws PartInitException {
    this.setSite(site);
    this.setInput(input);
    this.setProjectID(((STPAEditorInput) input).getProjectID());
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(this);

  }

  @Override
  public void dispose() {
    ProjectManager.getLOGGER().debug("Editor: " + getTitle() + " closed"); //$NON-NLS-2$
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(this);
    super.dispose();
  }

  @Override
  public boolean isDirty() {
    if (!ProjectManager.getContainerInstance().getProjects().containsKey(projectID)) {
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(this, false);
    }
    return false;
  }

  @Override
  public boolean isSaveAsAllowed() {
    return false;
  }

  @Override
  public void setFocus() {
    // d
  }

  /**
   * @return the projectID
   */
  public UUID getProjectID() {
    return this.projectID;
  }

  /**
   * @param projectID
   *          the projectID to set
   */
  public void setProjectID(UUID projectID) {
    this.projectID = projectID;
  }

  @Override
  public void update(Observable dataModelController, Object updatedValue) {
    ObserverValue type = (ObserverValue) updatedValue;
    switch (type) {
    case DELETE: {
      this.getSite().getPage().closeEditor(this, false);
      break;
    }
    default:
      break;
    }

  }

  
  @Override
  public void partActivated(IWorkbenchPart arg0) {
    if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getActiveEditor() != null) {
      if (!PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
          .getSite().getId().equals("acast.steps.step2_1")) { //$NON-NLS-1$
        if (arg0 == this) {
          ((STPAEditorInput) getEditorInput()).activate();
        } else if (arg0 != this) {
          ((STPAEditorInput) getEditorInput()).deactivate();
        }
      }
    }
  }

  @Override
  public void partBroughtToTop(IWorkbenchPart arg0) {
    // is not used by the implementation

  }

  @Override
  public void partClosed(IWorkbenchPart arg0) {
    if (arg0 == this) {
      ((STPAEditorInput) getEditorInput()).deactivate();
    }
  }

  @Override
  public void partDeactivated(IWorkbenchPart arg0) {
    if (arg0 == this) {
      ((STPAEditorInput) getEditorInput()).deactivate();
    }

  }

  @Override
  public void partOpened(IWorkbenchPart arg0) {
    // is not used by the implementation

  }

  public void selectAll() {
    // so nothing by default
  }

  public EnumSet<ObserverValue> getUpdateValues() {
    return updateValues;
  }

  public void setUpdateValues(EnumSet<ObserverValue> updateValues) {
    this.updateValues = updateValues;
  }

  public void addUpdateValue (ObserverValue value) {
    this.updateValues.add(value);
  }
}
