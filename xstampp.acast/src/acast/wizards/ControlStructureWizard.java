package acast.wizards;

import messages.Messages;
import xstampp.astpa.util.jobs.CSExportJob;

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
		super("acast.steps.step2_1");
		String[] filters = new String[] { "*.png", "*.jpg", "*.bmp" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.setExportPage(new ControlStructureExportPage(filters, Messages.ControlStructure));
	}

	protected ControlStructureWizard(String id) {
		super(id);
	}

	@Override
	public boolean performFinish() {
		return this.performFinish("acast.steps.step2_1");
	}

	protected boolean performFinish(String editorID) {
		int offset = ((ControlStructureExportPage) this.getExportPage()).getImgOffset();
		boolean decoCoice = ((ControlStructureExportPage) this.getExportPage()).getDecoChoice();
		CSExportJob job = new CSExportJob(this.getExportPage().getExportPath(), editorID,
				this.getExportPage().getProjectID(), offset, decoCoice);
		job.setPreview(true);
		job.schedule();
		return true;
	}

}
