package testplugin.wizard;

import messages.Messages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import testplugin.datamodel.TestController;
import xstampp.ui.common.ProjectManager;
import xstampp.util.NewProjectPage;

public class NewWizard extends Wizard implements INewWizard {

	private final NewProjectPage page;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public NewWizard() {
		ImageDescriptor desc = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(DEFAULT_IMAGE);
		this.page = new NewProjectPage(Messages.CreateNewProject,
				Messages.NewProject, desc,"txt");
		this.addPage(this.page);
	}

	@Override
	public boolean performFinish() {

		ProjectManager.getContainerInstance().startUp(TestController.class, this.page.getNewProjectName(),
													this.page.getNewProjectPath());
		return true;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		// initially empty

	}
}