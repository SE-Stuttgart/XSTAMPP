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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;

import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;

/**
 * Class that stores whether the save menu should be enabled or not
 * 
 * @author Fabian Toth
 * 
 */
public class CommandState extends AbstractSourceProvider implements ISelectionChangedListener,Observer{

	/**
	 * The id of the state
	 * 
	 * @author Fabian Toth
	 */
	public static final String SAVE_STATE = "xstampp.ui.menu.file.commands.savestate"; //$NON-NLS-1$
	/**
	 * The id of the state
	 * 
	 * @author Fabian Toth
	 */
	public static final String SAVE_ALL_STATE = "xstampp.ui.menu.file.commands.saveAllState"; //$NON-NLS-1$
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
	
	private List<Observable> saveList = new ArrayList<>();
 	
	/**
	 * The id of the state
	 * 
	 * @author Lukas Balzer
	 * @since version 2.0.0
	 */
	public static final String TEXT_STATE = "xstampp.ui.menu.file.commands.textstate"; //$NON-NLS-1$
	
	
 
	@Override
	public void dispose() {
		// Nothing to do here
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] {SAVE_ALL_STATE,SAVE_STATE,TEXT_STATE };
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
			map.put(SAVE_ALL_STATE, S_DISABLED);
			map.put(SAVE_STATE, S_DISABLED);
			map.put(TEXT_STATE, S_DISABLED);
			return map;
		}
		return map;
	}




	@Override
	public void selectionChanged(SelectionChangedEvent arg0) {
		if(arg0.getSelection() instanceof IProjectSelection){
			UUID id = ((IProjectSelection) arg0.getSelection()).getProjectId();
			if(ProjectManager.getContainerInstance().getUnsavedChanges(id)){
				changeState(SAVE_STATE,S_ENABLED, null);
				
			}else{
				changeState(SAVE_STATE,S_DISABLED, null);
			}
		}
		
	}

	@Override
	public void update(Observable arg0, Object updatedValue) {
		IDataModel dataModel = (IDataModel) arg0;
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case UNSAVED_CHANGES :
			if(dataModel.hasUnsavedChanges()){
				changeState(SAVE_STATE,S_ENABLED, null);
			}else{
				changeState(SAVE_STATE,S_DISABLED, null);
			}
		default:
			break;
		}
	}
	
	private void changeState(String key,String state, Observable model) {
		this.fireSourceChanged(ISources.WORKBENCH, key,state);
		if(model != null && state.equals(S_ENABLED)){
			this.saveList.add(model);
		}else if(model != null && state.equals(S_DISABLED)){
			this.saveList.remove(model);
		}
		if(this.saveList.isEmpty()){
			this.fireSourceChanged(ISources.WORKBENCH, SAVE_ALL_STATE,S_DISABLED);
		}else{
			this.fireSourceChanged(ISources.WORKBENCH, SAVE_ALL_STATE,S_ENABLED);
		}
		
	}
	
	
}
