package xstampp.ui.menu.file.commands;

import java.util.prefs.Preferences;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.preferences.IPreferenceConstants;
import xstampp.util.ChooseWorkLocation;

public class SwitchWorkspaceCommand extends AbstractHandler {

	public SwitchWorkspaceCommand() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Preferences.userNodeForPackage(ChooseWorkLocation.class).putBoolean(
				IPreferenceConstants.WS_REMEMBER, false);
		PlatformUI.getWorkbench().restart();
		return null;
	}

}
