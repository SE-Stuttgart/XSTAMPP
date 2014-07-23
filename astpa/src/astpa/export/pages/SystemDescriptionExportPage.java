/**
 * 
 * @author Lukas Balzer
 */
package astpa.export.pages;


import messages.Messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import astpa.export.AbstractExportPage;

/**
 *
 * @author Lukas Balzer
 *
 */
public class SystemDescriptionExportPage extends AbstractExportPage{

	
	private Text pathText;
	private Composite control;
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
	public SystemDescriptionExportPage(String[] filters,String pageName,String projectName) {
		super(pageName, projectName);
		this.setTitle(pageName);
		this.filters=filters;
		this.setDescription(Messages.SetValuesForTheExportFile);
	}

	@Override
	public void createControl(Composite parent){
		this.control = new Composite(parent,SWT.NONE);
		this.control.setLayout(new FormLayout());

		PathComposite pathChooser= new PathComposite(this.filters,this.control, SWT.NONE);
		
		FormData data= new FormData();
		data.top= new FormAttachment(COMPONENT_OFFSET);
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

}
