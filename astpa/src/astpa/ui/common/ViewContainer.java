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

package astpa.ui.common;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import messages.Messages;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.area.AreaTreeModel;
import org.apache.fop.area.AreaTreeParser;
import org.apache.fop.area.Span;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.xml.sax.SAXException;

import astpa.Activator;
import astpa.controlstructure.CSAbstractEditor;
import astpa.controlstructure.CSEditor;
import astpa.controlstructure.CSEditorWithPM;
import astpa.controlstructure.IControlStructureEditor;
import astpa.export.stepData.STPAdataWizard;
import astpa.export.stepImages.PdfExportWizard;
import astpa.export.stepImages.UCATableExportWizard;
import astpa.model.DataModelController;
import astpa.model.ITableModel;
import astpa.model.ObserverValue;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.acchaz.AccidentsView;
import astpa.ui.acchaz.HazardsView;
import astpa.ui.causalfactors.CausalFactorsView;
import astpa.ui.linking.LinkingView;
import astpa.ui.menu.file.commands.Welcome;
import astpa.ui.navigation.NavigationView;
import astpa.ui.sds.CSCView;
import astpa.ui.sds.ControlActionView;
import astpa.ui.sds.DesignRequirementView;
import astpa.ui.sds.SafetyConstraintView;
import astpa.ui.sds.SystemGoalView;
import astpa.ui.statusline.StatusLineManager;
import astpa.ui.systemdescription.SystemDescriptionView;
import astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import astpa.ui.welcome.WelcomeView;

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
public class ViewContainer extends ViewPart {
	
	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	/**
	 * The log4j logger
	 */
	private static final Logger LOGGER = Logger.getRootLogger();
	
	/**
	 * The ID of the view container.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	public static final String ID = "astpa.ui.common.viewcontainer"; //$NON-NLS-1$
	
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
	
	public enum ExportConstants{
		/**
		 * Enum value for the Pdf Export
		 * 
		 * @author Lukas Balzer
		 */
		PDF,
		
		/**
		 * Enum constant for exporting the UCA tabel
		 * @author Lukas Balzer
		 */
		UCA_TABLE;
	}
	/**
	 * Class used to manage references to views.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	private class IViewReference {
		
		/**
		 * The parent composite of the view.
		 */
		private Composite parent;
		
		/**
		 * The view instance.
		 */
		private IViewBase viewInstance;
		
		
		/**
		 * Ctor of the view reference.
		 * 
		 * @param parent the parent composite in which the gui elements are
		 *            stored.
		 * @param viewInstance the instance of the view.
		 * 
		 * @author Patrick Wickenhaeuser
		 */
		public IViewReference(Composite parent, IViewBase viewInstance) {
			this.parent = parent;
			this.viewInstance = viewInstance;
		}
		
		/**
		 * Sets the visibility of the view
		 * 
		 * @author Fabian Toth
		 * 
		 * @param b whether the view should be shown or not
		 */
		public void setVisible(boolean b) {
			this.parent.setVisible(b);
		}
		
		/**
		 * Gets the title of the view
		 * 
		 * @author Fabian Toth
		 * 
		 * @return the title
		 */
		public String getTitle() {
			return this.viewInstance.getTitle();
		}
		
		/**
		 * Perform the action to prepare the view
		 * 
		 * @author Fabian Toth
		 */
		public void onActivateView() {
			this.viewInstance.onActivateView();
		}
		
		/**
		 * this method triggers an signal to the view to start the export
		 * if available
		 *
		 * @author Lukas Balzer
		 *
		 */
		public void export(){
			if(!this.viewInstance.triggerExport("")){
				String msg= "Sorry but there's no Export available for" + this.getTitle();
				MessageDialog.openInformation(PlatformUI.createDisplay().getActiveShell(), "No Export Available", msg);
			}
		}
		
		/**
		 * Perform the actions before the view will be hidden
		 * 
		 * @author Fabian Toth
		 */
		public void onCloseView() {
			this.viewInstance.update(null, ObserverValue.SAVE);
		}
		
		/**
		 * Set the dataModel of the view
		 * 
		 * @author Fabian Toth
		 * 
		 * @param dataModel the new data Model
		 */
		public void setDataModel(DataModelController dataModel) {
			this.viewInstance.setDataModelInterface(dataModel);
		}
	}
	
	private class ExportJobChangeAdapter extends JobChangeAdapter {
		
		@Override
		public void done(final IJobChangeEvent event) {
			if (event.getResult().isOK()) {
				Display.getDefault().syncExec(new Runnable() {
					
					@Override
					public void run() {
						ViewContainer.this.dataModelController.updateValue(ObserverValue.EXPORT_FINISHED);
					}
				});
				super.done(event);
			}
		}
		
	}
	
	
	/**
	 * The list of the initialized views.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private Map<String, IViewReference> initializedViews = null;
	
	/**
	 * The title bar.
	 * 
	 * @author Lukas Balzer
	 */
	private Composite titleComposite;
	
	
	/**
	 * The title Label.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private ViewTitle titleLabel;
	
	/**
	 * 
	 * @author Lukas Balzer
	 */
	private Button exportButton;
	/**
	 * The root composite for everythings
	 */
	private Composite parentComposite;
	
	/**
	 * The root composite which is the parent of all views in the view
	 * container.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private Composite viewAreaRoot = null;
	
	/**
	 * The root composite for the navigation view.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private Composite navigationRoot = null;
	
	/**
	 * The navigation view.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private NavigationView navigationView = null;
	
	/**
	 * The currently active view. Is equal to null if none is active.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private IViewReference activeView = null;
	
	/**
	 * The datamodel controller.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private DataModelController dataModelController;
	
	/**
	 * the list of the views that should be initialized
	 */
	private List<Class<?>> views;
	
	/**
	 * the place where the last file was saved
	 */
	private File savedFile = null;
	
	/**
	 * defines if this is the first start up
	 */
	private boolean firstStartUp;
	
	/**
	 * The composite the welcome view is displayed in.
	 */
	private Composite welcomeViewRoot = null;
	
	/**
	 * The welcome view.
	 */
	private WelcomeView welcomeView = null;
	
	
	/**
	 * Initializes the container in which the views are stored. Sets the active
	 * view to null.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	public ViewContainer() {
		
		this.initControlStructure = false;
		this.initCSWithProcessModel = false;
		this.firstStartUp = true;
		this.dataModelController = new DataModelController();
		// initialize Statusline
		StatusLineManager.getInstance().setDataModelInterface(this.dataModelController);
		this.initializedViews = new HashMap<String, IViewReference>();
		this.activeView = null;
		this.views = new ArrayList<>();
		this.views.add(SystemDescriptionView.class);
		this.views.add(AccidentsView.class);
		this.views.add(HazardsView.class);
		this.views.add(LinkingView.class);
		this.views.add(SafetyConstraintView.class);
		this.views.add(SystemGoalView.class);
		this.views.add(DesignRequirementView.class);
		this.views.add(CSEditor.class);
		this.views.add(CSEditorWithPM.class);
		this.views.add(ControlActionView.class);
		this.views.add(UnsafeControlActionsView.class);
		this.views.add(CSCView.class);
		this.views.add(CausalFactorsView.class);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		ViewContainer.LOGGER.info("Intialize View Container"); //$NON-NLS-1$
		FormLayout formLayout = new FormLayout();
		this.parentComposite = new Composite(parent, SWT.NONE);
		this.parentComposite.setLayout(formLayout);
		
		// the navigation root is used to handle the visibility of the
		// navigation view
		FormData navigationData = new FormData();
		navigationData.width = ViewContainer.NAVIGATION_WIDTH;
		navigationData.top = new FormAttachment(0);
		navigationData.bottom = new FormAttachment(ViewContainer.FULL_SIZE);
		navigationData.left = new FormAttachment(0);
		this.navigationRoot = new Composite(this.parentComposite, SWT.BORDER);
		this.navigationRoot.setLayoutData(navigationData);
		this.navigationRoot.setLayout(new FillLayout());
		this.navigationView = new NavigationView();
		this.navigationView.setDataModelInterface(this.dataModelController);
		this.navigationView.createPartControl(this.navigationRoot);
		this.navigationRoot.setVisible(false);
		ViewContainer.LOGGER.info("Navigation has been initialized"); //$NON-NLS-1$
		
		this.titleComposite = new Composite(this.parentComposite, SWT.BORDER);
		FormData titleCompositeData= new FormData();
		titleCompositeData.height = ViewContainer.TITLE_HEIGHT;
		titleCompositeData.top = new FormAttachment(0);
		titleCompositeData.left = new FormAttachment(this.navigationRoot);
		titleCompositeData.right = new FormAttachment(ViewContainer.FULL_SIZE);
		this.titleComposite.setLayoutData(titleCompositeData);
		this.titleComposite.setLayout(new FormLayout());
		this.titleComposite.setVisible(false);
		
		this.exportButton = new Button(this.titleComposite, SWT.PUSH);
		this.exportButton.setText(Messages.Export);
		FormData exportButtonData = new FormData();
		exportButtonData.height = ViewContainer.TITLE_HEIGHT;
		exportButtonData.right = new FormAttachment(ViewContainer.FULL_SIZE);
		this.exportButton.setLayoutData(exportButtonData);
		this.exportButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				ViewContainer.this.activeView.export();
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				//do nothing when mouse is clicked
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// Do nothing when double clicked
			}
		});
		
		// the title of the current view
		this.titleLabel = new ViewTitle(this.titleComposite, SWT.NONE, ""); //$NON-NLS-1$
		FormData titleLabelData= new FormData();
		titleLabelData.height = ViewContainer.TITLE_HEIGHT;
		titleLabelData.top = new FormAttachment(0);
		titleLabelData.left = new FormAttachment(this.navigationRoot);
		titleLabelData.right = new FormAttachment(this.exportButton);
		
		this.titleLabel.setLayoutData(titleLabelData);
		this.titleLabel.setVisible(false);
		ViewContainer.LOGGER.info("View title has been initialized"); //$NON-NLS-1$
		
		
		
		// the root element of the view area. This is where the views are
		// displayed.
		FormData viewAreaData = new FormData();
		viewAreaData.left = new FormAttachment(this.navigationRoot);
		viewAreaData.right = new FormAttachment(ViewContainer.FULL_SIZE);
		viewAreaData.top = new FormAttachment(this.titleComposite);
		viewAreaData.bottom = new FormAttachment(ViewContainer.FULL_SIZE);
		this.viewAreaRoot = new Composite(this.parentComposite, SWT.BORDER);
		this.viewAreaRoot.setLayoutData(viewAreaData);
		this.viewAreaRoot.setLayout(new FormLayout());
		this.viewAreaRoot.setVisible(false);
		ViewContainer.LOGGER.info("View area has been initialized"); //$NON-NLS-1$
		
		// the welcome page filling the whole screen
		this.welcomeViewRoot = new Composite(this.parentComposite, SWT.NONE);
		this.welcomeViewRoot.setVisible(false);
		
		this.welcomeView = new WelcomeView();
		this.welcomeView.createPartControl(this.welcomeViewRoot);
		
		// initialize the views
		FormData viewTestData = new FormData();
		viewTestData.left = new FormAttachment(0);
		viewTestData.right = new FormAttachment(ViewContainer.FULL_SIZE);
		viewTestData.top = new FormAttachment(0);
		viewTestData.bottom = new FormAttachment(ViewContainer.FULL_SIZE);
		for (Class<?> clazz : this.views) {
			try {
				IViewBase newView = (IViewBase) clazz.newInstance();
				Composite viewRoot = new Composite(this.viewAreaRoot, SWT.NONE);
				viewRoot.setLayoutData(viewTestData);
				viewRoot.setLayout(new FillLayout());
				newView.setDataModelInterface(this.dataModelController);
				if (newView.getId().equals(CSEditor.ID) || newView.getId().equals(CSEditorWithPM.ID)) {
					((CSAbstractEditor) newView).setSite(this.getSite());
				}
				newView.createPartControl(viewRoot);
				this.initializedViews.put(newView.getId(), new IViewReference(viewRoot, newView));
				ViewContainer.LOGGER.info(newView.getId() + " has been initialized"); //$NON-NLS-1$
				viewRoot.setVisible(false);
				
			} catch (InstantiationException | IllegalAccessException e) {
				ViewContainer.LOGGER.error("Instance of \"" + clazz.toString() //$NON-NLS-1$
					+ "\" could not be created\n" + e.getMessage()); //$NON-NLS-1$
			}
		}
		this.dataModelController.setStored();
		
		boolean state = true;
		if (Activator.getDefault().getPreferenceStore().contains(WelcomeView.getShowWelcomeOnStartupPreferences())) {
			state =
				Activator.getDefault().getPreferenceStore()
					.getBoolean(WelcomeView.getShowWelcomeOnStartupPreferences());
		}
		
		if (state) {
			this.showWelcomePage();
		} else {
			this.hideWelcomePage();
			this.activateView(SystemDescriptionView.ID);
		}
	}
	
	@Override
	public void setFocus() {
		// intentionally empty
	}
	
	/**
	 * Starts the view container and makes it visible
	 * 
	 * @author Fabian Toth
	 */
	public void startUp() {
		if (this.firstStartUp) {
			this.navigationRoot.setVisible(true);
			this.titleLabel.setVisible(true);
			this.titleComposite.setVisible(true);
			this.viewAreaRoot.setVisible(true);
			this.setShowNavigationView(true);
			this.firstStartUp = false;
			this.activateView(SystemDescriptionView.ID);
			this.navigationView.activateSystemDescription();
			this.navigationView.setPojectCreated();
			this.dataModelController.updateValue(ObserverValue.PROJECT_NAME);
		} else {
			if (this.overwriteDataModel()) {
				this.dataModelController = new DataModelController();
				this.setNewDataModel();
				this.savedFile = null;
			}
		}
		this.initControlStructure = false;
		this.initCSWithProcessModel = false;
		// close welcome site
		Welcome.shutWelcome();
	}
	
	/**
	 * Activates the given view
	 * 
	 * @param viewName the id of the view to activate.
	 * 
	 * @return returns whether the view has been activated.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	public boolean activateView(String viewName) {
		if ((viewName == null) || viewName.isEmpty()) {
			ViewContainer.LOGGER.error("No view name specified"); //$NON-NLS-1$
			return false;
		}
		
		IViewReference viewRef;
		
		viewRef = this.initializedViews.get(viewName);
		
		if (viewRef == null) {
			ViewContainer.LOGGER.error("ViewContainer: View \"" + viewName //$NON-NLS-1$
				+ "\" can not be found"); //$NON-NLS-1$
			return false;
		}
		
		// save the previous view
		if (this.activeView != null) {
			this.activeView.onCloseView();
		}
		
		// deactivate the old one
		if (this.activeView != null) {
			this.activeView.setVisible(false);
		}
		
		// refresh the new view
		if (viewName == CSEditorWithPM.ID) {
			
			((IControlStructureEditor) viewRef.viewInstance).initialSync(this.initControlStructure,
				this.initCSWithProcessModel);
			this.initCSWithProcessModel = true;
		}
		if (viewName == CSEditor.ID) {
			this.initControlStructure = true;
		}
		viewRef.onActivateView();
		
		// set the new view
		this.activeView = viewRef;
		this.activeView.setVisible(true);
		
		// set the new title
		this.setViewTitle(this.activeView.getTitle());
		ViewContainer.LOGGER.info("ViewContainer: View \"" + viewName //$NON-NLS-1$
			+ "\" activated"); //$NON-NLS-1$
		
		// update layout
		this.viewAreaRoot.layout();
		
		return true;
	}
	
	/**
	 * Shows or hides the navigation view.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param visible flag indicating whether the navigation view is visible.
	 */
	public void setShowNavigationView(boolean visible) {
		this.navigationRoot.setVisible(visible);
	}
	
	/**
	 * Sets the current title.
	 * 
	 * @param title the new title.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	protected void setViewTitle(String title) {
		this.titleLabel.setText(title);
		this.titleComposite.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(this.store,
				IPreferenceConstants.SPLITTER_BACKGROUND)));

		this.exportButton.setBackground(this.titleComposite.getBackground());
	}
	
	/**
	 * Saves the data model to a file
	 * 
	 * @author Fabian Toth
	 * 
	 * @return whether the operation was successful or not
	 */
	public boolean saveDataModelAs() {
		FileDialog fileDialog = new FileDialog(this.parentComposite.getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(new String[] {"*.haz"}); //$NON-NLS-1$
		fileDialog.setFilterNames(new String[] {"A-STPA project file (*.haz)"}); //$NON-NLS-1$
		fileDialog.setFileName(this.dataModelController.getProjectName());
		String fileName = fileDialog.open();
		if (fileName == null) {
			return false;
		}
		File file = new File(fileName);
		if (file.exists()) {
			boolean result =
				MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), Messages.ConfirmSaveAs,
					String.format(ViewContainer.OVERWRITE_MESSAGE, file.getName()));
			if (!result) {
				return false;
			}
		}
		this.savedFile = file;
		this.dataModelController.prepareForSave();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(DataModelController.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// Write to file
			m.marshal(this.dataModelController, this.savedFile);
		} catch (JAXBException e) {
			ViewContainer.LOGGER.error(e.getMessage(), e);
			return false;
		}
		this.dataModelController.setStored();
		return true;
	}
	
	/**
	 * Saves the data model to the file in the variable. If this is null
	 * saveDataModelAs() is called
	 * 
	 * @author Fabian Toth
	 * 
	 * @return whether the operation was successful or not
	 */
	public boolean saveDataModel() {
		if (this.savedFile == null) {
			return this.saveDataModelAs();
		}
		this.dataModelController.prepareForSave();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(DataModelController.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// Write to file
			m.marshal(this.dataModelController, this.savedFile);
		} catch (JAXBException e) {
			ViewContainer.LOGGER.error(e.getMessage(), e);
			return false;
		}
		this.dataModelController.setStored();
		return true;
	}
	
	/**
	 * Loads the data model from a file if it is valid
	 * 
	 * @author Fabian Toth
	 * @author Jarkko Heidenwag
	 * 
	 * @return whether the operation was successful or not
	 */
	public boolean loadDataModel() {
		if (this.overwriteDataModel()) {
			FileDialog fileDialog = new FileDialog(this.parentComposite.getShell(), SWT.OPEN);
			fileDialog.setFilterExtensions(new String[] {"*.haz"}); //$NON-NLS-1$
			fileDialog.setFilterNames(new String[] {"A-STPA project file (*.haz)"}); //$NON-NLS-1$
			try {
				JAXBContext context = JAXBContext.newInstance(DataModelController.class);
				Unmarshaller um = context.createUnmarshaller();
				String file = fileDialog.open();
				if (file != null) {
					this.savedFile = new File(file);
					
					// validate the file
					URL schemaFile = this.getClass().getResource("/hazschema.xsd"); //$NON-NLS-1$
					Source xmlFile = new StreamSource(this.savedFile);
					
					SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
					Schema schema = schemaFactory.newSchema(schemaFile);
					
					Validator validator = schema.newValidator();
					try {
						validator.validate(xmlFile);
					} catch (SAXException e) {
						// The .haz is invalid!
						MessageDialog.openInformation(this.getSite().getShell(), Messages.Information,
							Messages.ThisHazFileIsInvalid);
						ViewContainer.LOGGER.error(e.getMessage(), e);
						return false;
					} catch (IOException e) {
						ViewContainer.LOGGER.error(e.getMessage(), e);
						return false;
					}
					
					this.dataModelController = (DataModelController) um.unmarshal(new FileReader(file));
					this.setNewDataModel();
					Welcome.shutWelcome();
					
					// Show all views
					this.navigationRoot.setVisible(true);
					this.titleLabel.setVisible(true);
					this.viewAreaRoot.setVisible(true);
					this.setShowNavigationView(true);
					this.initControlStructure = true;
					this.initCSWithProcessModel = true;
					this.activateView(SystemDescriptionView.ID);
					this.navigationView.activateSystemDescription();
				}
			} catch (JAXBException | FileNotFoundException e) {
				ViewContainer.LOGGER.error(e.getMessage(), e);
				return false;
			} catch (SAXException e) {
				ViewContainer.LOGGER.error(e.getMessage(), e);
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Open wizard with export values.
	 * 
	 * @author Sebastian Sieber
	 * @param type 
	 * @return boolean true
	 * 
	 */
	public boolean openExportWizard(ExportConstants type) {
		Wizard wizard = null;
		switch(type){
		case PDF:
			wizard = new PdfExportWizard(
					this.dataModelController.getProjectName());
			break;
		case UCA_TABLE:
			wizard = new UCATableExportWizard();
			break;
		}
		// call wizard
		WizardDialog dialog =
			new WizardDialog(this.parentComposite.getShell(), wizard);
		dialog.open();
		return true;
	}
	
	
	/**
	 * Exports the PDF document.
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param filePath String
	 * @return whether exporting succeeded.
	 */
	public boolean exportFromXSL(String filePath){
		return export(filePath, org.apache.xmlgraphics.util.MimeConstants.MIME_PDF, "/fopxsl.xsl",false);//$NON-NLS-1$
	}
			
	/**
	 * Exports the PDF document.
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param filePath String
	 * @param id the if of the view which shall be exported
	 * @return whether exporting succeeded.
	 */
	public boolean export(String filePath, String id){
		IViewReference viewRef;
		viewRef = this.initializedViews.get(id);
	
		return viewRef.viewInstance.triggerExport(filePath);
	}
	/**
	 * calls the Export function in the given view.
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param filePath
	 *            String
	 * @param viewIds
	 *            specifies the view for which the export should be triggered
	 * @return whether exporting succeeded.
	 */
	public boolean exportViewData(String filePath, String[] viewIds) {
		
		IViewReference viewRef;
		boolean worked = true;
		String line;
		File tableCSV = new File(filePath);
		
		try(BufferedWriter csvWriter= new BufferedWriter(new FileWriter(tableCSV));) {
			for(String id: viewIds){
				viewRef = this.initializedViews.get(id);
				worked= viewRef.viewInstance.writeCSVData(csvWriter, ';');
			}	
			csvWriter.close();
			File imageFile= new File(filePath);
			if (imageFile.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(imageFile);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return worked;
		
	}
	
	/**
	 * Exports the PDF document.
	 * 
	 * @author Sebastian Sieber,Lukas Balzer
	 * 
	 * @param filePath String
	 * @param xslName the name of the file in which the xsl file is stored which should be used
	 * @param type the file type which shall be exported
	 * @param asOne true if all content shall be exported on a single page
	 * @return whether exporting succeeded.
	 */
	public boolean export(String filePath,String type,String xslName, boolean asOne) {
		this.dataModelController.prepareForExport();
		// put the xml jaxb content into an output stream
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		if (filePath != null) {
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(DataModelController.class);
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(this.dataModelController, outStream);
			} catch (JAXBException e) {
				ViewContainer.LOGGER.error(e.getMessage(), e);
				return false;
			}
		} else {
			ViewContainer.LOGGER.error("Report cannot be exported: Invalid file path"); //$NON-NLS-1$
			return false;
		}
		this.dataModelController.prepareForSave();
		// start the job, that exports the pdf from the JAXB stream
		Job exportJob = new ExportJob(Messages.ExportingPdf, filePath, outStream,type,xslName,asOne);
		exportJob.schedule();
		exportJob.addJobChangeListener(new ExportJobChangeAdapter());
		return true;
		
	}
	
	/**
	 * Checks if there are unsaved changes or not
	 * 
	 * @return whether there are unsaved changes or not
	 * 
	 * @author Fabian Toth
	 */
	public boolean getUnsavedChanges() {
		return this.dataModelController.hasUnsavedChanges();
	}
	
	/**
	 * Method to update the message of the status line
	 * 
	 * @param message the new message
	 * 
	 * @author Fabian Toth
	 */
	public void updateStatus(final String message) {
		final IActionBars bars = this.getViewSite().getActionBars();
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				bars.getStatusLineManager().setMessage(message);
			}
		});
	}
	
	/**
	 * Updates the message and the image of the status line
	 * 
	 * @param message the new message
	 * @param image the new image
	 * 
	 * @author Fabian Toth
	 */
	public void updateStatusWithImage(final String message, final Image image) {
		final IActionBars bars = this.getViewSite().getActionBars();
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				bars.getStatusLineManager().setMessage(image, message);
			}
		});
	}
	
	/**
	 * Updates all views when a new data model was created/loaded
	 * 
	 * @author Fabian Toth
	 * 
	 */
	private void setNewDataModel() {
		this.navigationView.setDataModelInterface(this.dataModelController);
		for (IViewReference view : this.initializedViews.values()) {
			view.setDataModel(this.dataModelController);
		}
		StatusLineManager.getInstance().setDataModelInterface(this.dataModelController);
		for (ObserverValue value : ObserverValue.values()) {
			this.dataModelController.updateValue(value);
		}
		
		this.activateView(SystemDescriptionView.ID);
		this.navigationView.activateSystemDescription();
		this.dataModelController.setStored();
	}
	
	/**
	 * Asks the user if the data model should be overwritten
	 * 
	 * @author Fabian Toth
	 * 
	 * @return true, if the data model should be overwritten
	 */
	private boolean overwriteDataModel() {
		if (!this.dataModelController.hasUnsavedChanges()) {
			return true;
		}
		MessageDialog dialog =
			new MessageDialog(Display.getCurrent().getActiveShell(), Messages.ASTPA, null,
				ViewContainer.DISCARD_MESSAGE, MessageDialog.CONFIRM, new String[] {Messages.Store, Messages.Discard,
					Messages.Abort}, 0);
		int resultNum = dialog.open();
		switch (resultNum) {
		case -1:
			return false;
		case 0:
			return this.saveDataModel();
		case 1:
			return true;
		case 2:
			return false;
		default:
			return false;
		}
	}
	
	/**
	 * Shows the welcome page and hides all other views.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public void showWelcomePage() {
		this.navigationRoot.setVisible(false);
		this.titleLabel.setVisible(false);
		this.setShowNavigationView(false);
		this.viewAreaRoot.setVisible(false);
		
		this.welcomeViewRoot.setVisible(true);
		this.welcomeView.onActivateView();
	}
	
	/**
	 * Hides the welcome page.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public void hideWelcomePage() {
		this.navigationRoot.setVisible(true);
		this.titleLabel.setVisible(true);
		this.setShowNavigationView(true);
		this.viewAreaRoot.setVisible(true);
		
		this.welcomeViewRoot.setVisible(false);
	}
	
	/**
	 * Calls the observer of the data model with the given value
	 * 
	 * @author Fabian Toth
	 * 
	 * @param value the value to call
	 */
	public void callObserverValue(ObserverValue value) {
		this.dataModelController.updateValue(value);
	}
	
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @return
	 * 		a Map with the view Titles mapped to their ids
	 */
	public Map<String, String> getInitializedViews(){
		Map<String,String> steps= new HashMap<>();
		for(String tmp: this.initializedViews.keySet().toArray(new String[0])){
			if(!tmp.equals(CSEditor.ID) && !tmp.equals(CSEditorWithPM.ID)){
				steps.put(tmp, this.initializedViews.get(tmp).getTitle());
			}
		}
		return steps;
	}
}

/**
 * Eclipse job that handles the export
 * 
 * @author Fabian Toth
 * 
 */
class ExportJob extends Job {
	
	private static final float MP_TO_INCH = 72270f;
	private static final Logger LOGGER = Logger.getRootLogger();
	private String filePath;
	private ByteArrayOutputStream outStream;
	private final  String fileType;
	private final String xslName;
	private final boolean asOne;
	
	/**
	 * Constructor of the export job
	 * 
	 * @author Fabian Toth, Lukas Balzer
	 * 
	 * @param name the name of the job
	 * @param filePath the path to the pdf file
	 * @param outStream the haz-xml as stream
	 * @param xslName the name of the file in which the xsl file is stored which should be used
	 * @param type the file type which shall be exported
	 * @param asOne true if all content shall be exported on a single page
	 */
	public ExportJob(String name, String filePath, ByteArrayOutputStream outStream, String type, String xslName, boolean asOne) {
		super(name);
		this.filePath = filePath;
		this.outStream = outStream;
		this.fileType = type;
		this.xslName = xslName;
		this.asOne = asOne;
	}
	 
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.ExportPdf, IProgressMonitor.UNKNOWN);
		
		FopFactory fopFactory = FopFactory.newInstance();
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		
		ByteArrayOutputStream pdfoutStream = new ByteArrayOutputStream();
		
		
		StreamSource informationSource = new StreamSource(new ByteArrayInputStream(this.outStream.toByteArray()));
		
		/**
		 * the xslfoTransormer is beeing related to the xsl which describes the pdf export
		 */
		Transformer xslfoTransformer;
		try {
			URL xslUrl = this.getClass().getResource(this.xslName);
			
			if (xslUrl == null) {
				ExportJob.LOGGER.error("Fop xsl file not found"); //$NON-NLS-1$
				return Status.CANCEL_STATUS;
			}
			
			StreamSource transformXSLSource = new StreamSource(xslUrl.openStream());
			
			File pdfFile = new File(this.filePath);
			if (!pdfFile.exists()) {
				pdfFile.createNewFile();
			}
			
			TransformerFactory transfact = TransformerFactory.newInstance();
			transfact.setURIResolver(
			        new URIResolver() {
			            @Override
						public Source resolve(String href, String base) {
			                return new StreamSource(this.getClass().getClassLoader().getResourceAsStream("/"+href)); //$NON-NLS-1$
			            }
			        });
			xslfoTransformer = transfact.newTransformer(transformXSLSource);
			
			try (OutputStream out = new BufferedOutputStream(new FileOutputStream(pdfFile));
				FileOutputStream str = new FileOutputStream(pdfFile);) {
				if(this.asOne){
					fopFactory.setPageHeight(getFirstDocumentSpan(xslfoTransformer));
				}
		        
				Fop fop;
				fop = fopFactory.newFop(this.fileType, foUserAgent, pdfoutStream);
				Result res = new SAXResult(fop.getDefaultHandler());
				
				//transform the informationSource with the transformXSLSource
				xslfoTransformer.transform(informationSource, res);
				
				str.write(pdfoutStream.toByteArray());
				str.close();
				if (pdfFile.exists()) {
					if (Desktop.isDesktopSupported()) {
						Desktop.getDesktop().open(pdfFile);
					}
				}
			} 
		} catch (SAXException | IOException | TransformerException e) {
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}
	
	private String getFirstDocumentSpan(Transformer xslTransformer)
		throws TransformerException, FOPException{
		FopFactory fopFactory = FopFactory.newInstance();
		FOUserAgent userAgent = fopFactory.newFOUserAgent();
		StreamSource informationSource = new StreamSource(new ByteArrayInputStream(this.outStream.toByteArray()));	
		ByteArrayOutputStream areaTreeStream = new ByteArrayOutputStream();
		Fop fop;
		
		//creae a new fop as areaTree
		fop = fopFactory.newFop(MimeConstants.MIME_FOP_AREA_TREE, userAgent, areaTreeStream);
		Result areaTreeResult = new SAXResult(fop.getDefaultHandler());
	
		//transform the informationSource with the transformXSLSource
		//the areaTreeResult is a complete description of the export dokument
		xslTransformer.transform(informationSource, areaTreeResult);
		
		StreamSource treeSource= new StreamSource(new ByteArrayInputStream(areaTreeStream.toByteArray()));
		
		AreaTreeModel treeModel=new AreaTreeModel();
		AreaTreeParser areaTreeParser= new AreaTreeParser();
		areaTreeParser.parse(treeSource, treeModel, userAgent);
		Span span=(Span) treeModel.getCurrentPageSequence().getPage(0).getBodyRegion().getMainReference().getSpans().get(0);
		float pageHeight= span.getBPD()/MP_TO_INCH;
		return Float.toString(pageHeight) + "in";//$NON-NLS-1$
		
		
	}
	
	
}
