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

import java.net.URL;

import messages.Messages;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import xstampp.util.ChooseWorkLocation;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws Exception {

		// setup the log4j logger
		try {
			Logger logger = Logger.getRootLogger();

			PatternLayout layout = new PatternLayout("[%-5p] [%d]: %m%n"); //$NON-NLS-1$

			// write to console
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			logger.addAppender(consoleAppender);
		} catch (Exception ex) {
			System.out.println("log4j setup failed\n" + ex); //$NON-NLS-1$
		}
		Display display = PlatformUI.createDisplay();
		Logger logger = Logger.getRootLogger();
		try {
			
			// Choose a reasonable workspace Location
			Location instanceLoc = Platform.getInstanceLocation();
			if (!ChooseWorkLocation.shouldRememberWS(true) && !instanceLoc.isSet()) {
				ChooseWorkLocation chooser = new ChooseWorkLocation(
						display.getActiveShell());
				if (chooser.open() == Window.CANCEL) {
					try {
						PlatformUI.getWorkbench().close();
					} catch (Exception err) {
						//
					}
					System.exit(0);
					return IApplication.EXIT_OK;
				}
			}else{
				ChooseWorkLocation.initializeWS();
			}

			if (ChooseWorkLocation.getLastUsedWorkspace() != null) {
				instanceLoc.set(
						new URL(Messages.File, null, ChooseWorkLocation
								.getLastUsedWorkspace()), false);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(Messages.TheWorkspaceCannotBeChangedWhen
					+ Messages.UsuallyTheIDEStarts);
		}

		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display,
					new ApplicationWorkbenchAdvisor());

			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning()) {
			return;
		}
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}
}
