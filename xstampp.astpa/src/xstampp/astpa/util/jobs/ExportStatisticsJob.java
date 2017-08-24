package xstampp.astpa.util.jobs;

import java.io.File;
import java.io.IOException;
import java.util.Observable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import xstampp.astpa.model.DataModelController;
import xstampp.util.XstamppJob;

public class ExportStatisticsJob extends XstamppJob {

	private DataModelController controller;
	private File indexFile;

	/**
	 * 
	 * @param name
	 *            the name of the {@link Job}
	 * @param controller
	 *            the {@link DataModelController} from which the statistical daa
	 *            will be retrieved
	 * @param indexFile
	 *            the index.html file used to display the statistics
	 */
	public ExportStatisticsJob(String name, DataModelController controller, File indexFile) {
		super(name);
		this.controller = controller;
		this.indexFile = indexFile;
	}

	@Override
	protected Observable getModelObserver() {
		return controller;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
		indexFile.mkdirs();
		
		try {
			indexFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
