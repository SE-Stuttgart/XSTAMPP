package xstampp.astpa.wizards.stepImages;

import messages.Messages;
import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.util.jobs.CSExportJob;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.astpa.wizards.pages.ControlStructureExportPage;

/**
 * The Wizard for Exporting the control structure as image
 * 
 * @author Lukas Balzer
 * 
 */
public class ControlStructureWizard extends AbstractExportWizard {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public ControlStructureWizard() {
		super(CSEditor.ID);
		String[] filters = new String[] {  "*.png", "*.jpg", "*.bmp" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.setExportPage(new ControlStructureExportPage(filters,
				Messages.ControlStructure + Messages.AsImage));
	}

	protected ControlStructureWizard(String id) {
		super(id);
	}

	@Override
	public boolean performFinish() {
		return this.performFinish(CSEditor.ID);
	}

	protected boolean performFinish(String editorID) {
		int offset = ((ControlStructureExportPage) this.getExportPage())
				.getImgOffset();
		boolean decoCoice = ((ControlStructureExportPage) this.getExportPage())
				.getDecoChoice();
		CSExportJob job = new CSExportJob(this.getExportPage().getExportPath(),
				editorID, this.getExportPage().getProjectID(), offset,
				decoCoice);
		job.setPreview(true);
		job.schedule();
		return true;
	}

}
