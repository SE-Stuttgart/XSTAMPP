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

package xstampp.ui.navigation;

import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import xstampp.model.IDataModel;
import xstampp.ui.navigation.api.IProjectSelection;

public class HeadSelector implements IProjectSelection {

  @Override
  public boolean isEmpty() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void addOpenEntry(String id, IMenuManager manager) {
    // TODO Auto-generated method stub

  }

  @Override
  public UUID getProjectId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean expandTree(boolean expand, boolean first) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void changeItem(TreeItem item) {
    // TODO Auto-generated method stub

  }

  @Override
  public TreeItem getItem() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void activate() {
    // TODO Auto-generated method stub

  }

  @Override
  public void setPathHistory(String pathHistory) {
    // TODO Auto-generated method stub

  }

  @Override
  public void cleanUp() {
    // TODO Auto-generated method stub

  }

  @Override
  public void addChild(IProjectSelection child) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setSelectionListener(Listener selectionListener) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getPathHistory() {
    return Messages.PlatformName + " -";
  }

  @Override
  public IDataModel getProjectData() {
    // TODO Auto-generated method stub
    return null;
  }

}
