package acast.export.pdf;

import messages.Messages;
import acast.export.TableExportPage;
import acast.ui.accidentDescription.AccidentDescriptionView;
import acast.wizards.AbstractExportWizard;

public class AccidentDescriptionPDFWizard extends AbstractExportWizard{
	
	public AccidentDescriptionPDFWizard() {
		super(AccidentDescriptionView.ID);
		String[] filters = new String[] { "*.pdf" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.setExportPage(new TableExportPage(filters,
				"Accident Description" + Messages.AsPDF));
	}

	@Override
	public boolean performFinish() {
		return this.performXSLExport(				
				"/fopAccidentDescription.xsl", Messages.ExportingPdf, false); 
	}

}
