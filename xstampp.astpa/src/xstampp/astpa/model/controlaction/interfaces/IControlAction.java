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

package xstampp.astpa.model.controlaction.interfaces;

import java.util.List;

import xstampp.astpa.model.ITableModel;
import xstampp.astpa.model.controlaction.UnsafeControlActionType;

/**
 * Interface for a control action that only shows the getters
 * 
 * @author Fabian Toth
 * 
 */
public interface IControlAction extends ITableModel {

	/**
	 * Gets all unsafe control actions of this control action
	 * 
	 * @return all unsafe control actions of this control action
	 * 
	 * @author Fabian Toth
	 */
	List<IUnsafeControlAction> getUnsafeControlActions();

	/**
	 * Gets all unsafe control actions of the given type of this control action
	 * 
	 * @param unsafeControlActionType
	 *            the type of the unsafe control actions
	 * @return all unsafe control actions of the given type of this control
	 *         action
	 * 
	 * @author Fabian Toth
	 */
	List<IUnsafeControlAction> getUnsafeControlActions(
			UnsafeControlActionType unsafeControlActionType);
}
