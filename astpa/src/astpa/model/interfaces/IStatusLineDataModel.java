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

package astpa.model.interfaces;

/**
 * The interface for the status line
 */
public interface IStatusLineDataModel extends IDataModel {
	
	/**
	 * Checks if there are unsaved changes or not
	 * 
	 * @return whether there are unsaved changes or not
	 * 
	 * @author Fabian Toth
	 */
	boolean hasUnsavedChanges();
	
	/**
	 * Getter for the projectName.
	 * 
	 * @return projectName
	 * 
	 * @author Sebastian Sieber
	 */
	String getProjectName();
}
