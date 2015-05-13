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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import messages.Messages;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import xstampp.astpa.controlstructure.controller.editparts.ActuatorEditPart;
import xstampp.astpa.controlstructure.controller.editparts.CSAbstractEditPart;
import xstampp.astpa.controlstructure.controller.editparts.CSConnectionEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ControlActionEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ControlledProcessEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ControllerEditPart;
import xstampp.astpa.controlstructure.controller.editparts.DashedBoxEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ProcessModelEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ProcessValueEditPart;
import xstampp.astpa.controlstructure.controller.editparts.ProcessVariableEditPart;
import xstampp.astpa.controlstructure.controller.editparts.RootEditPart;
import xstampp.astpa.controlstructure.controller.editparts.SensorEditPart;
import xstampp.astpa.controlstructure.controller.editparts.TextFieldEditPart;
import xstampp.astpa.controlstructure.figure.CSAnchor;
import xstampp.astpa.controlstructure.figure.CSFlyAnchor;
import xstampp.astpa.controlstructure.figure.IAnchorFigure;
import xstampp.astpa.model.controlstructure.interfaces.IAnchor;
import xstampp.astpa.model.controlstructure.interfaces.IComponent;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

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
	private Map<UUID, EditPart> editPartMap = new HashMap<UUID, EditPart>();
	private final String stepId;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepId
	 *            this steps id
	 */
	public CSEditPartFactory(IControlStructureEditorDataModel model,
			String stepId) {
		this.dataModel = model;
		this.stepId = stepId;
	}

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		// the EditPart which acts as the controller in the MVC architecture of
		// gef
		AbstractGraphicalEditPart part = null;
		UUID id;
		switch (((IComponent) model).getComponentType()) {
		case CONTROLACTION: {
			part = new ControlActionEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			UUID caId = ((IRectangleComponent) model).getControlActionLink();
			if ((this.dataModel.getControlAction(caId) == null)
					|| (caId == null)) {
				UUID newLinkId = this.dataModel.addControlAction(
						Messages.ControlAction,
						Messages.DescriptionOfThisControlAction);
				((IRectangleComponent) model).linktoControlAction(newLinkId);
			}
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
		case TEXTFIELD: {
			part = new TextFieldEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		case DASHEDBOX: {
			part = new DashedBoxEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
			break;
		}
		default: {
			part = new RootEditPart(this.dataModel, this.stepId);
			id = ((IRectangleComponent) model).getId();
		}
		}
		// allocates the model to it's controller
		part.setModel(model);
		this.editPartMap.put(id, part);
		return part;
	}

	private CSConnectionEditPart getConnectionFrom(IConnection model) {

		EditPart source = this.editPartMap.get((model).getSourceAnchor()
				.getOwnerId());
		EditPart target = this.editPartMap.get((model).getTargetAnchor()
				.getOwnerId());

		IFigure sourceFigure = ((CSAbstractEditPart) source).getFigure();
		IFigure targetFigure = ((CSAbstractEditPart) target).getFigure();

		IAnchor sourceModel = model.getSourceAnchor();
		IAnchor targetModel = model.getTargetAnchor();

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
}
