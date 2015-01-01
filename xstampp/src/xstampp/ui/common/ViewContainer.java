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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import messages.Messages;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.SAXException;

import xstampp.Activator;
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

	private final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();

	/**
	 * The width of the navigation
	 */
	private static final int NAVIGATION_WIDTH = 250;

	/**
	 * Constant for the full size of the form layout
	 */
	private static final int FULL_SIZE = 100;

	/**
	 * The height of the title label
	 */
	private static final int TITLE_HEIGHT = 30;

	/**
	 * The message which the dialog shows
	 */
	private static final String DISCARD_MESSAGE = Messages.ThereAreUnsafedChangesDoYouWantToStoreThem;

	private static final String OVERWRITE_MESSAGE = Messages.DoYouReallyWantToOverwriteTheFile;

	/**
	 * should be set when the control structure has been initially build
	 */
	private boolean initControlStructure;

	private boolean initCSWithProcessModel;

	private class LoadJobChangeAdapter extends JobChangeAdapter {

		@Override
		public void done(IJobChangeEvent event) {
			if (event.getResult() == Status.CANCEL_STATUS) {
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						MessageDialog.openInformation(Display.getDefault()
								.getActiveShell(), Messages.Information,
								Messages.ThisHazFileIsInvalid);
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
			File saveFile = ((LoadJob) this.event.getJob()).getFile();
			ViewContainer.getContainerInstance().projectSaveFiles.put(
					projectId, saveFile);
		}
	}

	private Map<UUID, IDataModel> projectDataMap;
	private Map<UUID, File> projectSaveFiles;

	/**
	 * The title Label.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private ViewTitle titleLabel;

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
		this.initControlStructure = false;
		this.initCSWithProcessModel = false;
	}

	@Override
	public UUID startUp(Class<?> controller, String projectName, String path) {
		IDataModel newController;
		try {
			newController = (IDataModel) controller.newInstance();
			newController.setProjectName(projectName);
			newController.updateValue(ObserverValue.PROJECT_NAME);
			this.initControlStructure = false;
			this.initCSWithProcessModel = false;
			UUID projectId = this.addProjectData(newController);
			this.projectSaveFiles.put(projectId, new File(path));
			this.saveDataModel(projectId);
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
		File newNameFile = new File(projectFile, projectName);
		if (projectFile.renameTo(newNameFile)) {
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

		return this.saveDataModel(projectId);
	}

	@Override
	public boolean saveDataModel(UUID projectId) {
		if (this.projectSaveFiles.get(projectId) == null) {
			return this.saveDataModelAs(projectId);
		}
		final IDataModel tmpController = this.projectDataMap.get(projectId);

		tmpController.prepareForSave();
		Job save = new SaveJob(this.projectSaveFiles.get(projectId),
				ViewContainer.getLOGGER(), tmpController);
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
			temp = temp && this.saveDataModel(id);
		}
		return temp;
	}

	@Override
	public boolean loadDataModel() {
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
			if (copy.isFile()
					&& !MessageDialog.openQuestion(PlatformUI.getWorkbench()
							.getDisplay().getActiveShell(),
							Messages.FileExists,
							Messages.DoYouReallyWantToOverwriteTheFile)) {
				return false;
			}
			try (BufferedReader reader = new BufferedReader(new FileReader(
					outer));
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							copy))) {
				for (Object line : reader.lines().toArray()) {

					writer.append((CharSequence) line);
				}
				reader.close();
				writer.close();
				return this.loadDataModelFile(copy.getPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (file != null) {
			return this.loadDataModelFile(file);

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
	public boolean loadDataModelFile(String file) {
		IDataModel dataModel = null;
		for (IConfigurationElement extElement : Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor("xstampp.extension.steppedProcess")) {
			if(file.endsWith(extElement.getAttribute("extension"))){
				dataModel = (IDataModel) STPAPluginUtils.executeCommand(extElement.getAttribute("controller"));
			}
		}
		// if (this.overwriteDataModel()) {
		if (file != null && dataModel != null) {
			Job load = new LoadJob(dataModel,file, ViewContainer.getLOGGER());
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
		}
		this.saveDataModel(id);
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
			return this.projectDataMap.containsKey(projectId);
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

	public LoadJob(IDataModel dataModel,String filename, Logger log) {
		super(Messages.loadHaz);
		this.file = new File(filename);
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
			Source xmlFile = new StreamSource(this.getFile());

			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);

			Validator validator = schema.newValidator();
			monitor.worked(1);
			validator.validate(xmlFile);

			JAXBContext context = JAXBContext.newInstance(this.controller.getClass());

			Unmarshaller um = context.createUnmarshaller();

			this.setController((IDataModel) um.unmarshal(new FileReader(
					this.filename)));
			monitor.worked(2);
			monitor.done();

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
}

/**
 * a runtime job which is stores a DataModel in a given File
 * 
 * @author Lukas Balzer
 * 
 */
class SaveJob extends Job {

	final File file;
	final Logger log;
	final IDataModel controller;

	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param file
	 *            the file in which the job should store the results
	 * @param log
	 *            the logger in which all messeges are written
	 * @param controller
	 *            the Data model which should be stored, this must be a
	 *            JAXBContext
	 */
	public SaveJob(File file, Logger log, IDataModel controller) {
		super(Messages.saveHaz);
		this.file = file;
		this.log = log;
		this.controller = controller;
		
	}
	

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.savingHaz, IProgressMonitor.UNKNOWN);
		JAXBContext context;
		try {
			System.out.println(this.controller.getClass());
			context = JAXBContext.newInstance(this.controller.getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Write to file
			m.marshal(this.controller, this.file);
		} catch (JAXBException e) {
			this.log.error(e.getMessage(), e);
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}
}
