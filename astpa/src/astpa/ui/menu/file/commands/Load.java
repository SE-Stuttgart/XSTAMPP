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

package astpa.ui.menu.file.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import astpa.ui.common.ViewContainer;

/**
 * Handler that loads a analysis from the file system
 * 
 * @author Fabian Toth
 * 
 */
public class Load extends AbstractHandler {
	private String recentPath=""; ////$NON-NLS-1$
	
	/**
	 * the Data Model will now load from this path 
	 * @author Lukas Balzer
	 *
	 * @param path the recentPath which sould be loaded
	 */
	public void setRecentPath(String path){
		this.recentPath=path;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	
		ViewContainer viewContainer =
			(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ViewContainer.ID);
		// Enable the save entries in the menu
		ISourceProviderService sourceProviderService =
			(ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
		SaveState saveStateService = (SaveState) sourceProviderService.getSourceProvider(SaveState.STATE);
		saveStateService.setEnabled();
		this.recentPath=	event.getParameter("loadRecentProject");
		System.out.println(this.recentPath);
		if(this.recentPath == null){
			return viewContainer.loadDataModel();
		}
		return viewContainer.loadDataModelFile(recentPath);
	}
	
}

