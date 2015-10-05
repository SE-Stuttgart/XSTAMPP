package acast.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import messages.Messages;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import acast.Activator;
import acast.export.ExportInformation;
import acast.jobs.SaveJob;
import acast.model.ITableModel;
import acast.model.causalfactor.ICausalComponent;
import acast.model.controlaction.ControlAction;
import acast.model.controlaction.ControlActionController;
import acast.model.controlaction.interfaces.IControlAction;
import acast.model.controlaction.interfaces.IHAZXControlAction;
import acast.model.controlstructure.ControlStructureController;
import acast.model.controlstructure.Responsibility;
import acast.model.controlstructure.components.Anchor;
import acast.model.controlstructure.components.Component;
import acast.model.controlstructure.components.ComponentType;
import acast.model.controlstructure.components.ConnectionType;
import acast.model.controlstructure.interfaces.IConnection;
import acast.model.controlstructure.interfaces.IRectangleComponent;
import acast.model.hazacc.HazController;
import acast.model.hazacc.Hazard;
import acast.model.interfaces.IAccidentDescriptionViewDataModel;
import acast.model.interfaces.IControlStructureEditorDataModel;
import acast.model.interfaces.IHazardViewDataModel;
import acast.model.interfaces.IProximalEventsViewDataModel;
import acast.model.interfaces.ISafetyConstraintViewDataModel;
import acast.model.sds.SDSController;
import acast.model.sds.SafetyConstraint;
import acast.ui.accidentDescription.ProximalEvent;
import acast.ui.accidentDescription.ProximalEventsController;
import acast.ui.accidentDescription.Recommendation;

@XmlRootElement(namespace = "acast.model")
public class Controller extends Observable implements IDataModel,
		IAccidentDescriptionViewDataModel, IHazardViewDataModel,
		ISafetyConstraintViewDataModel, IControlStructureEditorDataModel,
		IProximalEventsViewDataModel {

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

	/**
	 * Shows if there are unsaved changes or not
	 */
	private boolean unsavedChanges;

	public Controller() {
		this.projectDataManager = new ProjectDataController();
		this.hazController = new HazController();
		this.controlActionController = new ControlActionController();
		this.sdsController = new SDSController();
		this.controlStructureController = new ControlStructureController();
		this.proximalEventsController = new ProximalEventsController();
		this.unsavedChanges = false;
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
		this.exportInformation = null;
		this.updateValue(ObserverValue.SAVE);
	}

	@Override
	public void updateValue(ObserverValue value) {
		Controller.LOGGER.debug("Trigger update for " + value.name()); //$NON-NLS-1$
		this.setChanged();
		int c = this.countObservers();
		this.notifyObservers(value);
	}

	@XmlTransient
	@Override
	public String getProjectName() {
		return this.projectDataManager.getProjectName();
	}

	@Override
	public void setStored() {
		this.unsavedChanges = false;
		this.setChanged();
		this.notifyObservers(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public boolean hasUnsavedChanges() {
		return this.unsavedChanges;
	}

	@Override
	public boolean setProjectName(String projectName) {
		if (projectName == null) {
			return false;
		}
		this.projectDataManager.setProjectName(projectName);
		this.setUnsavedAndChanged(ObserverValue.PROJECT_NAME);
		return true;
	}

	private void setUnsavedAndChanged(ObserverValue value) {
		this.unsavedChanges = true;
		this.updateValue(value);
		this.updateValue(ObserverValue.UNSAVED_CHANGES);
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
		return this.controlActionController.getAllControlActionsU();
	}

	@Override
	public ITableModel getControlAction(UUID controlActionId) {
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

		boolean result = true;
		for (IRectangleComponent child : this.getRoot().getChildren()) {
			result = result
					&& this.controlStructureController.sychronizeLayout(child
							.getId());
		}
		return result;
	}

	@Override
	public boolean changeComponentText(UUID componentId, String text) {
		if ((componentId == null) || (text == null)) {
			return false;
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

		UUID id = this.sdsController.addSafetyConstraint(title, description);
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
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
		return true;
	}

	@Override
	public boolean setAccidentDate(String Date) {
		if (Date == null) {
			return false;
		}
		this.projectDataManager.setAccidentDate(Date);
		// TODO SET OBSERVER
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
		return true;
	}

	@Override
	public boolean setPictureList(String[] pictureList) {
		if (pictureList == null) {
			return false;
		}
		boolean result = this.projectDataManager.setPictureList(pictureList);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
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
	public List<ICausalComponent> getCasualComponents() {
		return this.controlStructureController.getCausalComponents();
	}

	@Override
	public Responsibility getResponsibility(UUID ident, String id) {
		return this.controlStructureController.getResponsibility(ident, id);
	}

	@Override
	public void addResponsibility(UUID ident, String id, String description) {
		this.controlStructureController.addResponsibility(ident, id,
				description);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);

	}

	@Override
	public void changeResponsibility(UUID ident, String id, String description) {
		this.controlStructureController.changeResponsibility(ident, id,
				description);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);

	}

	@Override
	public void removeResponsibility(UUID ident, String id) {
		this.controlStructureController.removeResponsibility(ident, id);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);

	}

	@Override
	public Responsibility getContext(UUID ident, String id) {
		return this.controlStructureController.getContext(ident, id);
	}

	@Override
	public void addContext(UUID ident, String id, String description) {
		this.controlStructureController.addContext(ident, id, description);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void changeContext(UUID ident, String id, String description) {
		this.controlStructureController.changeContext(ident, id, description);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void removeContext(UUID ident, String id) {
		this.controlStructureController.removeContext(ident, id);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public Responsibility getFlaw(UUID ident, String id) {
		return this.controlStructureController.getFlaw(ident, id);
	}

	@Override
	public void addFlaw(UUID ident, String id, String description) {
		this.controlStructureController.addFlaw(ident, id, description);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void changeFlaw(UUID ident, String id, String description) {
		this.controlStructureController.changeFlaw(ident, id, description);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void removeFlaw(UUID ident, String id) {
		this.controlStructureController.removeFlaw(ident, id);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public Responsibility getUnsafeAction(UUID ident, String id) {
		return this.controlStructureController.getUnsafeActions(ident, id);
	}

	@Override
	public void addUnsafeAction(UUID ident, String id, String description) {
		this.controlStructureController.addUnsafeAction(ident, id, description);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void changeUnsafeAction(UUID ident, String id, String description) {
		this.controlStructureController.changeUnsafeAction(ident, id,
				description);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void removeunsafeAction(UUID ident, String id) {
		this.controlStructureController.removeUnsafeAction(ident, id);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public Recommendation getRecommendation(UUID ident, String id) {
		return this.controlStructureController.getRecommendation(id, ident);
	}

	@Override
	public void addRecommendation(UUID ident, String id, String description) {
		this.controlStructureController.addRecommendation(ident, id,
				description);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void changeRecommendation(UUID ident, String id, String description) {
		this.controlStructureController.changeRecommendation(ident, id,
				description);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void removeRecommendation(UUID ident, String id) {
		this.controlStructureController.removeRecommendation(ident, id);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void updateResponsibility(UUID ident, String id, String newId) {
		this.controlStructureController.updateResponsibility(ident, id, newId);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void updateContext(UUID ident, String id, String newId) {
		this.controlStructureController.updateContext(ident, id, newId);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void updateFlaws(UUID ident, String id, String newId) {
		this.controlStructureController.updateFlaw(ident, id, newId);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void updateUnsafeAction(UUID ident, String id, String newId) {
		this.controlStructureController.updateUnsafeAction(ident, id, newId);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void updateRecommendation(UUID ident, String id, String newId) {
		this.controlStructureController.updateRecommendation(ident, id, newId);
		this.setUnsavedAndChanged(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public void setRelativeOfComponent(UUID componentId, UUID relativeId) {
		this.controlStructureController.setRelativeOfComponent(componentId,
				relativeId);
		updateValue(ObserverValue.CONTROL_STRUCTURE);
		updateValue(ObserverValue.UNSAVED_CHANGES);

	}

	@Override
	public void setSafetyCritical(UUID componentId, boolean isSafetyCritical) {
		this.controlStructureController.setSafetyCritical(componentId,
				isSafetyCritical);

	}

	@Override
	public void setComment(UUID componentId, String comment) {
		this.controlStructureController.setComment(componentId, comment);
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
	public void linkControlAction(UUID caId, UUID componentId) {
		this.controlActionController.setComponentLink(componentId, caId);

	}

	@Override
	public IHAZXControlAction getControlActionU(UUID controlActionId) {
		if (controlActionId == null) {
			return null;
		}

		return this.controlActionController.getControlActionU(controlActionId);
	}

	@Override
	public List<Responsibility> getResponsibilitiesList(UUID id) {
		return this.controlStructureController.getResponsibilitiesList(id);
	}

	@Override
	public List<Responsibility> getContextList(UUID id) {
		return this.controlStructureController.getContextList(id);
	}

	@Override
	public List<Responsibility> getFlawsList(UUID id) {
		return this.controlStructureController.getFlawsList(id);
	}

	@Override
	public List<Responsibility> getUnsafeActionsList(UUID id) {
		return this.controlStructureController.getUnsafeActionsList(id);
	}

	@Override
	public List<Recommendation> getRecommendationList(UUID id) {
		if (this.controlStructureController.getRecommendationList(id)==null) {
			return new ArrayList<Recommendation>();
		} else {
			return this.controlStructureController.getRecommendationList(id);
		}
	}

}
