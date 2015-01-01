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

import java.util.UUID;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.ViewContainer;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.util.STPAEditorInput;

/**
 * Handler that saves the analysis to the last location on runtime
 * 
 * @author Fabian Toth
 * 
 */
public class Save extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Object selection =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView("astpa.explorer").getViewSite()
				.getSelectionProvider().getSelection();
		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		UUID saveId;
		if(selection instanceof IProjectSelection){
			saveId=((IProjectSelection)selection).getProjectId();
		}else if (editor != null && editor.getEditorInput() instanceof STPAEditorInput) {
			saveId = ((STPAEditorInput) editor.getEditorInput()).getProjectID();
		}else{
			return false;
		}
		return ViewContainer.getContainerInstance().saveDataModel(saveId);
	}
}
