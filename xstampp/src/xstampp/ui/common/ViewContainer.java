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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import messages.Messages;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
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
import org.xml.sax.SAXException;

import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.navigation.ProjectExplorer;
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
public class ViewContainer implements IProcessController {

	/**
	 * The log4j logger
	 */
	private static final Logger LOGGER = Logger.getRootLogger();
	private static ViewContainer containerInstance;
	/**
	 * The ID of the view container.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	public static final String ID = "astpa.ui.common.viewcontainer"; //$NON-NLS-1$



	/**
	 * The message which the dialog shows
	 */
	private static final String DISCARD_MESSAGE = Messages.ThereAreUnsafedChangesDoYouWantToStoreThem;

	private static final String OVERWRITE_MESSAGE = Messages.DoYouReallyWantToOverwriteTheFile;

	private Map<UUID, IDataModel> projectDataMap;
	private Map<UUID, File> projectSaveFiles;
	

	private class LoadJobChangeAdapter extends JobChangeAdapter {

		@Override
		public void done(IJobChangeEvent event) {
			if (event.getResult() == Status.CANCEL_STATUS) {
				final String name = ((LoadJob) event.getJob()).getFile().getName();
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						MessageDialog.openInformation(Display.getDefault()
								.getActiveShell(), Messages.Information,
								String.format(Messages.ThisHazFileIsInvalid,name));
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
		private final IJobChangeEvent event;

		public LoadRunnable(IJobChangeEvent event) {
			this.event = event;
		}

		@Override
		public void run() {
			UUID projectId = ViewContainer.getContainerInstance()
					.addProjectData(((LoadJob) this.event.getJob()).getController());
			File saveFile = ((LoadJob) this.event.getJob()).getSaveFile();
			ViewContainer.getContainerInstance().projectSaveFiles.put(
					projectId, saveFile);
			ViewContainer.getContainerInstance().saveDataModel(projectId, false);
			ViewContainer.getContainerInstance().synchronizeProjectName(projectId);
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
	public ViewContainer() {

		this.projectDataMap = new HashMap<>();
		this.projectSaveFiles = new HashMap<>();
	}

	@Override
	public UUID startUp(Class<?> controller, String projectName, String path) {
		IDataModel newController;
		try {
			newController = (IDataModel) controller.newInstance();
			newController.setProjectName(projectName);
			newController.initializeProject();
			newController.updateValue(ObserverValue.PROJECT_NAME);
			UUID projectId = this.addProjectData(newController);
			this.projectSaveFiles.put(projectId, new File(path));
			this.saveDataModel(projectId, false);
			return projectId;
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param projectId
	 *            the id of the project which should be renamed
	 * @param projectName
	 *            the new name
	 * @return true if the project has been successfully renamed
	 */
	public boolean renameProject(UUID projectId, String projectName) {

		File projectFile = this.projectSaveFiles.get(projectId);
		
		Path newPath=projectFile.toPath().getParent();
		File newNameFile = new File(newPath.toFile(),projectName + ".haz");
		
		if (projectFile.renameTo(newNameFile) || !projectFile.exists()) {
			
			this.projectSaveFiles.remove(projectId);
			this.projectSaveFiles.put(projectId, newNameFile);
			return this.projectDataMap.get(projectId).setProjectName(
					projectName);
		}
		return false;
	}

	@Override
	public boolean saveDataModelAs(final UUID projectId) {

		IDataModel tmpController = this.projectDataMap.get(projectId);
		FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(new String[] { "*.haz" }); //$NON-NLS-1$
		fileDialog
				.setFilterNames(new String[] { "A-STPA project file (*.haz)" }); //$NON-NLS-1$
		fileDialog.setFileName(tmpController.getProjectName());
		String fileName = fileDialog.open();
		if (fileName == null) {
			return false;
		}
		File file = new File(fileName);
		if (file.exists()) {
			boolean result = MessageDialog.openConfirm(Display.getCurrent()
					.getActiveShell(), Messages.ConfirmSaveAs, String.format(
					ViewContainer.OVERWRITE_MESSAGE, file.getName()));
			if (!result) {
				return false;
			}
		}

		return this.saveDataModel(projectId, false);
	}

	@Override
	public boolean saveDataModel(UUID projectId, boolean isUIcall) {
		if (this.projectSaveFiles.get(projectId) == null) {
			return this.saveDataModelAs(projectId);
		}
		final IDataModel tmpController = this.projectDataMap.get(projectId);

		tmpController.prepareForSave();
		
		Job save = tmpController.doSave(this.projectSaveFiles.get(projectId),
				ViewContainer.getLOGGER(), isUIcall);
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
			temp = temp && this.saveDataModel(id, false);
		}
		return temp;
	}

	private void synchronizeProjectName(UUID projectID){
		File saveFile=this.projectSaveFiles.get(projectID);
		String projName= this.getTitle(projectID);
		renameProject(projectID, saveFile.getName().split("\\.")[0]);
	}
	@Override
	public boolean importDataModel() {
		FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench()
				.getDisplay().getActiveShell(), SWT.OPEN);
		fileDialog.setFilterExtensions(new String[] { "*.haz" }); //$NON-NLS-1$
		fileDialog
				.setFilterNames(new String[] { "A-STPA project file (*.haz)" }); //$NON-NLS-1$

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
							Messages.FileExists,String.format(Messages.DoYouReallyWantToOverwriteTheFile,outer.getName()))) {
				return false;
				}
				for(UUID id: this.projectSaveFiles.keySet()){
					if(this.projectSaveFiles.get(id).equals(copy) && !removeProjectData(id)){
						MessageDialog.openError(null, Messages.Error, Messages.CantOverride);
						return false;
					}
				}
				
			}
//			
//			try (BufferedReader reader = new BufferedReader(new FileReader(
//					outer));
//					BufferedWriter writer = new BufferedWriter(new FileWriter(
//							copy))) {
//				String currentLine;
//				while((currentLine = reader.readLine()) != null) {
//					writer.append(currentLine);
//					writer.newLine();
//				}
//				reader.close();
//				writer.close();
//				return this.loadDataModelFile(copy.getPath());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
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
	 * 
	 * @return whether the operation was successful or not
	 */
	@Override
	public boolean loadDataModelFile(String file,String saveFile) {
		IDataModel dataModel = null;
		for (IConfigurationElement extElement : Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor("astpa.extension.steppedProcess")) {
			if(file.endsWith(extElement.getAttribute("extension"))){
				dataModel = (IDataModel) STPAPluginUtils.executeCommand(extElement.getAttribute("controller"));
			}
		}
		// if (this.overwriteDataModel()) {
		if (file != null && dataModel != null) {
			Job load = new LoadJob(dataModel,file,saveFile, ViewContainer.getLOGGER());
			load.schedule();
			load.addJobChangeListener(new LoadJobChangeAdapter());
			return true;
		}

		// }

		return false;
	}

	/**
	 * Exports the PDF document.
	 * 
	 * @author Sebastian Sieber,Lukas Balzer
	 * 
	 * @param xslName
	 *            the name of the file in which the xsl file is stored which
	 *            should be used
	 * @param page
	 *            the export page
	 * @param jobMessage
	 *            the job message which is shown in the progress bar
	 * @param forceCSDeco
	 *            if the deco of the control structure should be shown or not
	 * @return whether exporting succeeded.
	 */
	public boolean export(Job exportJob, UUID projectId) {
		this.projectDataMap.get(projectId).prepareForExport();

		// start the job, that exports the pdf from the JAXB stream

		exportJob.schedule();
		return true;

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

		return this.projectDataMap.get(projectId).hasUnsavedChanges();
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
			if (this.projectDataMap.get(id).hasUnsavedChanges()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates all views when a new data model was created/loaded
	 * 
	 * @author Fabian Toth
	 * 
	 */
	private void setNewDataModel(UUID id) {
		for (ObserverValue value : ObserverValue.values()) {
			this.projectDataMap.get(id).updateValue(value);
			synchronizeProjectName(id);
		}
		this.saveDataModel(id, false);
		this.projectDataMap.get(id).setStored();
	}

	/**
	 * Asks the user if the data model should be overwritten
	 * 
	 * @author Fabian Toth
	 * @deprecated since vs 2.0 due to multiple datamodels are stored
	 * 
	 * @return true, if the data model should be overwritten
	 */
	@Deprecated
	private boolean overwriteDataModel() {
		if (!this.projectDataMap.get(1).hasUnsavedChanges()) {
			return true;
		}
		MessageDialog dialog = new MessageDialog(
				Display.getCurrent().getActiveShell(),
				Messages.PlatformName,
				null,
				ViewContainer.DISCARD_MESSAGE,
				MessageDialog.CONFIRM,
				new String[] { Messages.Store, Messages.Discard, Messages.Abort },
				0);
		int resultNum = dialog.open();
		switch (resultNum) {
		case -1:
			return false;
		case 0:
			// return this.saveDataModel();
		case 1:
			return true;
		case 2:
			return false;
		default:
			return false;
		}
	}

	@Override
	public void callObserverValue(ObserverValue value) {
		for (UUID id : this.getProjectKeys()) {
			this.projectDataMap.get(id).updateValue(value);
		}
	}

	/**
	 * @return the containerInstance
	 */
	public static ViewContainer getContainerInstance() {
		if (ViewContainer.containerInstance == null) {
			ViewContainer.containerInstance = new ViewContainer();
		}
		return ViewContainer.containerInstance;
	}

	@Override
	public IDataModel getDataModel(UUID projectId) {
		if (this.projectDataMap.containsKey(projectId)) {
			return this.projectDataMap.get(projectId);
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
		if (this.projectDataMap.containsValue(controller)) {
			for (UUID id : this.projectDataMap.keySet()) {
				if (this.projectDataMap.get(id) == controller) {
					return id;
				}
			}
		}
		return null;
	}

	@Override
	public String getTitle(UUID projectId) {
		if (this.projectDataMap.containsKey(projectId)) {
			return this.projectDataMap.get(projectId).getProjectName();
		}
		return Messages.NewProject;
	}

	public UUID addProjectData(IDataModel controller) {
		UUID id = UUID.randomUUID();
		this.projectDataMap.put(id, controller);
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

		File projectFile = this.projectSaveFiles.get(projectId);
		if (projectFile.delete()) {
			this.projectDataMap.remove(projectId);
			this.projectSaveFiles.remove(projectId);
			IViewPart explorer = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
									.getActivePage().findView("astpa.explorer");
			((ProjectExplorer)explorer).update(null, ObserverValue.DELETE);
			return !this.projectDataMap.containsKey(projectId);
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
		return this.projectDataMap.keySet();
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return a Map with all projectNames mapped to their UUID's
	 */
	public Map<UUID, String> getProjects() {
		Map<UUID, String> map = new HashMap<>();
		for (UUID id : this.projectDataMap.keySet()) {
			map.put(id, this.projectDataMap.get(id).getProjectName());
		}
		return map;
	}

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
	 * @return the lOGGER
	 */
	public static Logger getLOGGER() {
		return ViewContainer.LOGGER;
	}

}

class LoadJob extends Job {
	private final File file;
	private final String filename;
	private final Logger log;
	private IDataModel controller;
	private Class<?> clazz;
	private File saveFile;

	public LoadJob(IDataModel dataModel,String filename, String savePath, Logger log) {
		super(Messages.loadHaz);
		this.file = new File(filename);
		this.saveFile=new File(savePath);
		this.log = log;
		this.filename = filename;
		this.controller=dataModel;
	}

	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.loadingHaz, 2);

		try {
			// validate the file
			URL schemaFile = this.getClass().getResource("/hazschema.xsd"); //$NON-NLS-1$
			BufferedReader reader= new BufferedReader(new FileReader(this.getFile()));
			StringBuffer buffer = new StringBuffer();
			String line;
			
			while((line = reader.readLine()) != null){
				if(line.contains("<") || line.contains(">")){
					//gt and lt are replaced by null chars for the time of unescaping
					line = line.replace(">", "\0\0");
					line = line.replace("<", "\0");
				}
				line = StringEscapeUtils.unescapeHtml4(line);
				//now all gt/lt signs left after the unescaping are not part of the xml
				//syntax and get back escaped to assure the correct xml parsing
				line = line.replace("&", "&amp;");
				line = line.replace(">", "&gt;");
				line = line.replace("<", "&lt;");

				line = line.replace("\0\0",">");
				line = line.replace("\0","<");
				buffer.append(line);
				buffer.append("\n");
			}
			reader.close();
			
			Reader stream= new StringReader(buffer.toString()); 
			Source xmlFile = new StreamSource(stream,getFile().toURI().toASCIIString());
			
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);
	
			Validator validator = schema.newValidator();
			monitor.worked(1);
			validator.validate(xmlFile);
			System.setProperty( "com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");
			JAXBContext context = JAXBContext.newInstance(this.controller.getClass());

			Unmarshaller um = context.createUnmarshaller();
			StringReader stringReader=new StringReader(buffer.toString());
			this.setController((IDataModel) um.unmarshal(stringReader));
			reader.close();
			monitor.worked(2);
			monitor.done();
			 System.getProperties().remove("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize");

		} catch (SAXException e) {
			this.log.error(e.getMessage(), e);
			return Status.CANCEL_STATUS;
		} catch (IOException e) {
			this.log.error(e.getMessage(), e);
			return Status.CANCEL_STATUS;
		} catch (JAXBException e) {
			e.printStackTrace();
			return Status.CANCEL_STATUS;

		}
		return Status.OK_STATUS;
	}

	/**
	 * @return the controller
	 */
	public IDataModel getController() {
		return this.controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(IDataModel controller) {
		this.controller = controller;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * @return the file
	 */
	public File getSaveFile() {
		return this.saveFile;
	}
}


