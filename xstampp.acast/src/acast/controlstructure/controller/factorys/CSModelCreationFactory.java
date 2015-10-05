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

package acast.controlstructure.controller.factorys;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.requests.CreationFactory;

import acast.model.causalfactor.ICausalComponent;
import acast.model.controlstructure.components.Component;
import acast.model.controlstructure.components.ComponentType;
import acast.model.interfaces.IControlStructureEditorDataModel;
import messages.Messages;

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
	private int count;

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
	public CSModelCreationFactory(ComponentType type, IControlStructureEditorDataModel model) {
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
			count = 1;
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getComponentType().equals(this.type)) {
					count++;
				}
			}
			text = Messages.ControlAction + count;
			UUID caLink = this.dataModel.addControlAction(text, Messages.DescriptionOfThisControlAction);
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getText().equals(text)) {
					text = text + " (2)";
				}

			}

			return new Component(caLink, text, new Rectangle(), this.type);
		}
		case ACTUATOR: {
			count = 1;
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getComponentType().equals(this.type)) {
					count++;
				}
			}
			text = Messages.Actuator + count;
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getText().equals(text)) {
					text = text + " (2)";
				}

			}
			break;
		}
		case DASHEDBOX: {

			text = Messages.DashedBox;
			break;
		}
		case CONTAINER: {

			text = "?";
			break;

		}
		case CONTROLLER: {
			count = 1;
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getComponentType().equals(this.type)) {
					count++;
				}
			}
			text = Messages.Controller + count;
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getText().equals(text)) {
					text = text + " (2)";
				}

			}
			break;
		}
		case CONTROLLED_PROCESS: {
			count = 1;
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getComponentType().equals(this.type)) {
					count++;
				}
			}
			text = Messages.ControlledProcess + count;
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getText().equals(text)) {
					text = text + " (2)";
				}

			}
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
			count = 1;
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getComponentType().equals(this.type)) {
					count++;
				}
			}
			text = Messages.Sensor + count;
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getText().equals(text)) {
					text = text + " (2)";
				}

			}
			break;
		}
		case TEXTFIELD: {
			count = 1;
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getComponentType().equals(this.type)) {
					count++;
				}
			}
			text = Messages.TextBox;
			for (ICausalComponent x : this.dataModel.getCasualComponents()) {
				if (x.getText().equals(text)) {
					text = text + " (2)";
				}

			}
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
