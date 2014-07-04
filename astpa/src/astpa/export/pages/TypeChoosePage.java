/**
 * 
 * @author Lukas Balzer
 */
package astpa.export.pages;


import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 *
 * @author Lukas Balzer
 *
 */
public class TypeChoosePage extends WizardPage{

	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param pageName
	 * 			the name of the page which shall be displayed on top of it
	 */
	public TypeChoosePage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		Composite main= new Composite(parent, SWT.NONE);
		main.setLayout(new FormLayout());
		Group group = new Group(parent, SWT.SHADOW_IN);
		FormData data= new FormData(parent.getBorderWidth(), parent.getBounds().height);
		group.setLayout(new GridLayout());
		group.setLayoutData(data);
		group.setText("sample");
		Button button1= new Button(group, SWT.PUSH);
		button1.setText("a");
		GridData gridData=new GridData();
		gridData.horizontalIndent = 10;
		button1.setLayoutData(gridData);
		
		// Required to avoid an error in the system
		this.setControl(main);
	}

	

}
