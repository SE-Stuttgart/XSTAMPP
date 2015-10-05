package acast.export.pdf;

import acast.wizards.AbstractExportWizard;
import acast.wizards.pages.PdfExportPage;
import messages.Messages;
import xstampp.preferences.IPreferenceConstants;

public class ReportPDFWizard extends AbstractExportWizard {
	private final PdfExportPage page;

	public ReportPDFWizard() {
		super();
		String projectName = this.getStore().getString(IPreferenceConstants.PROJECT_NAME);
		this.page = new PdfExportPage("PDF Report", projectName);
		this.setExportPage(this.page);
	}

	@Override
	public boolean performFinish() {
		this.getStore().setValue(IPreferenceConstants.COMPANY_NAME,
				this.page.getTextCompany().getText());

		this.getStore().setValue(IPreferenceConstants.COMPANY_LOGO,
				this.page.getTextLogo());

		return this
				.performXSLExport(
						"/fopxsl.xsl", Messages.ExportingPdf, this.page.getDecoChoice()); 
	}

}
