package xstampp.astpa.wizards.pages;

import java.io.File;

import messages.Messages;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xstampp.astpa.wizards.AbstractWizardPage;
import xstampp.ui.common.ViewContainer;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class NewProjectPage extends AbstractWizardPage implements
		ModifyListener {

	private final static int DEFAULT_MARGIN = 10;
	private final static int DEFAULT_HEIGHT = 30;

	private Text newProjectName;
	private PathComposite pathComposite;
	private Button defaultPathWidget;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param pageName
	 *            the page name
	 */
	public NewProjectPage(String pageName) {
		super(pageName);
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param pageName
	 *            this Pages Name
	 * @param title
	 *            the title which appears on top of the wizard
	 * @param titleImage
	 *            the Image which appears on top of the wizard dialog
	 */
	public NewProjectPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new FormLayout());

		// -----Create new Project Name Composite---------
		FormData data = new FormData();
		data.top = new FormAttachment(0, NewProjectPage.DEFAULT_MARGIN);
		data.left = new FormAttachment(0, NewProjectPage.DEFAULT_MARGIN);
		data.right = new FormAttachment(100, NewProjectPage.DEFAULT_MARGIN);
		data.height = NewProjectPage.DEFAULT_HEIGHT;

		Composite nameComposite = new Composite(control, SWT.None);
		nameComposite.setLayoutData(data);
		nameComposite.setLayout(new GridLayout(2, false));

		Label newProjectLabel = new Label(nameComposite, SWT.NONE);
		newProjectLabel.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));
		newProjectLabel.setText(Messages.NewProject + ": "); //$NON-NLS-1$
		this.newProjectName = new Text(nameComposite, SWT.SINGLE | SWT.BORDER);
		this.newProjectName.setLayoutData(new GridData(300, SWT.DEFAULT));
		this.newProjectName.addModifyListener(this);

		// -----Create Project Path Composite----------
		data = new FormData();
		data.top = new FormAttachment(nameComposite,
				NewProjectPage.DEFAULT_MARGIN);
		data.left = new FormAttachment(0, NewProjectPage.DEFAULT_MARGIN);
		data.right = new FormAttachment(100, NewProjectPage.DEFAULT_MARGIN);
		data.height = NewProjectPage.DEFAULT_HEIGHT;

		this.defaultPathWidget = new Button(control, SWT.CHECK);
		this.defaultPathWidget.setText(Messages.UseWorkspace);
		this.defaultPathWidget.setLayoutData(data);
		this.defaultPathWidget.setSelection(true);
		this.defaultPathWidget.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				NewProjectPage.this.pathComposite
						.setEnabled(!NewProjectPage.this.defaultPathWidget
								.getSelection());

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing to do here

			}
		});

		data = new FormData();
		data.top = new FormAttachment(this.defaultPathWidget,
				NewProjectPage.DEFAULT_MARGIN);
		data.left = new FormAttachment(0, NewProjectPage.DEFAULT_MARGIN);
		data.right = new FormAttachment(100, NewProjectPage.DEFAULT_MARGIN);
		data.height = NewProjectPage.DEFAULT_HEIGHT;

		this.pathComposite = new PathComposite(
				new String[] { "*.haz" }, control, SWT.None); //$NON-NLS-1$
		this.pathComposite.setLayoutData(data);
		this.pathComposite.setText(Platform.getInstanceLocation().getURL()
				.getPath());
		this.pathComposite.getTextWidget().addModifyListener(this);
		this.pathComposite.setEnabled(false);
		this.setPageComplete(false);
		this.setControl(control);
	}

	@Override
	public boolean checkFinish() {
		this.setErrorMessage(null);
		if ((this.newProjectName == null)
				|| this.getNewProjectName().equals("")) { //$NON-NLS-1$
			return false;
		}
		if (!this.defaultPathWidget.getSelection()
				&& ((this.pathComposite == null) || this.pathComposite
						.getText().equals(""))) { //$NON-NLS-1$
			return false;
		}
		if (ViewContainer.getContainerInstance().getProjects()
				.containsValue(this.getNewProjectName())) {
			this.setErrorMessage(Messages.ProjectExists);
			return false;
		}
		File path = new File(this.getNewProjectPath());
		if (path.exists()) {
			this.setErrorMessage(Messages.FileExists);
			return false;
		}
		return true;
	}

	/**
	 * @return the newProjectName
	 */
	public String getNewProjectName() {
		return this.newProjectName.getText();
	}

	/**
	 * @return the path where the Data for the new project is stored
	 */
	public String getNewProjectPath() {
		if (this.defaultPathWidget.getSelection()) {
			return Platform.getInstanceLocation().getURL().getPath()
					+ File.separator + this.getNewProjectName() + ".haz"; //$NON-NLS-1$
		}
		return this.pathComposite.getText();
	}

	@Override
	public void modifyText(ModifyEvent arg0) {
		this.setPageComplete(this.checkFinish());

	}

}
