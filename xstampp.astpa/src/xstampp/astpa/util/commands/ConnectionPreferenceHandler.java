package xstampp.astpa.util.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.astpa.Activator;
import xstampp.preferences.IControlStructureConstants;

public class ConnectionPreferenceHandler extends AbstractHandler {

	public ConnectionPreferenceHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if(Activator.getDefault().getPreferenceStore().getBoolean(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS)){
			Activator.getDefault().getPreferenceStore().setValue(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS,Boolean.FALSE);
		}else{
			Activator.getDefault().getPreferenceStore().setValue(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS,Boolean.TRUE);
		}
		return null;
	}

}
