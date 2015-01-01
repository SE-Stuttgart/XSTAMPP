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

package xstampp.astpa.controlstructure.controller.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.commands.Command;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * @author Lukas
 * 
 */
public abstract class ControlStructureAbstractCommand extends Command {

	private final IControlStructureEditorDataModel dataModel;
	private IFigure feedbackLayer;
	private final String stepID;

	protected static final List<ComponentType> COMPONENTS_STEP1 = new ArrayList<ComponentType>() {
		private static final long serialVersionUID = 1L;
		{
			this.add(ComponentType.ACTUATOR);
			this.add(ComponentType.CONTROLLER);
			this.add(ComponentType.CONTROLACTION);
			this.add(ComponentType.CONTROLLED_PROCESS);
			this.add(ComponentType.SENSOR);
			this.add(ComponentType.TEXTFIELD);
		}
	};

	protected static final List<ComponentType> COMPONENTS_STEP3 = new ArrayList<ComponentType>() {
		private static final long serialVersionUID = 1L;

		{
			this.add(ComponentType.PROCESS_MODEL);
			this.add(ComponentType.PROCESS_VALUE);
			this.add(ComponentType.PROCESS_VARIABLE);
		}
	};

	protected static final HashMap<String, List<ComponentType>> COMPONENTS_MAP = new HashMap<String, List<ComponentType>>() {
		private static final long serialVersionUID = 1L;

		{
			this.put(CSEditor.ID,
					ControlStructureAbstractCommand.COMPONENTS_STEP1);
			this.put(CSEditorWithPM.ID,
					ControlStructureAbstractCommand.COMPONENTS_STEP3);

		}
	};

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepID
	 *            the stepEditor ID
	 */
	public ControlStructureAbstractCommand(
			IControlStructureEditorDataModel model, String stepID) {
		super();
		this.dataModel = model;
		this.stepID = stepID;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return The DataModel which contains all model classes
	 */
	public IControlStructureEditorDataModel getDataModel() {
		return this.dataModel;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param layer
	 *            the feedback layer which is used to display the feedback
	 */
	public void setFeedbackLayer(IFigure layer) {
		this.feedbackLayer = layer;
	}

	protected void deleteFeedback() {
		this.feedbackLayer.getChildren().clear();
		this.feedbackLayer.repaint();
	}

	protected String getStepID() {
		return this.stepID;
	}
}
