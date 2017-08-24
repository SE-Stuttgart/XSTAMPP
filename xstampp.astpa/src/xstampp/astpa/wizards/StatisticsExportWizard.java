package xstampp.astpa.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import xstampp.astpa.Activator;
import xstampp.astpa.wizards.pages.StatisticsExportPage;

public class StatisticsExportWizard extends Wizard implements INewWizard {

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		addPage(new StatisticsExportPage("Export Statistics", Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
