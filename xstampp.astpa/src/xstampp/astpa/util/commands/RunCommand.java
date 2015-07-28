package xstampp.astpa.util.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.wizards.RunWizard;

public class RunCommand extends AbstractHandler {

	public RunCommand() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell= new Shell(PlatformUI.getWorkbench().getDisplay()); 
		WizardDialog dialog = new WizardDialog(shell, new RunWizard());
		dialog.open();
		return null;
	}

}
