package xstampp.util;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.IServiceLocator;

import xstampp.ui.common.ProjectManager;

/**
 * this class provides useful static methods for the interaction with the plugin
 * 
 * @author Lukas Balzer
 * @since version 2.0.0
 * 
 */
public final class STPAPluginUtils {

	private STPAPluginUtils() {

	}

	/**
	 * Executes a registered command without command values
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param commandId
	 *            the id under which the command is registered in the plugin
	 * @return the command return value or null if non/ the command was not
	 *         executed
	 */
	public static Object executeCommand(String commandId) {
		IServiceLocator serviceLocator = PlatformUI.getWorkbench();
		ICommandService commandService = (ICommandService) serviceLocator
				.getService(ICommandService.class);
		Command command = commandService.getCommand(commandId);
		if (command != null) {
			try {
				return command.executeWithChecks(new ExecutionEvent());
			} catch (ExecutionException | NotDefinedException
					| NotEnabledException | NotHandledException e) {
				ProjectManager.getLOGGER().error(
						"Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			ProjectManager.getLOGGER().error(
					"Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return null;
	}

	/**
	 * Executes a registered command with command values
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param commandId
	 *            the id under which the command is registered in the plugin
	 * @param params
	 *            a map containing values like
	 *            <code> 'ParameterName,value' </code>
	 * @return the command return value or null if non/ the command was not
	 *         executed
	 */
	public static Object executeParaCommand(String commandId,
			Map<String, String> params) {
		IServiceLocator serviceLocator = PlatformUI.getWorkbench();
		ICommandService commandService = (ICommandService) serviceLocator
				.getService(ICommandService.class);
		IHandlerService handlerService = (IHandlerService) serviceLocator
				.getService(IHandlerService.class);
		Command command = commandService.getCommand(commandId);
		if (command == null) {
			ProjectManager.getLOGGER().debug(commandId + " is no valid command id");
			return false;
		}
		ParameterizedCommand paraCommand = ParameterizedCommand
				.generateCommand(command, params);
		if(paraCommand == null){
			ProjectManager.getLOGGER().debug("One of: "+params.toString()+ " is no valid parameter id");
			return false;
		}
		try {
			return handlerService.executeCommand(paraCommand, null);

		} catch (ExecutionException | NotDefinedException
				| NotEnabledException | NotHandledException e) {
			Logger.getRootLogger().error(
					"Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return true;
	}

}
