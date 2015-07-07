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

package xstampp.astpa.controlstructure.controller.policys;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import xstampp.astpa.controlstructure.CSAbstractEditor;
import xstampp.astpa.controlstructure.controller.commands.ComponentRenameCommand;
import xstampp.astpa.controlstructure.controller.editparts.IControlStructureEditPart;
import xstampp.astpa.controlstructure.figure.CSFigure;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.astpa.util.DirectEditor;

/**
 * 
 * CSDirectEditPolicy gives a policy that executes a edit action which is
 * performed directly and in an extra editor
 * 
 * @version 1.0
 * @author Lukas Balzer
 * 
 */
public class CSDirectEditPolicy extends DirectEditPolicy {

	private IRectangleComponent model;
	private String oldName = null;
	private String value = null;
	private IControlStructureEditorDataModel dataModel;
	private final String stepID;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepId
	 *            TODO
	 */
	public CSDirectEditPolicy(IControlStructureEditorDataModel model,
			String stepId) {
		super();
		this.stepID = stepId;

		this.dataModel = model;
	}

	/**
	 * getDirectEditCommand() defines the command which changes the model
	 * 
	 * @return the command which interacts with the model
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#getDirectEditCommand(org
	 *      .eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		String stepID = (String) this.getHost().getViewer()
				.getProperty(CSAbstractEditor.STEP_EDITOR);

		ComponentRenameCommand command = new ComponentRenameCommand(
				this.oldName, this.dataModel, stepID);
		this.oldName = null;
		// The method getHost() calls the componentModel which makes the
		// requests

		Rectangle newLayout = ((DirectEditor) request.getCellEditor())
				.getBounds().getCopy();
		 this.getHost().getFigure().translateToRelative(newLayout);
		command.setModel(this.model);
		command.setNewLayout(newLayout);

		command.setNewName(this.value);

		return command;
	}

	/**
	 * showCurrentEditValue() defines the content of the editor, which is used
	 * to perform this directEdit and updates the Layout of the TextField during
	 * the Edit
	 * 
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#showCurrentEditValue(org
	 *      .eclipse.gef.requests.DirectEditRequest)
	 * 
	 * @author Lukas Balzer
	 */
	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		this.value = (String) request.getCellEditor().getValue();
		if (this.oldName == null) {

			this.model = (IRectangleComponent) this.getHost().getModel();
			this.oldName = this.model.getText();
		}
		if (!this.value.equals(this.oldName)) {
			((CSFigure) this.getHostFigure())
					.setForegroundColor(ColorConstants.white);
		}

	}

	@Override
	public IControlStructureEditPart getHost() {
		return (IControlStructureEditPart) super.getHost();
	}
}
