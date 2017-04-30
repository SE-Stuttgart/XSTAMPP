/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.ui.navigation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

import xstampp.ui.navigation.api.IDynamicStepsProvider;
import xstampp.ui.navigation.api.IProjectSelection;

/**
 * An Abstract selector for a dynamic list of editors.
 * 
 * @author Lukas Balzer
 */
public class DynamicStepSelector extends AbstractSelector implements IMenuListener {

  private String commandId;
  private IDynamicStepsProvider provider;
  private String editorId;

  public DynamicStepSelector(TreeItem item, UUID projectId, IProjectSelection parent,
      String commandId) {
    super(item, projectId, parent);
    this.commandId = commandId;
  }

  @Override
  public void menuAboutToShow(IMenuManager manager) {
  }
  
  public void addMenu(IMenuManager manager) {
    CommandContributionItemParameter parameter = new CommandContributionItemParameter(
        PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "", commandId, SWT.PUSH);
    Map<String, String> params = new HashMap<>();
    params.put("xstampp.dynamicStep.commandParameter.projectId", getProjectId().toString());
    parameter.parameters = params;
    CommandContributionItem contributionItem = new CommandContributionItem(parameter);

    manager.add(contributionItem);
  }

  public IDynamicStepsProvider getProvider() {
    return provider;
  }

  public void setProvider(IDynamicStepsProvider provider) {
    this.provider = provider;
  }

  public String getEditorId() {
    return editorId;
  }

  public void setEditorId(String editorId) {
    this.editorId = editorId;
  }
}
