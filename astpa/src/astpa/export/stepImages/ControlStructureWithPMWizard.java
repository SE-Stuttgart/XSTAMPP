package astpa.export.stepImages;

import messages.Messages;
import astpa.controlstructure.CSEditorWithPM;
import astpa.export.AbstractExportWizard;
import astpa.export.pages.SimpleExportPage;
import astpa.preferences.IPreferenceConstants;

/**
 *
 * @author Lukas Balzer
 *
 */
public class ControlStructureWithPMWizard extends AbstractExportWizard{
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public ControlStructureWithPMWizard() {
		super(CSEditorWithPM.ID);
		String[] filters= new String[] {"*.png", "*.jpg", "*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setExportPage(new SimpleExportPage(filters,Messages.ExportPreferences,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		return performNormalExport();
	}

}
