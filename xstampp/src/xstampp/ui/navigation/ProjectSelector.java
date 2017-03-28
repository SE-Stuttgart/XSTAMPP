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
package xstampp.ui.navigation;

import java.util.UUID;

import org.apache.regexp.recompile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

import xstampp.ui.common.ProjectManager;

/**
 * A subclass of {@link AbstractSelector} for a project selction
 * 
 * @author Lukas Balzer
 * @see AbstractSelector
 *
 */
public class ProjectSelector extends AbstractSelector {
  private IContributionItem openEntry;
  private IAction customAction;
  private boolean isUnsaved,isReadOnly;
  /**
   *
   * @author Lukas Balzer
   *
   * @param item
   *          {@link AbstractSelector#getItem()}
   * @param projectId
   *          {@link AbstractSelector#getProjectId()}
   */
  public ProjectSelector(TreeItem item, UUID projectId, IProjectSelection parent) {
    super(item, projectId, parent);
    this.isUnsaved = false;
    this.isReadOnly = false;
    refreshPath();

  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public void addOpenEntry(String id, IMenuManager manager) {
    if (this.openEntry != null) {
      manager.appendToGroup(id, this.openEntry);
    }
    if (this.customAction != null) {
      manager.appendToGroup(id, this.customAction);
    }
  }

  @Override
  public void setUnsaved(boolean unsaved) {
    if ( unsaved != isUnsaved) {
      this.isUnsaved = unsaved;
      refreshPath();
    }
  }
  
  public void setReadOnly(boolean isReadOnly) {
    if (this.isReadOnly != isReadOnly) {
      this.isReadOnly = isReadOnly;
      refreshPath();
    }
  }
  
  private void refreshPath() {
    String text = ProjectManager.getContainerInstance().getTitle(getProjectId());
    if ( isUnsaved ) {
      text = "*" + text;
    }
    
    if( isReadOnly ) {
      text += "[READ ONLY]"; 
    }
    final String newHeader = text;
    Display.getDefault().asyncExec(new Runnable() {
      
      @Override
      public void run() {

        getItem().setText(newHeader + "["+ProjectManager.getContainerInstance().getProjectExtension(getProjectId()) + "]"); 
        setPathHistory(newHeader);       
      }
    });
  }
  
}

