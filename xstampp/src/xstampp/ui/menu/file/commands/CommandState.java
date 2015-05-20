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

package xstampp.ui.menu.file.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.interfaces.ITextEditor;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;

/**
 * Class that stores whether the save menu should be enabled or not
 * 
 * @author Fabian Toth
 * 
 */
public class CommandState extends AbstractSourceProvider implements IPartListener, ISelectionChangedListener{

	/**
	 * The id of the state
	 * 
	 * @author Fabian Toth
	 */
	public static final String SAVE_STATE = "xstampp.ui.menu.file.commands.savestate"; //$NON-NLS-1$
	/**
	 * The string representation of the ENABLED value
	 * 
	 * @author Fabian Toth
	 */
	public static final String S_ENABLED = "ENABLED"; //$NON-NLS-1$
	/**
	 * The string representation of the DISABLED value
	 * 
	 * @author Fabian Toth
	 */
	public static final String S_DISABLED = "DISABLED"; //$NON-NLS-1$
	
	
	/**
	 * The id of the state
	 * 
	 * @author Lukas Balzer
	 * @since version 2.0.0
	 */
	public static final String TEXT_STATE = "xstampp.ui.menu.file.commands.textstate"; //$NON-NLS-1$
	
	
	enum State {
		ENABLED, DISABLED
	}

	private static State curState = State.DISABLED;
 
	@Override
	public void dispose() {
		// Nothing to do here
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] {SAVE_STATE,TEXT_STATE };
	}

	@Override
	public Map<String, String> getCurrentState() {
		Map<String, String> map = new HashMap<>();
		if ((PlatformUI.getWorkbench() == null)
				|| (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null)
				|| (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage() == null)
				|| (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().getActiveEditor() == null)) {
			map.put(SAVE_STATE, S_DISABLED);
			map.put(TEXT_STATE, S_DISABLED);
			return map;
		} else if (CommandState.curState == State.ENABLED) {
			map.put(SAVE_STATE, S_ENABLED);
		} else if (CommandState.curState == State.DISABLED) {
			map.put(SAVE_STATE, S_DISABLED);
		}
		
		
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor() instanceof ITextEditor){
			map.put(TEXT_STATE, S_ENABLED);
		}else{
			map.put(TEXT_STATE, S_DISABLED);
		}
		return map;
	}

	/**
	 * Sets the save entries in the menu to enabled
	 * 
	 * @author Fabian Toth
	 */
	public void setEnabled() {
		CommandState.curState = State.ENABLED;
		this.fireSourceChanged(ISources.WORKBENCH,SAVE_STATE,S_ENABLED);
		if(PlatformUI.getWorkbench() != null){
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(this);
		}
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("astpa.explorer");
		if(view instanceof ProjectExplorer){
			((ProjectExplorer) view).addSelectionChangedListener(this);
		}
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActiveEditor() instanceof ITextEditor){
			this.fireSourceChanged(ISources.WORKBENCH, TEXT_STATE,S_ENABLED);
		}else{
			this.fireSourceChanged(ISources.WORKBENCH, TEXT_STATE,S_ENABLED);
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// not used by this implementation
		
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		// not used by this implementation
		
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		// not used by this implementation
		
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		// not used by this implementation
		
	}

	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		if(arg0.getSelection() instanceof IProjectSelection){
			UUID id = ((IProjectSelection) arg0.getSelection()).getProjectId();
			if(ProjectManager.getContainerInstance().getUnsavedChanges(id)){
				this.fireSourceChanged(ISources.WORKBENCH, TEXT_STATE,S_ENABLED);
				
			}else{
				this.fireSourceChanged(ISources.WORKBENCH, TEXT_STATE,S_DISABLED);
			}
		}
		
	}

}
