package xstpa.ui.dialogs;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstpa.ui.View;

public class EditRelatedUcaWizard {
    // ==================== 2. Instance Fields ============================
    private Shell shell;
    private org.eclipse.swt.widgets.List linkedList;
    private org.eclipse.swt.widgets.List availableList;
	private boolean refreshView;
	private List<UUID> ucaLinks;
	private List<ICorrespondingUnsafeControlAction> availableLinks;
	private IExtendedDataModel model;
    
    // ==================== 4. Constructors ===============================


	public EditRelatedUcaWizard(IExtendedDataModel model, List<UUID> list)
    {
		this.refreshView = false;
		shell = new Shell(Display.getCurrent().getActiveShell(),SWT.CLOSE |SWT.RESIZE | SWT.TITLE| SWT.APPLICATION_MODAL );
        shell.setLayout(new GridLayout(2, false));
        
        shell.setText("Add Unsafe Control Actions");
        shell.setImage(View.LOGO);
        shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellDeactivated(ShellEvent e) {
				EditRelatedUcaWizard.this.shell.close();
			}
			
		});
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width)/4;
        int y = (dim.height)/4;
        int width = (dim.width)/2;
        int height = (dim.height)/2;
        // set the Location
        shell.setSize(width, height);
        shell.setLocation(x,y);
        this.model = model;
        this.ucaLinks = list;
        this.availableLinks = new ArrayList<>();
        for(ICorrespondingUnsafeControlAction uca : model.getAllUnsafeControlActions()){
        	if(!ucaLinks.contains(uca.getId())){
        		this.availableLinks.add(uca);
        	}
        	
        }
        createContents(shell);
//        shell.pack();
        shell.setSize(width, height);
        shell.setLocation(x,y);
        
    }
	
    
    // ==================== 5. Creators =============================
    

	private void createContents(final Composite parent) {
    	
	    // Add the Label for relationParamListComposite
	    Label availableLable = new Label(shell, SWT.NONE);
	    availableLable.setText("All Unsafe Control Actions");
    	
	    // Add the Label for relationParamListComposite
	    Label linkedLable = new Label(shell, SWT.NONE);
	    linkedLable.setText("Linked Unsafe Control Actions");
	    
	    // Add the List for relationParamListComposite
	    availableList = new org.eclipse.swt.widgets.List(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	    GridData data = new GridData();
	    data.grabExcessHorizontalSpace = true;
	    data.grabExcessVerticalSpace = true;
	    data.minimumWidth = 350;
	    data.minimumHeight = 200;
	    availableList.setLayoutData(data);
	    
	    // Add the List for relationParamListComposite
	    linkedList = new org.eclipse.swt.widgets.List(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	    data = new GridData();
	    data.grabExcessHorizontalSpace = true;
	    data.grabExcessVerticalSpace = true;
	    data.minimumWidth = 350;
	    data.minimumHeight = 200;
	    linkedList.setLayoutData(data);
	    refreshLists();
	    
	    // The Add Button
	    Button addHazard = new Button (shell, SWT.PUSH);
	    addHazard.setText("Add");
	    addHazard.setLayoutData(new GridData(100, 30));
	    
	    // The remove Button
	    Button removeHazard = new Button (shell, SWT.PUSH);
	    removeHazard.setText("Remove");
	    removeHazard.setLayoutData(new GridData(100, 30));

	    
	    // The Ok Button
	    Button okBtn = new Button (shell, SWT.PUSH);
	    okBtn.setText("OK");
	    okBtn.setLayoutData(new GridData(100, 30));
	    
	    // closes the window
	    okBtn.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		refreshView = true;
	    		close();
	    	}
	    });
	    
	    // adds the constraint to the list and clears the editor
	    addHazard.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		if (availableList.getSelectionIndex() != -1) {	
    				int index = availableList.getSelectionIndex();
    				// display the current changes
    				ucaLinks.add(availableLinks.get(index).getId());
    				refreshLists();	

	    		}
	    		
	    	}
	    });
	    
	    // adds the constraint to the list and clears the editor
	    removeHazard.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		if (linkedList.getSelectionIndex() != -1) {	
    				int index = linkedList.getSelectionIndex();
					ucaLinks.remove(index);
					refreshLists();
	    		}
	    	}
	    });
    	
    }
    
	private void refreshLists(){
		linkedList.removeAll();
		availableList.removeAll();
		ArrayList<UUID> linkedIDs = new ArrayList<>();
		List<ICorrespondingUnsafeControlAction> availableIDs = new ArrayList<>();
		for (ICorrespondingUnsafeControlAction uca : model.getAllUnsafeControlActions()) {
	    	int number = model.getUCANumber(uca.getId());
			if(ucaLinks.contains(uca.getId())){
		    	linkedList.add("UCA1."+number);
		    	linkedIDs.add(uca.getId());
			}else{
				availableList.add("UCA1."+number+" : " +uca.getDescription());
				availableIDs.add(uca);
			}
		}
		ucaLinks = linkedIDs;
		availableLinks = availableIDs;
	}
    public boolean open()
    {
        shell.open();     
        while (!shell.isDisposed()) {
		    if (!Display.getDefault().readAndDispatch()) {
		    	Display.getDefault().sleep();
		    }
		}
        return this.refreshView;
    }
	
    public void close()
    {
        shell.setVisible(false);
    }
    
    public List<UUID> getUcaLinks(){
    	return this.ucaLinks;
    }
}
