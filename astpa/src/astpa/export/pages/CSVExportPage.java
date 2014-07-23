package astpa.export.pages;

import messages.Messages;

import org.eclipse.jface.dialogs.Dialog;
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
import org.eclipse.swt.widgets.Text;

import astpa.export.AbstractExportPage;

/**
 * a page to prepare and execute a CSV Export
 * 
 * @author Lukas Balzer
 *
 */
public class CSVExportPage extends AbstractExportPage {
	private char seperator;
	private Text pathText;
	private String[] filters;
	/**
	 * @author Lukas Balzer
	 * @param filters
	 * 			the file extensions, which shall be excepted by in the dialog
	 * @param pageName
	 * 			the Name of this page, that is displayed in the header of the wizard
	 * @param projectName 
	 * 			The Name of the project
	 */
	public CSVExportPage(String[] filters,String pageName,String projectName) {
		super(pageName, projectName);
		this.setTitle(pageName);
		this.filters=filters;
		this.setDescription(Messages.PrepareDataExport);

	}

	@Override
	public void createControl(Composite parent){
		Composite control = new Composite(parent,SWT.NONE);
		control.setLayout(new FormLayout());
		
		Group seperatorGroup= new Group(control, SWT.SHADOW_NONE);
		seperatorGroup.setLayout(new RowLayout(SWT.VERTICAL));
		seperatorGroup.setText(Messages.SeperatorCharacter);
		
		Button semicolon= new Button(seperatorGroup, SWT.RADIO);
		Button tab= new Button(seperatorGroup, SWT.RADIO);
		Button komma= new Button(seperatorGroup, SWT.RADIO);
		
		semicolon.setText(Messages.Semicolon); 
		semicolon.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				CSVExportPage.this.seperator = ';'; //$NON-NLS-1$
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing
			}
		});
		semicolon.setSelection(true);
		this.seperator= ';';
		
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
				CSVExportPage.this.seperator = ','; //$NON-NLS-1$
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing
			}
		});
		
		FormData data= new FormData();
		data.top= new FormAttachment(COMPONENT_OFFSET);
		data.width = Dialog.DIALOG_DEFAULT_BOUNDS;
		seperatorGroup.setLayoutData(data);
		
		PathComposite pathChooser= new PathComposite(this.filters,control, SWT.NONE);
		
		data= new FormData();
		data.top= new FormAttachment(seperatorGroup,COMPONENT_OFFSET);
		pathChooser.setLayoutData(data);
		this.pathText=pathChooser.getText();

		// Required to avoid an error in the system
		this.setControl(control);


	}


	@Override
	public String getExportPath() {
		return this.pathText.getText();
	}

	@Override
	public void setExportPath(String path) {
		this.pathText.setText(path);
		
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
	public char getSeperator(){
		return this.seperator;
		
	}
}
