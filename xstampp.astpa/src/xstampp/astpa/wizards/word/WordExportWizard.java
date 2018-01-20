/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.wizards.word;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.preference.IPreferenceStore;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.util.jobs.word.ReportConfiguration;
import xstampp.astpa.util.jobs.word.ReportConfiguration.ReportType;
import xstampp.astpa.util.jobs.word.STPAWordJob;
import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.wizards.PDFExportConfiguration;
import xstampp.ui.wizards.PdfExportPage;

/**
 * Creates wizard for export.
 * 
 * @author Sebastian Sieber
 * 
 */
public class WordExportWizard extends AbstractExportWizard {

  private final PdfExportPage page;

  /**
   * 
   * @author Lukas Balzer
   * 
   */
  public WordExportWizard() {
    super();
    String projectName = this.getStore().getString(
        IPreferenceConstants.PROJECT_NAME);
    this.page = new PdfExportPage(new PDFExportConfiguration("Word Report", projectName), Activator.PLUGIN_ID);
    this.page.setFilterExtensions(new String[] { "*.docx" },
        new String[] { "Word Document" });
    this.setExportPage(this.page);
  }

  @Override
  public boolean performFinish() {

    this.getStore().setValue(IPreferenceConstants.COMPANY_NAME,
        this.page.getTextCompany().getText());

    this.getStore().setValue(IPreferenceConstants.COMPANY_LOGO,
        this.page.getTextLogo());
    if (getExportPage().getExportPath().endsWith("docx")) {
      final DataModelController controller = (DataModelController) ProjectManager
          .getContainerInstance().getDataModel(page.getProjectID());
      ReportConfiguration config = new ReportConfiguration("Final STPA Report", ReportType.FINAL,
          getExportPage().getExportPath());
      config.setTextSize(getExportPage().getContentSize());
      config.setTableHeadSize(getExportPage().getHeadSize());
      config.setTitleSize(getExportPage().getTitleSize());
      config.setDecorate(page.getDecoChoice());
      STPAWordJob exportJob = new STPAWordJob(config, controller, true);
      exportJob.addJobChangeListener(new JobChangeAdapter() {
        @Override
        public void done(IJobChangeEvent event) {
          controller.prepareForSave();
        }
      });
      if (getExportPage().getPageFormat() != null) {
        config.setPageFormat(getExportPage().getPageFormat());
      }
      controller.prepareForExport();
      exportJob.schedule();
      return true;
    }
    return false;
  }

  @Override
  public IPreferenceStore getStore() {
    return Activator.getDefault().getPreferenceStore();
  }
}
