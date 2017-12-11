/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.controlstructure.utilities;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

import xstampp.astpa.controlstructure.controller.editparts.IControlStructureEditPart;
import xstampp.astpa.messages.Messages;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.model.ObserverValue;

/**
 * the class which provides methods to create the Context menu for the CSEditor
 * 
 * 
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 */
public class CSContextMenuProvider extends ContextMenuProvider {

  private ActionRegistry actionRegistry;
  private IControlStructureEditorDataModel dataController;

  /**
   * 
   * 
   * @author Aliaksei Babkovich, Lukas Balzer
   * 
   * @param viewer
   *          the viewer that represents the contents of the editor
   * @param registry
   *          the action Registry class which contains all actions possible for this EditorViewer
   * @param iControlStructureEditorDataModel
   *          The data model of the project with that this context menu interacts
   */
  public CSContextMenuProvider(EditPartViewer viewer, ActionRegistry registry,
      IControlStructureEditorDataModel dataController) {
    super(viewer);
    this.dataController = dataController;
    this.setActionRegistry(registry);
  }

  @Override
  public void buildContextMenu(IMenuManager menu) {
    GEFActionConstants.addStandardActionGroups(menu);
    @SuppressWarnings("unchecked") //$NON-NLS-1$
    List<Object> editParts = getViewer().getSelectedEditParts();
    if (!editParts.isEmpty() && editParts.get(0) instanceof IControlStructureEditPart) {
      final UUID id = ((IControlStructureEditPart) editParts.get(0)).getId();
      menu.appendToGroup(GEFActionConstants.GROUP_EDIT,
          createMoveMenu(id, true, Messages.CSContextMenuProvider_MoveUP));
      menu.appendToGroup(GEFActionConstants.GROUP_EDIT,
          createMoveMenu(id, false, Messages.CSContextMenuProvider_MoveDOWN));
    }
    Iterator<?> actions = this.getActionRegistry().getActions();
    while (actions.hasNext()) {
      IAction action = (IAction) actions.next();
      if (action.getClass().equals(DirectEditAction.class)) {
        menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
      }
      if (action.getClass().equals(DeleteAction.class)) {
        menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
      }
    }

  }

  private IContributionItem createMoveMenu(final UUID id, final boolean moveUp, String name) {
    MenuManager m = new MenuManager();
    IAction action = new Action() {
      @Override
      public void run() {
        dataController.moveEntry(false, moveUp, id, ObserverValue.CONTROL_STRUCTURE);
        super.run();
      }

      @Override
      public String getText() {
        return Messages.CSContextMenuProvider_MoveUP_DOWNOneStep;
      }
    };
    m.add(action);
    action = new Action() {
      @Override
      public void run() {
        dataController.moveEntry(true, moveUp, id, ObserverValue.CONTROL_STRUCTURE);
        super.run();
      }

      @Override
      public String getText() {
        return Messages.CSContextMenuProvider_MoveUP_DOWNAllWay;
      }
    };
    m.setMenuText(name);
    m.add(action);
    return m;
  }

  private void setActionRegistry(ActionRegistry registry) {
    this.actionRegistry = registry;
  }

  private ActionRegistry getActionRegistry() {
    return this.actionRegistry;
  }

}
