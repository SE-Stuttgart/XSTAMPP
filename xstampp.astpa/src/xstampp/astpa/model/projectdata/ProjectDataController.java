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

package xstampp.astpa.model.projectdata;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import messages.Messages;

import org.eclipse.swt.custom.StyleRange;

/**
 * Class for main informations about the A-STPA-Project
 * 
 * @author Jaqueline Patzek, Fabian Toth
 * 
 */
public class ProjectDataController {

	private String projectName = Messages.NewProject;
	private String projectDescription = ""; //$NON-NLS-1$

	@XmlJavaTypeAdapter(StyleRangeAdapter.class)
	@XmlElementWrapper(name = "styleRanges")
	@XmlElement(name = "styleRange")
	private List<StyleRange> styleRanges;

	/**
	 * Constructor of the project data controller
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public ProjectDataController() {
		this.styleRanges = new ArrayList<>();
	}

	/**
	 * Gets the name/title of the A-STPA-Project
	 * 
	 * @return current name/title of the A-STPA-Project
	 * 
	 * @author Jaqueline Patzek
	 */
	public String getProjectName() {
		return this.projectName;
	}

	/**
	 * Sets the name/title of the A-STPA-Project
	 * 
	 * @param projectName
	 *            which will be set
	 * 
	 * @author Jaqueline Patzek
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Gets the description of the A-STPA-Project
	 * 
	 * @return current description of the A-STPA-Project
	 * 
	 * @author Jaqueline Patzek
	 */
	public String getProjectDescription() {
		return this.projectDescription;
	}

	/**
	 * Sets the description of the A-STPA-Project
	 * 
	 * @param projectDescription
	 *            which has to be set
	 * 
	 * @author Jaqueline Patzek
	 */
	public void setProjectDescription(String projectDescription) {
		this.projectDescription =projectDescription;
	}

	/**
	 * Getter for style ranges.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @return StyleRanges[]
	 */
	public List<StyleRange> getStyleRanges() {
		return this.styleRanges;
	}

	/**
	 * Adds a style range
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param styleRange
	 *            the new style range
	 * @return true, if the style range has been added
	 */
	public boolean addStyleRange(StyleRange styleRange) {
		return this.styleRanges.add(styleRange);
	}

	/**
	 * Getter for the style ranges
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the style ranges as array
	 */
	public StyleRange[] getStyleRangesAsArray() {
		return this.styleRanges
				.toArray(new StyleRange[this.styleRanges.size()]);
	}
}
