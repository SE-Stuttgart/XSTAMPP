/*******************************************************************************
 * Copyright (c) 2013 ASTPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac Jarkko, Heidenwag, Benedikt Markt, Jaqueline Patzek Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksey Babkovic, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.ui.common;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;
import java.util.UUID;

import messages.Messages;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.navigation.ProjectExplorer;
import xstampp.util.AbstractLoadJob;
import xstampp.util.STPAPluginUtils;

/**
 * The view container contains the navigation view and the view area.
 * 
 * The navigation view is by default invisible and has to be set visible by
 * using setShowNavigationView(true).
 * 
 * 
 * @author Patrick Wickenhaeuser, Fabian Toth, Sebastian Sieber
 * 
 */
/**
 * 
 * @author Lukas Balzer
 * 
 */
public class ProjectManager {

	/**
	 * The log4j logger
	 */
	private static final Logger LOGGER = Logger.getRootLogger();
	private static ProjectManager containerInstance;
	/**
	 * The ID of the view container.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	public static final String ID = "astpa.ui.common.viewcontainer"; //$NON-NLS-1$


	private static final String OVERWRITE_MESSAGE = Messages.DoYouReallyWantToOverwriteTheContentAt;

	private Map<UUID, IDataModel> projectDataToUUID;
	private Map<UUID, File> projectSaveFilesToUUID;
	private Map<UUID, String> extensionsToUUID;
	private Map<String, IConfigurationElement> elementsToExtensions;
	

	private class LoadJobChangeAdapter extends JobChangeAdapter {

		@Override
		public void done(IJobChangeEvent event) {
			if (event.getResult() == Status.CANCEL_STATUS) {
				final AbstractLoadJob job = (AbstractLoadJob) event.getJob();
				final String name = job.getFile().getName();
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						StringBuffer msg = new StringBuffer(Messages.LoadFailed + name);
						for(String error : job.getErrors()){
							msg.append("\n" + error); //$NON-NLS-1$
						}
						MessageDialog.openInformation(Display.getDefault()
								.getActiveShell(), Messages.Information,msg.toString());
					}
				});
			}
			if (event.getResult().isOK()) {
				Display.getDefault().syncExec(new LoadRunnable(event));
				super.done(event);
			}
		}
	}

	private class LoadRunnable implements Runnable {
		private final AbstractLoadJob job;

		public LoadRunnable(IJobChangeEvent event) {
			this.job = (AbstractLoadJob) event.getJob();
		}

		@Override
		public void run() {
			UUID projectId = UUID.randomUUID();
			ProjectManager.getContainerInstance().projectDataToUUID.put(projectId, job.getController());
			IViewPart navi = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView(ProjectExplorer.ID);
			
			File saveFile = this.job.getSaveFile();
			ProjectManager.getContainerInstance().extensionsToUUID.put(projectId, saveFile.getName().split("\\.")[1]);//$NON-NLS-1$
			ProjectManager.getContainerInstance().projectSaveFilesToUUID.put(
					projectId, saveFile);
			ProjectManager.getContainerInstance().saveDataModel(projectId, false, false);
			ProjectManager.getContainerInstance().synchronizeProjectName(projectId);
			if (navi != null) {
				((ProjectExplorer) navi).updateProjects();
			}
		}
	}




	/**
	 * defines if this is the first start up
	 */
	// private boolean firstStartUp;

	/**
	 * Initializes the container in which the views are stored. Sets the active
	 * view to null.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	public ProjectManager() {
		this.extensionsToUUID = new HashMap<>();
		this.projectDataToUUID = new HashMap<>();
		this.projectSaveFilesToUUID = new HashMap<>();
	}
	
	/**
	 * creates a new project in the given location
	 * 
	 * @author Lukas Balzer
	 * @param controller 
	 * 		the controller class which should be used  
	 * @param projectName
	 *            he name of the new project
	 * @param path
	 *            the path where the new project is stored
	 * @return The UUID of the new project
	 */
	public UUID startUp(Class<?> controller, String projectName, String path) {
		IDataModel newController;
		try {
			newController = (IDataModel) controller.newInstance();
			newController.setProjectName(projectName);
			newController.initializeProject();
			newController.updateValue(ObserverValue.PROJECT_NAME);
			UUID projectId = this.addProjectData(newController, path);
			
			this.saveDataModel(projectId, false, false);
			return projectId;
		} catch (InstantiationException | IllegalAccessException e) {	
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * renames the store file to the given name,
	 * if a such a file doesn't already exist
	 * @author Lukas Balzer
	 * 
	 * @param projectId
	 *            the id of the project which should be renamed
	 * @param projectName
	 *            the new name
	 * @return true if the project has been successfully renamed
	 */
	public boolean renameProject(UUID projectId, String projectName) {

		File projectFile = this.projectSaveFilesToUUID.get(projectId);
		String ext= this.extensionsToUUID.get(projectId);
		Path newPath=projectFile.toPath().getParent();
		File newNameFile = new File(newPath.toFile(),projectName + "."+ext); //$NON-NLS-1$
		
		if (projectFile.renameTo(newNameFile) || !projectFile.exists()) {
			
			this.projectSaveFilesToUUID.remove(projectId);
			this.projectSaveFilesToUUID.put(projectId, newNameFile);
			return this.projectDataToUUID.get(projectId).setProjectName(
					projectName);
		}
		return false;
	}

	/**
	 * Saves the data model to a file
	 * 
	 * @author Fabian Toth,Lukas Balzer
	 * @param projectId
	 *            the id of the project
	 * 
	 * @return whether the operation was successful or not
	 */
	public boolean saveDataModelAs(final UUID projectId) {

		IDataModel tmpController = this.projectDataToUUID.get(projectId);
		FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), SWT.SAVE);
		List<String> extensions = new ArrayList<>();
		IConfigurationElement extElement = getConfigurationFor(projectId);
		String[] filterNames=new String[]{};
		if(extElement.getAttribute("extensionDescriptions") != null){
			filterNames = extElement.getAttribute("extensionDescriptions").split(";");//$NON-NLS-1$ //$NON-NLS-2$
		}
		
		for(String ext:extElement.getAttribute("extension").split(";")){//$NON-NLS-1$
			extensions.add("*."+ext); //$NON-NLS-1$
		}
		fileDialog.setFilterExtensions(extensions.toArray(new String[]{}));
		if(extensions.size() == filterNames.length){
			fileDialog.setFilterNames(filterNames);
		}
		fileDialog.setFileName(tmpController.getProjectName());
		String fileName = fileDialog.open();
		if (fileName == null) {
			return false;
		}
		File file = new File(fileName);
		if (file.exists()) {
			boolean result = MessageDialog.openConfirm(Display.getCurrent()
					.getActiveShell(), Messages.ConfirmSaveAs, String.format(
					ProjectManager.OVERWRITE_MESSAGE, file.getName()));
			if (!result) {
				return false;
			}
		}
		this.projectSaveFilesToUUID.replace(projectId, file);
		this.extensionsToUUID.replace(projectId, fileName.split("\\.")[1]);
		return this.saveDataModel(projectId, false, false);
	}

	/**
	 * Saves the data model to the file in the variable. If this is null
	 * saveDataModelAs() is called
	 * 
	 * @author Fabian Toth,Lukas Balzer
	 * @param projectId
	 *            the id of the project
	 * @param isUIcall informs the runtime if the call is initiated by the 
	 * 			user or the system
	 * @param saveAs TODO
	 * @return whether the operation was successful or not
	 */
	public boolean saveDataModel(UUID projectId, boolean isUIcall, boolean saveAs) {
		if (this.projectSaveFilesToUUID.get(projectId) == null || saveAs) {
			return this.saveDataModelAs(projectId);
		}
		final IDataModel tmpController = this.projectDataToUUID.get(projectId);

		tmpController.prepareForSave();
		
		Job save = tmpController.doSave(this.projectSaveFilesToUUID.get(projectId),
				ProjectManager.getLOGGER(), isUIcall);
		save.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				if (event.getResult().isOK()) {

					Display.getDefault().syncExec(new Runnable() {

						@Override
						public void run() {
							tmpController.setStored();
						}
					});
					super.done(event);
				}
			}
		});
		save.schedule();
		return true;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return whether all projects could be saved or not
	 */
	public boolean saveAllDataModels() {
		boolean temp = true;
		for (UUID id : this.getProjectKeys()) {
			temp = temp && this.saveDataModel(id, false, false);
		}
		return temp;
	}

	private void synchronizeProjectName(UUID projectID){
		File saveFile=this.projectSaveFilesToUUID.get(projectID);
		renameProject(projectID, saveFile.getName().split("\\.")[0]); //$NON-NLS-1$
	}
	
	/**
	 * Loads the data model from a file if it is valid
	 * 
	 * @author Fabian Toth
	 * @author Jarkko Heidenwag
	 * @author Lukas Balzer
	 * 
	 * @return whether the operation was successful or not
	 */
	public boolean importDataModel() {
		FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench()
				.getDisplay().getActiveShell(), SWT.OPEN);
		ArrayList<String> extensions= new ArrayList<>();
		for (String ext : this.elementsToExtensions.keySet()) {
			extensions.add("*." + ext);
		}
			
		fileDialog.setFilterExtensions(extensions.toArray(new String[]{}));
		
		String file = fileDialog.open();
		if ((file != null)
				&& !file.contains(Platform.getInstanceLocation().getURL()
						.getPath())) {

			File outer = new File(file);
			File copy = new File(Platform.getInstanceLocation().getURL()
					.getPath(), outer.getName());
			if (copy.isFile()){
				if(!MessageDialog.openQuestion(PlatformUI.getWorkbench()	
							.getDisplay().getActiveShell(),
							Messages.FileExists,String.format(Messages.DoYouReallyWantToOverwriteTheContentAt,outer.getName()))) {
				return false;
				}
				Set<UUID> idSet =this.projectSaveFilesToUUID.keySet();
				for(UUID id: idSet){
					if(this.projectSaveFilesToUUID.get(id).equals(copy) && !removeProjectData(id)){
						MessageDialog.openError(null, Messages.Error, Messages.CantOverride);
						return false;
					}
				}
			}

			this.loadDataModelFile(file,copy.getPath());
		} else if (file != null) {
			return this.loadDataModelFile(file,file);

		}

		return false;
	}

	/**
	 * Loads the data model from a file if it is valid
	 * 
	 * @author Lukas Balzer
	 * @param file
	 *            the file which contains the dataModel
	 * @param saveFile the file the project shuold be saved in
	 * 
	 * @return whether the operation was successful or not
	 */
	public boolean loadDataModelFile(String file,String saveFile) {
		Object jobObject = null;
		String pluginName = ""; //$NON-NLS-1$
		for (Entry<String, IConfigurationElement> extElement : 
			this.elementsToExtensions.entrySet()) { //$NON-NLS-1$
			
			if(file.endsWith(extElement.getKey())){ //$NON-NLS-1$
				pluginName = extElement.getValue().getAttribute("id"); //$NON-NLS-1$
				jobObject = STPAPluginUtils.
						executeCommand(extElement.getValue().getAttribute("command")); //$NON-NLS-1$
				break;
			}
		}
		
		if (file != null && jobObject != null && jobObject instanceof AbstractLoadJob) {
			((AbstractLoadJob) jobObject).setFile(file);
			((AbstractLoadJob) jobObject).setSaveFile(saveFile);
			((AbstractLoadJob) jobObject).schedule();
			((AbstractLoadJob) jobObject).addJobChangeListener(new LoadJobChangeAdapter());
			return true;
		}
		else if(jobObject == null){
			LOGGER.error(Messages.FileFormatNotSupported + ": " + file); //$NON-NLS-1$
		}
		else if(!(jobObject instanceof AbstractLoadJob)){
			LOGGER.error(String.format(Messages.InvalidPluginCommand,pluginName));
		}


		return false;
	}



	/**
	 * Checks if there are unsaved changes or not
	 * 
	 * @return whether there are unsaved changes or not
	 * 
	 * @author Fabian Toth,Lukas Balzer
	 * @param projectId
	 *            the id of the project for which the request is given
	 */
	public boolean getUnsavedChanges(UUID projectId) {

		return this.projectDataToUUID.get(projectId).hasUnsavedChanges();
	}

	/**
	 * Checks if there are unsaved changes or not
	 * 
	 * @return whether there are unsaved changes or not
	 * 
	 * @author Fabian Toth,Lukas Balzer
	 */
	public boolean getUnsavedChanges() {
		for (UUID id : this.getProjectKeys()) {
			if (this.projectDataToUUID.get(id).hasUnsavedChanges()) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Calls the observer of the data model with the given value
	 * 
	 * @author Fabian Toth
	 * 
	 * @param value
	 *            the value to call
	 */
	public void callObserverValue(ObserverValue value) {
		for (UUID id : this.getProjectKeys()) {
			this.projectDataToUUID.get(id).updateValue(value);
		}
	}

	/**
	 * @return the containerInstance
	 */
	public static ProjectManager getContainerInstance() {
		if (ProjectManager.containerInstance == null) {
			ProjectManager.containerInstance = new ProjectManager();
		}
		return ProjectManager.containerInstance;
	}

	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param projectId
	 * 			the id which is stored for the requested proejcts data
	 * @return the DataModel as IDataModel for the given project id
	 */
	public IDataModel getDataModel(UUID projectId) {
		if (this.projectDataToUUID.containsKey(projectId)) {
			return this.projectDataToUUID.get(projectId);
		}
		return null;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param controller
	 *            the controller given as ObserverS
	 * @return the id or null
	 */
	public UUID getProjectID(Observable controller) {
		if (this.projectDataToUUID.containsValue(controller)) {
			for (UUID id : this.projectDataToUUID.keySet()) {
				if (this.projectDataToUUID.get(id) == controller) {
					return id;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param projectId
	 *            the id with which the project data are stored in the DataModel
	 * @return the title of the project
	 */
	public String getTitle(UUID projectId) {
		if (this.projectDataToUUID.containsKey(projectId)) {
			return this.projectDataToUUID.get(projectId).getProjectName();
		}
		return Messages.NewProject;
	}

	/**
	 *	generates a random uuid for the project and the registered extension and data model 
	 *	to it 
	 * @author Lukas Balzer
	 *
	 * @param controller
	 * 			the data model as IDataModel which contains the projects data
	 * @param path TODO
	 * @return	generates a random uuid and stores/returns it as the projectId
	 */
	public UUID addProjectData(IDataModel controller, String path) {
		UUID id = UUID.randomUUID();
		this.projectSaveFilesToUUID.put(id, new File(path));
		this.projectDataToUUID.put(id, controller);
		IViewPart navi = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView("astpa.explorer"); //$NON-NLS-1$
		if (navi != null) {
			((ProjectExplorer) navi).updateProjects();
		}
		return id;

	}

	/**
	 * removes a project from the project list and updates the navigation
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param projectId
	 *            the id of the project which shoul be removed from the Map of
	 *            projects
	 * @return whether the removal was succesful or not
	 */
	public boolean removeProjectData(UUID projectId) {

		File projectFile = this.projectSaveFilesToUUID.get(projectId);
		if (projectFile.delete()) {
			this.projectDataToUUID.remove(projectId);
			this.projectSaveFilesToUUID.remove(projectId);
			IViewPart explorer = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
									.getActivePage().findView("astpa.explorer"); //$NON-NLS-1$
			((ProjectExplorer)explorer).update(null, ObserverValue.DELETE);
			return !this.projectDataToUUID.containsKey(projectId);
		}

		return false;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return a Set with all UUID's of the currently loaded projects
	 */
	public Set<UUID> getProjectKeys() {
		return this.projectDataToUUID.keySet();
	}

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param id	
	 * 		the id for the requested project
	 * @return
	 * 		the extension which is registered for the project
	 */
	public String getProjectExtension(UUID id) {
		File saveFile = this.projectSaveFilesToUUID.get(id);
		
		String ext = saveFile.getName();
		String[] tmp =ext.split("\\."); 
		if(tmp.length > 0){
			ext=tmp[1];
		}
		return ext;
	}
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return a Map with all projectNames mapped to their UUID's
	 */
	public Map<UUID, String> getProjects() {
		Map<UUID, String> map = new HashMap<>();
		for (UUID id : this.projectDataToUUID.keySet()) {
			map.put(id, this.projectDataToUUID.get(id).getProjectName());
		}
		return map;
	}

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param path
	 * 		a file path 
	 * @return	
	 * 		a mime constant for the handling of the file
	 */
	public String getMimeConstant(String path) {
		if (path.endsWith("pdf")) { //$NON-NLS-1$
			return org.apache.xmlgraphics.util.MimeConstants.MIME_PDF;
		}
		if (path.endsWith("png")) { //$NON-NLS-1$
			return org.apache.xmlgraphics.util.MimeConstants.MIME_PNG;
		}
		if (path.endsWith("svg")) { //$NON-NLS-1$
			return org.apache.xmlgraphics.util.MimeConstants.MIME_SVG;
		}
		return null;
	}
	
	/**
	 * maps the given Configuration to the extension string 
	 * @author Lukas
	 *
	 * @param ext the extension e.g.: "ext"
	 * @param element the registered stepped process
	 */
	public void registerExtension(String ext,IConfigurationElement element){
		if(this.elementsToExtensions == null){
			this.elementsToExtensions = new HashMap<>();
		}
		this.elementsToExtensions.put(ext, element);
	}

	/**
	 *
	 * @author Lukas
	 *
	 * @param ext the extension e.g.: "ext"
	 * @return if elementsToExtensions contains the extension
	 */
	public boolean isRegistered(String ext){
		return this.elementsToExtensions.containsKey(ext);
	}
	
	public IConfigurationElement getConfigurationFor(UUID projectID){
		return this.elementsToExtensions.get(getProjectExtension(projectID));
	}
	/**
	 * @return the lOGGER
	 */
	public static Logger getLOGGER() {
		return ProjectManager.LOGGER;
	}

}



