package astpa.export.pages;

import messages.Messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import astpa.export.AbstractExportPage;

/**
 *A Simple implementation of the ApstactExportPage
 *containing elements for the control structure export
 *
 * @author Lukas Balzer
 *
 */
public class ControlStructureExportPage extends AbstractExportPage{

	private Text pathText;
	private Composite control;
	private String[] filters;
	private Spinner spinner;
	private Button dekoSwitch;
	/**
	 * @author Lukas Balzer
	 * @param filters
	 * 			the file extensions, which shall be excepted by in the dialog
	 * @param pageName
	 * 			the Name of this page, that is displayed in the header of the wizard
	 * @param projectName 
	 * 			The Name of the project
	 */
	public ControlStructureExportPage(String[] filters,String pageName,String projectName) {
		super(pageName, projectName);
		this.setTitle(pageName);
		this.filters=filters;
		this.setDescription(Messages.SetValuesForTheExportFile);
	}

	@Override
	public void createControl(Composite parent){
		this.control = new Composite(parent,SWT.NONE);
		this.control.setLayout(new FormLayout());
		
		Composite offsetComposite = new Composite(this.control, SWT.NONE);
		FormData data= new FormData();
		data.top= new FormAttachment(COMPONENT_OFFSET);
		offsetComposite.setLayoutData(data);
		offsetComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label offsetLabel= new Label(offsetComposite, SWT.NONE);
		offsetLabel.setText(Messages.OffsetValue);
		RowData rowData= new RowData(LABEL_WIDTH, LABEL_HEIGHT);
		offsetLabel.setLayoutData(rowData);
		this.spinner = new Spinner(offsetComposite, SWT.BORDER);
	    this.spinner.setSelection(10);
	
	    Composite decoSwitchComposite = new Composite(this.control, SWT.NONE);
		data= new FormData();
		data.top= new FormAttachment(offsetComposite,COMPONENT_OFFSET);
		decoSwitchComposite.setLayoutData(data);
		decoSwitchComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label decoLabel= new Label(decoSwitchComposite, SWT.NONE);
		decoLabel.setText("Decoration");
		decoLabel.setLayoutData(rowData);
		this.dekoSwitch= new Button(decoSwitchComposite, SWT.CHECK);
		
		
		PathComposite pathChooser= new PathComposite(this.filters,this.control, SWT.NONE);
		data= new FormData();
		data.top= new FormAttachment(decoSwitchComposite,COMPONENT_OFFSET);
		pathChooser.setLayoutData(data);
		this.pathText=pathChooser.getText();

		// Required to avoid an error in the system
		this.setControl(this.control);


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
	 * @return the spinner value
	 */
	public int getImgOffset() {
		return this.spinner.getSelection();
	}
	
	/**
	 *
	 * @return if the structure is being decorated 
	 */
	public boolean getDecoChoice(){
		return this.dekoSwitch.getSelection();
	}

}
