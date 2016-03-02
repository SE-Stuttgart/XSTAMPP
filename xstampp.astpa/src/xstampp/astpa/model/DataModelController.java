/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;
import org.osgi.framework.Bundle;

import xstampp.astpa.Activator;
import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.haz.causalfactor.CausalFactorHazardLink;
import xstampp.astpa.haz.controlaction.UCAHazLink;
import xstampp.astpa.haz.controlaction.UnsafeControlActionType;
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.haz.controlaction.interfaces.IUCAHazLink;
import xstampp.astpa.haz.hazacc.Link;
import xstampp.astpa.model.causalfactor.CausalFactor;
import xstampp.astpa.model.causalfactor.CausalFactorController;
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.controlaction.ControlAction;
import xstampp.astpa.model.controlaction.ControlActionController;
import xstampp.astpa.model.controlaction.IValueCombie;
import xstampp.astpa.model.controlaction.NotProvidedValuesCombi;
import xstampp.astpa.model.controlaction.ProvidedValuesCombi;
import xstampp.astpa.model.controlaction.interfaces.IHAZXControlAction;
import xstampp.astpa.model.controlaction.rules.RefinedSafetyRule;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.ControlStructureController;
import xstampp.astpa.model.controlstructure.components.Anchor;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.components.ConnectionType;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.export.ExportInformation;
import xstampp.astpa.model.hazacc.Accident;
import xstampp.astpa.model.hazacc.HazAccController;
import xstampp.astpa.model.hazacc.Hazard;
import xstampp.astpa.model.interfaces.IAccidentViewDataModel;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IControlActionViewDataModel;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.astpa.model.interfaces.ICorrespondingSafetyConstraintDataModel;
import xstampp.astpa.model.interfaces.IDesignRequirementViewDataModel;
import xstampp.astpa.model.interfaces.IHazardViewDataModel;
import xstampp.astpa.model.interfaces.ILinkingViewDataModel;
import xstampp.astpa.model.interfaces.INavigationViewDataModel;
import xstampp.astpa.model.interfaces.ISafetyConstraintViewDataModel;
import xstampp.astpa.model.interfaces.IStatusLineDataModel;
import xstampp.astpa.model.interfaces.ISystemDescriptionViewDataModel;
import xstampp.astpa.model.interfaces.ISystemGoalViewDataModel;
import xstampp.astpa.model.interfaces.IUnsafeControlActionDataModel;
import xstampp.astpa.model.projectdata.ProjectDataController;
import xstampp.astpa.model.sds.DesignRequirement;
import xstampp.astpa.model.sds.SDSController;
import xstampp.astpa.model.sds.SafetyConstraint;
import xstampp.astpa.model.sds.SystemGoal;
import xstampp.astpa.util.jobs.SaveJob;
import xstampp.model.ILTLProvider;
import xstampp.model.ISafetyDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.menu.file.commands.CommandState;

/**
 * Data Model controller class
 * 
 * @author Fabian Toth
 * @since 2.0
 * 
 */
@XmlRootElement(namespace = "astpa.model")
public class DataModelController extends Observable implements
		ISafetyDataModel,IHAZXModel,ILinkingViewDataModel, INavigationViewDataModel,
		ISystemDescriptionViewDataModel, IAccidentViewDataModel,
		IHazardViewDataModel, IStatusLineDataModel,
		IDesignRequirementViewDataModel, ISafetyConstraintViewDataModel,
		ISystemGoalViewDataModel, IControlActionViewDataModel,
		IControlStructureEditorDataModel, IUnsafeControlActionDataModel,
		ICausalFactorDataModel, ICorrespondingSafetyConstraintDataModel {


	private static final Logger LOGGER = ProjectManager.getLOGGER();
	private static final String HAZ ="haz";
	private static final String HAZX ="hazx";
	@XmlAttribute(name = "astpaversion")
	private String astpaVersion;

	@XmlElement(name = "exportinformation")
	private ExportInformation exportInformation;

	@XmlElement(name = "projectdata")
	private ProjectDataController projectDataManager;

	@XmlElement(name = "hazacc")
	private HazAccController hazAccController;

	@XmlElement(name = "sds")
	private SDSController sdsController;

	@XmlElement(name = "controlstructure")
	private ControlStructureController controlStructureController;


	@XmlElement(name = "ignoreLTLValue")
	private Component ignoreLtlValue;
	
	@XmlElement(name = "cac")
	private ControlActionController controlActionController;

	@XmlElement(name = "causalfactor")
	private CausalFactorController causalFactorController;

	/**
	 * Shows if there are unsaved changes or not
	 */
	private boolean unsavedChanges;

	
	private String projectExtension;
	private boolean refreshLock;
	/**
	 * Constructor of the DataModel Controller
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public DataModelController() {
		this.projectDataManager = new ProjectDataController();
		this.hazAccController = new HazAccController();
		this.sdsController = new SDSController();
		this.controlStructureController = new ControlStructureController();
		this.controlActionController = new ControlActionController();
		this.causalFactorController = new CausalFactorController();
		this.unsavedChanges = false;
		getIgnoreLTLValue();
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		if (bundle != null) {
			Dictionary<?, ?> dictionary = bundle.getHeaders();
			String versionWithQualifier = (String) dictionary
					.get(Messages.BundleVersion);
			this.astpaVersion = versionWithQualifier.substring(0,
					versionWithQualifier.lastIndexOf('.'));
		}
		// Enable the save entries in the menu
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getService(ISourceProviderService.class);
		CommandState saveStateService = (CommandState) sourceProviderService
				.getSourceProvider(CommandState.SAVE_STATE);
		addObserver(saveStateService);
	}

	@Override
	public void updateValue(ObserverValue value) {
		this.setChanged();
		if(!this.refreshLock){
			DataModelController.LOGGER.debug("Trigger update for " + value.name()); //$NON-NLS-1$
			this.notifyObservers(value);
		}
	}

	@Override
	public void lockUpdate(){
		this.refreshLock = true;
		DataModelController.LOGGER.debug("setData update lock to prevent system lacks");
	}
	
	@Override
	public void releaseLockAndUpdate(ObserverValue value){
		this.refreshLock = false;
		DataModelController.LOGGER.debug("released update lock");
		if(hasChanged()){
			setUnsavedAndChanged(value);
		}
		
	}
	@Override
	public boolean hasUnsavedChanges() {
		return this.unsavedChanges;
	}

	/**
	 * Sets that there are no unsaved changes
	 * 
	 * @author Fabian Toth
	 */
	@Override
	public void setStored() {
		this.unsavedChanges = false;
		this.setChanged();
		this.notifyObservers(ObserverValue.UNSAVED_CHANGES);
	}

	@Override
	public boolean prepareForExport() {

		this.exportInformation = null;
		this.hazAccController.prepareForExport();
		this.controlActionController.prepareForExport(this.hazAccController,this.controlStructureController);
		this.causalFactorController.prepareForExport(this.hazAccController,
				this.controlStructureController.getInternalComponents());
		this.projectDataManager.prepareForExport();
		this.exportInformation = new ExportInformation();
		ProjectManager.getLOGGER().debug("Project: " + getProjectName() + " prepared for export");
		return true;
	}

	@Override
	public void prepareForSave() {
		this.hazAccController.prepareForSave();
		this.controlActionController.prepareForSave();
		this.causalFactorController
				.prepareForSave(this.controlStructureController
						.getInternalComponents());
		this.projectDataManager.prepareForSave();
		this.exportInformation = null;
		ProjectManager.getLOGGER().debug("Project: " + getProjectName() + " prepared for save");
		
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
		if(this.projectDataManager.setProjectName(projectName)){
			this.setUnsavedAndChanged(ObserverValue.PROJECT_NAME);
		}
		return true;
	}

	@XmlTransient
	@Override
	public String getProjectDescription() {
		return this.projectDataManager.getProjectDescription();
	}

	@Override
	public boolean setProjectDescription(String projectDescription) {
		if (projectDescription == null) {
			return false;
		}
		
		if(this.projectDataManager.setProjectDescription(projectDescription)){
			this.setUnsavedAndChanged(ObserverValue.PROJECT_DESCRIPTION);
		}
		return true;
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
	public List<ITableModel> getAllAccidents() {
		return this.hazAccController.getAllAccidents();
	}

	@Override
	public ITableModel getAccident(UUID accidentId) {
		if (accidentId == null) {
			return null;
		}
		ITableModel accident = this.hazAccController.getAccident(accidentId);
		if (!(accident instanceof Accident)) {
			return null;
		}

		return accident;
	}

	@Override
	public List<ITableModel> getLinkedHazards(UUID accidentId) {
		if (accidentId == null) {
			return null;
		}

		return this.hazAccController.getLinkedHazards(accidentId);
	}

	@Override
	public UUID addAccident(String title, String description) {
		if ((title == null) || (description == null)) {
			return null;
		}

		UUID id = this.hazAccController.addAccident(title, description);
		this.setUnsavedAndChanged(ObserverValue.ACCIDENT);
		return id;
	}

	@Override
	public boolean removeAccident(UUID accidentId) {
		if (accidentId == null) {
			return false;
		}
		if (!(this.hazAccController.getAccident(accidentId) instanceof Accident)) {
			return false;
		}

		boolean result = this.hazAccController.removeAccident(accidentId);
		this.setUnsavedAndChanged(ObserverValue.ACCIDENT);
		return result;
	}

	@Override
	public boolean setAccidentDescription(UUID accidentId, String description) {
		if ((accidentId == null) || (description == null)) {
			return false;
		}
		if (!(this.hazAccController.getAccident(accidentId) instanceof Accident)) {
			return false;
		}
		((Accident) this.hazAccController.getAccident(accidentId))
				.setDescription(description);

		this.setUnsavedAndChanged(ObserverValue.ACCIDENT);
		return true;
	}

	@Override
	public boolean setAccidentTitle(UUID accidentId, String title) {
		if ((accidentId == null) || (title == null)) {
			return false;
		}
		if (!(this.hazAccController.getAccident(accidentId) instanceof Accident)) {
			return false;
		}
		((Accident) this.hazAccController.getAccident(accidentId))
				.setTitle(title);

		this.setUnsavedAndChanged(ObserverValue.ACCIDENT);
		return true;
	}

	@Override
	public List<ITableModel> getAllHazards() {
		return this.hazAccController.getAllHazards();
	}

	@Override
	public ITableModel getHazard(UUID hazardId) {
		if (hazardId == null) {
			return null;
		}
		ITableModel hazard = this.hazAccController.getHazard(hazardId);
		if (!(hazard instanceof Hazard)) {
			return null;
		}
		return hazard;
	}

	@Override
	public List<ITableModel> getLinkedAccidents(UUID hazardId) {
		if (hazardId == null) {
			return null;
		}

		return this.hazAccController.getLinkedAccidents(hazardId);
	}

	@Override
	public boolean removeHazard(UUID hazardId) {
		if (hazardId == null) {
			return false;
		}
		if (!(this.hazAccController.getHazard(hazardId) instanceof Hazard)) {
			return false;
		}
		this.causalFactorController.removeAllLinks(hazardId);
		this.controlActionController.removeAllLinks(hazardId);
		boolean result = this.hazAccController.removeHazard(hazardId);
		this.setUnsavedAndChanged(ObserverValue.HAZARD);
		return result;
	}

	@Override
	public UUID addHazard(String title, String description) {
		if ((title == null) || (description == null)) {
			return null;
		}

		UUID id = this.hazAccController.addHazard(title, description);
		this.setUnsavedAndChanged(ObserverValue.HAZARD);
		return id;
	}

	@Override
	public boolean setHazardTitle(UUID hazardId, String title) {
		if ((title == null) || (hazardId == null)) {
			return false;
		}
		if (!(this.hazAccController.getHazard(hazardId) instanceof Hazard)) {
			return false;
		}

		((Hazard) this.hazAccController.getHazard(hazardId)).setTitle(title);
		this.setUnsavedAndChanged(ObserverValue.HAZARD);
		return true;
	}

	@Override
	public boolean setHazardDescription(UUID hazardId, String description) {
		if ((description == null) || (hazardId == null)) {
			return false;
		}
		if (!(this.hazAccController.getHazard(hazardId) instanceof Hazard)) {
			return false;
		}

		((Hazard) this.hazAccController.getHazard(hazardId))
				.setDescription(description);
		this.setUnsavedAndChanged(ObserverValue.HAZARD);
		return true;
	}

	@Override
	public boolean addLink(UUID accidentId, UUID hazardId) {
		if ((accidentId == null) || (hazardId == null)) {
			return false;
		}
		if (!(this.hazAccController.getHazard(hazardId) instanceof Hazard)) {
			return false;
		}
		if (!(this.hazAccController.getAccident(accidentId) instanceof Accident)) {
			return false;
		}

		this.hazAccController.addLink(accidentId, hazardId);
		this.setUnsavedAndChanged(ObserverValue.HAZ_ACC_LINK);
		return true;
	}

	@Override
	public boolean deleteLink(UUID accidentId, UUID hazardId) {
		if ((accidentId == null) || (hazardId == null)) {
			return false;
		}

		boolean result = this.hazAccController.deleteLink(accidentId, hazardId);
		this.setUnsavedAndChanged(ObserverValue.HAZ_ACC_LINK);
		return result;
	}

	@Override
	public List<ITableModel> getAllDesignRequirements() {
		return this.sdsController.getAllDesignRequirements();
	}

	@Override
	public UUID addDesignRequirement(String title, String description) {
		if ((title == null) || (description == null)) {
			return null;
		}

		UUID id = this.sdsController.addDesignRequirement(title, description);
		this.setUnsavedAndChanged(ObserverValue.DESIGN_REQUIREMENT);
		return id;
	}

	@Override
	public boolean removeDesignRequirement(UUID designRequirementId) {
		if (designRequirementId == null) {
			return false;
		}
		if (!(this.sdsController.getDesignRequirement(designRequirementId) instanceof DesignRequirement)) {
			return false;
		}
		boolean result = this.sdsController
				.removeDesignRequirement(designRequirementId);
		this.setUnsavedAndChanged(ObserverValue.DESIGN_REQUIREMENT);
		return result;
	}

	@Override
	public boolean setDesignRequirementTitle(UUID designRequirementId,
			String title) {
		if ((title == null) || (designRequirementId == null)) {
			return false;
		}
		if (!(this.sdsController.getDesignRequirement(designRequirementId) instanceof DesignRequirement)) {
			return false;
		}

		((DesignRequirement) this.sdsController
				.getDesignRequirement(designRequirementId)).setTitle(title);
		this.setUnsavedAndChanged(ObserverValue.DESIGN_REQUIREMENT);
		return true;
	}

	@Override
	public boolean setDesignRequirementDescription(UUID designRequirementId,
			String description) {
		if ((description == null) || (designRequirementId == null)) {
			return false;
		}
		if (!(this.sdsController.getDesignRequirement(designRequirementId) instanceof DesignRequirement)) {
			return false;
		}

		((DesignRequirement) this.sdsController
				.getDesignRequirement(designRequirementId))
				.setDescription(description);
		this.setUnsavedAndChanged(ObserverValue.DESIGN_REQUIREMENT);
		return true;
	}

	@Override
	public ITableModel getDesignRequirement(UUID designRequirementId) {
		if (designRequirementId == null) {
			return null;
		}
		ITableModel designRequirement = this.sdsController
				.getDesignRequirement(designRequirementId);
		if (!(designRequirement instanceof DesignRequirement)) {
			return null;
		}
		return designRequirement;
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

	@Override
	public List<ITableModel> getAllSystemGoals() {
		return this.sdsController.getAllSystemGoals();
	}

	@Override
	public UUID addSystemGoal(String title, String description) {
		if ((title == null) || (description == null)) {
			return null;
		}

		UUID id = this.sdsController.addSystemGoal(title, description);
		this.setUnsavedAndChanged(ObserverValue.SYSTEM_GOAL);
		return id;
	}

	@Override
	public boolean removeSystemGoal(UUID systemGoalId) {
		if (systemGoalId == null) {
			return false;
		}
		if (!(this.sdsController.getSystemGoal(systemGoalId) instanceof SystemGoal)) {
			return false;
		}

		boolean result = this.sdsController.removeSystemGoal(systemGoalId);
		this.setUnsavedAndChanged(ObserverValue.SYSTEM_GOAL);
		return result;
	}

	@Override
	public boolean setSystemGoalTitle(UUID systemGoalId, String title) {
		if ((systemGoalId == null) || (title == null)) {
			return false;
		}
		if (!(this.sdsController.getSystemGoal(systemGoalId) instanceof SystemGoal)) {
			return false;
		}

		((SystemGoal) this.sdsController.getSystemGoal(systemGoalId))
				.setTitle(title);
		this.setUnsavedAndChanged(ObserverValue.SYSTEM_GOAL);
		return true;
	}

	@Override
	public boolean setSystemGoalDescription(UUID systemGoalId,
			String description) {
		if ((systemGoalId == null) || (description == null)) {
			return false;
		}
		if (!(this.sdsController.getSystemGoal(systemGoalId) instanceof SystemGoal)) {
			return false;
		}

		((SystemGoal) this.sdsController.getSystemGoal(systemGoalId))
				.setDescription(description);
		this.setUnsavedAndChanged(ObserverValue.SYSTEM_GOAL);
		return true;
	}

	@Override
	public ITableModel getSystemGoal(UUID systemGoalId) {
		if (systemGoalId == null) {
			return null;
		}
		if (!(this.sdsController.getSystemGoal(systemGoalId) instanceof SystemGoal)) {
			return null;
		}

		return this.sdsController.getSystemGoal(systemGoalId);
	}

	@Override
	public List<IControlAction> getAllControlActions() {
		List<IControlAction> result = new ArrayList<>();
		for (IHAZXControlAction controlAction : this.controlActionController.getAllControlActionsU()) {
			result.add(controlAction);
		}
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
	public boolean removeControlAction(UUID controlActionId) {
		if (controlActionId == null) {
			return false;
		}
		ITableModel caObject =this.controlActionController.getControlAction(controlActionId);
		if (caObject == null || !(caObject instanceof ControlAction)) {
			return false;
		}else if(this.controlStructureController.removeComponent(((ControlAction)caObject).getComponentLink())){
			this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		}
		boolean result = this.controlActionController
				.removeControlAction(controlActionId);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_ACTION);
		return result;
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
		if(changeComponentText(((ControlAction) this.controlActionController
				.getControlAction(controlActionId)).getComponentLink(), title)){
			this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		}
		this.setUnsavedAndChanged(ObserverValue.CONTROL_ACTION);
		return true;
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
	public UUID setRoot(Rectangle layout, String text) {
		if ((layout == null) || (text == null)) {
			return null;
		}

		UUID result = this.controlStructureController.setRoot(layout, text);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
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
	public IRectangleComponent getComponent(UUID componentId) {
		if ((componentId == null)) {
			return null;
		}
		if(ignoreLtlValue != null && componentId.equals(ignoreLtlValue.getId())){
			return ignoreLtlValue;
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
	public List<IHAZXControlAction> getAllControlActionsU() {
		return this.controlActionController.getAllControlActionsU();
	}

	@Override
	public IHAZXControlAction getControlActionU(UUID controlActionId) {
		if (controlActionId == null) {
			return null;
		}

		return this.controlActionController.getControlActionU(controlActionId);
	}
	
	@Override
	public void linkControlAction(UUID caId,UUID componentId) {
		this.controlActionController.setComponentLink(componentId, caId);
	}
	
	@Override
	public UUID addUnsafeControlAction(UUID controlActionId,
			String description, UnsafeControlActionType unsafeControlActionType) {
		if ((controlActionId == null) || (description == null)
				|| (unsafeControlActionType == null)) {
			return null;
		}

		UUID result = this.controlActionController.addUnsafeControlAction(
				controlActionId, description, unsafeControlActionType);
		this.setUnsavedAndChanged(ObserverValue.UNSAFE_CONTROL_ACTION);
		return result;
	}

	@Override
	public boolean removeUnsafeControlAction(UUID unsafeControlActionId) {
		if (unsafeControlActionId == null) {
			return false;
		}

		this.controlActionController.removeAllLinks(unsafeControlActionId);
		boolean result = this.controlActionController
				.removeUnsafeControlAction(unsafeControlActionId);
		this.setUnsavedAndChanged(ObserverValue.UNSAFE_CONTROL_ACTION);
		return result;
	}

	@Override
	public List<ITableModel> getLinkedHazardsOfUCA(UUID unsafeControlActionId) {
		if (unsafeControlActionId == null) {
			return null;
		}

		List<IUCAHazLink> links = this.controlActionController
				.getLinksOfUCA(unsafeControlActionId);
		List<ITableModel> result = new ArrayList<>();
		for (IUCAHazLink link : links) {
			result.add(this.getHazard(link.getHazardId()));
		}
		return result;
	}

	@Override
	public boolean addUCAHazardLink(UUID unsafeControlActionId, UUID hazardId) {
		if ((unsafeControlActionId == null) || (hazardId == null)) {
			return false;
		}
		if (!(this.hazAccController.getHazard(hazardId) instanceof Hazard)) {
			return false;
		}
		if (this.controlActionController
				.getInternalUnsafeControlAction(unsafeControlActionId) == null) {
			return false;
		}

		boolean result = this.controlActionController.addUCAHazardLink(
				unsafeControlActionId, hazardId);

		this.setUnsavedAndChanged(ObserverValue.UNSAFE_CONTROL_ACTION);
		return result;
	}

	@Override
	public boolean removeUCAHazardLink(UUID unsafeControlActionId, UUID hazardId) {
		if ((unsafeControlActionId == null) || (hazardId == null)) {
			return false;
		}
		boolean result = this.controlActionController.removeUCAHazardLink(
				unsafeControlActionId, hazardId);
		this.setUnsavedAndChanged(ObserverValue.UNSAFE_CONTROL_ACTION);
		return result;
	}

	@Override
	public boolean setUcaDescription(UUID unsafeControlActionId,
			String description) {
		if ((unsafeControlActionId == null) || (description == null)) {
			return false;
		}

		boolean result = this.controlActionController.setUcaDescription(
				unsafeControlActionId, description);
		this.setUnsavedAndChanged(ObserverValue.UNSAFE_CONTROL_ACTION);
		return result;
	}

	@Override
	public List<ICorrespondingUnsafeControlAction> getAllUnsafeControlActions() {
		return this.controlActionController.getAllUnsafeControlActions();
	}

	
	@Override
	public int getUCANumber(UUID ucaID){
		return this.controlActionController.getUCANumber(ucaID);
	}
	
	@Override
	public UUID setCorrespondingSafetyConstraint(UUID unsafeControlActionId,
			String safetyConstraintDescription) {
		if ((unsafeControlActionId == null)
				|| (safetyConstraintDescription == null)) {
			return null;
		}

		UUID id = this.controlActionController
				.setCorrespondingSafetyConstraint(unsafeControlActionId,
						safetyConstraintDescription);
		this.setUnsavedAndChanged(ObserverValue.UNSAFE_CONTROL_ACTION);
		return id;
	}

	@Override
	public List<ICausalComponent> getCausalComponents() {
		return this.controlStructureController.getCausalComponents();
	}

	@Override
	public UUID addCausalFactor(UUID causalComponentId, String causalFactorText) {
		if ((causalComponentId == null) || (causalFactorText == null)) {
			return null;
		}

		UUID id = this.causalFactorController.addCausalFactor(
				this.controlStructureController.getInternalComponents(),
				causalComponentId, causalFactorText);
		this.setUnsavedAndChanged(ObserverValue.CAUSAL_FACTOR);
		return id;
	}

	@Override
	public boolean setCausalFactorText(UUID causalFactorId,
			String causalFactorText) {
		if ((causalFactorId == null) || (causalFactorText == null)) {
			return false;
		}
		List<Component> components = this.controlStructureController
				.getInternalComponents();
		if (components == null) {
			return false;
		}
		boolean result = this.causalFactorController.setCausalFactorText(
				components, causalFactorId, causalFactorText);
		this.setUnsavedAndChanged(ObserverValue.CAUSAL_FACTOR);
		return result;
	}

	@Override
	public boolean addCausalFactorHazardLink(UUID causalFactorId, UUID hazardId) {
		if ((causalFactorId == null) || (hazardId == null)) {
			return false;
		}
		if (!(this.hazAccController.getHazard(hazardId) instanceof Hazard)) {
			return false;
		}
		List<Component> components = this.controlStructureController
				.getInternalComponents();
		if (components == null) {
			return false;
		}

		boolean found = false;
		for (Component component : components) {
			for (CausalFactor causalFactor : component
					.getInternalCausalFactors()) {
				if (causalFactor.getId().equals(causalFactorId)) {
					found = true;
				}
			}
		}
		if (!found) {
			return false;
		}

		boolean result = this.causalFactorController.addCausalFactorHazardLink(
				causalFactorId, hazardId);
		this.setUnsavedAndChanged(ObserverValue.CAUSAL_FACTOR);
		return result;
	}

	@Override
	public List<ITableModel> getLinkedHazardsOfCf(UUID causalFactorId) {
		if (causalFactorId == null) {
			return null;
		}

		List<UUID> hazardIds = this.causalFactorController
				.getLinkedHazardsOfCf(causalFactorId);
		List<ITableModel> linkedHazards = new ArrayList<>();
		for (UUID id : hazardIds) {
			linkedHazards.add(this.hazAccController.getHazard(id));
		}

		return linkedHazards;
	}

	@Override
	public boolean removeCausalFactorHazardLink(UUID causalFactorId,
			UUID hazardId) {
		if ((causalFactorId == null) || (hazardId == null)) {
			return false;
		}

		boolean result = this.causalFactorController
				.removeCausalFactorHazardLink(causalFactorId, hazardId);
		this.setUnsavedAndChanged(ObserverValue.CAUSAL_FACTOR);
		return result;
	}

	@Override
	public boolean removeCausalFactor(UUID causalFactorId) {
		if (causalFactorId == null) {
			return false;
		}
		boolean result = this.controlStructureController
				.removeCausalFactor(causalFactorId);
		this.causalFactorController.removeAllLinks(causalFactorId);
		this.setUnsavedAndChanged(ObserverValue.CAUSAL_FACTOR);
		return result;
	}

	@Override
	public List<ISafetyConstraint> getCorrespondingSafetyConstraints() {
		return this.controlActionController.getCorrespondingSafetyConstraints();
	}

	@Override
	public boolean setCausalSafetyConstraintText(UUID causalSafetyCosntraintId,
			String causalSafetyConstraintText) {
		if ((causalSafetyCosntraintId == null)
				|| (causalSafetyConstraintText == null)) {
			return false;
		}
		List<Component> components = this.controlStructureController
				.getInternalComponents();
		if (components == null) {
			return false;
		}

		boolean result = this.causalFactorController
				.setCausalSafetyConstraintText(components,
						causalSafetyCosntraintId, causalSafetyConstraintText);
		this.setUnsavedAndChanged(ObserverValue.CAUSAL_FACTOR);
		return result;
	}

	@Override
	public boolean setNoteText(UUID causalFactorId, String noteText) {
		if ((causalFactorId == null) || (noteText == null)) {
			return false;
		}
		List<Component> components = this.controlStructureController
				.getInternalComponents();
		if (components == null) {
			return false;
		}

		boolean result = this.causalFactorController.setCausalFactorNoteText(
				components, causalFactorId, noteText);
		this.setUnsavedAndChanged(ObserverValue.CAUSAL_FACTOR);
		return result;
	}

	private void setUnsavedAndChanged(ObserverValue value) {
		this.updateValue(value);
		setUnsavedAndChanged();
	}

	@Override
	public void setUnsavedAndChanged() {
			this.unsavedChanges = true;
			this.updateValue(ObserverValue.UNSAVED_CHANGES);
	}
	
	public boolean isReadyForExport(){
		return this.exportInformation != null;
	}
	@Override
	public boolean setCSImagePath(String path) {
		if (this.exportInformation == null) {
			return false;
		}
		Image img = new Image(null, path);
		File imgFile = new File(path);
		this.exportInformation.setCsImageWidth(String.valueOf(img.getBounds().width));
		this.exportInformation.setCsImageHeight(String.valueOf(img.getBounds().height));
		return this.exportInformation.setCsImagePath(imgFile.toURI().toString());
	}

	@Override
	public boolean setCSPMImagePath(String path) {
		if (this.exportInformation == null) {
			return false;
		}
		Image img = new Image(null, path);
		File imgFile = new File(path);
		this.exportInformation
				.setCsPmImageWidth(String.valueOf(img.getBounds().width));
		this.exportInformation.setCsPmImageHeight(String.valueOf(img
				.getBounds().height));

		return this.exportInformation.setCspmImagePath(imgFile.toURI()
				.toString());
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
	public boolean recoverControlAction(UUID id) {
		return this.controlActionController.recoverControlAction(id);

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
	public UUID addComponent(UUID controlActionId, UUID parentId,
			Rectangle layout, String text, ComponentType type, Integer index) {
		if ((parentId == null) || (layout == null) || (text == null)
				|| (type == null)) {
			return null;
		}
//		if (!(this.getComponent(parentId) instanceof Component)) {
//			return null;
//		}

		UUID result = this.controlStructureController.addComponent(
				controlActionId, parentId, layout, text, type, index);
//		this.controlActionController.setComponentLink(result, controlActionId);
		this.setUnsavedAndChanged(ObserverValue.CONTROL_STRUCTURE);
		return result;
	}
	
	

	@Override
	public Job doSave(final File file, Logger log, boolean isUIcall) {
		String sLossOfData =  String.format("%s contains data that can only be stored in an extended STPA File (.hazx)\n"
											+ "Do you want to change the file extension to store the extended data?/n"
											+ "NOTE: the file is not longer compatible with older versions of XSTAMPP or A-STPA",
											file.getName());
		if(usesHAZXData() && file.getName().endsWith("haz")&&
			MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Unexpected data",sLossOfData)){
			UUID id = ProjectManager.getContainerInstance().getProjectID(this);
			ProjectManager.getContainerInstance().changeProjectExtension(id, "hazx");
			return null;
		}
		SaveJob job= new SaveJob(file,this);
//		//the compabillity mode is only needed in haz files, since the hazx format could never be stored the other way
//		if(isUIcall && file.getName().endsWith("haz")){ //$NON-NLS-1$
//			Runnable question = new SaveRunnable(job);
//			
//			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay().syncExec(question);
//			while(!job.isReady()){
//				//wait till the user desided whether or not to store on compatibility  mode
//			}
//			
//		}
		return job;
	}

	@Override
	public void initializeProject() {
		this.controlStructureController.initializeCSS();
	}

	@Override
	public boolean isCSComponentSafetyCritical(UUID componentId){
		return this.controlStructureController.isSafetyCritical(componentId);
	}
	
	/**
	 * return whether the control action for the given id is safetycritical or not
	 *
	 * @author Lukas Balzer
	 *
	 * @param caID must be an id of a control action
	 * @return whether the ca is safety critical or not 
	 */
	public boolean isCASafetyCritical(UUID caID){
		return this.controlActionController.isSafetyCritical(caID);
	}
	@Override
	public String getFileExtension() {
		if(this.projectExtension == null){
			return "haz";
		}
		return this.projectExtension;
	}
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param ext must be one of 
	 * @return whether the given extension is supported or not
	 */
	public boolean setProjectExtension(String ext){
		if(ext.equals(HAZ) || ext.equals(HAZX)){
			this.projectExtension = ext;
			return true;
		}
		return false;
	}
	@Override
	public String getPluginID() {
		return Activator.PLUGIN_ID;
	}

	@Override
	public List<Link> getAllHazAccLinks() {
		return this.hazAccController.getAllHazAccLinks();
	}

	@Override
	public List<UCAHazLink> getAllUCALinks() {
		return this.controlActionController.getAllUCALinks();
	}

	@Override
	public List<CausalFactorHazardLink> getCausalFactorHazLinks() {
		return this.causalFactorController.getCausalFactorHazLinks();
	}
	
	
	@Override
	public String getAstpaVersion() {
		return this.astpaVersion;
	}

	@Override
	public void setRelativeOfComponent(UUID componentId, UUID relativeId) {
		this.controlStructureController.setRelativeOfComponent(componentId, relativeId);
		updateValue(ObserverValue.CONTROL_STRUCTURE);
		updateValue(ObserverValue.CONTROL_ACTION);
		updateValue(ObserverValue.UNSAVED_CHANGES);
	}

	
	@Override
	public Map<IRectangleComponent, Boolean> getRelatedProcessVariables(UUID componentId){
		return this.controlStructureController.getRelatedProcessVariables(componentId);
	}


	@Override
	public void setCSComponentComment(UUID componentId, String comment) {
		this.controlStructureController.setComment(componentId, comment);
	}

	@Override
	public boolean addUnsafeProcessVariable(UUID componentId, UUID variableID) {
		return this.controlStructureController.addUnsafeProcessVariable(componentId, variableID);
	}

	@Override
	public boolean removeUnsafeProcessVariable(UUID componentId, UUID variableID) {
		return this.controlStructureController.removeUnsafeProcessVariable(componentId, variableID);
	}


	/**
	 * @param caID the control action id which is used to look up the action
	 * @param isSafetyCritical the isSafetyCritical to set
	 */
	public void setCASafetyCritical(UUID caID, boolean isSafetyCritical) {
		this.controlActionController.setSafetyCritical(caID, isSafetyCritical);
	}


	/**
	 * @param caID the control action id which is used to look up the action
	 * @return the valuesWhenNotProvided
	 */
	public List<IValueCombie> getIValuesWhenCANotProvided(UUID caID) {
		ArrayList<IValueCombie> combies = new ArrayList<>();
		for(IValueCombie combie : this.controlActionController.getValuesWhenNotProvided(caID)){
			combies.add(combie);
		}
		return combies;
	}
	
	/**
	 * @param caID the control action id which is used to look up the action
	 * @return the NotProvidedValuesCombi objects when not provided
	 */
	public List<NotProvidedValuesCombi> getValuesWhenCANotProvided(UUID caID) {
		return this.controlActionController.getValuesWhenNotProvided(caID);
	}

	/**
	 * @param caID the control action id which is used to look up the action
	 * @param valuesWhenNotProvided the valuesWhenNotProvided to set
	 */
	public void setValuesWhenCANotProvided(UUID caID, List<NotProvidedValuesCombi> valuesWhenNotProvided) {
		this.controlActionController.setValuesWhenNotProvided(caID, valuesWhenNotProvided);
		setUnsavedAndChanged(ObserverValue.Extended_DATA);
	}
	/**
	 * adds the given values combination to the list of value combinations
	 * in which the system gets into a hazardous state if the control action is not provided
	 * 
	 * @param caID the uuid object of the control action
	 * @param valueWhenNotProvided the values combination
	 * @return whether or not the operation was successful, null if the given uuid is no legal controlAction id 
	 */
	public boolean addValuesWhenNotProvided(UUID caID, NotProvidedValuesCombi valueWhenNotProvided) {
		if(this.controlActionController.addValueWhenNotProvided(caID, valueWhenNotProvided)){
			setUnsavedAndChanged();
			return true;
		}
		return false;
	}

	/**
	 * removes the given value combinations from the list of value combinations
	 * in which the system gets into a hazardous state if the control action is not provided
	 * 
	 * @param caID the uuid object of the control action
	 * @param combieId the uuid of the value combination
	 * @return whether or not the operation was successful, null if the given uuid is no legal controlAction id 
	 */
	public boolean removeValueWhenNotProvided(UUID caID, UUID combieId) {
		if(this.controlActionController.removeValueWhenNotProvided(caID, combieId)){
			setUnsavedAndChanged();
			return true;
		}
		return false;
	}

	/**
	 * @param caID the control action id which is used to look up the action
	 * @return the IValueCombie objects when provided
	 */
	public List<IValueCombie> getIvaluesWhenCAProvided(UUID caID) {
		ArrayList<IValueCombie> combies = new ArrayList<>();
		for(IValueCombie combie : this.controlActionController.getValuesWhenProvided(caID)){
			combies.add(combie);
		}
		return combies;
	}

	/**
	 * @param caID the control action id which is used to look up the action
	 * @return the ProvidedValuesCombi when provided
	 */
	public List<ProvidedValuesCombi> getValuesWhenCAProvided(UUID caID) {
		return this.controlActionController.getValuesWhenProvided(caID);
	}
	/**
	 * @param caID the control action id which is used to look up the action
	 * @param valuesWhenProvided the valuesWhenProvided to set
	 */
	public void setValuesWhenCAProvided(UUID caID, List<ProvidedValuesCombi> valuesWhenProvided) {
		this.controlActionController.setValuesWhenProvided(caID, valuesWhenProvided);
		setUnsavedAndChanged(ObserverValue.Extended_DATA);
		
	}

	/**
	 * adds the given values combination to the list of value combinations
	 * in which the system gets into a hazardous state if the control action is provided
	 * 
	 * @param caID the uuid object of the control action
	 * @param valueWhenNotProvided the values combination
	 * @return whether or not the operation was successful, null if the given uuid is no legal controlAction id 
	 */
	public boolean addValueWhenProvided(UUID caID, ProvidedValuesCombi valueWhenProvided) {
		if(this.controlActionController.addValueWhenProvided(caID,valueWhenProvided)){
			setUnsavedAndChanged();
			return true;
		}
		return false;
	}

	/**
	 * removes the given value combinations from the list of value combinations
	 * in which the system gets into a hazardous state if the control action is provided
	 * 
	 * @param caID the uuid object of the control action
	 * @param combieId the values combination id
	 * @return whether or not the operation was successful, null if the given uuid is no legal controlAction id 
	 */
	public boolean removeValueWhenProvided(UUID caID, UUID combieId) {
		if(this.controlActionController.removeValueWhenProvided(caID,combieId)){
			setUnsavedAndChanged();
			return true;
		}
		return false;
	}
	/**
	 * {@link ControlAction#getNotProvidedVariables()}
	 * @param caID the control action id which is used to look up the action
	 * @return {@link ControlAction#getProvidedVariables()}
	 */
	public List<UUID> getCANotProvidedVariables(UUID caID) {
		ArrayList<UUID> list = new ArrayList<>();
		for(UUID id : this.controlActionController.getNotProvidedVariables(caID)){
			if(getComponent(id) != null){
				list.add(id);
			}
		}
		return list;
	}


	/**
	 * 
	 * {@link ControlAction#getProvidedVariables()}
	 * 
	 * @param caID the control action id which is used to look up the action
	 * @param notProvidedVariable the notProvidedVariables to set
	 */
	public void addCANotProvidedVariable(UUID caID, UUID notProvidedVariable) {

		if(this.controlStructureController.getComponent(notProvidedVariable) != null){
			this.controlActionController.addNotProvidedVariable(caID, notProvidedVariable);
			setUnsavedAndChanged();
		}else{
				LOGGER.debug("given provided id is not related to a valid component");
		}
	}


	/**
	 * {@link ControlAction#getProvidedVariables()}
	 * @param caID the control action id which is used to look up the action
	 * @return a copie of the provided variables list
	 */
	public List<UUID> getCAProvidedVariables(UUID caID) {
		ArrayList<UUID> list = new ArrayList<>();
		for(UUID id : this.controlActionController.getProvidedVariables(caID)){
			if(getComponent(id) != null){
				list.add(id);
			}
		}
		return list;
	}


	/**
	 * {@link ControlAction#addProvidedVariable(UUID)}
	 * @param caID the control action id which is used to look up the action
	 * 
	 * @param providedVariable the providedVariable to add
	 */
	public void addCAProvidedVariable(UUID caID, UUID providedVariable) {

		if(this.controlStructureController.getComponent(providedVariable) != null){
			this.controlActionController.addProvidedVariable(caID,providedVariable);
			setUnsavedAndChanged();
		}else{
			LOGGER.debug("given provided id is not related to a valid component");
		}
	}

	@Override
	public void setSafetyCritical(UUID componentId, boolean isSafetyCritical) {
		this.controlStructureController.setSafetyCritical(componentId, isSafetyCritical);
	}
	
	/**
	 * 
	 * remove the uuid of a process variable component from the list
	 * of variables depending on this control action when not provided
	 * 
	 * @param caID the control action id which is used to look up the action
	 * @param notProvidedVariable the notProvidedVariables to remove
	 * @return return whether the remove was successful or not, also returns false
	 * 			if the list is null or the uuid is not contained in the list 
	 */
	public boolean removeCANotProvidedVariable(UUID caID, UUID notProvidedVariable) {
		if(this.controlActionController.removeNotProvidedVariable(caID, notProvidedVariable)){
			setUnsavedAndChanged();
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * remove the uuid of a process variable component from the list
	 * of variables depending on this control action when provided
	 * 
	 * @param caID the control action id which is used to look up the action
	 * @param providedVariable the providedVariable to remove
	 * @return return whether the remove was successful or not, also returns false
	 * 			if the list is null or the uuid is not contained in the list 
	 */
	public boolean removeCAProvidedVariable(UUID caID, UUID providedVariable) {
		if(this.controlActionController.removeProvidedVariable(caID, providedVariable)){
			setUnsavedAndChanged();
			return true;
		}
		return false;
	}
	public boolean usesHAZXData(){
		if(this.controlActionController.usesHAZXData()) return true;
		if(this.controlStructureController.usesHAZXData()) return true;

		if(this.ignoreLtlValue != null) return true;
		return false;
	}
	
	/**
	 *  this function returns or creates (if null is stored)
	 *  a value component which can be referenced as null ignore component by
	 *  ACTS, so that in XSTPA a value can be set to '(don't care)' to
	 *  ignore its value in the LTL Formula
	 *    
	 * @return a value Component with the label '(don't care)'
	 */
	public IRectangleComponent getIgnoreLTLValue(){
		if(this.ignoreLtlValue == null){
			this.ignoreLtlValue = new Component("(don't care)", new Rectangle(), ComponentType.PROCESS_VALUE);
		}
		return this.ignoreLtlValue;
	}
	
	public List<ILTLProvider> getAllRefinedRules(){
		
		return this.controlActionController.getAllRefinedRules();
	}
	
	public ILTLProvider getRefinedRule(UUID id){
	
		for(ILTLProvider rule : this.controlActionController.getAllRefinedRules()){
			if(rule.getLtlProperty().equals(id)){
				return rule;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param ucaLinks
	 * @param combies
	 * @param ltlExp
	 * @param rule
	 * @param ruca
	 * @param constraint
	 * @param nr
	 * @param caID
	 * @param type the Type of the context the rule should be generated for one of the <code>TYPE</code> constants
	 * 				Defined in IValueCombie
	 * 
	 * @see IValueCombie
	 * @return the id of rule which has been added if 
	 */
	public UUID addRefinedRule(List<UUID> ucaLinks,String combies,String ltlExp,String rule,String ruca,String constraint,int nr,UUID caID, String type){
		UUID newRuleId = this.controlActionController.addRefinedRule(ucaLinks, ltlExp, rule, ruca, constraint, nr, caID,type, combies);
		if(newRuleId != null){
			setUnsavedAndChanged(ObserverValue.Extended_DATA);
		}
		return newRuleId;
	}
	
	public UUID updateRefinedRule(UUID ruleID,List<UUID> ucaLinks,String combies,String ltlExp,String rule,String ruca,String constraint,int nr,UUID caID, String type){
		boolean changed=false;
		boolean updated = false;
		UUID resultID = ruleID;
		for(ILTLProvider provider: this.controlActionController.getAllRefinedRules()){
			if(provider.getRuleId().equals(ruleID)){
				changed = changed ||((RefinedSafetyRule) provider).setLtlProperty(ltlExp);
				changed = changed ||((RefinedSafetyRule) provider).setRefinedSafetyConstraint(constraint);
				changed = changed ||((RefinedSafetyRule) provider).setRefinedUCA(ruca);
				changed = changed ||((RefinedSafetyRule) provider).setSafetyRule(rule);
				changed = changed ||((RefinedSafetyRule) provider).setUCALinks(ucaLinks);
				changed = changed ||((RefinedSafetyRule) provider).setNumber(nr);
				changed = changed ||((RefinedSafetyRule) provider).setCaID(caID);
				changed = changed ||((RefinedSafetyRule) provider).setType(type);
				changed = changed ||((RefinedSafetyRule) provider).setCriticalCombies(combies);
				updated = true;
				resultID = ruleID;
			}
		}
		if(!updated){
			resultID = addRefinedRule(ucaLinks, combies, ltlExp, rule, ruca, constraint, nr, caID, type);
		}
		if(changed){
			setUnsavedAndChanged(ObserverValue.Extended_DATA);
		}
		return resultID;
			
	}
	
	/**
	 * 
	 * @param removeAll whether all currently stored RefinedSafetyRule objects should be deleted<br>
	 * 					when this is true than the ruleId will be ignored
	 * @param ruleId an id of a RefinedSafetyRule object stored in a controlAction 
	 * 
	 * @return whether the delete was successful or not, also returns false if the rule could not be found or the 
	 * 					id was illegal
	 */
	public boolean removeRefinedSafetyRule(boolean removeAll, UUID ruleId){
		if(this.controlActionController.removeSafetyRule(removeAll, ruleId)){
			setUnsavedAndChanged();
			return true;
		}
		return false;
	}
	
	@Override
	public List<ILTLProvider> getLTLPropertys(){
		return getAllRefinedRules();
	}

	@Override
	public Map<String, List<String>> getValuesTOVariables() {
		
		Map<String, List<String>> resultMap = new HashMap<>();
		List<String> valueNames;
		for (IRectangleComponent parentComponent:getRoot().getChildren()) {
			if (parentComponent.getComponentType().name().equals("CONTROLLER")) {
		    	  
				// get the process models
				for (IRectangleComponent tempPM :  parentComponent.getChildren()) {
		    		  // get the variables
		    		  for (IRectangleComponent tempPMV : tempPM.getChildren()) {
		    			  valueNames = new ArrayList<>();
		    			  // get the values and add the new object to the processmodel list
		    			  for (IRectangleComponent tempPMVV : tempPMV.getChildren()) {
		    				  valueNames.add(tempPMVV.getText());
		    			  }
		    			  resultMap.put(tempPMV.getText(), valueNames);
		    		  }
				}
			}
		}
		return resultMap;
	}

}
