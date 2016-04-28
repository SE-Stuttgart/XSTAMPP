/*******************************************************************************

# * Copyright (c) 2013 ASTPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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
import java.util.Collections;
import java.util.Comparator;
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
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import xstampp.Activator;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.navigation.IProjectSelection;
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
public class ProjectManager implements IPropertyChangeListener {

	private final static String OUTPUT ="Output"; //$NON-NLS-1$
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
	private List<IPropertyChangeListener> listeners = new ArrayList<>();

	private static final String OVERWRITE_MESSAGE = Messages.DoYouReallyWantToOverwriteTheContentAt;

	private Map<UUID, IDataModel> projectDataToUUID;
	private Map<UUID, Object> projectAdditionsToUUID;
	private Map<UUID, File> projectSaveFilesToUUID;
	private Map<UUID, String> extensionsToUUID;
	private Map<String, String> extensionsToModelClass;
	private Map<String, IConfigurationElement> elementsToExtensions;
	private final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();

	/**
	 * when the plugin dependent load job is done, this change adapter promts out all error messages
	 * returned by the job in case of failure or calls {@link LoadRunnable} in case of success
	 *
	 * @author Lukas Balzer
	 *
	 */
	private class LoadJobChangeAdapter extends JobChangeAdapter {

		@Override
		public void done(IJobChangeEvent event) {
			final AbstractLoadJob job = (AbstractLoadJob) event.getJob();
			if (event.getResult() == Status.CANCEL_STATUS) {
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
				
				Display.getDefault().syncExec(new LoadRunnable(job.getSaveFile(),job.getController()));
				super.done(event);
			}
		}
	}

	private class LoadRunnable implements Runnable {
		private File saveFile;
		private IDataModel controller;
		
		public LoadRunnable(File saveFile,IDataModel controller) {
			this.saveFile = saveFile;
			this.controller = controller;
		}

		@Override
		public void run() {
			UUID projectId = UUID.randomUUID();
			ProjectManager.getContainerInstance().projectDataToUUID.put(projectId, this.controller);
			IViewPart navi = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView(ProjectExplorer.ID);
			
			ProjectManager.getContainerInstance().extensionsToUUID.put(projectId, saveFile.getName().split("\\.")[1]);//$NON-NLS-1$
			ProjectManager.getContainerInstance().projectSaveFilesToUUID.put(
					projectId, saveFile);
			if(!saveFile.exists()){
				ProjectManager.getContainerInstance().saveDataModel(projectId, false, false);
			}
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
		this.extensionsToModelClass = new HashMap<>();
		this.store.addPropertyChangeListener(this);
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
	 * changes the project extension and stores the file
	 * @param projectId the uuid stored for this project
	 * @param ext the new extension literal without the dot e.g.: <code>exe</code> or <code>haz</code> 
	 * @return
	 */
	public boolean changeProjectExtension(UUID projectId, String ext){
		this.extensionsToUUID.remove(projectId);
		this.extensionsToUUID.put(projectId, ext);
		renameProject(projectId, getTitle(projectId));
		saveDataModel(projectId, false, false);
		IViewPart explorer = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView("astpa.explorer"); //$NON-NLS-1$
		if(explorer != null){
			((ProjectExplorer)explorer).update(null, ObserverValue.PROJECT_NAME);
		}
		
		return true;
	}
	
	/**
	 *opens a filedialog in which the user can than choose a file on his system where he wants to store the dataModel into
	 *if the operation is successful means that the user has not canceled the dialog and has chosen a legal file
	 *{@link ProjectManager#saveDataModel(UUID, boolean, boolean)} is called with false in bath boolean arguments
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
		this.projectSaveFilesToUUID.remove(projectId);
		this.projectSaveFilesToUUID.put(projectId, file);
		this.extensionsToUUID.remove(projectId);
		this.extensionsToUUID.put(projectId, fileName.split("\\.")[1]);
		updateProjectTree();
		return this.saveDataModel(projectId, false, false);
		
	}

	/**
	 * Saves the data model to the file in the list projectSaveFilesToUUID. If this is null
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
		
		final Job save = tmpController.doSave(this.projectSaveFilesToUUID.get(projectId),
				ProjectManager.getLOGGER(), isUIcall);
		if(save == null){
			return false;
		}
		
		save.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				if(event.getResult().isOK()){
					try{
						tmpController.setStored();
					}catch(SWTException e){
						LOGGER.debug("cant't mark DataModel as saved!");
						e.printStackTrace();
					}
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
	 * promts a choose Dialog to the user and calls {@link #loadDataModelFile(String, String)} as needed
	 * 
	 * @author Fabian Toth
	 * @author Jarkko Heidenwag
	 * @author Lukas Balzer
	 * 
	 * @return whether the operation was successful or not
	 */
	public Job importDataModel() {
		FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench()
				.getDisplay().getActiveShell(), SWT.OPEN);
		ArrayList<String> extensions= new ArrayList<>();
		for (String ext : this.elementsToExtensions.keySet()) {
			extensions.add("*." + ext);
		}
			
		fileDialog.setFilterExtensions(extensions.toArray(new String[]{}));
		
		String file = fileDialog.open();
		if(this.projectSaveFilesToUUID.containsValue(new File(file))){
			MessageDialog.openError(PlatformUI.getWorkbench()	
							.getDisplay().getActiveShell(), "Project is allready open!", "This project allready exists in the workspace!");
			return null;
		}
		//if the file is not null but also not located in the workspace the project is loaded from the choosen file
		//but later stored in the workspace
		if ((file != null) && !file.contains(Platform.getInstanceLocation().getURL().getPath())) {

			File outer = new File(file);
			File copy = new File(Platform.getInstanceLocation().getURL()
					.getPath(), outer.getName());
			if (copy.isFile()){
				//if the imported file allready exists and the user wants to overwrite it with the new one,
				//the current project is searched and removed
				if(MessageDialog.openQuestion(PlatformUI.getWorkbench()	
							.getDisplay().getActiveShell(),
							Messages.FileExists,String.format(Messages.DoYouReallyWantToOverwriteTheContentAt,outer.getName()))) {
					
					Set<UUID> idSet =this.projectSaveFilesToUUID.keySet();
					for(UUID id: idSet){
						if(this.projectSaveFilesToUUID.get(id).equals(copy) && !removeProjectData(id)){
							MessageDialog.openError(null, Messages.Error, Messages.CantOverride);
							return null;
						}
					}
				}else{
					return null;
				}
			}

			this.loadDataModelFile(file,copy.getPath());
			
		} else if (file != null) {
			return this.loadDataModelFile(file,file);

		}

		return null;
	}

	/**
	 * recives the accurate load job by calling the load command given by the IConfigurationElement
	 * mapped to the given file extension of the storeFile.  
	 * Loads the data model from the loadFile by executing the loadJob with the LoadJobChangeAdapter
	 * 
	 * @author Lukas Balzer
	 * @param loadFile
	 *            the file which contains the dataModel
	 * @param saveFile
	 * 			  the file the project shuold be saved in,
	 * 			  normally the same as loadFile   
	 * 					
	 * 
	 * @return whether the operation was successful or not
	 */
	public Job loadDataModelFile(String loadFile,String saveFile) {
		
		Object jobObject = null;
		String pluginName = ""; //$NON-NLS-1$
		for (Entry<String, IConfigurationElement> extElement : 
			this.elementsToExtensions.entrySet()) { 
			
			if(loadFile.endsWith(extElement.getKey())){ 
				pluginName = extElement.getValue().getAttribute("id"); //$NON-NLS-1$
				jobObject = STPAPluginUtils.
						executeCommand(extElement.getValue().getAttribute("command")); //$NON-NLS-1$
				break;
			}
		}
		
		if (loadFile != null && jobObject != null && jobObject instanceof AbstractLoadJob) {
			((AbstractLoadJob) jobObject).setFile(loadFile);
			((AbstractLoadJob) jobObject).setSaveFile(saveFile);
			((AbstractLoadJob) jobObject).schedule();
			((AbstractLoadJob) jobObject).addJobChangeListener(new LoadJobChangeAdapter());
			return ((AbstractLoadJob) jobObject);
		}
		else if(jobObject == null){
			LOGGER.error(Messages.FileFormatNotSupported + ": " + loadFile); //$NON-NLS-1$
		}
		else if(!(jobObject instanceof AbstractLoadJob)){
			LOGGER.error(String.format(Messages.InvalidPluginCommand,pluginName));
		}


		return null;
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
	
	public File getOutputDir(UUID projectId){
		String projectName = getTitle(projectId);
		File outputDir =new File(Platform.getInstanceLocation().getURL().getPath().toString()
				+ OUTPUT+File.separator+projectName);
		if(!outputDir.exists()){
			outputDir.mkdirs();
		}
		return outputDir;
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
	 * @param path the path to the save file which should be used,\n
	 * 				the type of the project is determined using the extension of this file
	 * @return	generates a random uuid and stores/returns it as the projectId
	 */
	public UUID addProjectData(IDataModel controller, String path) {
		UUID id = UUID.randomUUID();
		this.projectSaveFilesToUUID.put(id, new File(path));
		this.projectDataToUUID.put(id, controller);
		updateProjectTree();
		return id;

	}

	private void updateProjectTree(){
		IViewPart navi = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView("astpa.explorer"); //$NON-NLS-1$
		if (navi != null) {
			((ProjectExplorer) navi).updateProjects();
		}
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
			this.projectDataToUUID.remove(projectId).updateValue(ObserverValue.DELETE);
			this.projectSaveFilesToUUID.remove(projectId);
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
	public ArrayList<UUID> getProjectKeys() {
		Set<UUID> keys = this.projectDataToUUID.keySet();
		ArrayList<UUID> tmp= new ArrayList<>(keys);
		Collections.sort(tmp,new Comparator<UUID>() {

			@Override
			public int compare(UUID arg0, UUID arg1) {
				int extSortPref = store.getInt(IPreferenceConstants.NAVIGATION_EXTENSION_SORT);
				int nameSortPref =	store.getInt(IPreferenceConstants.NAVIGATION_NAME_SORT);
				String a = getTitle(arg0);
				String b = getTitle(arg1);
				String extA = getProjectExtension(arg0);
				String extB = getProjectExtension(arg1);
				int extCompare =extSortPref * extA.compareTo(extB);
				if(extCompare == 0){
					return nameSortPref * a.compareTo(b);
				}
				return extCompare;
			}
			
		});
		
		return tmp;
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
	 * @author Lukas Balzer
	 *
	 * @param ext the extension e.g.: "ext"
	 * @param element the registered stepped process
	 */
	public void registerExtension(String modelClass,String ext,IConfigurationElement element){
		if(this.elementsToExtensions == null){
			this.elementsToExtensions = new HashMap<>();
		}
		this.elementsToExtensions.put(ext, element);
		this.extensionsToModelClass.put(modelClass, ext);
		LOGGER.debug("registered extension: " + ext); //$NON-NLS-1$
	}

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param ext the extension e.g.: "ext"
	 * @return if elementsToExtensions contains the extension
	 */
	public boolean isRegistered(String ext){
		return this.elementsToExtensions.containsKey(ext);
	}
	
	/**
	 * looks up the <code> IConfigurationElement </code> which is registered for the extension,
	 * of the projects save file
	 * 
	 * @author Lukas Balzer
	 *
	 * @param projectID the id of the requested project
	 * @return the configuration element as defined in the steppedProcess extension Point
	 */
	public IConfigurationElement getConfigurationFor(UUID projectID){
		String name =this.projectDataToUUID.get(projectID).getClass().getName();
		String ext = this.extensionsToModelClass.get(name);
		return this.elementsToExtensions.get(getProjectExtension(projectID));
	}
	/**
	 * @return the lOGGER
	 */
	public static Logger getLOGGER() {
		return ProjectManager.LOGGER;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty().contains("navigation")){
			updateProjectTree();
		}
	}

	/**
	 * return the stored addition for the project with the given id
	 * @param id should be a valid project id
	 * @return null if there is no addition stored for the project,
	 * 			or the stored value as an Object, the class handling must be
	 * 			preformed by callers
	 * @author Lukas Balzer
	 */
	public Object getProjectAdditionsFromUUID(UUID id) {
		if(this.projectAdditionsToUUID == null){
			return null;
		}
		return this.projectAdditionsToUUID.get(id);
	}

	/**
	 * 
	 * @param id a uuid 
	 * @param addition a class which should be stored for this runtime in addition to the
	 * 					project dataModel
	 * @author Lukas Balzer
	 */
	public void addProjectAdditionForUUID(UUID id, Object addition){
		if(this.projectAdditionsToUUID == null){
			this.projectAdditionsToUUID = new HashMap<>();
		}
		this.projectAdditionsToUUID.put(id, addition);
	}

	public void addSaveStateListener(IPropertyChangeListener listener){
		listeners.add(listener);
	}
	
	
}



