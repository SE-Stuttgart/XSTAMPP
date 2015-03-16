package xstampp.ui.common;

import java.util.UUID;

import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public interface IProcessController {

	/**
	 * creates a new project in the given location
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param projectName
	 *            he name of the new project
	 * @param path
	 *            the path where the new project is stored
	 * @return The UUID of the new project
	 */
	public abstract UUID startUp(Class<?> controller, String projectName,
			String path);

	/**
	 * Saves the data model to a file
	 * 
	 * @author Fabian Toth,Lukas Balzer
	 * @param projectId
	 *            the id of the project
	 * 
	 * @return whether the operation was successful or not
	 */
	public abstract boolean saveDataModelAs(UUID projectId);

	/**
	 * Saves the data model to the file in the variable. If this is null
	 * saveDataModelAs() is called
	 * 
	 * @author Fabian Toth,Lukas Balzer
	 * @param projectId
	 *            the id of the project
	 * @param isUIcall TODO
	 * 
	 * @return whether the operation was successful or not
	 */
	public abstract boolean saveDataModel(UUID projectId, boolean isUIcall);

	/**
	 * Loads the data model from a file if it is valid
	 * 
	 * @author Fabian Toth
	 * @author Jarkko Heidenwag
	 * @author Lukas Balzer
	 * 
	 * @return whether the operation was successful or not
	 */
	public abstract boolean importDataModel();

	/**
	 * Loads the data model from a file if it is valid
	 * 
	 * @author Lukas Balzer
	 * @param file
	 *            the file which contains the dataModel
	 * 
	 * @return whether the operation was successful or not
	 */
	public abstract boolean loadDataModelFile(String file,String saveFile);

	/**
	 * Calls the observer of the data model with the given value
	 * 
	 * @author Fabian Toth
	 * 
	 * @param value
	 *            the value to call
	 */
	public abstract void callObserverValue(ObserverValue value);

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param projectId
	 *            the id with which the project data are stored in the DataModel
	 * @return the title of the project
	 */
	public String getTitle(UUID projectId);

	public IDataModel getDataModel(UUID project);

}