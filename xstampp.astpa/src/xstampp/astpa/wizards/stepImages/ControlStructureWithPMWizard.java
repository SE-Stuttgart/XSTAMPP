package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.wizards.pages.ControlStructureExportPage;
import xstampp.preferences.IPreferenceConstants;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class ControlStructureWithPMWizard extends ControlStructureWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public ControlStructureWithPMWizard() {
		super(CSEditorWithPM.ID);
		String[] filters = new String[] { "*.png", "*.jpg", "*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.setExportPage(new ControlStructureExportPage(filters,
				Messages.ControlStructureDiagramWithProcessModel));
	}

	@Override
	public boolean performFinish() {
		return super.performFinish(CSEditorWithPM.ID);
	}
}
