package xstpa.ui;



import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;
import xstpa.Activator;
import xstpa.model.XSTPADataController;
import xstpa.ui.tables.AbstractTableComposite;
import xstpa.ui.tables.CADependenciesTable;
import xstpa.ui.tables.ControlActionTable;
import xstpa.ui.tables.ProcessContextTable;
import xstpa.ui.tables.ProcessValuesTable;
import xstpa.ui.tables.RefinedRulesTable;

public class View extends ViewPart{
	public static final String ID = "xstpa.view.contextTables";
	
	// Table column names/properties
	public static final String CONTROLLER = "Controllers";

	public static final String PM = "Process Models";

	public static final String PMV = "Process Model Variables";

	public static final String PMVV = "Values";
	
	public static final String COMMENTS = "Description";
	
	public static final String CONTROL_ACTIONS = "Control Actions";
	
	public static final String SAFETY_CRITICAL = "Safety Critical";
	
	public static final String CONTROLLER_WITH_PM_CLASS = "xstpa.ControllerWithPMEntry";
	
	public static final String CA_ENTRY_CLASS = "xstpa.ControlActionEntrys";
	
	public static final String PM_VALUE_CLASS = "xstpa.ProcessModelVariables";
	
	public static final String ENTRY_ID = "ID";
	
	public static final String LIST_of_CA = "List of Control Actions";
	
	public static final String CONTEXT = "Context";
	
	public static final String IS_HAZARDOUS = "Hazardous?";
	
	public static final String HAZ_IF_ANYTIME = "Hazardous if provided anytime";
	
	/**
	 * String <i>Hazardous if provided to early</i> 
	 */
	public static final String HAZ_IF_EARLY = "Hazardous if provided to early";
	
	public static final String HAZ_IF_LATE = "Hazardous if provided to late";
	
	public static final String LTL_RULES = "LTL Formula";
	
	public static final String UCA = "Related Unsafe CA";

	public static final String REL_HAZ = "Linked Hazards";
	
	public static final String REFINED_RULES = "generated Rules";
	
	public static final String CRITICAL_COMBI = "Critical Combinations";

	public static final String CONTEXT_TABLE="Context Table";
	
	public static final String CONTEXT_TYPE="Type";
	/**
	 * String array that contains all headers for the columns of the 
	 * properties table that shows all process values
	 */
	public static final String[] PROPS_COLUMNS = { CONTROLLER, PM, PMV, PMVV, COMMENTS };
	public static final String RULES_TABLE = "Rules Table";
	/**
	 * String array that contains all headers for the columns of the 
	 * control actions table that contains all stored control actions
	 */
	public static final String[] CA_PROPS_COLUMNS = { CONTROL_ACTIONS, SAFETY_CRITICAL, COMMENTS};
	
	/**
	 * String array that contains all headers for the columns of the 
	 * refined safety constraints table that cpntains all process values
	 */
	public static final String[] RS_PROPS_COLUMS = { ENTRY_ID, CONTROL_ACTIONS, CONTEXT, CRITICAL_COMBI, UCA, REL_HAZ, REFINED_RULES};

	
	// static fields to hold the images
	public static final Image CHECKED = Activator.getImageDescriptor("icons/checked.gif").createImage();
	  
	public static final Image UNCHECKED = Activator.getImageDescriptor("icons/unchecked.gif").createImage();
	
	public static final Image ADD = Activator.getImageDescriptor("icons/add.png").createImage();
	
	public static final Image DELETE = Activator.getImageDescriptor("icons/delete.png").createImage();
	
	public static final Image SETTINGS = Activator.getImageDescriptor("icons/Settings.png").createImage();
	
	public static final Image HEADER = Activator.getImageDescriptor("icons/tableheader1.jpg").createImage();
	
	public static final Image LTL = Activator.getImageDescriptor("icons/ltl.png").createImage();
	
	public static final Image EXPORT = Activator.getImageDescriptor("icons/run_pdf.png").createImage();
	
	public static final Image CHECK_CONFLICTS = Activator.getImageDescriptor("icons/check_conflicts.png").createImage();
	
	public static final Image LOGO = Activator.getImageDescriptor("icons/logo.png").createImage();
	
	public static final Image GENERATE = Activator.getImageDescriptor("icons/generate.png").createImage();
	
	public static final Device device = Display.getCurrent ();
	
	public static final Color BACKGROUND = new Color(device,204,204,255);
	
	public static final Color CONFLICT = new Color(device,255,0,0);
	
	public static final Color HIGHLIGHT = new Color(device,0,0,0);
	
	public static final Color NORMAL = new Color(device,224,224,224);
	
	private XSTPADataController dataController;
	
	Table  ltlTable;
	
	

	private List<AbstractTableComposite> tableList;
	private List<Button> tableButtons;

	private Composite xstpaNavigation;

	protected AbstractTableComposite ltlComposite;

	public static String[] contextProps;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
	    // set title and Image
	    setPartName("Context Tables");
	    //this.setContentDescription("Shows the Process Models and Context Tables");
	    setTitleImage(LOGO);
	    
	    Composite outercomposite = new Composite(parent, SWT.NONE);
	    
	    FormLayout formLayout = new FormLayout();
	    formLayout.marginHeight = 5;
	    formLayout.marginWidth = 5;
	    formLayout.spacing = 5;
	    outercomposite.setLayout( formLayout );
	    
	    xstpaNavigation = new Composite( outercomposite, SWT.BORDER );
	    xstpaNavigation.setLayout( new GridLayout(1, false) );
	    
	    FormData fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0 );
	    
	    fData.bottom = new FormAttachment( 100 );
	    xstpaNavigation.setLayoutData( fData );
	    
	    Composite xstpaTableComposite = new Composite(outercomposite, SWT.BORDER);
	    
	 // set the formdata for the right (table) part
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( xstpaNavigation );
	    fData.right = new FormAttachment( 100 );
	    fData.bottom = new FormAttachment( 100 );
	    xstpaTableComposite.setLayoutData( fData );
	    xstpaTableComposite.setLayout(new FormLayout());
	    this.tableButtons = new ArrayList<>();
	    this.tableList = new ArrayList<>();
	    
	    AbstractTableComposite mainTable = new ProcessValuesTable(xstpaTableComposite);
	    addTable(mainTable, PM);
	    
	    AbstractTableComposite compositeTable = new ControlActionTable(xstpaTableComposite);
	    addTable(compositeTable, "Control Actions");
	    
	    compositeTable = new CADependenciesTable(xstpaTableComposite);
	    addTable(compositeTable, "Dependencies");
	    
	    compositeTable = new ProcessContextTable(xstpaTableComposite);
	    addTable(compositeTable, "Context Table");
	    
	    compositeTable = new RefinedRulesTable(xstpaTableComposite);
	    addTable(compositeTable, RULES_TABLE);
	    
	    IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	    if(part != null && part instanceof StandartEditorPart){
	    	XSTPADataController controller = Activator.getDataFor(((StandartEditorPart)part).getProjectID());
	    	if(controller!= null){
	    		setController(controller);
	    	}else{
	    		setController(new XSTPADataController(null));
	    	}
	    }
	    openTable(mainTable, null);
	    /**
		 * Listener which Gets the project-id of the currently active editor
		 */
		ProjectManager.getLOGGER().debug("Set up the XSTPA context tabels");
	}
	
	private void addTable(final AbstractTableComposite table, String name){
		// set the formdata for the right (table) part
	    FormData tableForm = new FormData();
	    tableForm.top = new FormAttachment( 0 );
	    tableForm.left = new FormAttachment( 0 );
	    tableForm.right = new FormAttachment( 100 );
	    tableForm.bottom = new FormAttachment( 100 );
	    table.setLayoutData(tableForm);
	    this.tableList.add(table);
	    if(name != null && !name.isEmpty()){
		    // Add a button to switch tables (Context Table Button)
		    final Button tableBtn = new Button(xstpaNavigation, SWT.PUSH);
		    tableBtn.setText(name);
		    tableBtn.setLayoutData( new GridData(100,30));
		    tableBtn.addSelectionListener(new SelectionAdapter() {
			      public void widgetSelected(SelectionEvent event) {
			    	  openTable(table, tableBtn);
			  	    
			      }
		    });
		    this.tableButtons.add(tableBtn);
	    }
	}
	
	
	private void openTable(AbstractTableComposite table,Button button){
		for(Button btn : tableButtons){
			btn.setBackground(NORMAL);
		}
		for(AbstractTableComposite comp: tableList){
			if(comp != table){
				comp.setVisible(false);
				comp.deactivateTable();
			}
		}
  	    if(table != null){
  	    	table.activateTable();
  	    }
  	    if(button != null){
  	    	button.setBackground(HIGHLIGHT);
  	    }
	}

	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
	
	public void setController(XSTPADataController controller){
		if(controller != dataController){
			for(AbstractTableComposite table : this.tableList){
				table.setController(controller);
			}
			dataController = controller;
		}
	}
}
