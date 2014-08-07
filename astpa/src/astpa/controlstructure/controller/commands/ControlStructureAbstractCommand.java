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

package astpa.controlstructure.controller.commands;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.commands.Command;

import astpa.controlstructure.CSEditor;
import astpa.controlstructure.CSEditorWithPM;
import astpa.model.controlstructure.components.ComponentType;
import astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * @author Lukas
 * 
 */
public abstract class ControlStructureAbstractCommand extends Command {
	
	private final IControlStructureEditorDataModel dataModel;
	private IFigure feedbackLayer;
	private final String stepID;
	
	protected static final List<ComponentType> COMPONENTS_STEP1= new ArrayList<ComponentType>(){
		private static final long serialVersionUID = 1L;
		{
			add(ComponentType.ACTUATOR);
			add(ComponentType.CONTROLLER);
			add(ComponentType.CONTROLACTION);
			add(ComponentType.CONTROLLED_PROCESS);
			add(ComponentType.SENSOR);
			add(ComponentType.TEXTFIELD);
		}
	};
	
	protected static final List<ComponentType> COMPONENTS_STEP3= new ArrayList<ComponentType>(){
		private static final long serialVersionUID = 1L;

		{
			add(ComponentType.PROCESS_MODEL);
			add(ComponentType.PROCESS_VALUE);
			add(ComponentType.PROCESS_VARIABLE);
		}
	};
	
	protected static final HashMap<String, List<ComponentType>> COMPONENTS_MAP= new HashMap<String,List<ComponentType>>(){
		private static final long serialVersionUID = 1L;

		{
			put(CSEditor.ID,COMPONENTS_STEP1);
			put(CSEditorWithPM.ID,COMPONENTS_STEP3);
			
		}
	};
	
	/**
	 * 
	 * @author Lukas Balzer
	 *
	 * @param model The DataModel which contains all model classes
	 * @param stepID the stepEditor ID
	 */
	public ControlStructureAbstractCommand(IControlStructureEditorDataModel model, String stepID) {
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
	 * @param layer the feedback layer which is used to display the feedback
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
