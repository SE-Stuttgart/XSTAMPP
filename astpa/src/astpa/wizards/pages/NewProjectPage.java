package astpa.wizards.pages;

import java.io.File;

import messages.Messages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import astpa.wizards.AbstractExportPage;
import astpa.wizards.AbstractWizardPage;

/**
 *
 * @author Lukas Balzer
 *
 */
public class NewProjectPage extends AbstractWizardPage implements ModifyListener{


		

	
	private static int DEFAULT_MARGIN=10;
	private static int DEFAULT_HEIGHT=30;
	
	private Text newProjectName;
	private PathComposite pathComposite;
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param pageName
	 */
	public NewProjectPage(String pageName) {
		super(pageName);
	}

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param pageName this Pages Name
	 * @param title the title which appears on top of the wizard
	 * @param titleImage the Image which appears on top of the wizard dialog
	 */
	public NewProjectPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent,SWT.NONE);
		control.setLayout(new FormLayout());
		
		//-----Create new Project Name Composite---------
		FormData data= new FormData();
		data.top = new FormAttachment(0,DEFAULT_MARGIN);
		data.left = new FormAttachment(0,DEFAULT_MARGIN);
		data.right= new FormAttachment(100,DEFAULT_MARGIN);
		data.height= DEFAULT_HEIGHT;
	
		Composite nameComposite = new Composite(control, SWT.None);
		nameComposite.setLayoutData(data);
		nameComposite.setLayout(new GridLayout(2,false));
		
		Label newProjectLabel= new Label(nameComposite, SWT.NONE);
		newProjectLabel.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));
		newProjectLabel.setText(Messages.NewProject + ": ");	//$NON-NLS-1$
		this.newProjectName= new Text(nameComposite, SWT.SINGLE |SWT.BORDER);
		this.newProjectName.setLayoutData(new GridData(300, SWT.DEFAULT));
		this.newProjectName.addModifyListener(this);
		
		//-----Create Project Path Composite----------
		data= new FormData();
		data.top = new FormAttachment(nameComposite,DEFAULT_MARGIN);
		data.left = new FormAttachment(0,DEFAULT_MARGIN);
		data.right= new FormAttachment(100,DEFAULT_MARGIN);
		data.height= DEFAULT_HEIGHT;
	
		this.pathComposite = new PathComposite(new String[]{"*.haz"},control, SWT.None);
		this.pathComposite.setLayoutData(data);
		this.pathComposite.getTextWidget().addModifyListener(this);
		setPageComplete(false);
		setControl(control);
	}

	private void checkFinish(){
		if(this.newProjectName == null ||this.getNewProjectName().equals("")){
			return;
		}
		if(this.pathComposite == null || this.pathComposite.getText().equals("")){
			return;
		}
		File path= new File(getNewProjectPath());
		if(path.exists()){
			setErrorMessage("Path is not Empty");
		}
		this.setPageComplete(true);
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
		return this.pathComposite.getText();
	}
	
	@Override
	public void modifyText(ModifyEvent arg0) {
		checkFinish();
		
	}

}
