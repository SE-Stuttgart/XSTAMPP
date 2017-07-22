package acast.model.interfaces;

import java.util.List;

import org.eclipse.swt.custom.StyleRange;

import xstampp.model.IDataModel;

/**
 * @author Martin
 *
 */
public interface IAccidentDescriptionViewDataModel extends IDataModel {

	/**
	 * get the path of the picture which is currently selected in the list
	 * 
	 * @return
	 */
	String getCurrentPicture();

	/**
	 * sets the current picture
	 * 
	 * @param picture
	 * @return
	 */
	boolean setCurrentPicture(String picture);

	/**
	 * get the text of the AccidentCompany textbox
	 * 
	 * @return
	 */
	String getAccidentCompany();

	/**
	 * get the text of the AccidentLocation textbox
	 * 
	 * @return
	 */
	String getAccidentLocation();

	/**
	 * get the choosen Date in the AccidentView
	 * 
	 * @return
	 */
	String getAccidentDate();

	/**
	 * sets the text in AccidentCompany textbox
	 * 
	 * @param company
	 * @return
	 */
	boolean setAccidentCompany(String company);

	/**
	 * sets the text in the AccidentLocation textbox
	 * 
	 * @param Location
	 * @return
	 */
	boolean setAccidentLocation(String Location);

	/**
	 * sets the date
	 * 
	 * @param Date
	 * @return
	 */
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

	/**
	 * sets the picturelist for the accidentPictureView
	 * 
	 * @param pictureList
	 *            with filePaths of the choosen pictures
	 * @return
	 */
	boolean setPictureList(String[] pictureList);

	/**
	 * get the currentPictureList
	 * 
	 * @return
	 */
	List<String> getPictureList();
}
