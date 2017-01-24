/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstpa;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstpa.model.XSTPADataController;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "xstpa"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

    private static Map<IDataModel,XSTPADataController> xstpaDataToIDataModel = new HashMap<>();
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static XSTPADataController getDataFor(UUID modelID){
		IDataModel model = ProjectManager.getContainerInstance()
				.getDataModel(modelID);
			if(model != null){
			XSTPADataController data = xstpaDataToIDataModel.get(model);
			if(data == null && model instanceof IExtendedDataModel){
				data = new XSTPADataController((IExtendedDataModel) model);
				ProjectManager.getContainerInstance().addProjectAdditionForUUID(ProjectManager.getContainerInstance().
						getProjectID((Observable) model), data);
				model.addObserver(data);
				xstpaDataToIDataModel.put(model, data);
			}else if(data == null){
				data = null;
			}
			return data;
		}
		return null;
	}
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
