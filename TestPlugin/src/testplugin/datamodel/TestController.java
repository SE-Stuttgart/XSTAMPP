package testplugin.datamodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observer;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import testplugin.Activator;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.util.AbstractLoadJob;

public class TestController implements IDataModel {

	private String projectName;
	private String description ;

	public TestController() {
		this.setDescription("");
	}
	
	@Override
	public void addObserver(Observer observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteObserver(Observer observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Job doSave(File file, Logger log, boolean isUIcall) {
		// TODO Auto-generated method stub
		return new SaveJob("",file.getAbsolutePath(),getDescription());
	}

	@Override
	public int countObservers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean prepareForExport() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void prepareForSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateValue(ObserverValue delete) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getProjectName() {
		// TODO Auto-generated method stub
		return this.projectName;
	}

	@Override
	public void setStored() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasUnsavedChanges() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setProjectName(String projectName) {
		this.projectName = projectName;
		return true;
	}

	@Override
	public void initializeProject() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFileExtension() {
		return "txt";
	}
	
	@Override
	public String getPluginID() {
	
		return Activator.PLUGIN_ID;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}

 class SaveJob extends Job{

	private String path;
	private String desc;
	

	public SaveJob(String name,String path,String desc) {
		super(name);
		this.path = path;
		this.desc = desc;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		System.out.println("saved Test file");
		File f = new File(this.path);
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(f));
			w.write(desc);
			w.close();
			return Status.OK_STATUS;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Status.CANCEL_STATUS;
	}
	 
 }