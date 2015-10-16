package xstampp.astpa.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import xstampp.astpa.Activator;

/**
*
* @author Lukas Balzer
* @since 2.0.0
*
*/
public class StpaPreferencePage  extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage {

	public StpaPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
	}

	@Override
	public void init(IWorkbench workbench) {

		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("XSTAMPP Preferences");
		
	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		
	}

}
