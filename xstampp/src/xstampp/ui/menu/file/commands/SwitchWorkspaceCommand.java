package xstampp.ui.menu.file.commands;

import java.util.prefs.Preferences;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.preferences.IPreferenceConstants;
import xstampp.util.ChooseWorkLocation;


/**
 * this handler sets {@link IPreferenceConstants#WS_REMEMBER} and restarts the Platform
 * causing the system to ask for the prefered workspace on startup
 * 
 * @author Lukas Balzer
 * @since 1.0
 */
public class SwitchWorkspaceCommand extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Preferences.userNodeForPackage(ChooseWorkLocation.class).putBoolean(
				IPreferenceConstants.WS_REMEMBER, false);
		PlatformUI.getWorkbench().restart();
		return null;
	}

}
