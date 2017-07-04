package acast.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import acast.Activator;
import acast.export.ExportInformation;
import acast.jobs.SaveJob;
import acast.model.hazacc.HazController;
import acast.model.interfaces.IAccidentDescriptionViewDataModel;
import acast.model.interfaces.IProximalEventsViewDataModel;
import acast.model.interfaces.IResponsibilityDataModel;
import acast.ui.accidentDescription.ProximalEvent;
import acast.ui.accidentDescription.ProximalEventsController;
import acast.ui.accidentDescription.Responsibility;
import messages.Messages;
import xstampp.astpa.model.controlaction.ControlAction;
import xstampp.astpa.model.controlaction.ControlActionController;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlstructure.ControlStructureController;
import xstampp.astpa.model.controlstructure.components.Anchor;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.components.ConnectionType;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.hazacc.Hazard;
import xstampp.astpa.model.interfaces.IControlActionViewDataModel;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.astpa.model.interfaces.IHazardViewDataModel;
import xstampp.astpa.model.interfaces.ISafetyConstraintViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.sds.SDSController;
import xstampp.astpa.model.sds.SafetyConstraint;
import xstampp.model.AbstractDataModel;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;

@XmlRootElement(namespace = "acast.model")
public class Controller extends AbstractDataModel implements IDataModel,
		IAccidentDescriptionViewDataModel, IHazardViewDataModel,
		ISafetyConstraintViewDataModel, IControlActionViewDataModel,
		IControlStructureEditorDataModel, IProximalEventsViewDataModel,
		IResponsibilityDataModel {

	private static final Logger LOGGER = ProjectManager.getLOGGER();

	@XmlAttribute(name = "acastversion")
	private String acastVersion;

	@XmlElement(name = "exportinformation")
	private ExportInformation exportInformation;

	@XmlElement(name = "projectdata")
	private ProjectDataController projectDataManager;

	@XmlElement(name = "proxEvents")
	private ProximalEventsController proximalEventsController;

	@XmlElement(name = "haz")
	private HazController hazController;

	@XmlElement(name = "cac")
	private ControlActionController controlActionController;

	@XmlElement(name = "sds")
	private SDSController sdsController;

	@XmlElement(name = "controlstructure")
	private ControlStructureController controlStructureController;

	@XmlElement(name = "crc")
	private ResponsibilityController respController;

	@XmlAttribute(name="projectId")
  private UUID projectId;


	public Controller() {
		super();
		this.projectDataManager = new ProjectDataController();
		this.hazController = new HazController();
		this.controlActionController = new ControlActionController();
		this.sdsController = new SDSController();
		this.controlStructureController = new ControlStructureController();
		this.proximalEventsController = new ProximalEventsController();
		this.respController = new ResponsibilityController();
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		
		if (bundle != null) {
			Dictionary<?, ?> dictionary = bundle.getHeaders();
			String versionWithQualifier = (String) dictionary
					.get(Messages.BundleVersion);
			this.acastVersion = versionWithQualifier.substring(0,
					versionWithQualifier.lastIndexOf('.'));
		}

	}
  
	@Override
	public Job doSave(File file, Logger log, boolean isUIcall) {
		SaveJob job = new SaveJob(file, this);
		return job;
	}

	@Override
	public boolean prepareForExport() {

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				Controller.this.exportInformation = new ExportInformation();
				Controller.this.updateValue(ObserverValue.SAVE);
				Controller.this.updateValue(ObserverValue.EXPORT);
			}
		});
		return true;

	}

	@Override
	public void prepareForSave() {
	  this.controlActionController.prepareForSave(null,null);
		this.exportInformation = null;
		this.updateValue(ObserverValue.SAVE);
	}

	@Override
	public void updateValue(ObserverValue value) {
		Controller.LOGGER.debug("Trigger update for " + value.name()); //$NON-NLS-1$
		this.setChanged();
		this.notifyObservers(value);
	}

	@XmlTransient
	@Override
	public String getProjectName() {
		return this.projectDataManager.getProjectName();
	}


	@Override
	public boolean setProjectName(String projectName) {
		if (projectName == null) {
			return false;
		}
		if(!this.projectDataManager.getProjectName().equals(projectName)){
			this.projectDataManager.setProjectName(projectName);
			this.setUnsavedAndChanged(ObserverValue.PROJECT_NAME);
		}
		return true;
	}

	
	@XmlTransient
	@Override
	public String getAccidentDescription() {
		return this.projectDataManager.getAccidentDescription();
	}

	@Override
	public boolean setAccidentDescription(String accidentDescription) {
		if (accidentDescription == null) {
			return false;
		}
		this.projectDataManager.setAccidentDescription(accidentDescription);
		this.setUnsavedAndChanged(ObserverValue.PROJECT_DESCRIPTION);
		return true;
	}

	@Override
	public void initializeProject() {
		this.controlStructureController.initializeCSS();
	}

	@Override
	public String getFileExtension() {
		return "acc";
	}

	@Override
	public String getPluginID() {
		return Activator.PLUGIN_ID;
	}

	@Override
	public boolean addStyleRange(StyleRange styleRange) {
		if (styleRange == null) {
			return false;
		}
		boolean result = this.projectDataManager.addStyleRange(styleRange);
		return result;
	}

	@Override
	public List<StyleRange> getStyleRanges() {
		return this.projectDataManager.getStyleRanges();
	}

	@Override
	public StyleRange[] getStyleRangesAsArray() {
		return this.projectDataManager.getStyleRangesAsArray();
	}

	@Override
	public List<ITableModel> getAllHazards() {
		return this.hazController.getAllHazards();
	}

	@Override
	public UUID addHazard(String title, String description) {
		if ((title == null) || (description == null)) {
			return null;
		}
		UUID id = this.hazController.addHazard(title, description);
		this.setUnsavedAndChanged(ObserverValue.HAZARD);
		return id;
	}

	@Override
	public boolean removeHazard(UUID hazardId) {
		if (hazardId == null) {
			return false;
		}
		if (!(this.hazController.getHazard(hazardId) instanceof Hazard)) {
			return false;
		}
		// TODO
		// this.causalFactorController.removeAllLinks(hazardId);
		// this.controlActionController.removeAllLinks(hazardId);
		boolean result = this.hazController.removeHazard(hazardId);
		this.setUnsavedAndChanged(ObserverValue.HAZARD);
		return result;
	}

	@Override
	public boolean setHazardTitle(UUID hazardId, String title) {
		if ((title == null) || (hazardId == null)) {
			return false;
		}
		if (!(this.hazController.getHazard(hazardId) instanceof Hazard)) {
			return false;
		}

		((Hazard) this.hazController.getHazard(hazardId)).setTitle(title);
		this.setUnsavedAndChanged(ObserverValue.HAZARD);
		return true;
	}

	@Override
	public boolean setHazardDescription(UUID hazardId, String description) {
		if ((description == null) || (hazardId == null)) {
			return false;
		}
		if (!(this.hazController.getHazard(hazardId) instanceof Hazard)) {
			return false;
		}
		((Hazard) this.hazController.getHazard(hazardId))
				.setDescription(description);
		this.setUnsavedAndChanged(ObserverValue.HAZARD);
		return true;
	}

	@Override
	public ITableModel getHazard(UUID hazardId) {
		if (hazardId == null) {
			return null;
		}
		ITableModel hazard = this.hazController.getHazard(hazardId);
		if (!(hazard instanceof Hazard)) {
			return null;
		}
		return hazard;
	}

	@Override
	public List<ITableModel> getLinkedAccidents(UUID hazardId) {
		return null;
	}

	@Override
	public UUID addComponent(UUID parentId, Rectangle layout, String text,
			ComponentType type, Integer index) {
		if ((parentId == null) || (layout == null) || (text == null)
				|| (type == null)) {
			return null;
		}
		if (!(this.getComponent(parentId) instanceof Component)) {
			return null;
		}
		UUID result = this.controlStructureController.addComponent(parentId,
				layout, text, type, index);
		if (type != ComponentType.CONTROLACTION
				&& type != ComponentType.CONTAINER
				&& type != ComponentType.TEXTFIELD) {
			this.respController.addComponentName(text, result);
		}
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public UUID addComponent(UUID controlActionId, UUID parentId,
			Rectangle layout, String text, ComponentType type, Integer index) {
		if ((parentId == null) || (layout == null) || (text == null)
				|| (type == null)) {
			return null;
		}
		if (!(this.getComponent(parentId) instanceof Component)) {
			return null;
		}

		UUID result = this.controlStructureController.addComponent(
				controlActionId, parentId, layout, text, type, index);
		if (type != ComponentType.CONTROLACTION
				&& type != ComponentType.CONTAINER
				&& type != ComponentType.TEXTFIELD) {
			this.respController.addComponentName(text, result);
			PlatformUI.getPreferenceStore().firePropertyChangeEvent(
					"currentSelection", text, "add");
		}
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public boolean removeControlAction(UUID controlActionId) {
		if (controlActionId == null) {
			return false;
		}
		if (!(this.controlActionController.getControlAction(controlActionId) instanceof ControlAction)) {
			return false;
		}

		boolean result = this.controlActionController
				.removeControlAction(controlActionId);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_ACTION);
		return result;
	}

	@Override
	public UUID setRoot(Rectangle layout, String text) {
		if ((layout == null) || (text == null)) {
			return null;
		}

		UUID result = this.controlStructureController.setRoot(layout, text);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public UUID addControlAction(String title, String description) {
		if ((title == null) || (description == null)) {
			return null;
		}

		UUID id = this.controlActionController.addControlAction(title,
				description);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_ACTION);
		return id;
	}

	@Override
	public boolean setControlActionTitle(UUID controlActionId, String title) {
		if ((controlActionId == null) || (title == null)) {
			return false;
		}
		if (!(this.controlActionController.getControlAction(controlActionId) instanceof ControlAction)) {
			return false;
		}

		((ControlAction) this.controlActionController
				.getControlAction(controlActionId)).setTitle(title);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_ACTION);
		return true;
	}

	@Override
	public List<IControlAction> getAllControlActions() {
		List<IControlAction> result = new ArrayList<>();
		for (IControlAction controlAction : this.controlActionController.getAllControlActionsU()) {
			result.add(controlAction);
		}
		return result;
	}

	@Override
	public xstampp.astpa.model.interfaces.ITableModel getControlAction(UUID controlActionId) {
		if (controlActionId == null) {
			return null;
		}
		if (!(this.controlActionController.getControlAction(controlActionId) instanceof ControlAction)) {
			return null;
		}

		return this.controlActionController.getControlAction(controlActionId);
	}

	@Override
	public boolean changeComponentLayout(UUID componentId, Rectangle layout,
			boolean step1) {
		if ((componentId == null) || (layout == null)) {
			return false;
		}

		boolean result = this.controlStructureController.changeComponentLayout(
				componentId, layout, step1);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public boolean synchronizeLayouts() {
		if (this.getRoot() == null) {
			return false;
		}
		return this.controlStructureController.sychronizeLayout();
	}

	@Override
	public boolean changeComponentText(UUID componentId, String text) {
		if ((componentId == null) || (text == null)) {
			return false;
		}
		ComponentType type = this.controlStructureController.getComponent(
				componentId).getComponentType();
		if (type != ComponentType.CONTROLACTION
				&& type != ComponentType.CONTAINER
				&& type != ComponentType.TEXTFIELD) {
			if (this.respController.getComponentNames().containsKey(text)) {
				text = text + " (2)";
			} else {
				this.respController
						.removeComponentName(this.controlStructureController
								.getComponent(componentId).getText());
			}
			this.respController.addComponentName(text, componentId);

			PlatformUI.getPreferenceStore().firePropertyChangeEvent(
					"currentSelection", text, "change");
		}
		boolean result = this.controlStructureController.changeComponentText(
				componentId, text);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public boolean removeComponent(UUID componentId) {
		if ((componentId == null)) {
			return false;
		}
		ComponentType type = this.controlStructureController.getComponent(
				componentId).getComponentType();
		if (type != ComponentType.CONTROLACTION
				&& type != ComponentType.CONTAINER
				&& type != ComponentType.TEXTFIELD) {
			this.respController
					.removeComponentName(this.controlStructureController
							.getComponent(componentId).getText());
			PlatformUI.getPreferenceStore().firePropertyChangeEvent(
					"currentSelection",
					this.controlStructureController.getComponent(componentId)
							.getText(), "remove");
		}
		boolean result = this.controlStructureController
				.removeComponent(componentId);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public boolean recoverComponent(UUID parentId, UUID componentId) {
		if ((parentId == null) || (componentId == null)) {
			return false;
		}
		boolean result = this.controlStructureController.recoverComponent(
				parentId, componentId);
		this.respController.addComponentName(this.controlStructureController
				.getComponent(componentId).getText(), componentId);
		PlatformUI.getPreferenceStore().firePropertyChangeEvent(
				"currentSelection",
				this.controlStructureController.getComponent(componentId)
						.getText(), "add");
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public boolean recoverControlAction(UUID id) {
		return this.controlActionController.recoverControlAction(id);
	}

	@Override
	public IRectangleComponent getComponent(UUID componentId) {
		if ((componentId == null)) {
			return null;
		}

		return this.controlStructureController.getComponent(componentId);
	}

	@Override
	public IRectangleComponent getRoot() {
		if (this.controlStructureController.getRoot() == null) {
			this.controlStructureController.setRoot(new Rectangle(),
					new String());
		}
		return this.controlStructureController.getRoot();
	}

	@Override
	public UUID addConnection(Anchor sourceAnchor, Anchor targetAnchor,
			ConnectionType connectionType) {
		if ((sourceAnchor == null) || (targetAnchor == null)
				|| (connectionType == null)) {
			return null;
		}

		UUID result = this.controlStructureController.addConnection(
				sourceAnchor, targetAnchor, connectionType);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public boolean changeConnectionType(UUID connectionId,
			ConnectionType connectionType) {
		if ((connectionId == null) || (connectionType == null)) {
			return false;
		}

		boolean result = this.controlStructureController.changeConnectionType(
				connectionId, connectionType);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public boolean changeConnectionTarget(UUID connectionId, Anchor targetAnchor) {
		if ((connectionId == null) || (targetAnchor == null)) {
			return false;
		}

		boolean result = this.controlStructureController
				.changeConnectionTarget(connectionId, targetAnchor);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public boolean changeConnectionSource(UUID connectionId, Anchor sourceAnchor) {
		if ((connectionId == null) || (sourceAnchor == null)) {
			return false;
		}

		boolean result = this.controlStructureController
				.changeConnectionSource(connectionId, sourceAnchor);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public boolean removeConnection(UUID connectionId) {
		if (connectionId == null) {
			return false;
		}

		boolean result = this.controlStructureController
				.removeConnection(connectionId);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public boolean recoverConnection(UUID connectionId) {
		if (connectionId == null) {
			return false;
		}
		boolean result = this.controlStructureController
				.recoverConnection(connectionId);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}

	@Override
	public IConnection getConnection(UUID connectionId) {
		if (connectionId == null) {
			return null;
		}

		return this.controlStructureController.getConnection(connectionId);
	}

	@Override
	public List<IConnection> getConnections() {
		return this.controlStructureController.getConnections();
	}

	@Override
	public boolean setCSImagePath(String path) {
		Image img = new Image(null, path);
		File imgFile = new File(path);
		this.exportInformation
				.setCsImageWidth(String.valueOf(img.getBounds().width));
		this.exportInformation
				.setCsImageHeight(String.valueOf(img.getBounds().height));
		return this.exportInformation
				.setCsImagePath(imgFile.toURI().toString());
	}

	@Override
	public boolean setCSPMImagePath(String path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getComponentTrashSize() {
		return this.controlStructureController.getComponentTrashSize();
	}

	@Override
	public int getConnectionTrashSize() {
		return this.controlStructureController.getConnectionTrashSize();
	}

	@Override
	public List<ITableModel> getAllSafetyConstraints() {
		return this.sdsController.getAllSafetyConstraints();
	}

	@Override
	public UUID addSafetyConstraint(String title, String description) {
		if ((title == null) || (description == null)) {
			return null;
		}

		UUID id = this.sdsController.addSafetyConstraint(title, description, null);
		this.setUnsavedAndChanged(ObserverValue.SAFETY_CONSTRAINT);
		return id;
	}

	@Override
	public boolean removeSafetyConstraint(UUID safetyConstraintId) {
		if (safetyConstraintId == null) {
			return false;
		}
		if (!(this.sdsController.getSafetyConstraint(safetyConstraintId) instanceof SafetyConstraint)) {
			return false;
		}

		boolean result = this.sdsController
				.removeSafetyConstraint(safetyConstraintId);
		this.setUnsavedAndChanged(ObserverValue.SAFETY_CONSTRAINT);
		return result;
	}

	@Override
	public boolean setSafetyConstraintTitle(UUID safetyConstraintId,
			String title) {
		if ((title == null) || (safetyConstraintId == null)) {
			return false;
		}
		if (!(this.sdsController.getSafetyConstraint(safetyConstraintId) instanceof SafetyConstraint)) {
			return false;
		}

		((SafetyConstraint) this.sdsController
				.getSafetyConstraint(safetyConstraintId)).setTitle(title);
		this.setUnsavedAndChanged(ObserverValue.SAFETY_CONSTRAINT);
		return true;
	}

	@Override
	public boolean setSafetyConstraintDescription(UUID safetyConstraintId,
			String description) {
		if ((description == null) || (safetyConstraintId == null)) {
			return false;
		}
		if (!(this.sdsController.getSafetyConstraint(safetyConstraintId) instanceof SafetyConstraint)) {
			return false;
		}

		((SafetyConstraint) this.sdsController
				.getSafetyConstraint(safetyConstraintId))
				.setDescription(description);
		this.setUnsavedAndChanged(ObserverValue.SAFETY_CONSTRAINT);
		return true;
	}

	@Override
	public ITableModel getSafetyConstraint(UUID safetyConstraintId) {
		if (safetyConstraintId == null) {
			return null;
		}
		if (!(this.sdsController.getSafetyConstraint(safetyConstraintId) instanceof SafetyConstraint)) {
			return null;
		}

		return this.sdsController.getSafetyConstraint(safetyConstraintId);
	}

	@XmlTransient
	@Override
	public String getAccidentCompany() {
		return this.projectDataManager.getAccidentCompany();
	}

	@XmlTransient
	@Override
	public String getAccidentLocation() {
		return this.projectDataManager.getAccidentLocation();
	}

	@XmlTransient
	@Override
	public String getAccidentDate() {
		return this.projectDataManager.getAccidentDate();
	}

	@Override
	public boolean setAccidentCompany(String company) {
		if (company == null) {
			return false;
		}
		this.projectDataManager.setAccidentCompany(company);
		// TODO SET OBSERVER
		this.setUnsavedAndChanged(ObserverValue.PROJECT_DESCRIPTION);
		return true;
	}

	@Override
	public boolean setAccidentLocation(String Location) {
		if (Location == null) {
			return false;
		}
		this.projectDataManager.setAccidentLocation(Location);
		// TODO SET OBSERVER
		this.setUnsavedAndChanged();
		return true;
	}

	@Override
	public boolean setAccidentDate(String Date) {
		if (Date == null) {
			return false;
		}
		this.projectDataManager.setAccidentDate(Date);
		// TODO SET OBSERVER
		this.setUnsavedAndChanged();
		return true;
	}

	@Override
	public boolean setPictureList(String[] pictureList) {
		if (pictureList == null) {
			return false;
		}
		boolean result = this.projectDataManager.setPictureList(pictureList);
		this.setUnsavedAndChanged();
		return result;
	}

	@Override
	public List<String> getPictureList() {
		return this.projectDataManager.getPictureList();
	}

	@XmlTransient
	@Override
	public String getCurrentPicture() {
		return this.projectDataManager.getCurrentPicture();
	}

	@Override
	public boolean setCurrentPicture(String picture) {
		if (picture == null) {
			return false;
		}
		this.projectDataManager.setCurrentPicture(picture);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
		return true;
	}

	@Override
	public List<ProximalEvent> getEventList() {
		return this.proximalEventsController.getEventList();
	}

	@Override
	public ProximalEvent getEvent(int id) {
		return this.proximalEventsController.getEvent(id);
	}

	@XmlTransient
	@Override
	public boolean setEventList(List<ProximalEvent> list) {
		if (list == null) {
			return false;
		}
		boolean result = this.proximalEventsController.setEventList(list);
		// TODO CHANGE OBSERVER
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
		return result;
	}

	@Override
	public void setRelativeOfComponent(UUID componentId, UUID relativeId) {
		this.controlStructureController.setRelativeOfComponent(componentId,
				relativeId);
		updateValue(ObserverValue.CONTROL_STRUCTURE);
		updateValue(ObserverValue.UNSAVED_CHANGES);

	}

	@Override
	public boolean setSafetyCritical(UUID componentId, boolean isSafetyCritical) {
		return this.controlStructureController.setSafetyCritical(componentId,
				isSafetyCritical);

	}

	@Override
	public boolean addUnsafeProcessVariable(UUID componentId, UUID variableID) {
		return this.controlStructureController.addUnsafeProcessVariable(
				componentId, variableID);
	}

	@Override
	public boolean removeUnsafeProcessVariable(UUID componentId, UUID variableID) {
		return this.controlStructureController.removeUnsafeProcessVariable(
				componentId, variableID);
	}

	@Override
	public Map<IRectangleComponent, Boolean> getRelatedProcessVariables(
			UUID componentId) {
		return this.controlStructureController
				.getRelatedProcessVariables(componentId);
	}

	@Override
	public boolean linkControlAction(UUID caId, UUID componentId) {
		return this.controlActionController.setComponentLink(componentId, caId);

	}



	@Override
	public boolean isCSComponentSafetyCritical(UUID componentId) {
		return this.controlStructureController.isSafetyCritical(componentId);
	}

	@Override
	public void setCSComponentComment(UUID componentId, String comment) {
		this.controlStructureController.setComment(componentId, comment);
	}

	@Override
	public boolean setControlActionDescription(UUID controlActionId,
			String description) {
		if ((controlActionId == null) || (description == null)) {
			return false;
		}
		if (!(this.controlActionController.getControlAction(controlActionId) instanceof ControlAction)) {
			return false;
		}

		((ControlAction) this.controlActionController
				.getControlAction(controlActionId)).setDescription(description);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_ACTION);
		return true;
	}

	@Override
	public List<Responsibility> getResponsibilitiesListforComponent(UUID id) {
		return this.respController.getResponsibilitiesListforComponent(id);
	}

	@Override
	public List<Responsibility> getContextListforComponent(UUID id) {
		return this.respController.getContextListforComponent(id);
	}

	@Override
	public List<Responsibility> getFlawListforComponent(UUID id) {
		return this.respController.getFlawListforComponent(id);
	}

	@Override
	public List<Responsibility> getUnsafeActionListforComponent(UUID id) {
		return this.respController.getUnsafeActionListforComponent(id);
	}

	@Override
	public List<Responsibility> getRecommendationListforComponent(UUID id) {
		return this.respController.getRecommendationListforComponent(id);
	}

	@Override
	public List<Responsibility> getResponsibilitiesList() {
		return this.respController.getResponsibilitiesList();
	}

	@Override
	public Responsibility getResponsibility(UUID ident, String id) {
		return this.respController.getResponsibility(ident, id);
	}

	@Override
	public void addResponsibility(UUID ident, String id, String description,
			String name) {
		this.respController.addResponsibility(ident, id, description, name);

	}

	@Override
	public void changeResponsibility(UUID ident, String oldId, String newId,
			String newDescription, String name) {
		this.respController.changeResponsibility(ident, oldId, newId,
				newDescription, name);
		ProjectManager.getLOGGER().debug("Changed responsibility");
	}

	@Override
	public void removeResponsibility(UUID ident, String id) {
		this.respController.removeResponsibility(ident, id);

	}

	@Override
	public List<Responsibility> getContextList() {
		return this.respController.getContextList();
	}

	@Override
	public Responsibility getContext(UUID ident, String id) {
		return this.respController.getContext(ident, id);
	}

	@Override
	public void addContext(UUID ident, String id, String description,
			String name) {
		this.respController.addContext(ident, id, description, name);

	}

	@Override
	public void changeContext(UUID ident, String oldId, String newId,
			String newDescription, String name) {
		this.respController.changeContext(ident, oldId, newId, newDescription,
				name);

	}

	@Override
	public void removeContext(UUID ident, String id) {
		this.respController.removeContext(ident, id);

	}

	@Override
	public List<Responsibility> getFlawsList() {
		return this.respController.getFlawsList();
	}

	@Override
	public Responsibility getFlaw(UUID ident, String id) {
		return this.respController.getFlaw(ident, id);
	}

	@Override
	public void addFlaw(UUID ident, String id, String description, String name) {
		this.respController.addFlaw(ident, id, description, name);
	}

	@Override
	public void changeFlaw(UUID ident, String oldId, String newId,
			String newDescription, String name) {
		this.respController.changeFlaw(ident, oldId, newId, newDescription,
				name);

	}

	@Override
	public void removeFlaw(UUID ident, String id) {
		this.respController.removeFlaw(ident, id);

	}

	@Override
	public List<Responsibility> getUnsafeActionsList() {
		return this.respController.getUnsafeActionsList();
	}

	@Override
	public Responsibility getUnsafeAction(UUID ident, String id) {
		return this.respController.getUnsafeAction(ident, id);
	}

	@Override
	public void addUnsafeAction(UUID ident, String id, String description,
			String name) {
		this.respController.addUnsafeAction(ident, id, description, name);

	}

	@Override
	public void changeUnsafeAction(UUID ident, String oldId, String newId,
			String newDescription, String name) {
		this.respController.changeUnsafeAction(ident, oldId, newId,
				newDescription, name);

	}

	@Override
	public void removeunsafeAction(UUID ident, String id) {
		this.respController.removeunsafeAction(ident, id);
	}

	@Override
	public List<Responsibility> getRecommendationList() {
		return this.respController.getRecommendationList();
	}

	@Override
	public Responsibility getRecommendation(UUID ident, String id) {
		return this.respController.getRecommendation(ident, id);
	}

	@Override
	public void addRecommendation(UUID ident, String id, String description,
			String name) {
		this.respController.addRecommendation(ident, id, description, name);

	}

	@Override
	public void changeRecommendation(UUID ident, String oldId, String newId,
			String newDescription, String name) {
		this.respController.changeRecommendation(ident, oldId, newId,
				newDescription, name);

	}

	@Override
	public void removeRecommendation(UUID ident, String id) {
		this.respController.removeRecommendation(ident, id);
	}

	@Override
	public void addComponentName(String key, UUID value) {
		this.respController.addComponentName(key, value);

	}

	@Override
	public void removeComponentName(String key) {
		this.respController.removeComponentName(key);

	}

	@Override
	public Map<String, UUID> getComponentNames() {
		return this.respController.getComponentNames();
	}

	@Override
	public List<Responsibility> getFeedbackList() {
		return this.respController.getFeedbackList();
	}

	@Override
	public Responsibility getFeedback(UUID ident, String id) {
		return this.respController.getFeedback(ident, id);
	}

	@Override
	public void addFeedback(UUID ident, String id, String description,
			String name) {
		this.respController.addFeedback(ident, id, description, name);

	}

	@Override
	public void changeFeedback(UUID ident, String oldId, String newId,
			String newDescription, String name) {
		this.respController.changeFeedback(ident, oldId, newId, newDescription,
				name);

	}

	@Override
	public void removeFeedback(UUID ident, String id) {
		this.respController.removeFeedback(ident, id);

	}

	@Override
	public List<Responsibility> getCoordinationList() {
		return this.respController.getCoordinationList();
	}

	@Override
	public Responsibility getCoordination(UUID ident, String id) {
		return this.respController.getCoordination(ident, id);
	}

	@Override
	public void addCoordination(UUID ident, String id, String description,
			String name) {
		this.respController.addCoordination(ident, id, description, name);

	}

	@Override
	public void changeCoordination(UUID ident, String oldId, String newId,
			String newDescription, String name) {
		this.respController.changeCoordination(ident, oldId, newId,
				newDescription, name);

	}

	@Override
	public void removeCoordination(UUID ident, String id) {
		this.respController.removeCoordination(ident, id);
	}

	@Override
	public List<Responsibility> getFeedbackListforComponent(UUID id) {
		return this.respController.getFeedbackListforComponent(id);
	}

	@Override
	public List<Responsibility> getCoordinationListforComponent(UUID id) {
		return this.respController.getCoordinationListforComponent(id);
	}

	@Override
	public void lockUpdate() {
		//method not used at the moment
		
	}

	@Override
	public void releaseLockAndUpdate(ObserverValue[] value) {
		//method not used at the moment
	}

	@Override
	public boolean moveEntry(boolean allWay, boolean moveUp, UUID id, ObserverValue value) {
		// TODO Auto-generated method stub
		return false;
	}

  @Override
  public void setActiveRoot(UUID rootId) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<IRectangleComponent> getRoots() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UUID getProjectId() {
    if (this.projectId == null) {
      this.projectId = UUID.randomUUID();
    }
    return this.projectId;
  }

  @Override
  public void initializeProject(IDataModel original) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public <T> T getAdapter(Class<T> clazz) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public LinkController getLinkController() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ControlStructureController getControlStructureController() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean setSeverity(Object entry, Severity severity) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isUseSeverity() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean setUseSeverity(boolean useSeverity) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public List<ITableModel> getHazards(List<UUID> ids) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<ITableModel> getAllAccidents() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ITableModel getAccident(UUID accidentId) {
    // TODO Auto-generated method stub
    return null;
  }

	


}
