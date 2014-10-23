package astpa.wizards.stepImages;

import java.util.ArrayList;
import java.util.List;

import messages.Messages;
import astpa.controlstructure.CSEditor;
import astpa.preferences.IPreferenceConstants;
import astpa.wizards.AbstractExportWizard;
import astpa.wizards.pages.ControlStructureExportPage;

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

	protected ControlStructureWizard(String id) {
		super(id);
	}

	@Override
	public boolean performFinish() {
		List<Object> values= new ArrayList<>();
		values.add(this.getExportPage().getExportPath());
		values.add(((ControlStructureExportPage) this.getExportPage()).getImgOffset());
		values.add(((ControlStructureExportPage) this.getExportPage()).getDecoChoice());
		return performNormalExport(values.toArray());
	}
	
	

}
