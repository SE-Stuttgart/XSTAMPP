package xstampp.stpapriv.ui.relation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import xstampp.stpapriv.Activator;
import xstampp.stpapriv.model.relation.UnsafeUnsecureController;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;



public class SecView extends ViewPart{
	public static final String ID = "stpapriv.view.context";
		
	public static final String COMMENTS = "Description";
	
	public static final String CONTROL_ACTIONS = "Control Actions";
	
	public static final String UNSECURE_CONTROL_ACTIONS = "Unsecure Control Actions";
	
	public static final String SAFETY_CRITICAL = "Safety Critical";
	
	public static final String SECURITY_CRITICAL = "Security Critical";
	
	public static final String PRIVACY_CRITICAL = "Privacy Critical";
	
	public static final String CA_ENTRY_CLASS = "xstpasec.ControlActionEntrys";
	
	
	
	

	
	

	/**
	 * String array that contains all headers for the columns of the 
	 * control actions table that contains all stored control actions
	 */
	public static final String[] CA_PROPS_COLUMNS = {  CONTROL_ACTIONS, UNSECURE_CONTROL_ACTIONS, PRIVACY_CRITICAL, SECURITY_CRITICAL, SAFETY_CRITICAL, COMMENTS};
	

	
	// static fields to hold the images
	public static final Image CHECKED = Activator.getImageDescriptor("icons/checked.gif").createImage();
	  
	public static final Image UNCHECKED = Activator.getImageDescriptor("icons/unchecked.gif").createImage();

	public static final Device device = Display.getCurrent ();
	
	public static final Color BACKGROUND = new Color(device,204,204,255);
	
	public static final Color CONFLICT = new Color(device,255,0,0);
	
	public static final Color HIGHLIGHT = new Color(device,0,0,0);
	
	public static final Color NORMAL = new Color(device,224,224,224);
	
	private UnsafeUnsecureController dataController;
	
	AbstractTableComposite  mainTable;
	
	

	private List<AbstractTableComposite> tableList;
	private List<Button> tableButtons;


	protected AbstractTableComposite ltlComposite;

	public static String[] contextProps;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
	    // set title and Image
	    setPartName("Control Actions Table");
	    
	    Composite outercomposite = new Composite(parent, SWT.NONE);
	    
	    FormLayout formLayout = new FormLayout();
	    formLayout.marginHeight = 5;
	    formLayout.marginWidth = 5;
	    formLayout.spacing = 5;
	    outercomposite.setLayout( formLayout );

	    
	    FormData fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0 );
	    
	    fData.bottom = new FormAttachment( 100 );
	    
	    Composite xstpaTableComposite = new Composite(outercomposite, SWT.BORDER);
	    
	 // set the formdata for the right (table) part
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0 );
	    fData.right = new FormAttachment( 100 );
	    fData.bottom = new FormAttachment( 100 );
	    xstpaTableComposite.setLayoutData( fData );
	    xstpaTableComposite.setLayout(new FormLayout());
	    this.tableButtons = new ArrayList<>();
	    this.tableList = new ArrayList<>();
	    

	    
	     mainTable = new ControlTable(xstpaTableComposite);
	    addTable(mainTable, "Control Actions");
	    

	    
	    IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	    if(part != null && part instanceof StandartEditorPart){
	    	UnsafeUnsecureController controller = Activator.getDataFor(((StandartEditorPart)part).getProjectID());
	    	if(controller!= null){
	    		setController(controller);
	    	}else{
	    		setController(new UnsafeUnsecureController(null));
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
		mainTable.refreshTable();
	}
	
	@Override
	public void dispose() {
	  for(AbstractTableComposite table : this.tableList){
      table.dispose();
    }
	  super.dispose();
	}
	public void setController(UnsafeUnsecureController controller){
		if(controller != dataController){
			for(AbstractTableComposite table : this.tableList){
				table.setController(controller);
			}
			dataController = controller;
		}
	}
	
}
