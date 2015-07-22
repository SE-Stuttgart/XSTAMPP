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

import java.util.UUID;

import messages.Messages;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.requests.CreationFactory;

import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * This class is used to create new object instance of ComponentModel
 * 
 * @version 1.0
 * @author Aliaksei Babkovich
 * 
 */

public class CSModelCreationFactory implements CreationFactory {

	private ComponentType type;
	private final IControlStructureEditorDataModel dataModel;

	/**
	 * 
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * @param type
	 *            the Type of the model which should be constructed
	 * @param model
	 *            The DataModel which contains all model classes
	 * 
	 */
	public CSModelCreationFactory(ComponentType type,
			IControlStructureEditorDataModel model) {
		this.type = type;
		this.dataModel = model;
	}

	/**
	 * @return the requested component object
	 * @see Component
	 */
	@Override
	public Object getNewObject() {
		String text;

		switch (this.type) {
		case CONTROLACTION: {
			text = Messages.ControlAction;
			UUID caLink = this.dataModel.addControlAction(text,
					Messages.DescriptionOfThisControlAction);
			return new Component(caLink, text, new Rectangle(), this.type);
		}
		case ACTUATOR: {
			text = Messages.Actuator;
			break;
		}
		case DASHEDBOX:  {

			text = Messages.DashedBox;
			break;
		}
		case CONTAINER:{

			text = Messages.DashedBox;
			break;
			
		}
		case CONTROLLER: {
			text = Messages.Controller;
			break;
		}
		case CONTROLLED_PROCESS: {
			text = Messages.ControlledProcess;
			break;
		}
		case PROCESS_MODEL: {
			text = Messages.ProcessModel;
			break;
		}
		case PROCESS_VARIABLE: {
			text = Messages.ProcessVariable;
			break;
		}
		case PROCESS_VALUE: {
			text = Messages.ProcessValue;
			break;
		}
		case SENSOR: {
			text = Messages.Sensor;
			break;
		}
		case TEXTFIELD: {
			text = Messages.TextBox;
			break;
		}
		default: {
			text = ""; //$NON-NLS-1$
			break;
		}
		}
		return new Component(text, new Rectangle(), this.type);

	}

	@Override
	public Object getObjectType() {
		return this.type;
	}

}
