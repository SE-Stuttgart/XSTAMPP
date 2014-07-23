package astpa.export.stepImages;

import messages.Messages;
import astpa.controlstructure.CSEditor;
import astpa.export.AbstractExportWizard;
import astpa.export.pages.ControlStructureExportPage;
import astpa.preferences.IPreferenceConstants;

/**
 * The Wizard for Exporting the control structure  as image
 * @author Lukas Balzer
 *
 */
public class ControlStructureWizard extends AbstractExportWizard{
	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public ControlStructureWizard() {
		super(CSEditor.ID);
		String[] filters= new String[] {"*.png", "*.jpg", "*.bmp"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setExportPage(new ControlStructureExportPage(filters,Messages.ControlStructure,
										this.getStore().getString(IPreferenceConstants.PROJECT_NAME)));
	}

	@Override
	public boolean performFinish() {
		int offset=((ControlStructureExportPage) this.getExportPage()).getImgOffset();
		return performNormalExport(offset);
	}
	
	

}
