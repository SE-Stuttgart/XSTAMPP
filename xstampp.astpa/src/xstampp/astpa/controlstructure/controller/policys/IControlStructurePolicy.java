/*******************************************************************************
 * Copyright (c) 2013, 2015 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import org.eclipse.gef.EditPolicy;

import xstampp.astpa.controlstructure.controller.editparts.IControlStructureEditPart;

/**
 * 
 * @author Lukas
 * 
 */
public interface IControlStructurePolicy extends EditPolicy {

	/**
	 * 
	 * @author Lukas
	 * 
	 * @return the host casted to a IControlStructureEditPart
	 * @Override {@link EditPolicy#getHost()}
	 */
	@Override
	IControlStructureEditPart getHost();

}
