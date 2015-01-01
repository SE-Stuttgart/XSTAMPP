package xstampp.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public class SeperatorContribution extends WorkbenchWindowControlContribution {

	@Override
	protected Control createControl(Composite parent) {
		// Create a composite to place the label in
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new FillLayout());
		Label sep= new Label(comp, SWT.SEPARATOR);
		return null;
	}

}
