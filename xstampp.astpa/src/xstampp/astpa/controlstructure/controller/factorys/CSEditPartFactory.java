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

package xstampp.astpa.controlstructure.controller.factorys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import messages.Messages;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.preference.IPreferenceStore;
import org.w3c.dom.svg.GetSVGDocument;

import xstampp.astpa.controlstructure.controller.editparts.ActuatorEditPart;
import xstampp.astpa.controlstructure.controller.editparts.CSAbstractEditPart;
import xstampp.astpa.controlstructure.controller.editparts.CSConnectionEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ControlActionEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ControlledProcessEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ControllerEditPart;
import xstampp.astpa.controlstructure.controller.editparts.DashedBoxEditPart;
import xstampp.astpa.controlstructure.controller.editparts.IControlStructureEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ProcessModelEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ProcessValueEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ProcessVariableEditPart;
import xstampp.astpa.controlstructure.controller.editparts.RectangleEditPart;
import xstampp.astpa.controlstructure.controller.editparts.RootEditPart;
import xstampp.astpa.controlstructure.controller.editparts.SensorEditPart;
import xstampp.astpa.controlstructure.controller.editparts.TextFieldEditPart;
import xstampp.astpa.controlstructure.figure.CSAnchor;
import xstampp.astpa.controlstructure.figure.CSFlyAnchor;
import xstampp.astpa.controlstructure.figure.IAnchorFigure;
import xstampp.astpa.haz.controlstructure.interfaces.IAnchor;
import xstampp.astpa.model.controlstructure.interfaces.IComponent;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.preferences.IControlStructureConstants;

// import astpa.rcp.controlstucture.model.ComponentModel;

/**
 * 
 * The CSEditPartFactory is called from the Editor class and it creates a
 * connection between a given model and a EditPart which is defined in these
 * class
 * 
 * @version 1.0.3
 * @author Lukas Balzer, Aliaksei Babkovich
 * 
 */
public class CSEditPartFactory implements EditPartFactory {

	private IControlStructureEditorDataModel dataModel;
	private Map<UUID, IControlStructureEditPart> editPartMap = new HashMap<>();
	private final String stepId;
	private List<IControlStructureEditPart> addedParts;
	private IPreferenceStore store;
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepId
	 *            this steps id
	 * @param store The preference store which should be used, see that 
	 * 				the store must contain all keys defined in IControlStructureConstants
	 * @see IControlStructureConstants  
	 */
	public CSEditPartFactory(IControlStructureEditorDataModel model,
			String stepId, IPreferenceStore store) {
		this.dataModel = model;
		this.stepId = stepId;
		this.addedParts = new ArrayList<>();
		this.setStore(store);
	}

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		// the EditPart which acts as the controller in the MVC architecture of
		// gef
		IControlStructureEditPart part = null;
		UUID id;
		switch (((IComponent) model).getComponentType()) {
		case CONTROLACTION: {
			part = new ControlActionEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			UUID caId = ((IRectangleComponent) model).getControlActionLink();
			if ((this.dataModel.getControlActionU(caId) == null)
					|| (caId == null)) {
				UUID newLinkId = this.dataModel.addControlAction(
						Messages.ControlAction,
						Messages.DescriptionOfThisControlAction);
				((IRectangleComponent) model).linktoControlAction(newLinkId);
			}
			if(this.dataModel.getControlActionU(caId).getComponentLink() != id){
				this.dataModel.linkControlAction(caId, id);
			}
			break;
		}
		case CONTAINER:{
			part = new RectangleEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		case ACTUATOR: {
			part = new ActuatorEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		case SENSOR: {
			part = new SensorEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		case CONTROLLER: {
			part = new ControllerEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		case CONTROLLED_PROCESS: {
			part = new ControlledProcessEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		case PROCESS_MODEL: {
			part = new ProcessModelEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		case PROCESS_VARIABLE: {
			part = new ProcessVariableEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		case PROCESS_VALUE: {
			part = new ProcessValueEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		case CONNECTION: {
			id = ((IConnection) model).getId();
			part = this.getConnectionFrom((IConnection) model);
			
			break;
		}
		case ROOT: {
			part = new RootEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		case DASHEDBOX: {
			part = new DashedBoxEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		default: {
			part = new TextFieldEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
		}
		}
		// allocates the model to it's controller
		part.setModel(model);
		part.setPreferenceStore(store);
		this.editPartMap.put(id, part);
		this.addedParts.add(part);
		return part;
	}

	public List<IControlStructureEditPart> fetchNewParts(){
		List<IControlStructureEditPart> tempParts = new ArrayList<>(this.addedParts);
		this.addedParts = new ArrayList<>();
		return tempParts;
	}
	private CSConnectionEditPart getConnectionFrom(IConnection model) {
		
		CSAbstractEditPart source = (CSAbstractEditPart) this.editPartMap.get((model).getSourceAnchor()
				.getOwnerId());
		
		CSAbstractEditPart target = (CSAbstractEditPart)this.editPartMap.get((model).getTargetAnchor()
				.getOwnerId());
		
		IAnchor sourceModel = model.getSourceAnchor();
		IAnchor targetModel = model.getTargetAnchor();
		
		if(source== null){
			sourceModel.setIsFlying(true);
			source = (CSAbstractEditPart) this.editPartMap.get(this.dataModel.getRoot().getId());
		}
		IFigure sourceFigure =	source.getFigure();

		if(target == null){
			targetModel.setIsFlying(true);
			target = (CSAbstractEditPart) this.editPartMap.get(this.dataModel.getRoot().getId());
		}
		
		
		IFigure targetFigure =	target.getFigure();
		

		

		IAnchorFigure sourceAnchor;
		IAnchorFigure targetAnchor;

		if (source instanceof RootEditPart) {
			targetAnchor = new CSAnchor(targetFigure, targetModel);
			sourceAnchor = new CSFlyAnchor(sourceFigure, targetAnchor,
					sourceModel);
			
		} else if (target instanceof RootEditPart) {
			sourceAnchor = new CSAnchor(sourceFigure, sourceModel);
			targetAnchor = new CSFlyAnchor(targetFigure, sourceAnchor,
					targetModel);
		} else {
			sourceAnchor = new CSAnchor(sourceFigure, sourceModel);
			targetAnchor = new CSAnchor(targetFigure, targetModel);
		}
		CSConnectionEditPart part = new CSConnectionEditPart(this.dataModel,
				sourceAnchor, targetAnchor, model.getId(), this.stepId);
		part.setModel(model);
		part.setSource(source);
		part.setTarget(target);
		source.addSourceConnection(part);
		target.addTargetConnection(part);
		return part;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param id
	 *            an UUID which is mapped to an editPart
	 * @return an EditPart for the given id, or null if there is no editPart
	 *         mapped for this id
	 */
	public EditPart getEditPart(UUID id) {
		if (this.editPartMap.get(id) != null) {
			return this.editPartMap.get(id);
		}
		return new ActuatorEditPart(null, this.stepId);
	}

	/**
	 * @param store the store to set
	 */
	public void setStore(IPreferenceStore store) {
		this.store = store;
	}
}
