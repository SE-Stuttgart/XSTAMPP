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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.intro.IIntroPart;

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
	 * @param workbench
	 *            the workbench to set
	 */
	public static void setWorkbench(Workbench workbench) {
		Welcome.workbench = workbench;
	}

	/**
	 * 
	 * @author Jaqueline Patzek, Lukas Balzer
	 * 
	 * @return returns the currentIntro
	 */
	public static IIntroPart getcurrentIntro() {
		return Welcome.currentIntro;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPerspectiveDescriptor descriptor = PlatformUI.getWorkbench()
				.getPerspectiveRegistry()
				.findPerspectiveWithId("astpa.welcome.perspective");//$NON-NLS-1$
		if (descriptor != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().setPerspective(descriptor);
		}
		return null;
	}

}
