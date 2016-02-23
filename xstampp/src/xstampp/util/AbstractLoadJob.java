package xstampp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import messages.Messages;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.jobs.Job;

import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;

/**
 *	An abstarct definition of a load job used by Xstampp to load all
 *	projects in the workspace
 *
 * @author Lukas Balzer
 *
 */
public abstract class AbstractLoadJob extends XstamppJob {
	private File file;
	private final Logger log = ProjectManager.getLOGGER();
	private IDataModel controller;
	private File saveFile;
	private List<String> errors;
	/** 
	 *
	 * @author Lukas Balzer
	 *
	 */
	public AbstractLoadJob() {
		super(Messages.loadHaz);
		this.errors = new ArrayList<>();
	}

	


	/**
	 * @return the controller
	 */
	public IDataModel getController() {
		return this.controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(IDataModel controller) {
		this.controller = controller;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * @return the file
	 */
	public File getSaveFile() {
		return this.saveFile;
	}




	/**
	 * @return the log
	 */
	public Logger getLog() {
		return this.log;
	}
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @return a list of errors which occured during the job
	 */
	public List<String> getErrors() {
		return this.errors;
	}
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param saveFile
	 * 		the FIle in which the loaded file should than be saved
	 */
	public void setSaveFile(String saveFile) {
		this.saveFile = new File(saveFile);
	}
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param file
	 * 		the file which is to be loaded
	 */
	public void setFile(String file) {
		this.file = new File(file);
	}
	
	protected void addErrorMsg(String msg){
		this.errors.add(msg);
	}



}