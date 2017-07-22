package acast.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import acast.jobs.CASTLoadJob;

public class Load extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return new CASTLoadJob();
	}

}
