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
package xstampp.stpasec;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.model.IDataModel;
import xstampp.stpapriv.model.PrivacyController;
import xstampp.stpapriv.model.relation.UnsafeUnsecureController;
import xstampp.stpapriv.model.results.ConstraintResultController;
import xstampp.ui.common.ProjectManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	/**
	 * 
	 * The plug-in ID
	 */
	public static final String PLUGIN_ID = "xstampp.stpasec"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private static Map<IDataModel,UnsafeUnsecureController> xstpaDataToIDataModel = new HashMap<>();
	private static Map<IDataModel,ConstraintResultController> resultcontroller = new HashMap<>();
	public static PrivacyController model;


	public static PrivacyController getModel() {
		return model;
	}


	public static void setModel(PrivacyController model) {
		Activator.model = model;
	}


	public static Map<IDataModel, ConstraintResultController> getResultcontroller() {
		return resultcontroller;
	}


	public static void setResultcontroller(Map<IDataModel, ConstraintResultController> resultcontroller) {
		Activator.resultcontroller = resultcontroller;
	}


	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
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

	public static UnsafeUnsecureController getDataFor(UUID modelID) {
		IDataModel model = ProjectManager.getContainerInstance()
				.getDataModel(modelID);
			if(model != null){
				UnsafeUnsecureController data = xstpaDataToIDataModel.get(model);
			if(data == null && model instanceof IExtendedDataModel){
				data = new UnsafeUnsecureController((IExtendedDataModel) model);
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
	
	public static ConstraintResultController getResultsDataFor(UUID modelID) {
		IDataModel model = ProjectManager.getContainerInstance()
				.getDataModel(modelID);
			if(model != null){
				ConstraintResultController data = resultcontroller.get(model);
			if(data == null && model instanceof IExtendedDataModel){
				data = new ConstraintResultController((IExtendedDataModel) model);
				ProjectManager.getContainerInstance().addProjectAdditionForUUID(ProjectManager.getContainerInstance().
						getProjectID((Observable) model), data);
				model.addObserver(data);
				resultcontroller.put(model, data);
			}else if(data == null){
				data = null;
			}
			return data;
		}
		return null;
	}

}
