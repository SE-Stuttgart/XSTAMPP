package astpa.wizards;

import java.util.HashMap;
import java.util.Map;

import messages.Messages;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.IServiceLocator;

import astpa.Activator;
import astpa.wizards.pages.NewProjectPage;

/**
 *
 * @author Lukas Balzer
 *
 */
public class NewProjectWizard extends Wizard implements INewWizard{

	private final NewProjectPage page;
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public NewProjectWizard() {
		ImageDescriptor desc= Activator.getImageDescriptor("icons/branding/i64.png"); //$NON-NLS-1$
		this.page = new NewProjectPage(Messages.CreateNewProject, Messages.NewProject, desc);
		addPage(this.page);
	}

	@Override
	public boolean performFinish() {
		
		Map<String,String> values= new HashMap<>();
		values.put("astpa.projectName", this.page.getNewProjectName());
		values.put("astpa.projectPath", this.page.getNewProjectPath());
		executeParaCommand("astpa.newproject", values);
		return true;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		// initially empty
		
	}

	private Object executeParaCommand(String commandId,Map<String,String> params) {
		IServiceLocator serviceLocator = PlatformUI.getWorkbench();
		ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);
		IHandlerService handlerService=(IHandlerService) serviceLocator.getService(IHandlerService.class);
		Command command = commandService.getCommand(commandId);
		
		
		ParameterizedCommand paraCommand=ParameterizedCommand.generateCommand(command, params);
		if (command != null) {
			try {
				return handlerService.executeCommand(paraCommand, null);
				
			} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
				Logger.getRootLogger().error("Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			Logger.getRootLogger().error("Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}
		return null;
	}
}
