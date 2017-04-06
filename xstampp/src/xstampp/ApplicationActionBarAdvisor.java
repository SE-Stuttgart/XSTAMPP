/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import xstampp.ui.workbench.contributions.EditorContribution;

/**
 * Configures the action bar.
 * 
 * @author Patrick Wickenhaeuser
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

  /**
   * Constructor of the advisor.
   * 
   * @param configurer
   *          the configurer used to configure the action bar in the workbench
   *          window.
   * 
   * @author Patrick Wickenhaeuser
   */
  public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
    super(configurer);
  }

  @Override
  protected void makeActions(final IWorkbenchWindow window) {
    IWorkbenchAction action = ActionFactory.HELP_CONTENTS.create(window);
    this.register(action);

    IWorkbenchAction exportAction = ActionFactory.EXPORT.create(window);
    this.register(exportAction);
  }

  @Override
  protected void fillCoolBar(ICoolBarManager coolBar) {
    coolBar.setLockLayout(false);
    coolBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
    super.fillCoolBar(coolBar);
  }

  @Override
  protected void fillMenuBar(IMenuManager menuBar) {
    super.fillMenuBar(menuBar);
  }

  @Override
  protected void fillStatusLine(IStatusLineManager statusLine) {
    super.fillStatusLine(statusLine);
  }
}
