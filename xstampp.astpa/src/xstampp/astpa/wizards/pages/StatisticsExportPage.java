package xstampp.astpa.wizards.pages;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import xstampp.ui.wizards.AbstractExportPage;

public class StatisticsExportPage extends AbstractExportPage {

	public StatisticsExportPage(String pageName, String pluginID) {
		super(pageName, pluginID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean asOne() {
		return true;
	}

	@Override
	public void createControl(Composite parent) {
		parent.setLayout(new FormLayout());
		Composite projectChooser = addProjectChooser(parent, new FormAttachment());
		addPathComposite(new FormAttachment(projectChooser), null, parent, PathComposite.DIR_DIALOG);
		
	}

}
