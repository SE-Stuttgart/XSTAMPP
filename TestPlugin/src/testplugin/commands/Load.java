package testplugin.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;


import org.eclipse.core.runtime.Status;

import testplugin.datamodel.TestController;
import xstampp.util.AbstractLoadJob;


public class Load extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		return new LoadJob();
	}

}

class LoadJob extends AbstractLoadJob{

	public LoadJob() {
		super();
	}
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		this.setController(new TestController());
		this.getController().setProjectName(getFile().getName());
		return Status.OK_STATUS;
	}
	
}
