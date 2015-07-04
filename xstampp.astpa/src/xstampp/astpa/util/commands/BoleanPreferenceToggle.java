package xstampp.astpa.util.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.astpa.Activator;

public class BoleanPreferenceToggle extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String preference = event.getParameter("xstampp.astpa.command.preference");
		if(preference == null){
			return null;
		}
		
		Boolean current = Activator.getDefault().getPreferenceStore().getBoolean(preference);
		Activator.getDefault().getPreferenceStore().setValue(preference, !current);
		return null;
	}

}
