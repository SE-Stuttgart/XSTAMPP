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

package xstampp;

import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Configures the workbench.
 * 
 * @author Patrick Wickenhaeuser, Sebastian Sieber
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	/**
	 * The perspective's ID.
	 */
	private static final String PERSPECTIVE_ID = "astpa.welcome.perspective"; //$NON-NLS-1$

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);

	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return ApplicationWorkbenchAdvisor.PERSPECTIVE_ID;
	}

	@Override
	public void postStartup() {
		
		// remove default preference page for Install/Update and Security
		PreferenceManager pm = PlatformUI.getWorkbench().getPreferenceManager();

				pm.remove("org.eclipse.equinox.internal.p2.ui.sdk.ProvisioningPreferencePage"); //$NON-NLS-1$
				pm.remove("org.eclipse.equinox.internal.p2.ui.sdk.SitesPreferencePage "); //$NON-NLS-1$
				pm.remove("org.eclipse.equinox.internal.p2.ui.sdk.scheduler.AutomaticUpdatesPreferencePage"); //$NON-NLS-1$
				pm.remove("org.eclipse.equinox.security.ui.category"); //$NON-NLS-1$
				pm.remove("org.eclipse.equinox.security.ui.storage"); //$NON-NLS-1$
	}
}
