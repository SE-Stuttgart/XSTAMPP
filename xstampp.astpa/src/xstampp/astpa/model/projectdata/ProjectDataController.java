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


	@XmlElementWrapper(name = "rangeObjects")
	@XmlElement(name = "range")
	private List<DescriptionObject> rangeObjects;
	
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
	public boolean setProjectName(String projectName) {
		if(this.projectName.equals(projectName)){
			return false;
		}
		this.projectName =projectName;
		return true;
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
	 * @return TODO
	 */
	public boolean setProjectDescription(String projectDescription) {
		if(this.projectDescription.equals(projectDescription)){
			return false;
		}
		this.projectDescription =projectDescription;
		return true;
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
	
	
	public void prepareForExport(){
		this.rangeObjects = new ArrayList<>();
		int next=0;
		
		StyleRange[] ranges = this.styleRanges.toArray(new StyleRange[0]);
		this.styleRanges.clear();
		int i=0;
		while(i < projectDescription.length()){
			
			if(ranges.length > next && ranges[next].start == i){
				StyleRange range = ranges[next];
				this.styleRanges.add(range);
				next++;
				i += range.length;
				DescriptionObject obj = new DescriptionObject();
				try{
					if(range.start + range.length <= projectDescription.length()){
						obj.addRanges(range, projectDescription.substring(range.start,i));
					}else{
						obj.addRanges(range, projectDescription.substring(range.start));
					}
					this.rangeObjects.add(obj);
				}catch(IndexOutOfBoundsException e){
					e.fillInStackTrace();
				}
			}else if(ranges.length > next && ranges[next].start < projectDescription.length()){
				StyleRange range = ranges[next];
				DescriptionObject obj = new DescriptionObject();
				obj.addRanges(null, projectDescription.substring(i,range.start));
				this.rangeObjects.add(obj);
				
			}else{
				DescriptionObject obj = new DescriptionObject();
				obj.addRanges(null, projectDescription.substring(i));
				this.rangeObjects.add(obj);
				i = projectDescription.length();
			}
		}
	}
	
	public void prepareForSave(){
		this.rangeObjects= null;
	}
}
