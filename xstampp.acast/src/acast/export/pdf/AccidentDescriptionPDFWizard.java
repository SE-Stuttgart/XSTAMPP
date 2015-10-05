package acast.export.pdf;

import java.io.File;

import acast.export.TableExportPage;
import acast.jobs.ExportJob;
import acast.ui.accidentDescription.AccidentDescriptionView;
import acast.wizards.AbstractExportWizard;
import messages.Messages;
import xstampp.ui.common.ProjectManager;

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
