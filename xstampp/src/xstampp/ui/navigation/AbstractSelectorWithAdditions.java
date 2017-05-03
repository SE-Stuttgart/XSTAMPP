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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

/**
 * An Abstract selector for a defining a selector that provides additional commands used in a
 * context menu o toolbar.
 * 
 * @author Lukas Balzer
 */
public class AbstractSelectorWithAdditions extends AbstractSelector implements IExecutionListener {

  private String[] commandAdditions;
  private Map<String, String> commandParameters;
  private String commandsString;

  public AbstractSelectorWithAdditions(TreeItem item, TreeItemDescription descriptor) {
    super(item, descriptor);
    this.commandsString = "";
    setCommandAdditions(descriptor.getCommandAdditions());
    ICommandService service = (ICommandService) PlatformUI.getWorkbench()
        .getService(ICommandService.class);
    service.addExecutionListener(this);
  }

  /**
   * Adds the commandAdditions defined in the <code>commandAdditions</code> extension.
   * 
   * @param manager
   *          the menu manager of the context table. The Menu manager must regularly remove this
   *          since this method adds a new contribution.
   */
  public void addCommandAdditions(IMenuManager manager) {
    for (String commandId : this.commandAdditions) {
      CommandContributionItemParameter parameter = new CommandContributionItemParameter(
          PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "", commandId, SWT.PUSH);
      Map<String, String> params = new HashMap<>();
      params.put("xstampp.dynamicStep.commandParameter.projectId", getProjectId().toString());
      params.putAll(commandParameters);
      parameter.parameters = params;

      CommandContributionItem contributionItem = new CommandContributionItem(parameter);

      manager.add(contributionItem);
    }
  }

  public String[] getCommandAdditions() {
    return commandAdditions;
  }

  /**
   * sets the list of additional commands that will be available in the context menu of the
   * associated tree item.
   * 
   * @param commandAdditions
   *          a String list of command ids, which must also be registered in the plugin as commands.
   */
  public void setCommandAdditions(String[] commandAdditions) {
    this.commandAdditions = commandAdditions;
    this.commandsString = "";
    for (String commandId : this.commandAdditions) {
      commandsString += commandId;
    }
  }

  @Override
  public void cleanUp() {
    ICommandService service = (ICommandService) PlatformUI.getWorkbench()
        .getService(ICommandService.class);
    service.removeExecutionListener(this);
    super.cleanUp();
  }

  public Map<String, String> getCommandParameters() {
    return commandParameters;
  }

  public void setCommandParameters(Map<String, String> commandParameters) {
    this.commandParameters = commandParameters;
  }

  @Override
  public void notHandled(String commandId, NotHandledException exception) {
    // TODO Auto-generated method stub

  }

  @Override
  public void postExecuteFailure(String commandId, ExecutionException exception) {
    // TODO Auto-generated method stub

  }

  @Override
  public void postExecuteSuccess(String commandId, Object returnValue) {
    if (this.commandsString.contains(commandId)) {
      if (returnValue instanceof String) {
        getItem().setText((String) returnValue);
        setPathHistory((String) returnValue);
      }
    }
  }

  @Override
  public void preExecute(String commandId, ExecutionEvent event) {
    // TODO Auto-generated method stub

  }
}
