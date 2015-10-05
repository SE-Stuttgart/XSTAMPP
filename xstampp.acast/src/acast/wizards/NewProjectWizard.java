package acast.wizards;

import java.util.HashMap;
import java.util.Map;

import messages.Messages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import xstampp.util.NewProjectPage;
import xstampp.util.STPAPluginUtils;

public class NewProjectWizard extends Wizard implements INewWizard {
	
	private final NewProjectPage page;

	
	public NewProjectWizard() {
		ImageDescriptor desc = PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor("icons/branding/i64.png");
		this.page = new NewProjectPage(Messages.CreateNewProject,
				Messages.NewProject, desc, "acc");
		this.addPage(this.page);
	}


	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean performFinish() {
		Map<String, String> values = new HashMap<>();
		values.put("acast.new.name", this.page.getNewProjectName()); //$NON-NLS-1$
		values.put("acast.new.path", this.page.getNewProjectPath()); //$NON-NLS-1$
		STPAPluginUtils.executeParaCommand("cast.commands.new", values);
		return true;
	}

}
