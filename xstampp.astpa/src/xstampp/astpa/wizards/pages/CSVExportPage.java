package xstampp.astpa.wizards.pages;

import messages.Messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import xstampp.astpa.wizards.AbstractExportPage;
import xstampp.astpa.wizards.AbstractWizardPage;

/**
 * a page to prepare and execute a CSV Export
 * 
 * @author Lukas Balzer
 * 
 */
public class CSVExportPage extends AbstractExportPage {
	private char seperator;
	private String[] filters;

	/**
	 * @author Lukas Balzer
	 * @param filters
	 *            the file extensions, which shall be excepted by in the dialog
	 * @param pageName
	 *            the Name of this page, that is displayed in the header of the
	 *            wizard
	 * @param projectName
	 *            The Name of the project
	 */
	public CSVExportPage(String[] filters, String pageName, String projectName) {
		super(pageName, projectName);
		this.setTitle(pageName);
		this.filters = filters;
		this.setDescription(Messages.PrepareDataExport);

	}

	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new FormLayout());
		Composite projectChooser = this.addProjectChooser(control,
				new FormAttachment(null, AbstractWizardPage.COMPONENT_OFFSET));
		Group seperatorGroup = new Group(control, SWT.SHADOW_NONE);
		seperatorGroup.setLayout(new RowLayout(SWT.VERTICAL));
		seperatorGroup.setText(Messages.SeperatorCharacter);

		Button semicolon = new Button(seperatorGroup, SWT.RADIO);
		Button tab = new Button(seperatorGroup, SWT.RADIO);
		Button komma = new Button(seperatorGroup, SWT.RADIO);

		semicolon.setText(Messages.Semicolon);
		semicolon.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CSVExportPage.this.seperator = ';';
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing
			}
		});
		semicolon.setSelection(true);
		this.seperator = ';';

		tab.setText(Messages.Tabulator);
		tab.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CSVExportPage.this.seperator = SWT.TAB;

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing
			}
		});

		komma.setText(Messages.Comma);
		komma.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CSVExportPage.this.seperator = ',';

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing
			}
		});

		FormData data = new FormData();
		data.top = new FormAttachment(projectChooser,
				AbstractWizardPage.COMPONENT_OFFSET);
		data.width = parent.getBounds().width;
		seperatorGroup.setLayoutData(data);

		this.pathChooser = new PathComposite(this.filters, control, SWT.NONE);

		data = new FormData();
		data.top = new FormAttachment(seperatorGroup,
				AbstractWizardPage.COMPONENT_OFFSET);
		this.pathChooser.setLayoutData(data);

		// Required to avoid an error in the system
		this.setControl(control);

	}

	@Override
	public boolean asOne() {
		return true;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the character which shall be used as seperator in the CSV
	 */
	public char getSeperator() {
		return this.seperator;

	}
}
