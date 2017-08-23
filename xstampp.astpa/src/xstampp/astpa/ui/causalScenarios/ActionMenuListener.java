/*************************************************************************
 * Copyright (c) 2014-2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************/

package xstampp.astpa.ui.causalScenarios;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;

public class ActionMenuListener implements IMenuListener {

  private List<IAction> actions;

  public ActionMenuListener(IAction action) {
    super();
    addAction(action);
  }

  public ActionMenuListener(IAction[] actions) {
    super();
    for (int i = 0; i < actions.length; i++) {
      addAction(actions[i]);
    }
  }

  public ActionMenuListener(List<IAction> actions) {
    super();
    this.actions = actions;
  }

  @Override
  public void menuAboutToShow(IMenuManager manager) {
    for (IAction iAction : actions) {
      manager.add(iAction);
      iAction.setEnabled(iAction.isEnabled());
    }

  }

  public List<IAction> getActions() {
    return actions;
  }

  public void addAction(IAction actions) {
    if (this.actions == null) {
      this.actions = new ArrayList<>();
    }
    this.actions.add(actions);
  }

}
