package acast.model.interfaces;

import java.util.List;

import org.eclipse.swt.custom.StyleRange;

import xstampp.model.IDataModel;

public interface IAccidentDescriptionViewDataModel extends IDataModel{

	String getCurrentPicture();
	
	boolean setCurrentPicture(String picture);
	
	String getAccidentCompany();
	
	String getAccidentLocation();
	
	String getAccidentDate();
	
	boolean setAccidentCompany(String company);
	
	boolean setAccidentLocation(String Location);
	
	boolean setAccidentDate(String Date);
	
	
	/**
	 * Getter for the projectName.
	 * 
	 * @return projectName
	 */
	@Override
	String getProjectName();
	

	/**
	 * Getter for the projectDescription.
	 * 
	 * @return projectDescription
	 */
	String getAccidentDescription();

	/**
	 * Setter for the projectName. <br>
	 * Triggers an update for {@link astpa.model.ObserverValue#PROJECT_NAME}
	 * 
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
	 * 
	 * @param projectDescription
	 *            project description
	 * @return true if the description has been set
	 */
	boolean setAccidentDescription(String projectDescription);

	
	/**
	 * Adds a style range
	 * 
	 */
	boolean addStyleRange(StyleRange styleRange);

	/**
	 * Getter for style ranges.
	 * 
	 */
	List<StyleRange> getStyleRanges();

	/**
	 * Getter for the style ranges
	 * 
	 */
	StyleRange[] getStyleRangesAsArray();
	
	boolean setPictureList(String[] pictureList);
	
    List<String> getPictureList();
}
