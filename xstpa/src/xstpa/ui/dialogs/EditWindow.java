package xstpa.ui.dialogs;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import xstampp.util.STPAPluginUtils;
import xstpa.model.ControlActionEntry;
import xstpa.model.ProcessModelVariables;
import xstpa.model.Relation;
import xstpa.settings.PreferenceInitializer;
import xstpa.settings.PreferencePageSettings;
import xstpa.settings.XSTPAPreferenceConstants;
import xstpa.ui.View;

/**
 * 
 * This class is responsible for editing the Options for ACTS
 *
 */
public class EditWindow
{

	private abstract class CopyEditor extends EditingSupport{

		private TextCellEditor editor;

		public CopyEditor(TableViewer viewer) {
			super(viewer);
		    this.editor = new TextCellEditor(viewer.getTable()){
		    	@Override
		    	public boolean isDeleteEnabled() {
		    		return false;
		    	}
		    };
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return this.editor;
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected void setValue(Object element, Object value) {
			// TODO Auto-generated method stub
			
		}
	}
    // ==================== 2. Instance Fields ============================

    private Shell shell;
    // this Variables store the modes for ACTS   
    public static final String[] DALGO = { "ipog", "ipof", "ipof2", "ipog_d", "basechoice" };
    public static final String[] DALGO_TIP={
    	"For a moderate size System (max. 20 Parameters)",
    	"For a moderate size System (20 Parameters)",
    	"For a moderate size System (20 Parameters)",
    	"For a large size System",
    	"A special oneway testing Algorithm"
    };

    public static final String[] DSTRENGTH_LABELS ={"1","2","3","4","5","6","mixed"}; 
    public static final int[] DSTRENGTH ={1,2,3,4,5,6,-1}; 
    public Button[] algoButtons = new Button[DALGO.length];
    public static final String[] DMODE = { "scratch", "extend" };

    public static final String[] DCHANDLER = { "no", "forbiddentuples" , "solver"};
    public static final String[] DCHANDLER_LABELS = {"Ignore Constraints",
    												"Forbidden Tuples (default)",
    												"CSP Solver"};
    
    public static final String[] BOOL_TABLE = {"&&", "||", "=>"};
    public static final String[] RATIONAL_TABLE = {">", "<", "=", "!=", ">=", "<="};
    public static final String[] ARITHMETIC_TABLE = {"+", "-", "*", "/", "%"};
    private boolean refreshView;

    public static List<String> modes = new ArrayList<String>();
    public static List<String> constraints = new ArrayList<String>();
    private ControlActionEntry linkedCAE;
	private Combo strengthCombo,modeCombo,handlingCombo;
    public static List<Relation> relations = new ArrayList<Relation>();
    private HashMap<String,List<String>> valuesToVariables = new HashMap<>();
    private boolean isDirty;
    private SelectionAdapter dirtyListener= new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		isDirty = true;
    	}
	};
	private Text editor;
	private Label errorMsg;
    
    
    // ====================== 3. Subclasses ===================================
    
    /**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class MainViewContentProvider implements IStructuredContentProvider {
		
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		
		  /**
		   * Returns the objects for the tables
		   */
		  public Object[] getElements(Object inputElement) {
		    return ((List<?>) inputElement).toArray();
		  }
	}
	
    class relationsViewLabelProvider extends LabelProvider implements
	ITableLabelProvider {
		
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			
			Relation entry = (Relation) element;
	
			switch (columnIndex) {
		    case 0:		    	
		    	return String.valueOf((entry.getStrength()));
		    case 1:
		    	String temp = "";
		    	List<String> tempList = entry.getVariables();
		    	for (int i =0; i<tempList.size(); i++) {
		    		
		    		if (i == tempList.size()-1) {
		    			temp = temp.concat(tempList.get(i));
		    		}
		    		else {
		    			temp = temp.concat(tempList.get(i).concat(", "));
		    		}
		    	}
		      return temp;
			}
	
				
			return null;
			}
    }
    // ==================== 4. Constructors ===============================


	public EditWindow(ControlActionEntry linkedCAE)
    {
		this.refreshView = false;
		this.isDirty = false;
        shell = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE & SWT.MIN)| SWT.APPLICATION_MODAL);
        shell.addShellListener(new ShellAdapter() {
        	@Override
        	public void shellClosed(ShellEvent e) {
//        		if (isDirty&& MessageDialog.openConfirm(Display.getDefault().getActiveShell(),Messages.ThereAreUnsafedChanges,
//        												Messages.ThereAreUnsafedChangesDoYouWantToStoreThem)){
//        			apply();
//				}
        	}
        	@Override
        	public void shellDeactivated(ShellEvent e) {
        		shell.close();
        	}
		});
        
        
        shell.setLayout(new FormLayout());
        shell.setText("Context Table Settings");
        shell.setImage(View.LOGO);
        this.linkedCAE = linkedCAE;

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width)/4;
        int y = (dim.height)/2;
        // set the Location
        shell.setLocation(x,y);
        
        createContents(shell);
        
        shell.pack();
        
    }
    
    // ==================== 5. Creators =============================
    
    private void createContents(final Composite parent) {
    	
    	TabFolder folder = new TabFolder(parent, SWT.NONE);

	  
    	 // The FormLayout for the outer Composite
	    FormLayout formLayout = new FormLayout();
	    formLayout.marginHeight = 5;
	    formLayout.marginWidth = 5;
	    formLayout.spacing = 5;
	    
	    TabItem tab1 = new TabItem(folder, SWT.NONE);
	    tab1.setText("General Options");
	    tab1.setControl(createConfigComposite(folder, formLayout));

	    TabItem tab2 = new TabItem(folder, SWT.NONE);
	    tab2.setText("Relations");
	    tab2.setControl(createRelationsComposite(folder, formLayout));
	    
	    TabItem tab3 = new TabItem(folder, SWT.NONE);
	    tab3.setText("Constraints");
	    tab3.setControl(createConstraintsPage(folder, formLayout));
	    
	    
	    // Apply Button
	    Button ok = new Button(parent, SWT.PUSH);
	    ok.setText("Ok");
	    FormData fData = new FormData();
	    fData.right = new FormAttachment(100);
	    fData.bottom = new FormAttachment(100);
	    fData.height = SWT.DEFAULT;
	    ok.setLayoutData(fData);
	    
	    // Functionality of the Apply Button
	    ok.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		apply();
	    	}	  
	    });
	    
	    Button cancel = new Button(parent, SWT.PUSH);
	    cancel.setText("Cancel");
	    fData = new FormData();
	    fData.right = new FormAttachment(ok,-20);
	    fData.bottom = new FormAttachment(100);
	    fData.height = SWT.DEFAULT;
	    cancel.setLayoutData(fData);
	    
	    cancel.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		close();
	    	}
	    });
	    errorMsg = new Label(parent, SWT.READ_ONLY);
	    fData = new FormData();
	    fData.top = new FormAttachment(folder,5);
	    fData.left = new FormAttachment(0);
	    fData.right = new FormAttachment(cancel);
	    fData.bottom = new FormAttachment(100);
	    errorMsg.setLayoutData(fData);
	    
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0 );
	    fData.right = new FormAttachment(100);
	    fData.bottom = new FormAttachment(ok);
    	folder.setLayoutData(fData);
	   
    }

    private Composite createConfigComposite(TabFolder folder,FormLayout formLayout){
    	Composite outercomposite = new Composite(folder, SWT.NONE);	
    	outercomposite.setLayout(formLayout);
	    
	    Composite radioButtonsLabelComposite = new Composite( outercomposite, SWT.NONE );
	    
	    GridLayout grid = new GridLayout();
	    grid.marginHeight = 5;
	    grid.marginWidth = 5;
	    
	    radioButtonsLabelComposite.setLayout( grid );
	    
	    FormData fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0 );	    
	    
	    radioButtonsLabelComposite.setLayoutData( fData );
	    
	    Group algoGroup = new Group(radioButtonsLabelComposite, SWT.NONE);
	    algoGroup.setText("Algorithm: ");
	    algoGroup.setLayout(new GridLayout(5, false));
	    
	    
	    Composite mainComposite = new Composite( outercomposite, SWT.NONE );
	    
	    GridLayout mainGrid = new GridLayout();
	    mainGrid.marginHeight = 15;
	    mainGrid.marginWidth = 5;
	    mainGrid.horizontalSpacing = 20;
	    mainGrid.verticalSpacing = 20;
	    mainGrid.numColumns = 3;
	    mainComposite.setLayout( mainGrid );
	    
	    fData = new FormData();
	    fData.top = new FormAttachment( radioButtonsLabelComposite );
	    fData.left = new FormAttachment( 0 );
	    mainComposite.setLayoutData( fData );
	    
	    Composite buttonsComposite = new Composite( outercomposite, SWT.NONE );
	    GridLayout buttonGrid = new GridLayout();
	    buttonGrid.marginHeight = 35;
	    buttonGrid.marginWidth = 5;
	    buttonGrid.horizontalSpacing = 10;
	    buttonGrid.numColumns = 2;
	    buttonsComposite.setLayout( buttonGrid );
	    
	    fData = new FormData();
	    fData.top = new FormAttachment( mainComposite );
	    fData.left = new FormAttachment( 0 );
	    buttonsComposite.setLayoutData( fData );
	    
	    for(int i= 0;i< DALGO.length;i++){
	    	algoButtons[i] = new Button(algoGroup, SWT.RADIO);
	    	algoButtons[i].addSelectionListener(dirtyListener);
	    	algoButtons[i].setText(DALGO[i]);
	    	algoButtons[i].setToolTipText(DALGO_TIP[i]);
	    }
	   
	    // Add the components for the middle (main) part
	    Label strengthLabel = new Label(mainComposite, SWT.NONE);
	    strengthLabel.setText("Strength: ");
	    
	    strengthCombo = new Combo(mainComposite, SWT.READ_ONLY);
	    strengthCombo.addSelectionListener(dirtyListener);
	    for(String strength:DSTRENGTH_LABELS){
	    	strengthCombo.add(strength);
	    }
	   
	    
	    GridData data = new GridData(150, 80);
	    strengthCombo.setLayoutData(data);
	    
	    Label spaceHolderLabel = new Label(mainComposite, SWT.NONE);
	    spaceHolderLabel.setText("");
	    
	    //Second Row
	    Label modeLabel = new Label(mainComposite, SWT.NONE);
	    modeLabel.setText("Mode: ");
	    
	    modeCombo = new Combo(mainComposite, SWT.READ_ONLY);
	    modeCombo.addSelectionListener(dirtyListener);
	    modeCombo.add(DMODE[0]);
//	    modeCombo.add("Extend");
	    
	    modeCombo.select(0);
	    data = new GridData(150, 80);
	    modeCombo.setLayoutData(data);
	   
	    //Third Row
	    Label constraintHandlingLabel = new Label(mainComposite, SWT.WRAP);
	    constraintHandlingLabel.setText("Constraint\nHandling: ");
	    constraintHandlingLabel.pack();
	    
	    handlingCombo = new Combo(mainComposite, SWT.READ_ONLY);
	    handlingCombo.add(DCHANDLER_LABELS[0]);
	    handlingCombo.add(DCHANDLER_LABELS[1]);
	    handlingCombo.add(DCHANDLER_LABELS[2]);
	   
	    data = new GridData(150, 80);
	    handlingCombo.setLayoutData(data);
	    
//	    Button dontCareValues = new Button(mainComposite, SWT.CHECK);
//	    dontCareValues.setText("Randomize DontCare Values");
//	    dontCareValues.setSelection(true);
	    
	    // Add the buttons for the ButtonsComponent
	    
	    // Button which calls the PreferencePage for the ACTS Path
	    Button setPathBtn = new Button(buttonsComposite, SWT.PUSH);
	    setPathBtn.setText("Set ACTS Path");
	    setPathBtn.setLayoutData(new GridData(100, 30));
	    
	    Button loadDefaults = new Button(buttonsComposite, SWT.PUSH);
	    loadDefaults.setText("Load Defaults");
	    loadDefaults.setLayoutData(new GridData(100, 30));
	    /**
	     * Functionality of the setPathBtn
	     */
	    setPathBtn.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		Map<String,String> values=new HashMap<>();
  	    	  	values.put("preferencePageId", PreferencePageSettings.ID);
  	    	  	STPAPluginUtils.executeParaCommand("org.eclipse.ui.window.preferences", values);
  	    	  
	    	}
	    });
	    
	    loadDefaults.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		load(true);
	    	}
	    });
	    
	    load(false);
	    Label spacerLabel = new Label(buttonsComposite, SWT.NONE);
	    spacerLabel.setText("");
		return outercomposite;
    	
    }
    private Composite createRelationsComposite(TabFolder folder,FormLayout formLayout){
    	Composite relationsComposite = new Composite(folder, SWT.NONE);
	    
	    // The FormLayout for the relations Composite
	    relationsComposite.setLayout( formLayout );
	    
	    // Create the relationParamlistComposite
	    Composite relationParamlistComposite = new Composite(relationsComposite, SWT.NONE);
	    
	    GridLayout relationParamlistGrid = new GridLayout();
	    relationParamlistGrid.marginHeight = 5;
	    relationParamlistGrid.marginWidth = 5;
	    relationParamlistGrid.horizontalSpacing = 5;
	    relationParamlistGrid.numColumns = 1;
	    
	    relationParamlistComposite.setLayout( relationParamlistGrid );
	    
	    FormData fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0 );
	    
	    relationParamlistComposite.setLayoutData( fData );
	    
	    // Create the relationButtonsComposite
	    Composite relationButtonsComposite = new Composite(relationsComposite, SWT.NONE);
	    
	    GridLayout relationButtonsGrid = new GridLayout();
	    relationButtonsGrid.marginHeight = 5;
	    relationButtonsGrid.marginWidth = 5;
	    relationButtonsGrid.horizontalSpacing = 5;
	    relationButtonsGrid.numColumns = 1;
	    
	    relationButtonsComposite.setLayout( relationButtonsGrid );
	    
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( relationParamlistComposite );
	    
	    relationButtonsComposite.setLayoutData( fData );
	    
	    // Create the relationTableComposite
	    Composite relationTableComposite = new Composite(relationsComposite, SWT.NONE);
	    
	    GridLayout relationTableGrid = new GridLayout();
	    relationTableGrid.marginHeight = 5;
	    relationTableGrid.marginWidth = 5;
	    relationTableGrid.horizontalSpacing = 5;
	    relationTableGrid.numColumns = 1;
	    
	    relationTableComposite.setLayout( relationTableGrid );
	    
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( relationButtonsComposite );
	    
	    relationTableComposite.setLayoutData( fData );
	    
	    
	    // Add the Label for relationParamListComposite
	    Label params = new Label(relationParamlistComposite, SWT.NONE);
	    params.setText("Parameters");
	    
	    // Add the List for relationParamListComposite
	    final org.eclipse.swt.widgets.List paramList = new org.eclipse.swt.widgets.List(relationParamlistComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	    GridData data = new GridData(200, 350);
	    paramList.setLayoutData(data);
	    for (ProcessModelVariables entry : linkedCAE.getLinkedItems()) {
	    	paramList.add(entry.getName());
	    }
	    
	    // Add the Label for the relationButtonsComposite
	    Label strength = new Label(relationButtonsComposite, SWT.NONE);
	    strength.setText("Strength");
	    
	    //Add the Textbox for the relationButtonsComposite
	    final Text strengthBox = new Text(relationButtonsComposite, SWT.BORDER);
	    
	    // Add the Buttons for the relationButtonsComposite
	    final Button addRel = new Button(relationButtonsComposite, SWT.PUSH);
	    addRel.setText("Add");
	    data = new GridData(77, SWT.DEFAULT);
	    addRel.setLayoutData(data);
	    addRel.setEnabled(false);
	    
	    final Button removeRel = new Button(relationButtonsComposite, SWT.PUSH);
	    removeRel.setText("Remove");
	    data = new GridData(77, SWT.DEFAULT);
	    removeRel.setLayoutData(data);
	    removeRel.setEnabled(false);
	    
	    
	    //Add an empty Label, so the table spawns on same height as the other components
	    Label filler = new Label(relationTableComposite, SWT.NONE);
	    filler.setText(" ");
	    // Add the Table for relationTableComposite
	    final TableViewer relationsTableViewer = new TableViewer(relationTableComposite,SWT.BORDER | SWT.FULL_SELECTION );
	    relationsTableViewer.setContentProvider(new MainViewContentProvider());
	    relationsTableViewer.setLabelProvider(new relationsViewLabelProvider());
		
	    final Table relationsTable = relationsTableViewer.getTable();
	    data = new GridData(SWT.FILL, SWT.FILL, true, true);
	    data.heightHint = 333;
	    data.widthHint = 340;
	    relationsTable.setLayoutData(data);
	    
	    // add Columns for the parameterTable
	    new TableColumn(relationsTable, SWT.CENTER).setText("Strength");
	    new TableColumn(relationsTable, SWT.CENTER).setText("Parameter Names");
	    relationsTable.setHeaderVisible(true);
	    relationsTable.setLinesVisible(true);	    	    
	    relationsTableViewer.setInput(relations);
	    
	    // pack the table
	    for (int i = 0, n = relationsTable.getColumnCount(); i < n; i++) {
	    	relationsTable.getColumn(i).pack();
		  }
	    
	    // if the textbox gets selected, the add button becomes enabled
	    strengthBox.addListener(SWT.MouseUp, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if ( paramList.getSelectionCount() > 0) {
	    			addRel.setEnabled(true);
	    		}
				
			}
	    });
	    
	    // adds an Element to the table
	    addRel.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		boolean isNumeric = true;
	    		for(char c:strengthBox.getText().toCharArray()){
	    			isNumeric = Character.isDigit(c);
	    		}
	    		if ( isNumeric && (Integer.parseInt(strengthBox.getText()) <= paramList.getSelectionCount()) ) {
	    			relations.add(new Relation(Integer.parseInt(strengthBox.getText()), Arrays.asList(paramList.getSelection())));
	    			relationsTable.setVisible(false);
	    			relationsTableViewer.refresh();
	    			
	    		    // pack the table
	    		    for (int i = 0, n = relationsTable.getColumnCount(); i < n; i++) {
	    		    	relationsTable.getColumn(i).pack();
	    			  }
	    		    
	    		    relationsTable.setVisible(true);
	    			addRel.setEnabled(false);
	    			strengthBox.setText("");
	    			paramList.deselectAll();
	    		}
	    	}
	    });
	    
	    // removes an Element from the table
	    removeRel.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		relations.remove(relationsTable.getSelectionIndex());
	    		relationsTableViewer.refresh();
	    		if (relations.isEmpty()) {
	    			removeRel.setEnabled(false);
	    		}
	    	}
	    });
	    
	    // If an Element gets selected, the remove Button gets enabled
	    relationsTable.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    	
	    			removeRel.setEnabled(true);
	    		
	    	}
	    });
		return relationsComposite;
	    
	    
    }
    private Composite createConstraintsPage(TabFolder folder,FormLayout formLayout){
    	Composite constraintComposite = new Composite(folder, SWT.NONE);
	    
	    // The FormLayout for the constraints Composite
	    constraintComposite.setLayout( formLayout );
	    
	    
	    Composite constraintCommandsComposite = new Composite(constraintComposite, SWT.NONE);
	    
	    GridLayout constraintCommandsGrid = new GridLayout();
	    constraintCommandsGrid.marginHeight = 5;
	    constraintCommandsGrid.marginWidth = 5;
	    constraintCommandsGrid.horizontalSpacing = 5;
	    constraintCommandsGrid.numColumns = 1;
	    
	    constraintCommandsComposite.setLayout( constraintCommandsGrid );
	    
	    FormData fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0 );
	    fData.right = new FormAttachment( 50 );
	    
	    constraintCommandsComposite.setLayoutData( fData );
	    
	    
	    
	    Composite constraintTableComposite = new Composite(constraintComposite, SWT.NONE);
	    
	    GridLayout constraintTableGrid = new GridLayout();
	    constraintTableGrid.marginHeight = 5;
	    constraintTableGrid.marginWidth = 5;
	    constraintTableGrid.horizontalSpacing = 5;
	    constraintTableGrid.numColumns = 1;
	    constraintTableComposite.setLayout( constraintTableGrid );
	    
	    fData = new FormData();
	    fData.top = new FormAttachment( constraintCommandsComposite );
	    fData.left = new FormAttachment( 0 );	
	    fData.right = new FormAttachment( 50 );
	    
	    constraintTableComposite.setLayoutData( fData );
	    
	    
	    
	    Composite constraintEditorComposite = new Composite(constraintComposite, SWT.NONE);
	    
	    GridLayout constraintEditorGrid = new GridLayout();
	    constraintEditorGrid.marginHeight = 5;
	    constraintEditorGrid.marginWidth = 5;
	    constraintEditorGrid.horizontalSpacing = 5;
	    constraintEditorGrid.numColumns = 1;
	    constraintEditorComposite.setLayout( constraintEditorGrid );
	    
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 50 );
	    fData.right = new FormAttachment (100);

	    constraintEditorComposite.setLayoutData( fData );
	    
	    
	    // Add the group for constraintEditorComposite
	    Group constraintEditorGroup = new Group(constraintEditorComposite, SWT.NONE);
	    constraintEditorGroup.setText("Constraint Editor");	   
	    constraintEditorGroup.setLayout(new GridLayout(1, false));
	    
	    
	    Composite constraintEditorButtonsComposite = new Composite(constraintEditorComposite, SWT.NONE);
	    
	    GridLayout constraintEditorButtonsGrid = new GridLayout();
	    constraintEditorButtonsGrid.marginHeight = 5;
	    constraintEditorButtonsGrid.marginWidth = 5;
	    constraintEditorButtonsGrid.horizontalSpacing = 5;
	    constraintEditorButtonsGrid.numColumns = 2;
	    constraintEditorButtonsComposite.setLayout( constraintEditorButtonsGrid );
	    
	    
	    
	    Composite constraintDisplayComposite = new Composite(constraintComposite, SWT.NONE);
	    
	    GridLayout constraintDisplayGrid = new GridLayout();
	    constraintDisplayGrid.marginHeight = 5;
	    constraintDisplayGrid.marginWidth = 5;
	    constraintDisplayGrid.horizontalSpacing = 5;
	    constraintDisplayGrid.numColumns = 1;
	    constraintDisplayComposite.setLayout( constraintDisplayGrid );
	    
	    fData = new FormData();
	    fData.top = new FormAttachment( constraintEditorComposite );
	    fData.left = new FormAttachment( 50 );	    
	    fData.right = new FormAttachment( 100 );	    
	    constraintDisplayComposite.setLayoutData( fData );	    
	    
	    
	    // Add the Group for constraintDisplayComposite
	    Group constraintDisplayGroup = new Group(constraintDisplayComposite, SWT.NONE);
	    constraintDisplayGroup.setText("Added Constraints");
	    constraintDisplayGroup.setLayout(new GridLayout(1, false));
	    
	    
	    Composite constraintDisplayButtonsComposite = new Composite(constraintDisplayComposite, SWT.NONE);
	    
	    GridLayout constraintDisplayButtonsGrid = new GridLayout();
	    constraintDisplayButtonsGrid.marginHeight = 5;
	    constraintDisplayButtonsGrid.marginWidth = 5;
	    constraintDisplayButtonsGrid.horizontalSpacing = 5;
	    constraintDisplayButtonsGrid.numColumns = 2;
	    constraintDisplayButtonsComposite.setLayout( constraintDisplayButtonsGrid );
	    
	    // Add the group for constraint Commands
	    Group constraintCommandsGroup = new Group(constraintCommandsComposite, SWT.NONE);
	    constraintCommandsGroup.setText("Symbols");
	    constraintCommandsGroup.setLayout(new GridLayout(21, false));
	    
	    
	    // Add the Labels for constraintCommands
	    for (String string : BOOL_TABLE) {
	    	new Label(constraintCommandsGroup, SWT.NONE).setText(string);
	    }for (String string : ARITHMETIC_TABLE) {
	    	new Label(constraintCommandsGroup, SWT.NONE).setText(string);
	    }for (String string : RATIONAL_TABLE) {
	    	new Label(constraintCommandsGroup, SWT.NONE).setText(string);
	    }
	    // add the group for constraintTableComposite
	    Group constraintTableGroup = new Group(constraintTableComposite, SWT.NONE);
	    constraintTableGroup.setText("Parameters");
	    constraintTableGroup.setLayout(new GridLayout(1, false));
	    
	    // add the table for constraintTableComposite
		TableViewer constraintTableViewer = new TableViewer(constraintTableGroup, SWT.FULL_SELECTION );
		constraintTableViewer.setContentProvider(new MainViewContentProvider());
	    Table parameterTable = constraintTableViewer.getTable();
	    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
	    data.heightHint = 300;
	    data.widthHint = 296;
	    parameterTable.setLayoutData(data);
	    
	    // add Columns for the parameterTable
	    TableViewerColumn variableCViewer = new TableViewerColumn(constraintTableViewer, SWT.CENTER);
	    variableCViewer.getColumn().setText("Parameter Name");
	    final ColumnLabelProvider variableProvider =new ColumnLabelProvider(){
	    	@Override
	    	public String getText(Object element) {
	    		return ((ProcessModelVariables) element).getName();
	    	}
	    };
	    variableCViewer.setEditingSupport(new CopyEditor(constraintTableViewer){
	    	@Override
	    	protected String getValue(Object element) {
	    		return variableProvider.getText(element);
	    	}
	    });
	    variableCViewer.setLabelProvider(variableProvider);
	    TableViewerColumn valueCViewer = new TableViewerColumn(constraintTableViewer, SWT.CENTER);
	    final ColumnLabelProvider valueProvider =new ColumnLabelProvider(){
	    	@Override
	    	public String getText(Object element) {
	    		
	    		String temp = "";
	        	List<String> tempList = ((ProcessModelVariables) element).getValues();
	        	
	        	for (int i =0; i<tempList.size(); i++) {
	        		
	        		if (i == tempList.size()-1) {
	        			temp = temp.concat(tempList.get(i));
	        		}
	        		else {
	        			temp = temp.concat(tempList.get(i).concat(", "));
	        		}
	        	}
	        	return temp;
	    	}
	    };

	    valueCViewer.setEditingSupport(new CopyEditor(constraintTableViewer){
	    	@Override
	    	protected String getValue(Object element) {
	    		return valueProvider.getText(element);
	    	}
	    });
	    valueCViewer.getColumn().setText("Parameter Value");
	    valueCViewer.setLabelProvider(valueProvider);
	    
	    parameterTable.setHeaderVisible(true);
	    parameterTable.setLinesVisible(true);
	    for(ProcessModelVariables variable: linkedCAE.getLinkedItems()){
	    	ArrayList<String> list = new ArrayList<>(variable.getValues());
	    	this.valuesToVariables.put(variable.getName(),list);
	    }
	    constraintTableViewer.setInput(linkedCAE.getLinkedItems());
	    // pack the table
	    for (int i = 0, n = parameterTable.getColumnCount(); i < n; i++) {
			  parameterTable.getColumn(i).pack();
		  }
	    

	    // Add the Editor Field
	    editor = new Text(constraintEditorGroup, SWT.BORDER);    
	    data = new GridData(310, 124);
	    editor.setLayoutData(data);
	    editor.setMessage("Enter a Expression: e.g. Variable == \"Value\"");
	    // Add the Buttons for constraint Editor Composite
	    Button clear = new Button(constraintEditorButtonsComposite, SWT.PUSH);
	    clear.setText("Clear");
	    clear.setLayoutData(new GridData());
	    // If pressed, the editor field is cleared
	    clear.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		editor.setText("");
	    		editor.setFocus();
	    	}
	    });
	    
	    Button addConstraint = new Button(constraintEditorButtonsComposite, SWT.PUSH);
	    addConstraint.setText("Add Constraint");
	    addConstraint.setLayoutData(new GridData());
	    	    
	    
	    // Add the Display List and Initialize it
	    final org.eclipse.swt.widgets.List displayList = new org.eclipse.swt.widgets.List(constraintDisplayGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	    data = new GridData(300, 124);
	    displayList.setLayoutData(data);
	    for (String entry : constraints) {
	    	displayList.add(entry);
	    }
	    // Add the Buttons for constraintDisplayComposite
	    Button remove = new Button (constraintDisplayButtonsComposite, SWT.PUSH);
	    remove.setText("Remove");
	    remove.setLayoutData(new GridData());
	    
	    
	    // adds the constraint to the list and clears the editor
	    addConstraint.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		
	    		if(!editor.getText().isEmpty()){
	    			errorMsg.setText("");
		    		displayList.add(editor.getText());
		    		constraints.add(editor.getText());
		    		editor.setText("");
		    		editor.setFocus();
	    		}else{
	    			errorMsg.setText("The given constraint is not valid for the current model!");
	    		}
	    		
	    	}
	    });
	    // removes an Element from the List
	    remove.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		constraints.remove(displayList.getSelectionIndex());
	    		displayList.remove(displayList.getSelectionIndex());
	    	}
	    });
		return constraintComposite;
	    
	    
    }
    // ==================== 6. Action Methods =============================


    /**
     * <Constraint> ::= <Simple_Constraint> | <Constraint> <Boolean_Op>   <Constraint> 
     * @param constraint <Constraint>
     * @return whether the Syntax for <Constraint> is satisfied
     */
    private boolean validate(String editorText){
    	String constraint = editorText.replaceAll("(|)","");
    	
    	if(constraint == null){
    		return false;
    	}
    	for (String string : BOOL_TABLE) {
			if((constraint.contains(string))){
				String left = constraint.substring(0, constraint.indexOf(string)); 
				String right=constraint.substring(constraint.indexOf(string)+string.length());
				return validate(left) && validate(right);
			}
		}
    	return checkConstraint(constraint);
    	
    	
    }
    /**
     * <Simple_Constraint> ::= <Term> <Relational_Op> <Term>
     * 
     * @param constraint <Simple_Constraint>
     * @return whether the syntax of <Simple_Constraint> is correct or not
     */
    private boolean checkConstraint(String constraint){
    	for (String string : RATIONAL_TABLE) {
			if((constraint.contains(string))){
				String leftSide = constraint.substring(0, constraint.indexOf(string));
				String rightSide = constraint.substring(constraint.indexOf(string)+string.length());
				return checkTerm(leftSide) && checkTerm(rightSide);
			}
		}
    	return false;
    }
    
    /**
	 * <Term> ::= <Parameter> | <Parameter> <Arithmetic_Op> <Parameter> | 
	 * 			  <Parameter> <Arithmetic_Op> <Value>
	 */
    private boolean checkTerm(String term){
    	boolean isTerm = this.valuesToVariables.containsKey(term);
    	if(!isTerm){
    		
    		for (String string : ARITHMETIC_TABLE) {
    			if((term.contains(string))){
    				String leftSide = term.substring(0, term.indexOf(string));
    				String rightSide = term.substring(term.indexOf(string)+string.length());
    				if(this.valuesToVariables.containsKey(leftSide)){
    					isTerm = this.valuesToVariables.get(leftSide).contains(rightSide);
    					isTerm = isTerm || this.valuesToVariables.containsKey(rightSide);
    					
    					if(!isTerm){
    						isTerm = true;
	    					for(char c:rightSide.toCharArray()){
	    						isTerm = isTerm && Character.isDigit(c);
	    					}
    					}
    				}
    			}
    		}
    	}
    	return isTerm;
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

    private void apply(){
    	
    	PreferenceInitializer.store.setValue(XSTPAPreferenceConstants.ACTS_STRENGTH, DSTRENGTH[strengthCombo.getSelectionIndex()]);
    	for (int i=0;i<DALGO.length;i++) {
			if(algoButtons[i].getSelection()){
		    	PreferenceInitializer.store.setValue(XSTPAPreferenceConstants.ACTS_ALGORITHMUS, DALGO[i]);
			}
		}
    	PreferenceInitializer.store.setValue(XSTPAPreferenceConstants.ACTS_MODE, modeCombo.getText());
    	PreferenceInitializer.store.setValue(XSTPAPreferenceConstants.ACTS_CHANDLER, DCHANDLER[handlingCombo.getSelectionIndex()]);
		refreshView = true;
		close();
    }
    
    private void load(boolean defaultValues){
    	int strength;
    	String alg;
    	String mode;
    	String chandler;
    	if(defaultValues){
        	alg= DALGO[0];
        	strength =  1;
        	mode= DMODE[0];
        	chandler= DCHANDLER[1];
    	}else{
        	strength =  PreferenceInitializer.store.getInt(XSTPAPreferenceConstants.ACTS_STRENGTH);
        	alg= PreferenceInitializer.store.getString(XSTPAPreferenceConstants.ACTS_ALGORITHMUS);
        	mode= PreferenceInitializer.store.getString(XSTPAPreferenceConstants.ACTS_MODE);
        	chandler= PreferenceInitializer.store.getString(XSTPAPreferenceConstants.ACTS_CHANDLER);
    		
    	}
    	for(int i= 0; i<DSTRENGTH.length; i++){
    		if(DSTRENGTH[i] ==strength){
    			this.strengthCombo.select(i);
    		}
    	}
    	
    	for(int i=0;i<DALGO.length;i++){
    		if(DALGO[i].equals(alg)){
        		algoButtons[i].setSelection(true);
        	}else{
        		algoButtons[i].setSelection(false);
        	}
    	}

    	for(int i=0;i<DMODE.length;i++){
    		if(DMODE[i].equals(mode)){
        		modeCombo.select(i);
        	}
    	}
    	
    	for(int i=0;i<DCHANDLER.length;i++){
    		if(DCHANDLER[i].equals(chandler)){
        		handlingCombo.select(i);
        	}
    	}
    	
    }
    public void close()
    {
    	// Don't call shell.close(), because then
        // you'll have to re-create it
        shell.setVisible(false);
    }
    
    public List<String> getModes() {
		return modes;
	}
}
