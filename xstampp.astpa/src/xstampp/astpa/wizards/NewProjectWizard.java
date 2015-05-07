package xstampp.astpa.wizards;

import java.util.HashMap;
import java.util.Map;

import messages.Messages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import xstampp.astpa.Activator;
import xstampp.util.NewProjectPage;
import xstampp.util.STPAPluginUtils;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class NewProjectWizard extends Wizard implements INewWizard {

	private final NewProjectPage page;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public NewProjectWizard() {
		ImageDescriptor desc = Activator
				.getImageDescriptor("icons/branding/i64.png"); //$NON-NLS-1$
		this.page = new NewProjectPage(Messages.CreateNewProject,
				Messages.NewProject, desc,"haz");
		this.addPage(this.page);
	}

	@Override
	public boolean performFinish() {

		Map<String, String> values = new HashMap<>();
		values.put("astpa.new.name", this.page.getNewProjectName()); //$NON-NLS-1$
		values.put("astpa.new.path", this.page.getNewProjectPath()); //$NON-NLS-1$
		STPAPluginUtils.executeParaCommand("astpa.commands.new", values);
		return true;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		// initially empty

	}
}
