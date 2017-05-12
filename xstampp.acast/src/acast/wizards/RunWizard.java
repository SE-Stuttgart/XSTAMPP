package acast.wizards;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;

import acast.jobs.Run;
import acast.wizards.pages.PdfExportPage;

public class RunWizard extends AbstractExportWizard {

	private final static String OUTPUT = "Output"; //$NON-NLS-1$
	private RunPage page;

	public RunWizard() {
		super();
		Object selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ProjectExplorer.ID).getViewSite().getSelectionProvider().getSelection();
		String projectName = "";
		if (selection instanceof IProjectSelection) {
			projectName = ProjectManager.getContainerInstance()
					.getTitle(((IProjectSelection) selection).getProjectId());
		}
		File outputDir = new File(Platform.getInstanceLocation().getURL().getPath().toString() + OUTPUT);
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}
		this.page = new RunPage("Run Export", projectName);
		this.setExportPage(this.page);
	}

	@Override
	public boolean performFinish() {
		File outputDir = new File(this.page.getExportPath());

		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}
		File exportDir;
		if (this.page.pdfCheckbox.getSelection()) {
			exportDir = new File(outputDir + File.separator + Run.PDF_DIR);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			} else {
				for (File f : exportDir.listFiles()) {
					f.delete();
				}
			}
		}

		if (this.page.csvCheckbox.getSelection()) {
			exportDir = new File(outputDir + File.separator + Run.CSV_DIR);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			} else {
				for (File f : exportDir.listFiles()) {
					f.delete();
				}
			}
		}

		if (this.page.imgCheckbox.getSelection()) {
			exportDir = new File(outputDir + File.separator + Run.IMAGE_DIR);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			} else {
				for (File f : exportDir.listFiles()) {
					f.delete();
				}
			}
		}

		Run runjob = new Run(this.page.getProjectName(), outputDir + File.separator, this.page.getProjectID());
		runjob.exportPDFs(this.page.pdfCheckbox.getSelection());
		runjob.exportCSVs(this.page.csvCheckbox.getSelection());
		runjob.exportImages(this.page.imgCheckbox.getSelection());
		runjob.exportReport(this.page.reportCheckbox.getSelection());
		runjob.setDecoSwitch(this.page.getDecoChoice());
		runjob.schedule();
		return true;
	}

	private class RunPage extends PdfExportPage {

		private Button pdfCheckbox, imgCheckbox, csvCheckbox, reportCheckbox;

		public RunPage(String pageName, String projectName) {
			super(pageName, projectName, PathComposite.DIR_DIALOG);
		}

		@Override
		public void createControl(Composite parent) {
			super.createControl(parent);
			this.pathChooser.setText(
					Platform.getInstanceLocation().getURL().getFile() + OUTPUT + File.separator + getProjectName());
			FormData data = new FormData();
			data.bottom = new FormAttachment(100, 0);
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);

			Composite exportScope = new Composite((Composite) getControl(), SWT.NONE);
			exportScope.setLayoutData(data);

			exportScope.setLayout(new GridLayout(4, true));
			this.pdfCheckbox = new Button(exportScope, SWT.CHECK);
			this.pdfCheckbox.setText(Run.PDF_DIR);
			this.pdfCheckbox.setSelection(true);
			this.pdfCheckbox.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

			this.imgCheckbox = new Button(exportScope, SWT.CHECK);
			this.imgCheckbox.setText(Run.IMAGE_DIR);
			this.imgCheckbox.setSelection(true);
			this.imgCheckbox.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

			this.csvCheckbox = new Button(exportScope, SWT.CHECK);
			this.csvCheckbox.setText(Run.CSV_DIR);
			this.csvCheckbox.setSelection(true);
			this.csvCheckbox.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

			this.reportCheckbox = new Button(exportScope, SWT.CHECK);
			this.reportCheckbox.setText("final PDF Report");
			this.reportCheckbox.setSelection(true);
			this.reportCheckbox.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

			setPageComplete(checkFinish());
		}

		/**
		 * @return the pdfCheckbox
		 */
		public boolean getPdfChoice() {
			return this.pdfCheckbox.getSelection();
		}

		/**
		 * @return the imgCheckbox
		 */
		public boolean getImgChoice() {
			return this.imgCheckbox.getSelection();
		}

		/**
		 * @return the csvCheckbox
		 */
		public boolean getCsvChoice() {
			return this.csvCheckbox.getSelection();
		}

		/**
		 * @return the reportCheckbox
		 */
		public boolean getReportChoice() {
			return this.reportCheckbox.getSelection();
		}

	}
}
