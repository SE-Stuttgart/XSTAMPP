/**
 * 
 * @author Lukas Balzer
 */
package astpa.export.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 *
 * @author Lukas Balzer
 *
 */
public class STPADataPage extends CSVExportPage {

	private Composite control;
	Map<Button,String> steps;
	private Map<String, String> stepViews;

	/**
	 * @author Lukas Balzer
	 * @param filters
	 * 			the file extensions, which shall be excepted by in the dialog
	 * @param stepViews 
	 * 			a Map with all registered step Ids mapped to their titles
	 * @param pageName
	 * 			the Name of this page, that is displayed in the header of the wizard
	 * @param projectName 
	 * 			The Name of the project
	 */
	public STPADataPage(String[] filters,Map<String, String> stepViews ,String pageName,String projectName) {
		super(filters,pageName, projectName);
		this.setTitle(pageName);
		this.steps= new HashMap<>(stepViews.size());
		this.stepViews=stepViews;
		
		this.setDescription(Messages.SetValuesForTheExportFile);
	}
	
	@Override
	public void createControl(Composite parent){
		this.control = new Composite(parent,SWT.NONE);
		this.control.setBounds(parent.getBounds());
		this.control.setLayout(new FormLayout());
		
		Group group= new Group(this.control, SWT.SHADOW_IN);
		group.setText(Messages.ChangeExportValues);
		group.setLayout(new RowLayout(SWT.VERTICAL));
		FormData data= new FormData();
		data.top= new FormAttachment(COMPONENT_OFFSET);
		data.width = parent.getBounds().width;
		group.setLayoutData(data);
		for(String ref: this.stepViews.keySet().toArray(new String[0])){
			Button stepButton=new Button(group,SWT.CHECK);
			stepButton.setText(this.stepViews.get(ref));
			stepButton.setSelection(true);
			// each button is mapped to a view id, so it can later be tracked
			this.steps.put(stepButton, ref);
		}
		Composite selectionComp = new Composite(group, SWT.NONE);
		selectionComp.setLayout(new RowLayout(SWT.HORIZONTAL));
		Button chooseAll= new Button(selectionComp, SWT.PUSH);
		chooseAll.setText(Messages.AddAll);
		chooseAll.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(Button selector: STPADataPage.this.steps.keySet()){
					selector.setSelection(true);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing by default
				
			}
		});
		
		Button removeAll= new Button(selectionComp, SWT.PUSH);
		removeAll.setText(Messages.RemoveAll);
		removeAll.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(Button selector: STPADataPage.this.steps.keySet()){
					selector.setSelection(false);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing by default
				
			}
		});
		
		super.createControl(this.control);
		Composite inheritedComp = (Composite) getControl();
		data= new FormData();
		data.top= new FormAttachment(group,COMPONENT_OFFSET);
		inheritedComp.setLayoutData(data);
		
		setControl(this.control);

	}

	
	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @return
	 * 		return an array containing all 
	 */
	public String[] getSteps(){
		List<String> stepArray =new ArrayList<>();
		for(Button stepSelector: this.steps.keySet()){
			if(stepSelector.getSelection()){
				stepArray.add(this.steps.get(stepSelector));
				System.out.println(this.steps.get(stepSelector));
			}
		}
		
		return stepArray.toArray(new String[0]);
		
	}

	@Override
	public boolean asOne() {
		return true;
	}

}