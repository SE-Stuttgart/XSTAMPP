/*******************************************************************************
 * 
 * # * Copyright (c) 2013, 2017 ASTPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac Jarkko,
 * Heidenwag, Benedikt Markt, Jaqueline Patzek Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksey Babkovic, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import messages.Messages;
import xstampp.Activator;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.editors.STPAEditorInput;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUserProject;
import xstampp.util.AbstractLoadJob;
import xstampp.util.STPAPluginUtils;

/**
 * The view container contains the navigation view and the view area. The navigation view is by
 * default invisible and has to be set visible by using setShowNavigationView(true).
 * 
 * 
 * @author Patrick Wickenhaeuser
 * @author Fabian Toth
 * @author Sebastian Sieber
 * @author Lukas Balzer
 * 
 */
public class ProjectManager extends Observable implements IPropertyChangeListener {

  private static final String OUTPUT = "Output"; //$NON-NLS-1$

  /**
   * The log4j logger.
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

  private Map<UUID, ProjectFileContainer> projectContainerToUuid;
  private Map<UUID, Object> projectAdditionsToUuid;
  private Map<String, String> extensionsToModelClass;
  private Map<String, IConfigurationElement> elementsToExtensions;
  private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

  private class LoadRunnable implements Runnable {
    private File saveFile;
    private IDataModel controller;

    public LoadRunnable(File saveFile, IDataModel controller) {
      this.saveFile = saveFile;
      this.controller = controller;
    }

    @Override
    public void run() {
      UUID projectId = this.controller.getProjectId();
      projectContainerToUuid.put(projectId,
          new ProjectFileContainer(controller, this.saveFile.getPath()));
      this.controller.prepareForSave();
      if (!saveFile.exists()) {
        ProjectManager.getContainerInstance().saveDataModel(projectId, false, false);
      }
      IViewPart navi = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
          .findView(ProjectExplorer.ID);
      ProjectManager.getContainerInstance().synchronizeProjectName(projectId);
      if (navi != null) {
        ((ProjectExplorer) navi).updateProjects();
      }
    }
  }

  /**
   * Initializes the container in which the views are stored. Sets the active view to null.
   * 
   * @author Patrick Wickenhaeuser
   */
  public ProjectManager() {
    this.projectContainerToUuid = new HashMap<>();
    this.extensionsToModelClass = new HashMap<>();
    this.store.addPropertyChangeListener(this);
  }

  /**
   * calls {@link #startUp(Class, String, String, UUID)} with the originalId given as <b>null</b>.
   */
  public UUID startUp(Class<?> controller, String projectName, String path) {
    return startUp(controller, projectName, path, null);
  }

  public UUID startUp(Class<?> controller, String projectName, UUID originalId) {
    String extension = "." + getProjectExtension(originalId);
    File pathFile = new File(Platform.getInstanceLocation().getURL().getPath(),
        projectName + extension);
    return startUp(controller, projectName, pathFile.getAbsolutePath(), originalId);
  }

  /**
   * creates a new project in the given location.
   * 
   * @author Lukas Balzer
   * @param controller
   *          the controller class which should be used
   * @param projectName
   *          he name of the new project
   * @param path
   *          the path where the new project is stored
   * @param originalId
   *          The id of a project that should serve as model for the new project
   * @return The UUID of the new project
   */
  public UUID startUp(Class<?> controller, String projectName, String path, UUID originalId) {
    IDataModel newController;
    try {
      newController = (IDataModel) controller.newInstance();
      newController.setProjectName(projectName);
      UUID projectId = this.addProjectData(newController, path);
      if (this.projectContainerToUuid.containsKey(originalId)) {
        IDataModel originalModel = this.projectContainerToUuid.get(originalId).getController();
        newController.initializeProject(originalModel);
      } else {
        newController.initializeProject();
      }
      newController.updateValue(ObserverValue.PROJECT_NAME);
      updateProjectTree();

      this.saveDataModel(projectId, false, false);
      this.setChanged();
      this.notifyObservers();
      return projectId;
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * renames the store file to the given name, if a such a file doesn't already exist.
   * 
   * @author Lukas Balzer
   * 
   * @param projectId
   *          the id of the project which should be renamed
   * @param projectName
   *          the new name
   * @return true if the project has been successfully renamed
   */
  public boolean renameProject(UUID projectId, String projectName) {

    File projectFile = this.projectContainerToUuid.get(projectId).getProjectFile();
    String ext = this.projectContainerToUuid.get(projectId).getExtension();
    Path newPath = projectFile.toPath().getParent();
    File newNameFile = new File(newPath.toFile(), projectName + "." + ext); //$NON-NLS-1$

    if (projectFile.renameTo(newNameFile) || !projectFile.exists()) {
      this.projectContainerToUuid.get(projectId).setProjectName(projectName);
      updateProjectTree();
      return true;
    }
    return false;
  }

  /**
   * changes the project extension and stores the file
   * 
   * @param projectId
   *          the uuid stored for this project
   * @param ext
   *          the new extension literal without the dot e.g.: <code>exe</code> or <code>haz</code>
   * @return if the project extension could be stored
   */
  public boolean changeProjectExtension(UUID projectId, String ext) {
    this.projectContainerToUuid.get(projectId).setExtension(ext);
    renameProject(projectId, getTitle(projectId));
    saveDataModel(projectId, false, false);
    IViewPart explorer = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView("astpa.explorer"); //$NON-NLS-1$
    if (explorer != null) {
      ((ProjectExplorer) explorer).update(null, ObserverValue.PROJECT_NAME);
    }

    return true;
  }

  /**
   * opens a filedialog in which the user can than choose a file on his system where he wants to
   * store the dataModel into if the operation is successful means that the user has not canceled
   * the dialog and has chosen a legal file
   * {@link ProjectManager#saveDataModel(UUID, boolean, boolean)} is called with false in bath
   * boolean arguments.
   * 
   * @author Fabian Toth,Lukas Balzer
   * @param projectId
   *          the id of the project
   * 
   * @return whether the operation was successful or not
   */
  public boolean saveDataModelAs(final UUID projectId) {

    List<String> extensions = new ArrayList<>();
    IConfigurationElement extElement = getConfigurationFor(projectId);
    String[] filterNames = new String[] {};

    if (extElement != null) {
      String description = "extensionDescriptions"; //$NON-NLS-1$
      if (extElement.getAttribute(description) != null) {
        filterNames = extElement.getAttribute(description).split(";");//$NON-NLS-1$
      }

      String extension = "extension";//$NON-NLS-1$
      for (String ext : extElement.getAttribute(extension).split(";")) { //$NON-NLS-1$
        extensions.add("*." + ext); //$NON-NLS-1$
      }
    }
    FileDialog fileDialog = new FileDialog(
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
    fileDialog.setFilterExtensions(extensions.toArray(new String[] {}));
    if (extensions.size() == filterNames.length) {
      fileDialog.setFilterNames(filterNames);
    }

    IDataModel tmpController = this.projectContainerToUuid.get(projectId).getController();
    fileDialog.setFileName(tmpController.getProjectName());
    String fileName = fileDialog.open();
    if (fileName == null) {
      return false;
    }
    File file = new File(fileName);
    if (file.exists()) {
      boolean result = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(),
          Messages.ConfirmSaveAs, String.format(ProjectManager.OVERWRITE_MESSAGE, file.getName()));
      if (!result) {
        return false;
      }
    }
    String[] split = file.getName().split("\\."); //$NON-NLS-1$
    this.projectContainerToUuid.get(projectId).setProjectName(split[0]);
    this.projectContainerToUuid.get(projectId).setExtension(split[1]);
    updateProjectTree();
    return this.saveDataModel(projectId, false, false);

  }

  public UUID getActiveProject() {
    IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView(ProjectExplorer.ID);
    UUID projectId = null;

    IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getActiveEditor();

    if (part != null
        && part.getViewSite().getSelectionProvider().getSelection() instanceof IProjectSelection) {
      Object selection = part.getViewSite().getSelectionProvider().getSelection();
      // if there is any project or step selected in the explorer, this project
      // is stored
      projectId = ((IProjectSelection) selection).getProjectId();
    }
    // if the focus is currectly not on the explorer and the selection is not
    // project related,
    // the project related to the active editor is stored
    else if (editor != null && editor.getEditorInput() instanceof STPAEditorInput) {
      projectId = ((STPAEditorInput) editor.getEditorInput()).getProjectID();
    }
    // if none of the above cases are true the active project id is null
    return projectId;
  }

  /**
   * Saves the data model to the file in the list projectSaveFilesToUUID. If this is null
   * saveDataModelAs() is called
   * 
   * @author Fabian Toth,Lukas Balzer
   * @param projectId
   *          the id of the project
   * @param isUIcall
   *          informs the runtime if the call is initiated by the user or the system
   * @param saveAs
   *          whether the saveAs dialog should be opened or not
   * @return whether the operation was successful or not
   */
  public boolean saveDataModel(UUID projectId, boolean isUIcall, boolean saveAs) {
    assert (this.projectContainerToUuid.get(projectId) != null);

    if (saveAs) {
      return this.saveDataModelAs(projectId);
    }
    final ProjectFileContainer projectFileContainer = this.projectContainerToUuid.get(projectId);
    if (projectFileContainer.isLock()) {
      return false;
    }
    final IDataModel tmpController = this.projectContainerToUuid.get(projectId).getController();

    tmpController.prepareForSave();

    final Job save = tmpController.doSave(projectContainerToUuid.get(projectId).getProjectFile(),
        ProjectManager.getLOGGER(), isUIcall);
    if (save == null) {
      return false;
    }

    save.addJobChangeListener(new JobChangeAdapter() {

      @Override
      public void done(IJobChangeEvent event) {
        projectFileContainer.setLock(false);
        if (event.getResult().isOK()) {
          try {
            tmpController.setStored();
          } catch (SWTException e) {
            LOGGER.debug(Messages.ProjectManager_3);
            e.printStackTrace();
          }
        }

      }
    });
    projectFileContainer.setLock(true);
    save.schedule();
    return true;
  }

  /**
   * calls {@link #saveDataModel(UUID, boolean, boolean)} for all projects giving it the projectId
   * of each and preventing a save as call by setting the <i>saveAs</i> argument to false.
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

  private void synchronizeProjectName(UUID projectId) {
    File saveFile = projectContainerToUuid.get(projectId).getProjectFile();

    this.projectContainerToUuid.get(projectId).setProjectName(saveFile.getName().split("\\.")[0]);
  }

  /**
   * promts a choose Dialog to the user and calls {@link #loadDataModelFile(String, String)} as
   * needed.
   * 
   * @author Fabian Toth
   * @author Jarkko Heidenwag
   * @author Lukas Balzer
   * 
   * @return whether the operation was successful or not
   */
  public Job importDataModel() {
    FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
        SWT.OPEN);
    ArrayList<String> extensions = new ArrayList<>();
    extensions.add("*.*"); //$NON-NLS-1$
    for (String ext : this.elementsToExtensions.keySet()) {
      extensions.add("*." + ext); //$NON-NLS-1$
    }

    fileDialog.setFilterExtensions(extensions.toArray(new String[] {}));

    String file = fileDialog.open();

    // if the file is not null but also not located in the workspace the project
    // is loaded from the choosen file
    // but later stored in the workspace
    if ((file != null) && !file.contains(Platform.getInstanceLocation().getURL().getPath())) {

      File outer = new File(file);
      File copy = new File(Platform.getInstanceLocation().getURL().getPath(), outer.getName());
      if (copy.isFile()) {
        // if the imported file already exists and the user wants to overwrite
        // it with the new one,
        // the current project is searched and removed
        if (MessageDialog.openQuestion(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
            Messages.FileExists,
            String.format(Messages.DoYouReallyWantToOverwriteTheContentAt, outer.getName()))) {

          for (Entry<UUID, ProjectFileContainer> containerEntry : projectContainerToUuid
              .entrySet()) {
            if (containerEntry.getValue().getProjectFile().equals(copy)
                && !removeProjectData(containerEntry.getKey())) {
              MessageDialog.openError(null, Messages.Error, Messages.CantOverride);
              return null;
            }
          }
        } else {
          return null;
        }
      }

      this.loadDataModelFile(file, copy.getPath());

    } else if (file != null) {
      return this.loadDataModelFile(file, file);

    }

    return null;
  }

  /**
   * recives the accurate load job by calling the load command given by the IConfigurationElement
   * mapped to the given file extension of the storeFile. Loads the data model from the loadFile by
   * executing the loadJob with the LoadJobChangeAdapter
   * 
   * @author Lukas Balzer
   * @param loadFile
   *          the file which contains the dataModel
   * @param saveFile
   *          the file the project shuold be saved in, normally the same as loadFile
   * 
   * 
   * @return whether the operation was successful or not
   */
  public Job loadDataModelFile(String loadFile, String saveFile) {

    Object jobObject = null;
    String pluginName = ""; //$NON-NLS-1$
    for (Entry<String, IConfigurationElement> extElement : this.elementsToExtensions.entrySet()) {

      if (loadFile.endsWith(extElement.getKey())) {
        String id = "id"; //$NON-NLS-1$
        pluginName = extElement.getValue().getAttribute(id);
        String command = "command"; //$NON-NLS-1$
        jobObject = STPAPluginUtils.executeCommand(extElement.getValue().getAttribute(command));
        break;
      }
    }

    if (loadFile != null && jobObject != null && jobObject instanceof AbstractLoadJob) {
      ((AbstractLoadJob) jobObject).setFile(loadFile);
      ((AbstractLoadJob) jobObject).setSaveFile(saveFile);
      ((AbstractLoadJob) jobObject).schedule();
      ((AbstractLoadJob) jobObject).addJobChangeListener(new JobChangeAdapter() {

        @Override
        public void done(IJobChangeEvent event) {
          final AbstractLoadJob job = (AbstractLoadJob) event.getJob();
          if (event.getResult().isOK()) {
            Display.getDefault().syncExec(new LoadRunnable(job.getSaveFile(), job.getController()));
            super.done(event);
          }
        }
      });
      return ((AbstractLoadJob) jobObject);
    } else if (jobObject == null) {
      LOGGER.error(Messages.FileFormatNotSupported + ": " + loadFile); //$NON-NLS-1$
    } else if (!(jobObject instanceof AbstractLoadJob)) {
      LOGGER.error(String.format(Messages.InvalidPluginCommand, pluginName));
    }

    return null;
  }

  /**
   * Checks if there are unsaved changes or not.
   * 
   * @param projectId
   *          the id of the project for which the request is given
   * @return whether there are unsaved changes or not
   * 
   * @author Fabian Toth
   * @author Lukas Balzer
   */
  public boolean getUnsavedChanges(UUID projectId) {
    return this.projectContainerToUuid.get(projectId).getController().hasUnsavedChanges();
  }

  /**
   * Checks if there are unsaved changes or not.
   * 
   * @return whether there are unsaved changes or not
   * 
   * @author Fabian Toth,Lukas Balzer
   */
  public boolean getUnsavedChanges() {
    for (UUID id : this.getProjectKeys()) {
      if (this.projectContainerToUuid.get(id).getController().hasUnsavedChanges()) {

        return true;
      }
    }
    return false;
  }

  /**
   * checks and asks the user wether the application can be closed if there are unsafed changes of
   * unfinished jobs.
   * 
   * @return if the application can be safely closed
   */
  public boolean checkCloseApplication() {
    if (!STPAPluginUtils.getUnfinishedJobs().isEmpty() &&
        !MessageDialog.openConfirm(null,
            Messages.ApplicationWorkbenchWindowAdvisor_Unfinished_Jobs_Title,
            Messages.ApplicationWorkbenchWindowAdvisor_Unfinished_Jobs_Short
                + Messages.ApplicationWorkbenchWindowAdvisor_Unfinished_Jobs_Message)) {
      return false;
    }
    if (ProjectManager.getContainerInstance().getUnsavedChanges()) {
      MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(),
          Messages.PlatformName, null, Messages.ThereAreUnsafedChangesDoYouWantToStoreThem,
          MessageDialog.CONFIRM, new String[] { Messages.Store, Messages.Discard, Messages.Abort },
          0);
      int resultNum = dialog.open();
      switch (resultNum) {
      case -1:
        return false;
      case 0:
        return ProjectManager.getContainerInstance().saveAllDataModels();
      case 1:
        return true;
      case 2:
        return false;
      default:
        break;
      }
    }
    return true;
  }

  /**
   * Calls the observer of the data model with the given value.
   * 
   * @author Fabian Toth
   * 
   * @param value
   *          the value to call
   */
  public void callObserverValue(ObserverValue value) {
    for (UUID id : this.getProjectKeys()) {
      this.projectContainerToUuid.get(id).getController().updateValue(value);
    }
  }

  /**
   * returns the instance of the container used in the current runtime. If this method is called
   * initially the container instance is created in that call.
   * 
   * @return the containerInstance
   */
  public static ProjectManager getContainerInstance() {
    if (ProjectManager.containerInstance == null) {
      ProjectManager.containerInstance = new ProjectManager();
    }
    return ProjectManager.containerInstance;
  }

  /**
   * Returns the {@link IDataModel} stored for the given UUID.
   * 
   * @author Lukas Balzer
   *
   * @param projectId
   *          the id which is stored for the requested proejcts data
   * @return the DataModel as IDataModel for the given project id
   */
  public IDataModel getDataModel(UUID projectId) {
    if (this.projectContainerToUuid.containsKey(projectId)) {
      return this.projectContainerToUuid.get(projectId).getController();
    }
    return null;
  }

  /**
   * This method looks up the project file on the system and checks whether the it's marked as write
   * protected or not.
   * 
   * @param projectId
   *          The id of the project which's file resource should be checked.
   * @return whether or not the current process can write to the project file, returns <b>false</b>
   *         if the given project id is not mapped to a project
   */
  public boolean canWriteOnProject(UUID projectId) {
    boolean canWrite = false;
    if (this.projectContainerToUuid.containsKey(projectId)) {
      File projFile = this.projectContainerToUuid.get(projectId).getProjectFile();
      if (!projFile.exists()) {
        canWrite = true;
      } else {
        canWrite = projFile.canWrite();
      }
    }
    return canWrite;
  }

  /**
   * The inverse method for {@link #getDataModel(UUID)} checks whether the given controller is
   * registered as project in the current instance and if returns the project id for it.
   * 
   * @author Lukas Balzer
   * 
   * @param controller
   *          the controller given as ObserverS
   * @return the id or null
   */
  public UUID getProjectID(Observable controller) {
    for (UUID id : this.projectContainerToUuid.keySet()) {
      if (this.projectContainerToUuid.get(id).getController().equals(controller)) {
        return id;
      }
    }
    return null;
  }

  public File getOutputDir(UUID projectId) {
    String projectName = getTitle(projectId);
    File outputDir = new File(Platform.getInstanceLocation().getURL().getPath().toString() + OUTPUT
        + File.separator + projectName);
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }
    return outputDir;
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param projectId
   *          the id with which the project data are stored in the DataModel
   * @return the title of the project
   */
  public String getTitle(UUID projectId) {
    if (this.projectContainerToUuid.containsKey(projectId)) {
      return this.projectContainerToUuid.get(projectId).getProjectName();
    }
    return Messages.NewProject;
  }

  /**
   * generates a random uuid for the project and the registered extension and data model to it
   * 
   * @author Lukas Balzer
   *
   * @param controller
   *          the data model as IDataModel which contains the projects data
   * @param path
   *          the path to the save file which should be used,\n the type of the project is
   *          determined using the extension of this file
   * @return generates a random uuid and stores/returns it as the projectId
   */
  public UUID addProjectData(IDataModel controller, String path) {
    UUID id = controller.getProjectId();
    this.projectContainerToUuid.put(id, new ProjectFileContainer(controller, path));
    return id;

  }

  private void updateProjectTree() {
    IViewPart navi = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView("astpa.explorer"); //$NON-NLS-1$
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
   *          the id of the project which shoul be removed from the Map of projects
   * @return whether the removal was succesful or not
   */
  public boolean removeProjectData(UUID projectId) {
    if (projectContainerToUuid.containsKey(projectId)) {
      File projectFile = this.projectContainerToUuid.get(projectId).getProjectFile();
      if (!projectFile.exists() || projectFile.delete()) {
        this.projectContainerToUuid.remove(projectId).getController()
            .updateValue(ObserverValue.DELETE);
        return !this.projectContainerToUuid.containsKey(projectId);
      }
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
    Set<UUID> keys = this.projectContainerToUuid.keySet();
    ArrayList<UUID> tmp = new ArrayList<>(keys);
    Collections.sort(tmp, new Comparator<UUID>() {

      @Override
      public int compare(UUID arg0, UUID arg1) {
        int extSortPref = store.getInt(IPreferenceConstants.NAVIGATION_EXTENSION_SORT);
        int nameSortPref = store.getInt(IPreferenceConstants.NAVIGATION_NAME_SORT);
        String a = getTitle(arg0);
        String b = getTitle(arg1);
        String extA = getProjectExtension(arg0);
        String extB = getProjectExtension(arg1);
        int extCompare = extSortPref * extA.compareTo(extB);
        if (extCompare == 0) {
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
   *          the id for the requested project
   * @return the extension which is registered for the project
   */
  public String getProjectExtension(UUID id) {
    if (this.projectContainerToUuid.containsKey(id)) {
      return this.projectContainerToUuid.get(id).getExtension();
    }
    return new String();
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @return a Map with all projectNames mapped to their UUID's
   */
  public Map<UUID, String> getProjects() {
    Map<UUID, String> map = new HashMap<>();
    for (UUID id : this.projectContainerToUuid.keySet()) {
      map.put(id, this.projectContainerToUuid.get(id).getProjectName());
    }
    return map;
  }

  /**
   *
   * @author Lukas Balzer
   *
   * @param path
   *          a file path
   * @return a mime constant for the handling of the file
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
   * 
   * @author Lukas Balzer
   *
   * @param ext
   *          the extension e.g.: "ext"
   * @param element
   *          the registered stepped process
   */
  public void registerExtension(String modelClass, String ext, IConfigurationElement element) {
    if (this.elementsToExtensions == null) {
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
   * @param ext
   *          the extension e.g.: "ext"
   * @return if elementsToExtensions contains the extension
   */
  public boolean isRegistered(String ext) {
    return this.elementsToExtensions.containsKey(ext);
  }

  /**
   * looks up the <code> IConfigurationElement </code> which is registered for the extension, of the
   * projects save file
   * 
   * @author Lukas Balzer
   *
   * @param projectId
   *          the id of the requested project
   * @return the configuration element as defined in the steppedProcess extension Point
   */
  public IConfigurationElement getConfigurationFor(UUID projectId) {
    return this.elementsToExtensions.get(getProjectExtension(projectId));
  }

  /**
   * @return the lOGGER
   */
  public static Logger getLOGGER() {
    return ProjectManager.LOGGER;
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    if (event.getProperty().contains("navigation")) { //$NON-NLS-1$
      updateProjectTree();
    }
  }

  /**
   * return the stored addition for the project with the given id
   * 
   * @param id
   *          should be a valid project id
   * @return null if there is no addition stored for the project, or the stored value as an Object,
   *         the class handling must be preformed by callers
   * @author Lukas Balzer
   */
  public Object getProjectAdditionsFromUUID(UUID id) {
    if (this.projectAdditionsToUuid == null) {
      return null;
    }
    return this.projectAdditionsToUuid.get(id);
  }

  /**
   * 
   * @param id
   *          a uuid
   * @param addition
   *          a class which should be stored for this runtime in addition to the project dataModel
   * @author Lukas Balzer
   */
  public void addProjectAdditionForUUID(UUID id, Object addition) {
    if (this.projectAdditionsToUuid == null) {
      this.projectAdditionsToUuid = new HashMap<>();
    }
    this.projectAdditionsToUuid.put(id, addition);
  }

  /**
   * This method checks the access rights to the given project.<br>
   * If the project associated with the given id is not a {@link IUserProject} than this method
   * simply returns <b>true</b>.<br>
   * If the project has a UserSystem but no user is logged in this method opens a login dialog.
   * 
   * @param id
   *          The id of a project in the current runtime
   * @return if the project associated with the given id is a {@link IUserProject} this method
   *         checks the access rights of the current user.<br>
   *         <b>true</b> if the project is no {@link IUserProject}
   */
  public boolean canAccess(UUID id) {
    IDataModel dataModel = getDataModel(id);
    if (dataModel instanceof IUserProject) {
      return ((IUserProject) dataModel).getUserSystem().checkAccess(AccessRights.ACCESS);
    }
    return true;
  }

  public void addSaveStateListener(IPropertyChangeListener listener) {
    listeners.add(listener);
  }

}
