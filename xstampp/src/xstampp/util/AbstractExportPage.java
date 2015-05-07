package xstampp.util;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import xstampp.ui.common.ProjectManager;

/**
 * a class to prevent code cloning in the export Pages
 * 
 * @author Lukas Balzer
 * 
 */
public abstract class AbstractExportPage extends AbstractWizardPage implements
		IExportPage {

	private Map<UUID, String> projects;
	private Combo chooseList;
	protected PathComposite pathChooser;

	protected AbstractExportPage(String pageName, String projectName) {
		super(pageName, projectName);
		this.projects = ProjectManager.getContainerInstance().getProjects();
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param path
	 *            the path as String
	 */
	public void setExportPath(String path) {
		this.pathChooser.setText(path);

	}

	@Override
	protected String openExportDialog(String[] filters, String[] names) {
		FileDialog fileDialog = new FileDialog(this.getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(filters);
		fileDialog.setFilterNames(names);
		fileDialog.setFileName(this.getProjectName());
		String filePath = fileDialog.open();
		if (filePath != null) {
			this.setExportPath(filePath);
			this.setPageComplete(this.checkFinish());
			return filePath;
		}

		return ""; ////$NON-NLS-1$
	}

	/**
	 * adds a drop down list to the wizard page where the user can choose
	 * between all open projects <br>
	 * <i>the parent must have a FormLayout</i>
	 * 
	 * @author Lukas Balzer
	 * @param parent
	 *            the parent composite
	 * @param attachment
	 *            the
	 * @return the composite containing the project drop down list
	 * 
	 */
	public Composite addProjectChooser(Composite parent,
			FormAttachment attachment) {
		FormData data = new FormData();
		data.top = attachment;
		data.left = new FormAttachment(0, AbstractWizardPage.LABEL_COLUMN);
		final Composite projectChooser = new Composite(parent, SWT.NONE);
		projectChooser.setToolTipText(Messages.ChooseProjectForExport);
		projectChooser.setLayoutData(data);
		projectChooser.setLayout(new GridLayout(3, false));

		Label chooseLabel = new Label(projectChooser, SWT.None);
		chooseLabel.setLayoutData(new GridData(AbstractWizardPage.LABEL_WIDTH,
				SWT.DEFAULT));
		chooseLabel.setText(Messages.Project);

		this.chooseList = new Combo(projectChooser, SWT.DROP_DOWN);
		this.chooseList.setLayoutData(new GridData(300, SWT.DEFAULT));
		this.chooseList.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				projectChooser.notifyListeners(SWT.Selection, null);
				AbstractExportPage.this.setPageComplete(AbstractExportPage.this
						.checkFinish());
			}
		});
		for (UUID id : this.projects.keySet()) {
			this.chooseList.add(this.projects.get(id));
		}
		return projectChooser;

	}

	@Override
	public UUID getProjectId() {
		if (this.chooseList == null) {
			return null;
		}
		if (this.chooseList.getSelectionIndex() == -1) {
			return null;
		}
		return this.projects.keySet().toArray(new UUID[0])[this.chooseList
				.getSelectionIndex()];
	}

	@Override
	public String getExportPath() {
		if (this.pathChooser == null) {
			ProjectManager.getLOGGER().debug(
					"AbstractExportPage must define a pathChooser"); //$NON-NLS-1$
			return null;
		}
		return this.pathChooser.getText();
	}

	@Override
	public boolean checkFinish() {
		this.setErrorMessage(null);
		if (this.getProjectId() == null) {
			this.setErrorMessage(Messages.NoProjectSelected);
			return false;
		}
		if ((this.getExportPath() == null) || this.getExportPath().equals("")) { //$NON-NLS-1$
			this.setErrorMessage(Messages.IlegalPath);
			return false;
		}
		File fileTmp = new File(this.getExportPath());
		if (fileTmp.exists()) {
			this.setMessage(String.format(Messages.DoYouReallyWantToOverwriteTheFile,fileTmp.getName()),
					IMessageProvider.WARNING);
		}
		return true;
	}

	@Override
	protected void setControl(Control newControl) {
		this.setPageComplete(this.checkFinish());
		super.setControl(newControl);
	}
}
