package xstpa.ui.tables;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import xstpa.model.ControlActionEntrys;
import xstpa.model.ProcessModelVariables;
import xstpa.ui.View;
import xstpa.ui.tables.utils.DependencyViewLabelProvider;
import xstpa.ui.tables.utils.MainViewContentProvider;

public class CADependenciesTable extends AbstractTableComposite {


	private Composite compositeDependenciesTopRight;
	private Composite compositeDependenciesBottomRight;
	private TableViewer dependencyTableViewer;
	private TableViewer dependencyTopTableViewer;
	private TableViewer dependencyBottomTableViewer;
	private TabFolder dependenciesFolder;
	private Composite dependenciesOuterComposite;
	private Table dependencyTable;
	private Table dependencyBottomTable;
	private Table dependencyTopTable;

	public CADependenciesTable(Composite parent) {
		super(parent);
		setLayout(new FillLayout());
		
		// the tabfolder for the dependencies Composite
	    dependenciesFolder = new TabFolder(this, SWT.NONE);
	    dependenciesFolder.setLayout(new GridLayout(1, false));
	    
	    TabItem dependenciesTab1 = new TabItem(dependenciesFolder, SWT.NONE);
	    dependenciesTab1.setText("Control Action Provided");
	    TabItem dependenciesTab2 = new TabItem(dependenciesFolder, SWT.NONE);
	    dependenciesTab2.setText("Control Action Not Provided");	
	    
	    //Composite in which the folder items are located
	    dependenciesOuterComposite = new Composite(dependenciesFolder, SWT.NONE);
	    dependenciesOuterComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    dependenciesOuterComposite.setLayout(  new FormLayout() );
	    
	  //Composite in which the dependencies Table is
	    Composite dependenciesInnerComposite = new Composite(dependenciesOuterComposite, SWT.NONE);	    
	    dependenciesInnerComposite.setLayout( new GridLayout(1, false));
	    
	    
	    // set the formdata for the dependencies (inner)
	    FormData fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0 );
	    fData.right = new FormAttachment( 45 );
	    fData.bottom = new FormAttachment( 100 );
	    dependenciesInnerComposite.setLayoutData( fData );
	    
	 // the top right part
	    compositeDependenciesTopRight = new Composite(dependenciesOuterComposite, SWT.BORDER);
	    compositeDependenciesTopRight.setLayout( new GridLayout(1, false));
	    
	    // set the formdata for the dependencies (top right)
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( dependenciesInnerComposite );
	    fData.right = new FormAttachment( 100 );
	    fData.bottom = new FormAttachment( 55 );
	    compositeDependenciesTopRight.setLayoutData( fData );
	    	    
	    
	    // the bottom right part
	    compositeDependenciesBottomRight = new Composite(dependenciesOuterComposite, SWT.BORDER);
	    compositeDependenciesBottomRight.setLayout(new GridLayout(1, false));
	    
	    // set the formdata for the dependencies (bottom right)
	    fData = new FormData();
	    fData.top = new FormAttachment( compositeDependenciesTopRight );
	    fData.left = new FormAttachment( dependenciesInnerComposite );
	    fData.right = new FormAttachment( 100 );
	    fData.bottom = new FormAttachment( 100 );
	    
	    compositeDependenciesBottomRight.setLayoutData( fData );
	    
	    //Set the composite to the right tab
	    dependenciesTab1.setControl(dependenciesOuterComposite);
	    dependenciesTab2.setControl(dependenciesOuterComposite);
	    
	    
		    dependencyTableViewer = new TableViewer(dependenciesInnerComposite, SWT.FULL_SELECTION );
			dependencyTableViewer.setContentProvider(new MainViewContentProvider());
			dependencyTableViewer.setLabelProvider(new DependencyViewLabelProvider());
			
			dependencyTopTableViewer = new TableViewer(compositeDependenciesTopRight, SWT.FULL_SELECTION );
			dependencyTopTableViewer.setContentProvider(new MainViewContentProvider());
			dependencyTopTableViewer.setLabelProvider(new DependencyViewLabelProvider());		
			
			dependencyBottomTableViewer = new TableViewer(compositeDependenciesBottomRight, SWT.FULL_SELECTION );
			dependencyBottomTableViewer.setContentProvider(new MainViewContentProvider());
			dependencyBottomTableViewer.setLabelProvider(new DependencyViewLabelProvider());
	
	
			dependencyTable = dependencyTableViewer.getTable();
			dependencyTable.setHeaderVisible(true);
		    dependencyTable.setLinesVisible(true);
			dependencyTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		    // Columns for Dependencies table
		    new TableColumn(dependencyTable, SWT.LEFT).setText(View.ENTRY_ID);
		    new TableColumn(dependencyTable, SWT.LEFT).setText(View.CONTROL_ACTIONS);
		    
		    
			dependencyTopTable = dependencyTopTableViewer.getTable();
		    dependencyTopTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		    dependencyTopTable.setHeaderVisible(true);
		    dependencyTopTable.setLinesVisible(true);
		    // Columns for dependencies top (Process model Variables)
		    new TableColumn(dependencyTopTable, SWT.LEFT).setText(View.ENTRY_ID);
		    new TableColumn(dependencyTopTable, SWT.LEFT).setText(View.PMV);
		    
			dependencyBottomTable = dependencyBottomTableViewer.getTable();
		    dependencyBottomTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		    dependencyBottomTable.setHeaderVisible(true);
		    dependencyBottomTable.setLinesVisible(true);
		    // Columns for dependencies bottom (Dependencies)
		    new TableColumn(dependencyBottomTable, SWT.LEFT).setText(View.ENTRY_ID);
		    new TableColumn(dependencyBottomTable, SWT.LEFT).setText(View.PMV);
		//==============================================================================
		//START Table functionality definition
		//==============================================================================
			 /**
			 * Listener for the dependency table, checks which item gets selected and displays the linked list
			 */
		    dependencyTable.addSelectionListener(new SelectionAdapter() {
			      public void widgetSelected(SelectionEvent event) {
			    	refreshTable();
			      }
		    });
		    
		    /**
			 * Listener for the dependency table (top)
			 * 	gets the selected item
			 */
		    dependencyTopTable.addSelectionListener(new SelectionAdapter() {
			      public void widgetSelected(SelectionEvent event) {
			    	  //get the selected Process Model Variable to link it to a control Action
			    	  dataController.setLinkedPMV((ProcessModelVariables) event.item.getData());
			    	  
			      }  
			      });
		    
		    /**
			 * Listener for the dependency table (bottom)
			 * 	gets the selected item
			 */
		    dependencyBottomTable.addSelectionListener(new SelectionAdapter() {
			      public void widgetSelected(SelectionEvent event) {
			    	  //get the selected Process Model Variable to link it to a control Action
			    	  dataController.setLinkedPMV((ProcessModelVariables) event.item.getData());
			    	  
			      }  
			      });
		    
		    /**
			 * Listener for the dependency Combobox
			 *  You can Select if the Control Action is provided or not and can add different Variables to them
			 */
		    dependenciesFolder.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
			    	refreshTable();
		    	}  
		      });
		//=================================================================================================
		//END of creating the Table functionality
		//=================================================================================================
		    
		// the top right part (buttons)
	    Composite compositeDependenciesTopRightBtns = new Composite(compositeDependenciesTopRight, SWT.None);
	    compositeDependenciesTopRightBtns.setLayout(new GridLayout(5, false));

	    GridData gridData = new GridData(SWT.FILL, SWT.END, true, false);
	    compositeDependenciesTopRightBtns.setLayoutData(gridData);
	    
	    gridData = new GridData(SWT.FILL, SWT.END, true, true);
	    
	    //=================================================================================================
	    //START Button bar
	    //=================================================================================================
		    
		    // Add a button to Add one Item in Dependencies View
		    Button addBtn = new Button(compositeDependenciesTopRightBtns, SWT.PUSH);
		    addBtn.setText("Add");
		    addBtn.setLayoutData( gridData );
		    addBtn.addSelectionListener(new SelectionAdapter() {
		    	public void widgetSelected(SelectionEvent event) {
		    		if(dataController.getLinkedPMV() == null || !(dataController.getLinkedPMV() instanceof ProcessModelVariables)){
		    			return;
		    		}
		    		dataController.getLinkedCAE().addLinkedItem(dataController.getLinkedPMV());
		    		// sort the List after their "Id"
		    		dataController.getLinkedCAE().setLinkedItems(dataController.getLinkedCAE().sortItems(dataController.getLinkedCAE().getLinkedItems()));
		    		dataController.getLinkedCAE().removeAvailableItem(dataController.getLinkedPMV());
		    		
		    		if (dependenciesFolder.getSelectionIndex() == 0) {
			    		// Store it in the DataModel
		    			dataController.getModel().addCAProvidedVariable(dataController.getLinkedCAE().getId(), dataController.getLinkedPMV().getId());
		    		}else if (dependenciesFolder.getSelectionIndex() == 1) {
		    			dataController.getModel().addCANotProvidedVariable(dataController.getLinkedCAE().getId(), dataController.getLinkedPMV().getId());
		    		}
			    	refreshTable();
		    	}
		    });
		    
		    // Add a button to Add all Items
		    Button addAllBtn = new Button(compositeDependenciesTopRightBtns, SWT.PUSH);
		    addAllBtn.setText("Add All");
		    addAllBtn.setLayoutData( gridData );
		    addAllBtn.addSelectionListener(new SelectionAdapter() {
			    public void widgetSelected(SelectionEvent event) {
			    	List<ProcessModelVariables> temp = new ArrayList<ProcessModelVariables>();
			    	for (ProcessModelVariables entry: dataController.getVariablesList()) {
			    	  temp.add(entry);
			    	}
			    	dataController.getLinkedCAE().setLinkedItems(temp);
			    	dataController.getLinkedCAE().removeAllAvailableItems();
			    	
					  if (dependenciesFolder.getSelectionIndex() == 0) {
		    			  // Store it in the DataModel
						  for (int j = 0; j<dataController.getLinkedCAE().getLinkedItems().size();j++) {
						
							  	dataController.getModel().addCAProvidedVariable(dataController.getLinkedCAE().getId(), dataController.getLinkedCAE().getLinkedItems().get(j).getId());	  					  
						  }	  		    			  
					  }
					  else {
						  for (int j = 0; j<dataController.getLinkedCAE().getLinkedItems().size();j++) {
							  	
							  	dataController.getModel().addCANotProvidedVariable(dataController.getLinkedCAE().getId(), dataController.getLinkedCAE().getLinkedItems().get(j).getId());	  					  
						  }
					  }
				    refreshTable();
			    }
			});	    
		    
		    // Add a button to Remove one Item
		    Button removeBtn = new Button(compositeDependenciesTopRightBtns, SWT.PUSH);
		    removeBtn.setText("Remove");
		    removeBtn.setLayoutData(gridData);
		    removeBtn.addSelectionListener(new SelectionAdapter() {
			    public void widgetSelected(SelectionEvent event) {
		    		if(dataController.getLinkedPMV() == null || !(dataController.getLinkedPMV() instanceof ProcessModelVariables)){
		    			return;
		    		}
			    	dataController.getLinkedCAE().removeLinkedItem(dataController.getLinkedPMV());
			    	dataController.getLinkedCAE().addAvailableItem(dataController.getLinkedPMV());
			    	// sorts the List after their "Id"
			    	dataController.getLinkedCAE().setAvailableItems(dataController.getLinkedCAE().sortItems(dataController.getLinkedCAE().getAvailableItems()));
			    	
		    			  if (dependenciesFolder.getSelectionIndex() == 0) {
			    			  // Store it in the DataModel
		    				  dataController.getModel().removeCAProvidedVariable(dataController.getLinkedCAE().getId(), dataController.getLinkedPMV().getId());
		    			  }else {
		    				  dataController.getModel().removeCANotProvidedVariable(dataController.getLinkedCAE().getId(), dataController.getLinkedPMV().getId());
		    			  }
					    	refreshTable();
			    }
			});	
		    
		    // Add a button to Remove All Items
		    Button removeAllBtn = new Button(compositeDependenciesTopRightBtns, SWT.PUSH);
		    removeAllBtn.setText("Remove All");
		    removeAllBtn.setLayoutData( gridData);
		    removeAllBtn.addSelectionListener(new SelectionAdapter() {
			    public void widgetSelected(SelectionEvent event) {
			    	List<ProcessModelVariables> temp = new ArrayList<ProcessModelVariables>();
			    	for (ProcessModelVariables entry: dataController.getVariablesList()) {
			    	  temp.add(entry);
			    	}
			    	dataController.getLinkedCAE().setAvailableItems(temp);
			    	dataController.getLinkedCAE().removeAllLinkedItems();
			    	
			    	if (dependenciesFolder.getSelectionIndex() == 0) {
		    			  // Store it in the DataModel
	  				
	  				  for (int j = 0; j<dataController.getLinkedCAE().getAvailableItems().size();j++) {
	  					dataController.getModel().removeCAProvidedVariable(dataController.getLinkedCAE().getId(), dataController.getLinkedCAE().getAvailableItems().get(j).getId());
	  					  		  					  
	  				  }	  		    			  
	  			  }
	  			  else {
	  				  for (int j = 0; j<dataController.getLinkedCAE().getAvailableItems().size();j++) {
	  					  dataController.getModel().removeCANotProvidedVariable(dataController.getLinkedCAE().getId(), dataController.getLinkedCAE().getAvailableItems().get(j).getId());				  
	  				  }
	  			  }
			    	refreshTable();
			    }
			});
				
		//============================================================================
		//END Button Bar
		//============================================================================
		setVisible(false);
	}

	@Override
	public void activate() {
    	refreshTable();
        
		for (int i = 0, n = dependencyTable.getColumnCount(); i < n; i++) {
			  dependencyTable.getColumn(i).pack();
			  if (i == n-1) {
				 dependencyTable.getColumn(i).setWidth(dependencyTable.getSize().x - dependencyTable.getColumn(0).getWidth());
			  }
		}
		  

		for (int i = 0, n = dependencyTopTable.getColumnCount(); i < n; i++) {
			  dependencyTopTable.getColumn(i).pack();
			  if (i == n-1) {
				 dependencyTopTable.getColumn(i).setWidth(dependencyTopTable.getSize().x - dependencyTopTable.getColumn(0).getWidth());
			  }
		}
		  

		for (int i = 0, n = dependencyBottomTable.getColumnCount(); i < n; i++) {
			  dependencyBottomTable.getColumn(i).pack();
			  if (i == n-1) {
				 dependencyBottomTable.getColumn(i).setWidth(dependencyBottomTable.getSize().x - dependencyBottomTable.getColumn(0).getWidth());
			  }
		}
		layout(true);
		setVisible(true);

	}

	@Override
	public void refreshTable() {
		// create input for dependencyTableViewer
    	List<ControlActionEntrys> dependencyTableInput= new ArrayList<ControlActionEntrys>();
    	for(ControlActionEntrys entry : dataController.getDependenciesIFProvided()) {
    		if (entry.getSafetyCritical()) {
				dependencyTableInput.add(entry);
			}
		}
    	
        dependencyTableViewer.setInput(dependencyTableInput);
        
    	// If the view was already shown, select the old values, if not select default
    	if (dependencyTable.getSelectionIndex() == -1) {
			dependencyTable.select(0);
		}
    	
		//calculate index of selected dependency in the dependencies list
    	if(dependencyTable.getSelectionCount() > 0){
	    	  //get the selected Control Action to link it to a Process model Variable
    		ControlActionEntrys entry = (ControlActionEntrys) dependencyTable.getSelection()[0].getData();
	    	dataController.setLinkedCAE((dependenciesFolder.getSelectionIndex() == 0),entry.getId());
	    	dependencyTopTableViewer.setInput(dataController.getLinkedCAE().getAvailableItems());
	    	dependencyBottomTableViewer.setInput(dataController.getLinkedCAE().getLinkedItems());
    	}
	}

}
