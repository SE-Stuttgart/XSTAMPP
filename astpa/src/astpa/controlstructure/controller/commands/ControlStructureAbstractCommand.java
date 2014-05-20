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



import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.commands.Command;

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
