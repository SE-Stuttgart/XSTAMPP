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

package xstampp.astpa.model.interfaces;

import java.util.List;

import org.eclipse.swt.custom.StyleRange;

import xstampp.model.IDataModel;

/**
 * Interface to the data model for the system description view.
 * 
 * @author Sebastian Sieber
 * 
 */
public interface ISystemDescriptionViewDataModel extends IDataModel {

	/**
	 * Getter for the projectName.
	 * 
	 * @author Sebastian Sieber
	 * @return projectName
	 */
	@Override
	String getProjectName();

	/**
	 * Getter for the projectDescription.
	 * 
	 * @author Sebastian Sieber
	 * @return projectDescription
	 */
	String getProjectDescription();

	/**
	 * Setter for the projectName. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#PROJECT_NAME}
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param projectName
	 *            project name
	 * @return true if the project name has been set
	 * 
	 */
	@Override
	boolean setProjectName(String projectName);

	/**
	 * Setter for the projectDescription. <br>
	 * Triggers an update for
	 * {@link astpa.model.ObserverValue#PROJECT_DESCRIPTION}
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param projectDescription
	 *            project description
	 * @return true if the description has been set
	 */
	boolean setProjectDescription(String projectDescription);

	/**
	 * Adds a style range
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param styleRange
	 *            the new style range
	 * @return true, if the style range has been added
	 */
	boolean addStyleRange(StyleRange styleRange);

	/**
	 * Getter for style ranges.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @return the style ranges as list
	 */
	List<StyleRange> getStyleRanges();

	/**
	 * Getter for the style ranges
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the style ranges as array
	 */
	StyleRange[] getStyleRangesAsArray();
}
