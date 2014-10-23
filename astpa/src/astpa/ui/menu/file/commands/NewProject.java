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

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import astpa.ui.common.ViewContainer;

/**
 * Handler which starts the new Analysis Wizards
 * 
 * @author Fabian
 * 
 */
public class NewProject extends AbstractHandler {
	
	private static final Logger LOGGER = Logger.getRootLogger();
	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		NewProject.LOGGER.info("Start view container"); //$NON-NLS-1$
		ViewContainer viewContainer =
			(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ViewContainer.ID);
		
		if (viewContainer == null) {
			NewProject.LOGGER.error("View container not found"); //$NON-NLS-1$
			return null;
		}
		
		Object nameParam=event.getParameter("astpa.projectName");
		Object pathParam=event.getParameter("astpa.projectPath");
		if(nameParam != null && nameParam instanceof String && pathParam != null && pathParam instanceof String){
			viewContainer.startUp((String)nameParam, (String) pathParam);
		}else{
			viewContainer.startUp();
		}
		
		// Enable the save entries in the menu
		ISourceProviderService sourceProviderService =
			(ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
		SaveState saveStateService = (SaveState) sourceProviderService.getSourceProvider(SaveState.STATE);
		saveStateService.setEnabled();
		return null;
	}
	
	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return NewProject.LOGGER;
	}
}
