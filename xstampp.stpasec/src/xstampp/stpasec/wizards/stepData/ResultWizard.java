package xstampp.stpasec.wizards.stepData;

import messages.Messages;
import xstampp.stpasec.Activator;
import xstampp.stpasec.messages.SecMessages;
import xstampp.stpasec.ui.results.ResultEditor;
import xstampp.stpasec.util.jobs.ICSVExportConstants;
import xstampp.stpasec.wizards.AbstractExportWizard;
import xstampp.ui.wizards.CSVExportPage;

public class ResultWizard extends AbstractExportWizard{

	public ResultWizard() {
		super(ResultEditor.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters, SecMessages.Results + Messages.AsDataSet, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.RESULT);
	}
}
