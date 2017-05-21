/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.model.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.model.IDataModel;

/**
 * This class provides methods for the DataModel access of the Corresponding
 * Safety Constraints
 * 
 * @author Fabian Toth
 * 
 */
public interface ICorrespondingSafetyConstraintDataModel extends IDataModel,ICommonTables {

	/**
	 * Gets all corresponding safety constraints
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the list of all corresponding safety constraints
	 */
	List<ICorrespondingUnsafeControlAction> getAllUnsafeControlActions();

	/**
	 * Sets the corresponding safety constraint of the unsafe control action
	 * which is identified by the given id.<br>
	 * Triggers an update for
	 * {@link astpa.model.ObserverValue#UNSAFE_CONTROL_ACTION}
	 * 
	 * @author Fabian Toth
	 * 
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action
	 * @param safetyConstraintDescription
	 *            the text of the corresponding safety constraint
	 * @return the id of the corresponding safety constraint. null if the action
	 *         fails
	 */
	UUID setCorrespondingSafetyConstraint(UUID unsafeControlActionId,
			String safetyConstraintDescription);

	/**
	 * returns the current id number of the UnsafeControlAction with the given ucaID
	 *  
	 * @param ucaID the UnsafeControlAction id
	 * @return the current id
	 */
	int getUCANumber(UUID ucaID);

  /**
   * Returns the the control action as {@link ITableModel} for which the unsafe control action
   * belonging to the given {@link UUID}. If the id is not registered for a uca than <i>null</i> is
   * returned.
   * 
   * @param ucaId
   *          an {@link UUID} for a registered uca in the system
   * @return a {@link ITableModel} for a control action or <i>null</i>
   */
  ITableModel getControlActionForUca(UUID ucaId);
}
