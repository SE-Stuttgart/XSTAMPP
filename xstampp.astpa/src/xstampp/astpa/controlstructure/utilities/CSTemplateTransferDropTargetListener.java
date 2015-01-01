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

package xstampp.astpa.controlstructure.utilities;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;

import xstampp.astpa.controlstructure.controller.factorys.CSModelCreationFactory;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * 
 * this class is our own derived version of TemplateTransferDropTargetListener
 * 
 * @author Aliaksei Babkovich
 * @version 1.0
 * 
 */
public class CSTemplateTransferDropTargetListener extends
		TemplateTransferDropTargetListener {

	private final IControlStructureEditorDataModel dataModel;

	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param viewer
	 *            the EditPartViewer to which it shall drop
	 * @param model
	 *            The data model Controller
	 */
	public CSTemplateTransferDropTargetListener(EditPartViewer viewer,
			IControlStructureEditorDataModel model) {
		super(viewer);
		this.dataModel = model;
	}

	@Override
	protected CreationFactory getFactory(Object template) {
		return new CSModelCreationFactory((ComponentType) template,
				this.dataModel);
	}

}
