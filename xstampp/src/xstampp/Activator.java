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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import xstampp.ui.common.ProjectManager;
import xstampp.util.STPAPluginUtils;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	/**
	 * The plug-in ID
	 */
	public static final String PLUGIN_ID = "xstampp"; //$NON-NLS-1$

	/**
	 * The shared instance
	 */
	private static Activator plugin;

	private static BundleContext context;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext newContext) throws Exception {
		super.start(newContext);
		Activator.context = newContext;
		Activator.plugin = this;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext newContext) throws Exception {
		Activator.plugin = null;
		super.stop(newContext);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return Activator.plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				path);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static BundleContext getContext() {
		return Activator.context;
	}
	
	/**
	 * searches the registered plugins for any extensions of the type
	 * <code>"astpa.extension.steppedProcess"</code>
	 * and for each maps the declared file extension to the IConfigurationElement
	 * 
	 * @see IConfigurationElement
	 *
	 * @author Lukas Balzer
	 *
	 */
	public void loadProjects(){
		IConfigurationElement[] elements = Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor("xstampp.extension.steppedProcess");
		for (IConfigurationElement extElement : elements) { 
			String[] ext = extElement.getAttribute("extension").split(";"); 	//$NON-NLS-1$
			String modelClass = extElement.getAttribute("DataModelClass");
			for(int i=0;i< ext.length;i++){
				ProjectManager.getContainerInstance().registerExtension(modelClass,ext[i], extElement);
			}
		}
		
		File wsPath = new File(Platform.getInstanceLocation().getURL()
				.getPath());
		if (wsPath.isDirectory()) {
			for (File f : wsPath.listFiles()) {
				if (f.getName().startsWith(".")) { //$NON-NLS-1$
					continue;
				}
				
				String[] fileSegments = f.getName().split("\\."); //$NON-NLS-1$
				if ((fileSegments.length > 1)
						&& ProjectManager.getContainerInstance().isRegistered(fileSegments[1])) {
					Map<String, String> values = new HashMap<>();
					values.put("loadRecentProject", f.getAbsolutePath()); //$NON-NLS-1$
					STPAPluginUtils.executeParaCommand("xstampp.command.load", values);//$NON-NLS-1$
				}
			}
		}
	}
}
