package xstpa;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import messages.Messages;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
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

/**
 * 
 * This class is responsible for editing the Options for ACTS
 *
 */
public class editWindow
{

    // ==================== 2. Instance Fields ============================

    private Shell shell;
    // this Variables store the modes for ACTS   
    public static final String[] DALGO = { "ipog", "ipof", "ipof2", "ipog_d", "basechoice" };
    
    
    public static final String[] DMODE = { "scratch", "extend" };
    
    public static final String[] DCHANDLER = { "no", "solver", "forbiddentuples" };
    public static List<String> modes = new ArrayList<String>();
    public static List<String> constraints = new ArrayList<String>();
    private ControlActionEntrys linkedCAE;
    private View view;
	private Button ipogfButton, ipogButton, ipogf2Button, ipogdButton, baseChoiceButton,ignoreConstraints;
	private Combo strengthCombo,modeCombo,handlingCombo;
    public static List<Relation> relations = new ArrayList<Relation>();
    private boolean isDirty;
    private SelectionAdapter dirtyListener= new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		isDirty = true;
    	}
	};
    
    
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
    
    class settingsViewLabelProvider extends LabelProvider implements
	ITableLabelProvider {
		
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			
			ProcessModelVariables entry = (ProcessModelVariables) element;
	
			switch (columnIndex) {
		    case 0:
		    	String tempName = "";
		    	tempName = entry.getName().replace(" ", "_");
		    	return tempName;
		    case 1:
		    	String temp = "";
		    	List<String> tempList = entry.getValues();
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


	public editWindow(ControlActionEntrys linkedCAE, View view)
    {
		this.isDirty = false;
        shell = new Shell(SWT.SHELL_TRIM & (~SWT.RESIZE & SWT.MIN));
        shell.addShellListener(new ShellAdapter() {
        	@Override
        	public void shellClosed(ShellEvent e) {
        		if (isDirty&& MessageDialog.openConfirm(shell,Messages.ThereAreUnsafedChanges,
        												Messages.ThereAreUnsafedChangesDoYouWantToStoreThemAbort)){
        			apply();
				}
        		super.shellClosed(e);
        	}
        	
		});
        
        
        shell.setLayout(new GridLayout(1, false));
        shell.setText("Context Table Settings");
        shell.setImage(View.LOGO);
        this.linkedCAE = linkedCAE;

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width)/4;
        int y = (dim.height)/2;
        // set the Location
        shell.setLocation(x,y);
        
        this.view = view;
        createContents(shell);
        
        shell.pack();
        
    }
    
    // ==================== 5. Creators =============================
    
    private void createContents(final Composite parent) {
    	
    	TabFolder folder = new TabFolder(parent, SWT.NONE);
      	 
	    // ===================== Tab 1 (Constraints)Start ===============================
	    TabItem tab1 = new TabItem(folder, SWT.NONE);
	    tab1.setText("General Options");
    	
    	Composite outercomposite = new Composite(folder, SWT.NONE);	
    	
	    // The FormLayout for the outer Composite
	    FormLayout formLayout = new FormLayout();
	    formLayout.marginHeight = 5;
	    formLayout.marginWidth = 5;
	    formLayout.spacing = 5;
	    outercomposite.setLayout( formLayout );
	    
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
	    
	    
	    // Add the radio Buttons
	    ipogButton = new Button(algoGroup, SWT.RADIO);
	    ipogButton.addSelectionListener(dirtyListener);
	    ipogButton.setText("IPOG(Recommended)");
	    ipogButton.setToolTipText("For a moderate size System (max. 20 Parameters)");
	    if (!modes.isEmpty()) {
	    	if (modes.get(0).equals(DALGO[0])) {
	    		ipogButton.setSelection(true);
	    	}
	    }
	    else {
	    	ipogButton.setSelection(true);
	    }
	    
	    
	    ipogfButton = new Button(algoGroup, SWT.RADIO);
	    ipogfButton.addSelectionListener(dirtyListener);
	    ipogfButton.setToolTipText("For a moderate size System (20 Parameters)");
	    ipogfButton.setText("IPOG-F");
	    if (!modes.isEmpty()) {
	    	if (modes.get(0).equals(DALGO[1])) {
	    		ipogfButton.setSelection(true);
	    	}
	    }
	    
	    ipogf2Button = new Button(algoGroup, SWT.RADIO);
	    ipogf2Button.addSelectionListener(dirtyListener);
	    ipogf2Button.setToolTipText("For a moderate size System (20 Parameters)");
	    ipogf2Button.setText("IPOG-F2");
	    if (!modes.isEmpty()) {
	    	if (modes.get(0).equals(DALGO[2])) {
	    		ipogf2Button.setSelection(true);
	    	}
	    }
	    
	    ipogdButton = new Button(algoGroup, SWT.RADIO);
	    ipogdButton.addSelectionListener(dirtyListener);
	    ipogdButton.setToolTipText("For a large size System");
	    ipogdButton.setText("IPOG-D");
	    if (!modes.isEmpty()) {
	    	if (modes.get(0).equals(DALGO[3])) {
	    		ipogdButton.setSelection(true);
	    	}
	    }
	    
	    baseChoiceButton = new Button(algoGroup, SWT.RADIO);
	    baseChoiceButton.addSelectionListener(dirtyListener);
	    baseChoiceButton.setText("Base Choice");
	    baseChoiceButton.setToolTipText("A special oneway testing Algorithm");
	    if (!modes.isEmpty()) {
	    	if (modes.get(0).equals(DALGO[4])) {
	    		baseChoiceButton.setSelection(true);
	    	}
	    }
	    // Add the components for the middle (main) part
	    Label strengthLabel = new Label(mainComposite, SWT.NONE);
	    strengthLabel.setText("Strength: ");
	    
	    strengthCombo = new Combo(mainComposite, SWT.READ_ONLY);
	    strengthCombo.addSelectionListener(dirtyListener);
	    strengthCombo.add("1");
	    strengthCombo.add("2");
	    strengthCombo.add("3");
	    strengthCombo.add("4");
	    strengthCombo.add("5");
	    strengthCombo.add("6");
	    strengthCombo.add("Mixed");
	    if (!modes.isEmpty()) {
	    	if (modes.get(1).equals("-1")) {
	    		strengthCombo.select(6);
	    	}
	    	else {
	    		strengthCombo.select(Integer.parseInt(modes.get(1))-1);
	    	}
	    }
	    else {
	    	strengthCombo.select(1);
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
	    modeCombo.add("Scratch");
//	    modeCombo.add("Extend");
	    
	    modeCombo.select(0);
	    data = new GridData(150, 80);
	    modeCombo.setLayoutData(data);
	    
	    ignoreConstraints = new Button(mainComposite, SWT.CHECK);
	    ignoreConstraints.setText("Ignore Constraints");
	    if (!modes.isEmpty()) {
	    	if (modes.get(3).equals(DCHANDLER[0])) {
	    		ignoreConstraints.setSelection(true);
	    	}
	    }
	    
	    //Third Row
	    Label constraintHandlingLabel = new Label(mainComposite, SWT.WRAP);
	    constraintHandlingLabel.setText("Constraint\nHandling: ");
	    constraintHandlingLabel.pack();
	    
	    handlingCombo = new Combo(mainComposite, SWT.READ_ONLY);
	    handlingCombo.add("Forbidden Tuples (default)");
	    handlingCombo.add("CSP Solver");
	    if (!modes.isEmpty()) {
	    	if (modes.get(3).equals(DCHANDLER[2])) {
	    		handlingCombo.select(0);
	    	}
	    	else {
	    		handlingCombo.select(0);
	    	}
	    }
	    else {
	    	handlingCombo.select(0);
	    }
	    data = new GridData(150, 80);
	    handlingCombo.setLayoutData(data);
	    
//	    Button dontCareValues = new Button(mainComposite, SWT.CHECK);
//	    dontCareValues.setText("Randomize DontCare Values");
//	    dontCareValues.setSelection(true);
	    
	    // Add the buttons for the ButtonsComponent
	    
	    // Button which calls the PreferencePage for the ACTS Path
	    Button setPathBtn = new Button(buttonsComposite, SWT.PUSH);
	    setPathBtn.setText("Set ACTS Path");
	    data = new GridData(100, 30);
	    setPathBtn.setLayoutData(data);
	    
	    /**
	     * Functionality of the setPathBtn
	     */
	    setPathBtn.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		Map<String,String> values=new HashMap<>();
  	    	  	values.put("xstampp.command.preferencePage", "xstpa.preferencePage");
  	    	  	STPAPluginUtils.executeParaCommand("astpa.preferencepage", values);
  	    	  
	    	}
	    });

	    
	    Label spacerLabel = new Label(buttonsComposite, SWT.NONE);
	    spacerLabel.setText("");
	    
	    // Apply Button
	    Button apply = new Button(buttonsComposite, SWT.PUSH);
	    apply.setText("Apply");
	    data = new GridData(100, 30);
	    apply.setLayoutData(data);
	    
	    // Functionality of the Apply Button
	    apply.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		apply();
	    	}	  
	    });
	    
	    Button cancel = new Button(buttonsComposite, SWT.PUSH);
	    cancel.setText("Cancel");
	    data = new GridData(100, 30);
	    cancel.setLayoutData(data);
	    
	    cancel.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		close();
	    	}
	    });
	    		
	    	
	    // =============================== tab1 END ===================================
	    
	    
	    
	    
	    // ============================== tab2 (Relations) START =========================
	    TabItem tab2 = new TabItem(folder, SWT.NONE);
	    tab2.setText("Relations");
	    
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
	    
	    fData = new FormData();
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
	    data = new GridData(200, 350);
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
	    
	    final Button applyRel = new Button(relationButtonsComposite, SWT.PUSH);
	    applyRel.setText("Apply");
	    data = new GridData(77, SWT.DEFAULT);
	    applyRel.setLayoutData(data);
	    applyRel.setEnabled(true);
	    
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
	    		
	    		if ( (isNumeric(strengthBox.getText())) && (Integer.parseInt(strengthBox.getText()) <= paramList.getSelectionCount()) ) {
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
	    
	    // Functionality of the Apply Button
	    applyRel.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		apply();
	    	}	  
	    });
	    
	    tab2.setControl(relationsComposite);
	    
	    
	    // ============================== tab2 (Relations) END ==========================
	    
	    
	    
	    
	    
	    // ============================== tab3 (General Options) START ======================
	    TabItem tab3 = new TabItem(folder, SWT.NONE);
	    tab3.setText("Constraints");
	    
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
	    
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0 );
	    
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
	    fData.left = new FormAttachment( constraintCommandsComposite );
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
	    fData.left = new FormAttachment( constraintCommandsComposite );	    
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
	    Label separator = new Label(constraintCommandsGroup, SWT.NONE);
	    separator.setText(" | ");
	    
	    Label leftBrace = new Label(constraintCommandsGroup, SWT.NONE);	    
	    FontDescriptor boldDescriptor = FontDescriptor.createFrom(leftBrace.getFont()).setStyle(SWT.BOLD);
	    Font boldFont = boldDescriptor.createFont(leftBrace.getDisplay());
	    leftBrace.setFont( boldFont );
	    leftBrace.setText("( ");
	    
	    Label rightBrace = new Label(constraintCommandsGroup, SWT.NONE);
	    rightBrace.setFont( boldFont );
	    rightBrace.setText(") ");
	    
	    separator = new Label(constraintCommandsGroup, SWT.NONE);
	    separator.setText("| ");
	    
	    Label equalTo = new Label(constraintCommandsGroup, SWT.NONE);
	    equalTo.setFont( boldFont );
	    equalTo.setText("= ");
	    
	    Label notEqualTo = new Label(constraintCommandsGroup, SWT.NONE);
	    notEqualTo.setFont( boldFont );
	    notEqualTo.setText("!= ");
	    
	    Label greaterThan = new Label(constraintCommandsGroup, SWT.NONE);
	    greaterThan.setFont( boldFont );
	    greaterThan.setText("> ");
	    
	    Label lessThan = new Label(constraintCommandsGroup, SWT.NONE);
	    lessThan.setFont( boldFont );
	    lessThan.setText("< ");
	    
	    Label lessOrEqualTo = new Label(constraintCommandsGroup, SWT.NONE);
	    lessOrEqualTo.setFont( boldFont );
	    lessOrEqualTo.setText("<= ");
	    
	    Label greaterOrEqualTo = new Label(constraintCommandsGroup, SWT.NONE);
	    greaterOrEqualTo.setFont( boldFont );
	    greaterOrEqualTo.setText(">= ");
	    
	    separator = new Label(constraintCommandsGroup, SWT.NONE);
	    separator.setText("| ");
	    
	    Label and = new Label(constraintCommandsGroup, SWT.NONE);
	    and.setFont( boldFont );
	    and.setText("&& ");
	    
	    Label imply = new Label(constraintCommandsGroup, SWT.NONE);
	    imply.setFont( boldFont );
	    imply.setText("|| ");
	    
	    Label not = new Label(constraintCommandsGroup, SWT.NONE);
	    not.setFont( boldFont );
	    not.setText("! ");
	    
	    separator = new Label(constraintCommandsGroup, SWT.NONE);
	    separator.setText("| ");
	    
	    Label multiply = new Label(constraintCommandsGroup, SWT.NONE);
	    multiply.setFont( boldFont );
	    multiply.setText("* ");
	    
	    Label divide = new Label(constraintCommandsGroup, SWT.NONE);
	    divide.setFont( boldFont );
	    divide.setText("/ ");
	    
	    Label minus = new Label(constraintCommandsGroup, SWT.NONE);
	    minus.setFont( boldFont );
	    minus.setText("- ");
	    
	    Label mod = new Label(constraintCommandsGroup, SWT.NONE);
	    mod.setFont( boldFont );
	    mod.setText("% ");
	    
	    Label add = new Label(constraintCommandsGroup, SWT.NONE);
	    add.setFont( boldFont );
	    add.setText("+ ");
	    
	    separator = new Label(constraintCommandsGroup, SWT.NONE);
	    separator.setText("| ");
	    // add the group for constraintTableComposite
	    Group constraintTableGroup = new Group(constraintTableComposite, SWT.NONE);
	    constraintTableGroup.setText("Parameters");
	    constraintTableGroup.setLayout(new GridLayout(1, false));
	    
	    // add the table for constraintTableComposite
		TableViewer constraintTableViewer = new TableViewer(constraintTableGroup, SWT.FULL_SELECTION );
		constraintTableViewer.setContentProvider(new MainViewContentProvider());
		constraintTableViewer.setLabelProvider(new settingsViewLabelProvider());
		
	    Table parameterTable = constraintTableViewer.getTable();
	    data = new GridData(SWT.FILL, SWT.FILL, true, true);
	    data.heightHint = 300;
	    data.widthHint = 296;
	    parameterTable.setLayoutData(data);
	    
	    // add Columns for the parameterTable
	    new TableColumn(parameterTable, SWT.CENTER).setText("Parameter Name");
	    new TableColumn(parameterTable, SWT.CENTER).setText("Parameter Value");
	    parameterTable.setHeaderVisible(true);
	    parameterTable.setLinesVisible(true);
	    constraintTableViewer.setInput(linkedCAE.getLinkedItems());
	    // pack the table
	    for (int i = 0, n = parameterTable.getColumnCount(); i < n; i++) {
			  parameterTable.getColumn(i).pack();
		  }
	    

	    // Add the Editor Field
	    final Text editor = new Text(constraintEditorGroup, SWT.BORDER);    
	    data = new GridData(310, 124);
	    editor.setLayoutData(data);
	    editor.setMessage("Enter a Expression: e.g. Variable == \"Value\" or Variable < \"3\"");
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
	    
	    // The Apply Button
	    Button applyConstraints = new Button (constraintDisplayButtonsComposite, SWT.PUSH);
	    applyConstraints.setText("Apply");
	    applyConstraints.setLayoutData(new GridData());
	    
	    // adds the constraint to the list and clears the editor
	    addConstraint.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		displayList.add(editor.getText());
	    		constraints.add(editor.getText());
	    		editor.setText("");
	    		editor.setFocus();
	    		
	    	}
	    });
	    // removes an Element from the List
	    remove.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		constraints.remove(displayList.getSelectionIndex());
	    		displayList.remove(displayList.getSelectionIndex());
	    	}
	    });
	    
	 // Functionality of the Apply Button
	    applyConstraints.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {apply();}	  
	    });
	    
	    // TODO LOAD FROM FILE?
	    tab1.setControl(outercomposite);
	    tab3.setControl(constraintComposite);
	    
	    
	    // ============================== tab3 (Constraints) END =========================
    }


    // ==================== 6. Action Methods =============================


    
    public void open()
    {
        shell.open();
        
        
    }

    private void apply(){
    	// clear modes
		modes.clear();
    	//get the values and apply them
		// get the algo
		if (ipogButton.getSelection()) {
			modes.add(DALGO[0]);
		}
		if (ipogfButton.getSelection()) {
			modes.add(DALGO[1]);
		}
		if (ipogf2Button.getSelection()) {
			modes.add(DALGO[2]);
		}
		if (ipogdButton.getSelection()) {
			modes.add(DALGO[3]);
		}
		if (baseChoiceButton.getSelection()) {
			modes.add(DALGO[4]);
		}
		// add ddoi
		if (strengthCombo.getSelectionIndex() == 6) {
			modes.add("-1");
		}
		else {
			modes.add(Integer.toString(strengthCombo.getSelectionIndex()+1));
		}
		// dmode
		if (modeCombo.getSelectionIndex() == 0) {
			modes.add(DMODE[0]);
		}
		else {
			modes.add(DMODE[1]);
		}
		// dchandler
		if (ignoreConstraints.getSelection()) {
			modes.add(DCHANDLER[0]);
		}
		else if (handlingCombo.getSelectionIndex() == 0) {
			modes.add(DCHANDLER[1]);
		}
		else {
			modes.add(DCHANDLER[2]);
		}
		close();
		view.createTableColumns(0);
		view.writeFile(false);
        view.open(false);
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

	public boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}
