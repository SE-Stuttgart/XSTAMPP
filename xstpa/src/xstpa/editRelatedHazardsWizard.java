package xstpa;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;

public class editRelatedHazardsWizard {
    // ==================== 2. Instance Fields ============================
	private Boolean initialized = false;
    private Shell shell;
    private ProcessModelVariables entryToEdit;
    private View view;
    org.eclipse.swt.widgets.List linkedList;
    org.eclipse.swt.widgets.List availableList;
    
    // ==================== 4. Constructors ===============================


	public editRelatedHazardsWizard(ProcessModelVariables entryToEdit)
    {
//        shell = new Shell(Display.getCurrent(),SWT.SHELL_TRIM & (~SWT.RESIZE));
//        shell.setLayout(new GridLayout(2, false));
//        shell.setText("Add Hazards");
//        shell.setImage(View.LOGO);
//
//        // Get the size of the screen
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        int x = (dim.width)/4;
//        int y = (dim.height)/2;
//        // set the Location
//        shell.setLocation(x,y);
        
        this.entryToEdit = entryToEdit;
        //createContents(shell);
        //shell.pack();
        
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
	    if (entryToEdit != null) {
	    	
	    	for (String entry : entryToEdit.getUca().getDescriptions()) {
	    		availableList.add(entry);
	    	}
	    }
	    
	    // Add the List for relationParamListComposite
	    linkedList = new org.eclipse.swt.widgets.List(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	    data = new GridData();
	    data.grabExcessHorizontalSpace = true;
	    data.grabExcessVerticalSpace = true;
	    data.minimumWidth = 350;
	    data.minimumHeight = 200;
	    linkedList.setLayoutData(data);
	    if (entryToEdit.getUca().getLinkedDescriptions() != null) {
	    	
	    	for (String entry : entryToEdit.getUca().getLinkedDescriptions()) {
	    		linkedList.add(entry);
	    	}
	    }

	    
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
	    		close();
	    	}
	    });
	    
	    // adds the constraint to the list and clears the editor
	    addHazard.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		if (availableList.getSelectionIndex() != -1) {	
	    			
	    				for (int i = 0; i<entryToEdit.getUca().getDescriptions().size(); i++) {
	    					
	    					
	    					if (availableList.getItem(availableList.getSelectionIndex())
	    					.contains(entryToEdit.getUca().getDescriptions().get(i))) {
	    						
	    						entryToEdit.getUca().getLinkedDescriptions()
	        					.add(entryToEdit.getUca().getDescriptions().get(i));
	    						
	    						entryToEdit.getUca().getDescriptions()
	        					.remove(i);
	    					}
	    						
	    					
	    				}
	    			
	    				linkedList.add(availableList.getItem(availableList.getSelectionIndex()));
	    				availableList.remove(availableList.getSelectionIndex());
	    				
	    				view.refinedSafetyTable.setVisible(false);
	    				// refresh the viewer
	    				view.refinedSafetyViewer.refresh();
	    	    		// pack the table
	    				view.refinedSafetyTable.getColumn(view.refinedSafetyTable.getColumnCount()-3).pack();
	    				view.refinedSafetyTable.setVisible(true);
	    		}
	    		
	    	}
	    });
	    
	    // adds the constraint to the list and clears the editor
	    removeHazard.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		if (linkedList.getSelectionIndex() != -1) {
	    			
    				for (int i = 0; i<entryToEdit.getUca().getLinkedDescriptions().size(); i++) {
    					
    					
    					if (linkedList.getItem(linkedList.getSelectionIndex())
    					.contains(entryToEdit.getUca().getLinkedDescriptions().get(i))) {
    						
    	    				entryToEdit.getUca().getDescriptions()
    	    				.add(entryToEdit.getUca().getLinkedDescriptions().get(i));
    	    				
    	    				entryToEdit.getUca().getLinkedDescriptions()
    	    				.remove(i);
    					}
    				}
	    				
	    				availableList.add(linkedList.getItem(linkedList.getSelectionIndex()));
	    				linkedList.remove(linkedList.getSelectionIndex());
	    				
	    				view.refinedSafetyTable.setVisible(false);
	    				// refresh the viewer
	    				view.refinedSafetyViewer.refresh();
	    	    		// pack the table
	    				view.refinedSafetyTable.getColumn(view.refinedSafetyTable.getColumnCount()-1).pack();
	    				view.refinedSafetyTable.setVisible(true);

	    				
	    		}
	    		
	    	}
	    });
    	
    }
    public void initializeUCA() {
    	if (initialized) {
    		
    	}
    	else {
            shell = new Shell(Display.getCurrent(),SWT.SHELL_TRIM & (~SWT.RESIZE));
            shell.setLayout(new GridLayout(2, false));
            shell.setText("Add Hazards");
            shell.setImage(View.LOGO);

            // Get the size of the screen
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (dim.width)/4;
            int y = (dim.height)/2;
            // set the Location
            shell.setLocation(x,y);
            createContents(shell);
            shell.pack();
        	entryToEdit.getUca().initialize();
        	
    	    if (entryToEdit.getUca().getLinkedDescriptions() != null) {
    	    	
    	    	for (String entry : entryToEdit.getUca().getLinkedDescriptions()) {
    	    		linkedList.add(entry);
    	    	}
    	    }
    	    if (entryToEdit != null) {
    	    	
    	    	for (String entry : entryToEdit.getUca().getDescriptions()) {
    	    		availableList.add(entry);
    	    	}
    	    }
    	    initialized = true;
    	}


    }
    
    public void open(View view)
    {
    	this.view = view;
        shell.open();     
    }

    public void close()
    {
    	
        shell.setVisible(false);
    }
}
