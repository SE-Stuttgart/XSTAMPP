package xstampp.stpapriv.wizards.stepData;

import messages.Messages;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.stpapriv.Activator;
import xstampp.stpapriv.messages.PrivMessages;
import xstampp.stpapriv.ui.results.ResultEditor;
import xstampp.stpapriv.util.jobs.ICSVExportConstants;
import xstampp.ui.wizards.CSVExportPage;

public class ResultWizard extends AbstractExportWizard{

	public ResultWizard() {
		super(ResultEditor.ID);
		String[] filters = new String[] { "*.csv" }; //$NON-NLS-1$
		this.setExportPage(new CSVExportPage(filters, PrivMessages.Results + Messages.AsDataSet, Activator.PLUGIN_ID));
	}

	@Override
	public boolean performFinish() {
		return this.performCSVExport(ICSVExportConstants.RESULT);
	}
}
