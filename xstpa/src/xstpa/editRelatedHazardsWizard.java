package xstpa;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private List<String> linkedListContent = new ArrayList<String>();
    org.eclipse.swt.widgets.List linkedList;
    org.eclipse.swt.widgets.List availableList;
    
    // ==================== 4. Constructors ===============================


	public editRelatedHazardsWizard(ProcessModelVariables entryToEdit)
    {
        shell = new Shell(Display.getCurrent().getActiveShell(),SWT.SHELL_TRIM & (~SWT.RESIZE));
        shell.setLayout(new GridLayout(2, false));
        shell.setText("Add Unsafe Control Actions");
        shell.setImage(View.LOGO);

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width)/4;
        int y = (dim.height)/2;
        // set the Location
        shell.setLocation(x,y);
        
        this.entryToEdit = entryToEdit;
        createContents(shell);
        shell.pack();
        
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
	    	entryToEdit.getUca().getLinkedDescriptions().clear();
	    	List<UUID> tempLinkedIds = new ArrayList<UUID>();
	    	for (int i = 0; i<entryToEdit.getUca().getDescriptionIds().size(); i++) {
	    		// TODO CHECK AGAIN
	    		String tempAvail = "UCA"+i+" - ";
	    		String tempLinked = "UCA"+i+" - ";
	    		Boolean addEntry = true;
		    	for (int j = 0; j<entryToEdit.getUca().getLinkedDescriptionIds().size(); j++) {
		    		if (entryToEdit.getUca().getLinkedDescriptionIds().get(j)
		    					.equals(entryToEdit.getUca().getDescriptionIds().get(i))) {
		    				addEntry = false;
		    				tempLinked = tempLinked.concat(entryToEdit.getUca().getDescriptions().get(i));
		    				linkedListContent.add(tempLinked);
		    				entryToEdit.getUca().addLinkedDescription(entryToEdit.getUca().getDescriptions().get(i));
		    				tempLinkedIds.add(entryToEdit.getUca().getDescriptionIds().get(i));
		    		}
		    	}
		    	if (addEntry) {
		    		tempAvail = tempAvail.concat(entryToEdit.getUca().getDescriptions().get(i));
		    		availableList.add(tempAvail);
		    	}
	    	}
	    	entryToEdit.getUca().setLinkedDescriptionIds(tempLinkedIds);
	    }
	    	
	    
	    
	    // Add the List for relationParamListComposite
	    linkedList = new org.eclipse.swt.widgets.List(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	    data = new GridData();
	    data.grabExcessHorizontalSpace = true;
	    data.grabExcessVerticalSpace = true;
	    data.minimumWidth = 350;
	    data.minimumHeight = 200;
	    linkedList.setLayoutData(data);
//	    if (entryToEdit.getUca().getLinkedDescriptions() != null) {
	    if (entryToEdit != null) {
	    	
	    	
	    	for (String entry : linkedListContent) {
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
	    		view.storeRefinedSafety();
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
	    						
	    						// add the entry to the linked Descriptions
	    						entryToEdit.getUca().getLinkedDescriptions()
	        					.add(entryToEdit.getUca().getDescriptions().get(i));
	    						// add the Id of the Entry to linked Descriptions
	    						entryToEdit.getUca().getLinkedDescriptionIds()
	        					.add(entryToEdit.getUca().getDescriptionIds().get(i));
	    						

	    					}
	    						
	    					
	    				}
	    				// display the current changes
	    				linkedList.add(availableList.getItem(availableList.getSelectionIndex()));
	    				availableList.remove(availableList.getSelectionIndex());

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
    						
    	    				// Remove Linked Description Id
    	    				entryToEdit.getUca().getLinkedDescriptionIds()
    	    				.remove(i);
    	    				
    						// Remove Linked Description
    	    				entryToEdit.getUca().getLinkedDescriptions()
    	    				.remove(i);
    	    				
    	    				break;
    	    			
    					}
    				}
	    				
	    				availableList.add(linkedList.getItem(linkedList.getSelectionIndex()));
	    				linkedList.remove(linkedList.getSelectionIndex());

	    				
	    		}
	    		
	    	}
	    });
    	
    }
//    public Boolean initializeUCA() {
//    	if (initialized) {
//    		return true;
//    	}
//    	else {
//            shell = new Shell(Display.getCurrent(),SWT.SHELL_TRIM & (~SWT.RESIZE));
//            shell.setLayout(new GridLayout(2, false));
//            shell.setText("Add Hazards");
//            shell.setImage(View.LOGO);
//
//            // Get the size of the screen
//            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//            int x = (dim.width)/4;
//            int y = (dim.height)/2;
//            // set the Location
//            shell.setLocation(x,y);
//            createContents(shell);
//            shell.pack();
//        	entryToEdit.getUca().initialize();
//        	
//    	    if (entryToEdit.getUca().getLinkedDescriptions() != null) {
//    	    	
//    	    	for (String entry : entryToEdit.getUca().getLinkedDescriptions()) {
//    	    		linkedList.add(entry);
//    	    	}
//    	    }
//    	    if (entryToEdit != null) {
//    	    	
//    	    	for (String entry : entryToEdit.getUca().getDescriptions()) {
//    	    		availableList.add(entry);
//    	    	}
//    	    	if (availableList.getItemCount() == 0) {
//    	    		return false;
//    	    	}
//    	    }
//    	    initialized = true;
//    	    return true;
//    	}
//
//
//    }
    
    public void open(View view)
    {
    	this.view = view;
        shell.open();     
    }
    

    public void close()
    {
    	view.refinedSafetyViewer.setInput(view.refinedSafetyContent);
    	view.refinedSafetyTable.getColumn(4).pack();
        shell.setVisible(false);
    }
}
