/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package astpa.controlstructure.utilities;

import java.util.Iterator;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;

/**
 * the class which provides methods to create the Context menu for the CSEditor
 * 
 * 
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 */
public class CSContextMenuProvider extends ContextMenuProvider {
	
	private ActionRegistry actionRegistry;
	
	
	/**
	 * 
	 * 
	 * @author Aliaksei Babkovich, Lukas Balzer
	 * 
	 * @param viewer the viewer that represents the contents of the editor
	 * @param registry the action Registry class which contains all actions
	 *            possible for this EditorViewer
	 */
	public CSContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		this.setActionRegistry(registry);
	}
	
	@Override
	public void buildContextMenu(IMenuManager menu) {
		
		GEFActionConstants.addStandardActionGroups(menu);
		Iterator<IAction> iter = this.getActionRegistry().getActions();
		while (iter.hasNext()) {
			IAction action = iter.next();
			if (action.getClass().equals(DirectEditAction.class)) {
				menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
			}
			if (action.getClass().equals(SelectAllAction.class)) {
				menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
			}
			if (action.getClass().equals(DeleteAction.class)) {
				menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
			}
			if (action.getClass().equals(UndoAction.class)) {
				menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
			}
			if (action.getClass().equals(SaveAction.class)) {
				
				menu.appendToGroup(GEFActionConstants.GROUP_SAVE, action);
			}
			if (action.getClass().equals(RedoAction.class)) {
				menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
			}
		}
		
	}
	
	private void setActionRegistry(ActionRegistry registry) {
		this.actionRegistry = registry;
	}
	
	private ActionRegistry getActionRegistry() {
		return this.actionRegistry;
	}
	
}
