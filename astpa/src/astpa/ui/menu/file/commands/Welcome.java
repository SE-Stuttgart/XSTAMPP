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
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;

import astpa.ui.common.ViewContainer;

/**
 * 
 * @author Jaqueline Patzek
 * 
 */
@SuppressWarnings("restriction")
public class Welcome extends AbstractHandler {
	
	private static Workbench workbench;
	
	private static IIntroPart currentIntro;
	
	/**
	 * The log4j logger
	 */
	private static final Logger LOGGER = Logger.getRootLogger();
	
	
	/**
	 * 
	 * @author Jaqueline Patzek
	 * 
	 */
	public Welcome() {
		Welcome.setWorkbench((Workbench) PlatformUI.getWorkbench());
	}
	
	/**
	 * 
	 * @author Jaqueline Patzek
	 * 
	 * @return returns the workbench
	 */
	public static Workbench getWorkbench() {
		return Welcome.workbench;
	}
	
	/**
	 * @param workbench the workbench to set
	 */
	public static void setWorkbench(Workbench workbench) {
		Welcome.workbench = workbench;
	}
	
	/**
	 * 
	 * @author Jaqueline Patzek
	 * 
	 * @return returns the currentIntro
	 */
	public static IIntroPart getcurrentIntro() {
		return Welcome.currentIntro;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Welcome.currentIntro = Welcome.getWorkbench().getIntroManager()
		// .showIntro(null, false);
		
		ViewContainer viewContainer =
			(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ViewContainer.ID);
		
		if (viewContainer == null) {
			Welcome.LOGGER.error("View Container not found"); //$NON-NLS-1$
			
			return null;
		}
		
		viewContainer.showWelcomePage();
		
		return null;
	}
	
	/**
	 * 
	 * @author Jaqueline Patzek Shuts the Welcome-Screen
	 * 
	 */
	public static void shutWelcome() {
		final IIntroManager introManager = PlatformUI.getWorkbench().getIntroManager();
		IIntroPart part = introManager.getIntro();
		introManager.closeIntro(part);
		
		ViewContainer viewContainer =
			(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ViewContainer.ID);
		
		if (viewContainer == null) {
			Welcome.LOGGER.error("View Container not found"); //$NON-NLS-1$
			
			return;
		}
		
		viewContainer.hideWelcomePage();
	}
}
