package xstpa;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.swing.JOptionPane;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import export.ExportContent;
import export.ExportJob;
import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.controlaction.NotProvidedValuesCombi;
import xstampp.astpa.model.controlaction.ProvidedValuesCombi;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.hazacc.Hazard;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.STPAEditorInput;

public class View extends ViewPart implements Observer {
	public static final String ID = "xstpa.view";
	
	// Table column names/properties
	public static final String CONTROLLER = "Controllers";

	public static final String PM = "Process Models";

	public static final String PMV = "Process Model Variables";

	public static final String PMVV = "Values";
	
	public static final String COMMENTS = "Description";
	
	public static final String CONTROL_ACTIONS = "Control Actions";
	
	public static final String SAFETY_CRITICAL = "Safety Critical";
	
	public static final String CWPMCLASS = "xstpa.ControllerWithPMEntry";
	
	public static final String CAECLASS = "xstpa.ControlActionEntrys";
	
	public static final String PMVCLASS = "xstpa.ProcessModelVariables";
	
	public static final String ENTRY_ID = "ID";
	
	public static final String LIST_of_CA = "List of Control Actions";
	
	public static final String CONTEXT = "Context";
	
	public static final String HAZARDOUS = "Hazardous?";
	
	public static final String HANYTIME = "Hazardous if provided anytime";
	
	public static final String HEARLY = "Hazardous if provided to early";
	
	public static final String HLATE = "Hazardous if provided to late";
	
	public static final String LTL_RULES = "LTL Formula";
	
	public static final String UCA = "Related Unsafe CA";

	public static final String REL_HAZ = "Linked Hazards";
	
	public static final String REFINED_SAFETY = "Refined Safety Requirements";
	
	public static final String CRITICAL_COMBI = "Critical Combinations";

	public static final String[] PROPS = { CONTROLLER, PM, PMV, PMVV, COMMENTS };
	
	public static final String[] CA_PROPS = { CONTROL_ACTIONS, SAFETY_CRITICAL, COMMENTS};
	
	public static final String[] RS_PROPS = { ENTRY_ID, CONTROL_ACTIONS, CONTEXT, CRITICAL_COMBI, UCA, REL_HAZ, REFINED_SAFETY};

	public static final String INPUT = Platform.getInstanceLocation().getURL().getPath()+"input.txt";
	
	public static final String INPUT2 = Platform.getInstanceLocation().getURL().getPath()+"input2.txt";
	
	public static final String OUTPUT = Platform.getInstanceLocation().getURL().getPath()+"output.txt";
	
	public static final String OUTPUT2 = Platform.getInstanceLocation().getURL().getPath()+"output2.txt";
	
	// static fields to hold the images
	private static final Image CHECKED = Activator.getImageDescriptor("icons/checked.gif").createImage();
	  
	private static final Image UNCHECKED = Activator.getImageDescriptor("icons/unchecked.gif").createImage();
	
	private static final Image ADD = Activator.getImageDescriptor("icons/add.png").createImage();
	
	private static final Image EDIT = Activator.getImageDescriptor("icons/edit.png").createImage();
	
	private static final Image DELETE = Activator.getImageDescriptor("icons/delete.png").createImage();
	
	private static final Image SETTINGS = Activator.getImageDescriptor("icons/Settings.png").createImage();
	
	private static final Image HEADER = Activator.getImageDescriptor("icons/tableheader1.jpg").createImage();
	
	private static final Image LTL = Activator.getImageDescriptor("icons/ltl.png").createImage();
	
	private static final Image CHECK_CONFLICTS = Activator.getImageDescriptor("icons/check_conflicts.png").createImage();
	
	static final Image LOGO = Activator.getImageDescriptor("icons/logo.png").createImage();
	
	private static final Image GENERATE = Activator.getImageDescriptor("icons/generate.png").createImage();
	
	private static final Device device = Display.getCurrent ();
	
	private static final Color BACKGROUND = new Color(device,204,204,255);
	
	private static final Color CONFLICT = new Color(device,255,0,0);
	
	private static final Color HIGHLIGHT = new Color(device,0,0,0);
	
	private static final Color NORMAL = new Color(device,224,224,224);
	
	TableViewer mainViewer, controlActionViewer, dependencyTableViewer, dependencyTopTableViewer, dependencyBottomTableViewer,
						contextViewer;

	TableViewer contextRightViewer;

	private TableViewer ltlViewer;
	
	public TableViewer refinedSafetyViewer;
	
	private MainViewLabelProvider mVLP = new MainViewLabelProvider();
	
	Table table, controlActionTable, dependencyTable, dependencyTopTable, dependencyBottomTable, 
					contextRightTable, contextTable, ltlTable;
	
	public Table refinedSafetyTable;
	
	private Composite compositeDependencies, compositeTable, compositeControlAction, contextComposite, contextCompositeRight, 
	compositeDependenciesTopRight, compositeDependenciesBottomRight, refinedSafetyComposite, ltlComposite;
	
	private IPartListener pL;
	
	private CellEditor[] contextEditor;
	
	public static ExportContent exportContent;
	
	
	List dependenciesBottom = new ArrayList();
	
	List<ControlActionEntrys> dependencies = new ArrayList<ControlActionEntrys>();
	
	List<ControlActionEntrys> dependenciesNotProvided = new ArrayList<ControlActionEntrys>();
	
	
	
	List controlActionList = new ArrayList();
	
	List<ControllerWithPMEntry> pmList = new ArrayList<ControllerWithPMEntry>();
	
	public List<ProcessModelVariables> contextRightContent = new ArrayList<ProcessModelVariables>();
	List<ProcessModelVariables> contextRightHazardousContent = new ArrayList<ProcessModelVariables>();
	List<ProcessModelVariables> contextRightNotHazardousContent = new ArrayList<ProcessModelVariables>();
	List<ProcessModelVariables> contextRightContentProvided = new ArrayList<ProcessModelVariables>();
	List<ProcessModelVariables> contextRightContentNotProvided = new ArrayList<ProcessModelVariables>();
	
	/**
	 * This List is responsible for the Upper right section in the dependency view
	 */
	private List<ProcessModelVariables> pmvList = new ArrayList<ProcessModelVariables>();
	
	private List<ProcessModelVariables> ltlContent = new ArrayList<ProcessModelVariables>();
	
	public List<ProcessModelVariables> refinedSafetyContent = new ArrayList<ProcessModelVariables>();
	
	static List<xstpa.Hazard> allHazards = new ArrayList<xstpa.Hazard>();
	
	static List<IUnsafeControlAction> unsafeCA;
	
	private ControlActionEntrys linkedCAE;
	
	private ProcessModelVariables linkedPMV;
	
	
	Observer ob = this;
	
	public static DataModelController model;
	
	private editWindow settingsWindow;
	
	private editRelatedHazardsWizard editHazards;
	
	public View view = this;
	
	private int contextTableCellX, refinedSafetyTableCellX, refinedSafetyTableCellY;
	
	private int contextTableCellY;
	
	private int tableIndex = 0;
	
	private int tempTableIndex = -1;
	
	private int conflictCounter = 0;
	
	private Boolean controlActionProvided = true;
	
	private Boolean showAll = true;
	
	private Boolean showHazardous = false;
	
	private Boolean showNotHazardous = false;

	private Boolean dependencyProvided = true;
	
	public static String[] contextProps;
	
	
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
		    return ((List) inputElement).toArray();
		  }
	}

	class MainViewLabelProvider extends LabelProvider implements
			ITableLabelProvider, IColorProvider {
		
		int counter = 0;
		public String getColumnText(Object element, int columnIndex) {
		
		if (CWPMCLASS == element.getClass().getName()) {
		
		ControllerWithPMEntry entry = (ControllerWithPMEntry) element;
	    switch (columnIndex) {
	    case 0:
	      return entry.getController();
	    case 1:
	      return entry.getPM();
	    case 2:
	    	return entry.getPMV();
	    case 3:
	    	return entry.getValues();
	    
	    case 4:
	    	return entry.getComments();
	    }
	    return null;
		}
		else if (CAECLASS == element.getClass().getName()) {
			ControlActionEntrys entry = (ControlActionEntrys) element;
			switch (columnIndex) {
			case 0:
				return entry.getControlAction();
			case 1:
				return null;
			case 2:
				return entry.getComments();
			
			}
			return null;
		}
		return null;
		}

		public Image getColumnImage(Object obj, int index) {			
			return getImage(obj);		
		}
		


		public Image getImage(Object obj) {
	        return null;
		}

		@Override
		public org.eclipse.swt.graphics.Color getForeground(Object element) {
			
			return null;
		}


		@Override
		public org.eclipse.swt.graphics.Color getBackground(Object element) {
			if (CWPMCLASS == element.getClass().getName()) {
				ArrayList list = (ArrayList) mainViewer.getInput();
				int index = list.indexOf(element);
				if ((index % 2) == 0) {
					return BACKGROUND;
				} else {	    
					return null;
				}
			}
			else {
				ArrayList list = (ArrayList) controlActionViewer.getInput();
				

				try {
					int index =  list.indexOf(element);
					if ((index % 2) == 0) {
						return BACKGROUND;
					} else {	    
						return null;
					}
				}
				catch (Exception e) {
					return null;
				}
				
			}		
			
		}
		
		
		
		
		

		
	}
	

	class DependencyViewLabelProvider extends LabelProvider implements
			ITableLabelProvider, IColorProvider {
		
		public String getColumnText(Object element, int columnIndex) {
		
		if (CWPMCLASS == element.getClass().getName()) {
		
		ControllerWithPMEntry entry = (ControllerWithPMEntry) element;
	    switch (columnIndex) {
	    case 0:
	      return entry.getController();
	    case 1:
	      return entry.getPMV();
	    }
	    return null;
		}
		else if (CAECLASS == element.getClass().getName()) {
			ControlActionEntrys entry = (ControlActionEntrys) element;
			switch (columnIndex) {
			case 0:
				return String.valueOf(entry.getNumber());
			case 1:
					
				return entry.getControlAction();
					
			}
			return null;
		}
		
		else if (PMVCLASS == element.getClass().getName()) {
			ProcessModelVariables entry = (ProcessModelVariables) element;
			switch (columnIndex) {
			case 0:
				return String.valueOf(entry.getNumber());
			case 1:
				return entry.getName();
			}
		}
		else {
			ProcessModelVariables entry = (ProcessModelVariables) element;
			switch (columnIndex) {
			case 0:
				return null;
			case 1:
				return entry.getName();
			}
		}
		return null;
		}

		public Image getColumnImage(Object obj, int index) {			
			return getImage(obj);		
		}
		


		public Image getImage(Object obj) {
	        return null;
		}
		
		@Override
		public org.eclipse.swt.graphics.Color getForeground(Object element) {
			
			return null;
		}


		@Override
		public org.eclipse.swt.graphics.Color getBackground(Object element) {

			 if (CAECLASS == element.getClass().getName()){
				ArrayList list = (ArrayList) dependencyTableViewer.getInput();
				int index = list.indexOf(element);
				if ((index % 2) == 0) {
					return BACKGROUND;
				} else {	    
					return null;
				}
			}
			 else {
				 return null;
			 }
			
			
		}
		
		

		
	}
	
	class ContextViewLabelProvider extends LabelProvider implements
	ITableLabelProvider, IColorProvider {
		
		@Override
		public Image getColumnImage(Object element, int columnIndex) {

			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			
			ProcessModelVariables entry = (ProcessModelVariables) element;
				if (columnIndex == 0) {
					return String.valueOf(contextRightContent.indexOf(entry)+1);
				}
			
				if ((columnIndex != contextRightTable.getColumnCount()-1)) {
					if (columnIndex-1 < entry.getValues().size()) {
						return entry.getValues().get(columnIndex-1);		
					}
						
				}
				
			return null;
		}
		
		public Image getImage() {
			return null;
			
		}

		@Override
		public org.eclipse.swt.graphics.Color getForeground(Object element) {
			
			return null;
		}

		@Override
		public org.eclipse.swt.graphics.Color getBackground(Object element) {
			ProcessModelVariables entry = (ProcessModelVariables) element;
			if (entry.getConflict()) {
				return CONFLICT;
			}
			else {
				ArrayList list = (ArrayList) contextRightViewer.getInput();
				int index = list.indexOf(element);
				if ((index % 2) == 0) {
					return BACKGROUND;
				} else {	    
					return null;
				}
			}
			
				
		}
	}	

	
	class RefinedSafetyViewLabelProvider extends LabelProvider implements
	ITableLabelProvider, IColorProvider {
		
		@Override
		public Image getColumnImage(Object element, int columnIndex) {

			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			
			ProcessModelVariables entry = (ProcessModelVariables) element;
			
			switch (columnIndex) {
			case 0:
				return String.valueOf(refinedSafetyContent.indexOf(entry));
			case 1:
				return entry.getLinkedControlActionName();
			case 2:
				if (entry.getHAnytime()) {
					entry.setContext("Provided");
					return entry.getContext();
				}
				else { 
					entry.setContext("Not Provided");
					return entry.getContext();
				}
				
			case 3:	
				String temp ="";
				String nameOfControlAction = null;
				if (entry.getPmVariables().isEmpty()) {
					List<String> vars = new ArrayList<String>();
					for (int i = 0;i<contextRightTable.getColumnCount();i++){
						vars.add(contextRightTable.getColumn(i).getText());
					}
					entry.setPmVariables(vars);
				}
				if (entry.getHAnytime()) {
					for (int i=0; i<entry.getValues().size(); i++){
				
						temp = temp.concat(entry.getPmVariables().get(i));
						temp = temp.concat("=");
						temp = temp.concat(entry.getValues().get(i));

						if (!(i==entry.getSizeOfValues()-1)) {
							temp = temp.concat(",");
						}
						else {
							nameOfControlAction = entry.getLinkedControlActionName();
						}
					}

				}
				else {
					for (int i=0; i<entry.getValues().size(); i++){

						temp = temp.concat(entry.getPmVariables().get(i));
						temp = temp.concat("=");
						temp = temp.concat(entry.getValues().get(i));
						
						if (!(i==entry.getSizeOfValues()-1)) {
							temp = temp.concat(",");
						}
						else {
							nameOfControlAction = entry.getLinkedControlActionName();
						}
					}

				}
				return temp;
				
			case 4:
				//String temp2 ="";
				//for (xstpa.Hazard rules : entry.getRelatedHazards().getLinkedItems()) {
					//temp2 = temp2.concat("H - "+rules.getNumber()+", ");
				//}
				//return temp2;
				// TODO UCAS
//				String tempDescription = "";
//				List<IUnsafeControlAction> unsafeCA;
//				for (int j = 0; j<model.getAllControlActions().size();j++) {
//					unsafeCA = model.getAllControlActions().get(j).getUnsafeControlActions();
//					for (int i=0; i<unsafeCA.size();i++){
//						
//						if (entry.getLinkedControlActionName().equals(model.getAllControlActions().get(j).getTitle())) {
//							if (!(i == unsafeCA.size()-1)) {
//								tempDescription = tempDescription.concat(unsafeCA.get(i).getDescription()+", ");
//							}
//							else {
//								tempDescription = tempDescription.concat(unsafeCA.get(i).getDescription());
//								return tempDescription;
//							}
//							
//								
//						}
//						
//						
//					}
//				}
				return "Click to see UCA's";

			
			case 5:
				String tempHazards = "";
				List<IUnsafeControlAction> unsafeCA;
				for (int j = 0; j<model.getAllControlActions().size();j++) {
					unsafeCA = model.getAllControlActions().get(j).getUnsafeControlActions();
					for (int i=0; i<unsafeCA.size();i++){
						
						if (entry.getLinkedControlActionName().equals(model.getAllControlActions().get(j).getTitle())) {
							List <ITableModel> linkedHazards = model.getLinkedHazardsOfUCA(unsafeCA.get(i).getId());
							for (int n=0; n<linkedHazards.size();n++) {
								if (!(n == linkedHazards.size()-1)) {
									tempHazards = tempHazards.concat("H-"+linkedHazards.get(i).getNumber()+", ");
								}
								else {
									tempHazards = tempHazards.concat("H-"+linkedHazards.get(i).getNumber());
									return tempHazards;
								}

							}
								
						}
					}
				}
				
				
			case 6:
				if (entry.getRefinedSafetyRequirements() != null) {
					return entry.getRefinedSafetyRequirements();
				}
				else return "Click to Edit";
			
			}

				
			return null;
		}

		
		public Image getImage() {
			return null;
			
		}

		@Override
		public org.eclipse.swt.graphics.Color getForeground(Object element) {
			
			return null;
		}

		@Override
		public org.eclipse.swt.graphics.Color getBackground(Object element) {
			ProcessModelVariables entry = (ProcessModelVariables) element;
			if (entry.getConflict()) {
				return CONFLICT;
			}
			else {
				ArrayList list = (ArrayList) refinedSafetyViewer.getInput();
				int index = list.indexOf(element);
				if ((index % 2) == 0) {
					return BACKGROUND;
				} else {	    
					return null;
				}
			}
			
				
		}
	}	
	
	
	class LtlViewLabelProvider extends LabelProvider implements
	ITableLabelProvider, IColorProvider {
		
		@Override
		public Image getColumnImage(Object element, int columnIndex) {

			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			
			ProcessModelVariables entry = (ProcessModelVariables) element;
	
			switch (columnIndex) {
			case 0:
				return String.valueOf(ltlContent.indexOf(entry));
			case 1:
				String temp = "G (";
				String nameOfControlAction = null;
				if (entry.getHAnytime()) {
					for (int i=0; i<entry.getValues().size(); i++){
						temp = temp.concat("(");					
						temp = temp.concat(entry.getPmVariables().get(i));
						temp = temp.concat("==");
						temp = temp.concat(entry.getValues().get(i));
						temp = temp.concat(")");
						if (!(i==entry.getSizeOfValues()-1)) {
							temp = temp.concat(" && ");
						}
						else {
							nameOfControlAction = entry.getLinkedControlActionName();
						}
					}
					temp = temp.concat(" ->! ");
					temp = temp.concat("(controlaction == " +nameOfControlAction+ "))");
				}
				else {
					for (int i=0; i<entry.getValues().size(); i++){
						temp = temp.concat("(");
						temp = temp.concat(entry.getPmVariables().get(i));
						temp = temp.concat("==");
						temp = temp.concat(entry.getValues().get(i));
						temp = temp.concat(")");
						if (!(i==entry.getSizeOfValues()-1)) {
							temp = temp.concat(" && ");
						}
						else {
							nameOfControlAction = entry.getLinkedControlActionName();
						}
					}
					temp = temp.concat(" -> ");
					temp = temp.concat("(controlaction == " +nameOfControlAction+ "))");
				}
				return temp;
			}

	
				
			return null;
		}
		
		public Image getImage() {
			return null;
			
		}

		@Override
		public org.eclipse.swt.graphics.Color getForeground(Object element) {
			
			return null;
		}

		@Override
		public org.eclipse.swt.graphics.Color getBackground(Object element) {
			ProcessModelVariables entry = (ProcessModelVariables) element;
			if (entry.getConflict()) {
				return CONFLICT;
			}
			else {
				ArrayList list = (ArrayList) ltlViewer.getInput();
				int index = list.indexOf(element);
				if ((index % 2) == 0) {
					return BACKGROUND;
				} else {	    
					return null;
				}
			}
			
				
		}
	}
	
	


	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
	    Composite outercomposite = new Composite(parent, SWT.NONE);
	    
	    FormLayout formLayout = new FormLayout();
	    formLayout.marginHeight = 5;
	    formLayout.marginWidth = 5;
	    formLayout.spacing = 5;
	    outercomposite.setLayout( formLayout );
	    
	    Composite innerLeft = new Composite( outercomposite, SWT.BORDER );
	    innerLeft.setLayout( new GridLayout(1, false) );
	    
	    //composite.setLayout(new RowLayout());
	    
	    FormData fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( 0 );
	    
	    fData.bottom = new FormAttachment( 100 );
	    innerLeft.setLayoutData( fData );
	    


	    
	    
	    compositeTable = new Composite(outercomposite, SWT.BORDER);
	    compositeTable.setLayout(new GridLayout(1, false));
	    
	    
	    // set the formdata for the right (table) part
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( innerLeft );
	    fData.right = new FormAttachment( 100 );
	    fData.bottom = new FormAttachment( 100 );
	    compositeTable.setLayoutData( fData );
	    
	    compositeControlAction = new Composite(outercomposite, SWT.BORDER);
	    compositeControlAction.setLayout(new GridLayout(1, false));
	    compositeControlAction.setVisible(false);
	    
	    // set the formdata for the right (controlActiontable) part
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( innerLeft );
	    fData.right = new FormAttachment( 100 );
	    fData.bottom = new FormAttachment( 100 );
	    compositeControlAction.setLayoutData( fData );
	    
	    // this is the composite for the dependencies section
	    compositeDependencies = new Composite(outercomposite, SWT.BORDER);
	    compositeDependencies.setLayout(new GridLayout(2, false));
	    compositeDependencies.setVisible(false);
	    
	    // set the formdata for the dependencies
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( innerLeft );
	    fData.right = new FormAttachment( 35 );
	    fData.bottom = new FormAttachment( 100 );
	    compositeDependencies.setLayoutData( fData );
	    
	    // the top right part
	    compositeDependenciesTopRight = new Composite(outercomposite, SWT.BORDER);
	    compositeDependenciesTopRight.setLayout( new GridLayout(1, false));
	    
	    // set the formdata for the dependencies (top right)
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( compositeDependencies );
	    fData.right = new FormAttachment( 58 );
	    fData.bottom = new FormAttachment( 55 );
	    compositeDependenciesTopRight.setLayoutData( fData );
	    	    
	    
	    // the bottom right part
	    compositeDependenciesBottomRight = new Composite(outercomposite, SWT.BORDER);
	    compositeDependenciesBottomRight.setLayout(new GridLayout(1, false));
	    
	    // set the formdata for the dependencies (bottom right)
	    fData = new FormData();
	    fData.top = new FormAttachment( compositeDependenciesTopRight );
	    fData.left = new FormAttachment( compositeDependencies );
	    fData.right = new FormAttachment( 58 );
	    fData.bottom = new FormAttachment( 100 );
	    
	    compositeDependenciesBottomRight.setLayoutData( fData );
	    
	    contextComposite = new Composite(outercomposite, SWT.BORDER);
	    contextComposite.setLayout( new GridLayout(1, false));
	    contextComposite.setVisible(false);
	    
	    // set the formdata for context table part (middle)
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( innerLeft );
	    fData.right = new FormAttachment( 18 );
	    fData.bottom = new FormAttachment( 100 );
	    contextComposite.setLayoutData( fData );
	    
	    
	    contextCompositeRight = new Composite(outercomposite, SWT.BORDER);
	    contextCompositeRight.setLayout( new GridLayout(1, false));	    
	    contextCompositeRight.setVisible(false);
	    
	    // set the formdata for context table part (right)
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( contextComposite );
	    fData.right = new FormAttachment (100);
	    fData.bottom = new FormAttachment( 100 );
	    contextCompositeRight.setLayoutData( fData ); 
	    
	    // refinedSafety Composite
	    refinedSafetyComposite = new Composite(outercomposite, SWT.BORDER);
	    refinedSafetyComposite.setLayout( new GridLayout(2, false));	    
	    refinedSafetyComposite.setVisible(false);
	    
	    // set the formdata for context table part (right)
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( innerLeft );
	    fData.right = new FormAttachment (100);
	    fData.bottom = new FormAttachment( 100 );
	    refinedSafetyComposite.setLayoutData( fData ); 
	    
	    
	    // ltl Composite
	    ltlComposite = new Composite(outercomposite, SWT.BORDER);
	    ltlComposite.setLayout( new GridLayout(1, false));	    
	    ltlComposite.setVisible(false);
	    
	    // set the formdata for context table part (right)
	    fData = new FormData();
	    fData.top = new FormAttachment( 0 );
	    fData.left = new FormAttachment( innerLeft );
	    fData.right = new FormAttachment (100);
	    fData.bottom = new FormAttachment( 100 );
	    ltlComposite.setLayoutData( fData ); 
	    
	    // the tabfolder for the contextRight Composite
	    final TabFolder contextRightFolder = new TabFolder(contextCompositeRight, SWT.NONE);
	    TabItem contextRightTab1 = new TabItem(contextRightFolder, SWT.NONE);
	    contextRightTab1.setText("Control Action Provided");
	    TabItem contextRightTab2 = new TabItem(contextRightFolder, SWT.NONE);
	    contextRightTab2.setText("Control Action Not Provided");
	    
	    //Composite in which the folder items are located
	    Composite contextCompositeInnerRight = new Composite(contextRightFolder, SWT.NONE);	    
	    contextCompositeInnerRight.setLayout( new GridLayout(2, false));
	    
	    //Set the composite to the right tab
	    contextRightTab1.setControl(contextCompositeInnerRight);
	    contextRightTab2.setControl(contextCompositeInnerRight);
	    
	    // Add a button to switch tables (Process Model Button)
	    final Button PMTable = new Button(innerLeft, SWT.PUSH);
	    PMTable.setText("Process Model");
	    PMTable.setLayoutData(new GridData(100,30));
	    //PMTable.setImage(BUTTON);
	    
		 // Add a button to switch tables (Control Action Button)
	    final Button controlActionTableBtn = new Button(innerLeft, SWT.PUSH);
	    controlActionTableBtn.setText("Control Actions");
	    controlActionTableBtn.setLayoutData(new GridData(100,30));
	    //controlActionTableBtn.setImage(BUTTON_PRESSED);
	    
		 // Add a button to switch tables (Dependencies Button)
	    final Button DependenciesTableBtn = new Button(innerLeft, SWT.PUSH);
	    DependenciesTableBtn.setText("Dependencies");
	    DependenciesTableBtn.setLayoutData(new GridData(100,30));
	    
	    // Add a button to switch tables (Context Table Button)
	    final Button contextTableBtn2 = new Button(innerLeft, SWT.PUSH);
	    contextTableBtn2.setText("Context Table");
	    contextTableBtn2.setLayoutData( new GridData(100,30));
	    
		 // Add a button to switch tables (Context Table Button)
	    //Button contextTableBtn = new Button(innerLeft, SWT.PUSH);
	    //contextTableBtn.setText("Context Table");
	    //contextTableBtn.setLayoutData(new GridData(100,30));
	    //contextTableBtn.setEnabled(false);
	    
		 // Add a button to switch to refinedSafety table
	    final Button refinedSafetyBtn = new Button(innerLeft, SWT.PUSH);
	    refinedSafetyBtn.setText("Refined SR");
	    refinedSafetyBtn.setLayoutData(new GridData(100,30));

	    
		 // Add a button to switch tables (AND / OR Button)
	    final Button andOrBtn = new Button(innerLeft, SWT.PUSH);
	    andOrBtn.setText("AND/OR Table");
	    andOrBtn.setLayoutData(new GridData(100,30));
	    andOrBtn.setEnabled(false);
	    
	    
	    

	    
	    
	    
	    
	    
	    
	    
	    
     
     

	    
	    // Add the TableViewers   
		mainViewer = new TableViewer(compositeTable, SWT.FULL_SELECTION );
		mainViewer.setContentProvider(new MainViewContentProvider());
		mainViewer.setLabelProvider(mVLP);
		
		controlActionViewer = new TableViewer(compositeControlAction, SWT.FULL_SELECTION );
		controlActionViewer.setContentProvider(new MainViewContentProvider());
		controlActionViewer.setLabelProvider(mVLP);
		
		dependencyTableViewer = new TableViewer(compositeDependencies, SWT.FULL_SELECTION );
		dependencyTableViewer.setContentProvider(new MainViewContentProvider());
		dependencyTableViewer.setLabelProvider(new DependencyViewLabelProvider());
		
		// Add a Combobox for Dependencys
		final Combo dependencyCombo = new Combo (compositeDependencies, SWT.READ_ONLY);
		dependencyCombo.add("Control Action Provided");
		dependencyCombo.add("Control Action not Provided");
		dependencyCombo.select(0);
		GridData data = new GridData(SWT.RIGHT, SWT.TOP, false, true); 
		dependencyCombo.setLayoutData(data);
		
		dependencyTopTableViewer = new TableViewer(compositeDependenciesTopRight, SWT.FULL_SELECTION );
		dependencyTopTableViewer.setContentProvider(new MainViewContentProvider());
		dependencyTopTableViewer.setLabelProvider(new DependencyViewLabelProvider());		
		
		dependencyBottomTableViewer = new TableViewer(compositeDependenciesBottomRight, SWT.FULL_SELECTION );
		dependencyBottomTableViewer.setContentProvider(new MainViewContentProvider());
		dependencyBottomTableViewer.setLabelProvider(new DependencyViewLabelProvider());
		
		contextViewer = new TableViewer(contextComposite, SWT.FULL_SELECTION );
		contextViewer.setContentProvider(new MainViewContentProvider());
		contextViewer.setLabelProvider(mVLP);
		
		refinedSafetyViewer = new TableViewer(refinedSafetyComposite, SWT.FULL_SELECTION );
		refinedSafetyViewer.setContentProvider(new MainViewContentProvider());
		refinedSafetyViewer.setLabelProvider(new RefinedSafetyViewLabelProvider());
		
		// Add a Composite which contains tools to edit refinedSafetyTable
	    Composite editRefinedSafetyTableComposite = new Composite( refinedSafetyComposite, SWT.NONE);
	    editRefinedSafetyTableComposite.setLayout( new GridLayout(1, false) );
	    data = new GridData(SWT.RIGHT, SWT.TOP, false, true); 
	    data.verticalIndent = 5;
	    editRefinedSafetyTableComposite.setLayoutData(data);
	    
		// Add a button to switch tables (LTL Button)
	    final Button ltlBtn = new Button(editRefinedSafetyTableComposite, SWT.PUSH);
	    ltlBtn.setToolTipText("Opens the LTL Table for all Hazardous Combinations");
	    ltlBtn.setImage(LTL);
	    ltlBtn.pack();
		
	    
	    // Add a button to edit table entrys to contextRightTable
	    //Button deleteRelatedHazardEntry = new Button(editRefinedSafetyTableComposite, SWT.PUSH);
	    //deleteRelatedHazardEntry.setImage(DELETE);
	    //deleteRelatedHazardEntry.setToolTipText("Deletes a linked Hazard of selected Column");
	    //deleteRelatedHazardEntry.pack();
	    
	    
		ltlViewer = new TableViewer(ltlComposite, SWT.FULL_SELECTION );
		ltlViewer.setContentProvider(new MainViewContentProvider());
		ltlViewer.setLabelProvider(new LtlViewLabelProvider());
		
		// Add A Label for the Filter
		Label filterLabel = new Label(contextCompositeInnerRight, SWT.NULL);
	    filterLabel.setText("");
		
		// Add a Combobox for the Filter
	    final Combo filterCombo = new Combo(contextCompositeInnerRight, SWT.READ_ONLY);
	    filterCombo.add("Show All");
	    filterCombo.add("Show Hazardous");
	    filterCombo.add("Show Not Hazardous");
	    filterCombo.select(0);
	    data = new GridData(150, 80);
	    filterCombo.setLayoutData(data);
	    
		contextRightViewer = new TableViewer(contextCompositeInnerRight, SWT.FULL_SELECTION );
		contextRightViewer.setContentProvider(new MainViewContentProvider());
		contextRightViewer.setLabelProvider(new ContextViewLabelProvider());
		
		// Add a Composite to edit all Tables
	    Composite editTableComposite = new Composite( contextCompositeInnerRight, SWT.NONE);
	    editTableComposite.setLayout( new GridLayout(1, false) );
	    data = new GridData(SWT.RIGHT, SWT.TOP, false, true); 
	    data.verticalIndent = 5;
	    editTableComposite.setLayoutData(data);
	    
	    // Add a Label which displays if there are any Error Messages
	    final Label errorLabel = new Label(contextCompositeInnerRight, SWT.NULL);
	    errorLabel.setText("There are 0 Conflicts!  ");
	    
	    // Add a button to add table entrys to contextRightTable
	    Button generateEntry = new Button(editTableComposite, SWT.PUSH);
	    generateEntry.setToolTipText("Generates a new Table with default Settings");
	    generateEntry.setImage(GENERATE);
	    generateEntry.pack();
	    
	    // Add a button to add table entrys to contextRightTable
	    Button addEntry = new Button(editTableComposite, SWT.PUSH);
	    addEntry.setImage(ADD);
	    addEntry.setToolTipText("Adds a new Entry");
	    addEntry.pack();
	    
	    // Add a button to edit table entrys to contextRightTable
	    //Button editEntry = new Button(editTableComposite, SWT.PUSH);
	    //editEntry.setImage(EDIT);
	    //editEntry.pack();
	    
	    // Add a button to delete table entrys to contextRightTable
	    Button deleteEntry = new Button(editTableComposite, SWT.PUSH);
	    deleteEntry.setToolTipText("Deletes the selected Row");
	    deleteEntry.setImage(DELETE);
	    deleteEntry.pack();
	    
	    // Add a button to change to the settings for ACTS
	    //Button settingsBtn = new Button(contextCompositeInnerRight, SWT.PUSH);
	    Button settingsBtn = new Button(editTableComposite, SWT.PUSH);
	    settingsBtn.setToolTipText("Opens the Settings for the Combinatorial Algorithm");
	    settingsBtn.setImage(SETTINGS);
	    //settingsBtn.setLayoutData(new GridData(100,30));
	    settingsBtn.pack();
	    
	    Button checkConflictsBtn = new Button(editTableComposite, SWT.PUSH);
	    checkConflictsBtn.setToolTipText("Checks the Table for any (logical) Conflicts");
	    checkConflictsBtn.setImage(CHECK_CONFLICTS);
	    checkConflictsBtn.pack();
	    

	    
	    
		// the top right part (buttons)
	    Composite compositeDependenciesTopRightBtns = new Composite(compositeDependenciesTopRight, SWT.CENTER);
	    compositeDependenciesTopRightBtns.setLayout(new GridLayout(5, false));

	    GridData gridData = new GridData(SWT.FILL, SWT.END, true, false);
	    gridData.heightHint = 25;
	    compositeDependenciesTopRightBtns.setLayoutData(gridData);
	    
	    gridData = new GridData(SWT.FILL, SWT.END, true, true);
	    gridData.heightHint = 15;
	    
	    // Add a button to Add one Item in Dependencies View
	    Button addBtn = new Button(compositeDependenciesTopRightBtns, SWT.PUSH);
	    addBtn.setText("Add");
	    addBtn.setLayoutData( gridData );
	    
	    // Add a button to Add all Items
	    Button addAllBtn = new Button(compositeDependenciesTopRightBtns, SWT.PUSH);
	    addAllBtn.setText("Add All");
	    addAllBtn.setLayoutData( gridData );
	    
	    // Add a button to Remove one Item
	    Button removeBtn = new Button(compositeDependenciesTopRightBtns, SWT.PUSH);
	    removeBtn.setText("Remove");
	    removeBtn.setLayoutData(gridData);
	    
	    // Add a button to Remove All Items
	    Button removeAllBtn = new Button(compositeDependenciesTopRightBtns, SWT.PUSH);
	    removeAllBtn.setText("Remove All");
	    removeAllBtn.setLayoutData( gridData);
	    
	    
	    
	    // add a label for the combo box
	    //Label comboLabel = new Label(contextComposite, SWT.NULL);
	    //comboLabel.setText("Control Actions:");
	    //GridData data = new GridData(SWT.RIGHT, SWT.TOP, false, true); 
	    //data.verticalIndent = 5;
	    //comboLabel.setLayoutData(data);
	    
	    // add a combo box to select if the control Action is provided or not
	    //Combo combo = new Combo(contextComposite, SWT.READ_ONLY);
	    //combo.add("Provided");
	    //combo.add("Not Provided");
		//combo.select(0);
	    //data = new GridData(SWT.FILL, SWT.TOP, false, true);   
	    //combo.setLayoutData(data);
	    
		table = mainViewer.getTable();
		controlActionTable = controlActionViewer.getTable();
		dependencyTable = dependencyTableViewer.getTable();
		dependencyTopTable = dependencyTopTableViewer.getTable();
		dependencyBottomTable = dependencyBottomTableViewer.getTable();
		contextTable = contextViewer.getTable();		
		contextRightTable = contextRightViewer.getTable();
		refinedSafetyTable = refinedSafetyViewer.getTable();
	    ltlTable = ltlViewer.getTable();
	    
	    table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    controlActionTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    dependencyTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    dependencyTopTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    dependencyBottomTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    contextTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    refinedSafetyTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    ltlTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    
	    data = new GridData(SWT.FILL, SWT.FILL, true, true);
	    data.heightHint = 300;
	    data.widthHint = 800;
	    contextRightTable.setLayoutData(data);
	    
	    // create menu for contextRightTable
	    final Menu contextRightMenu = new Menu(contextRightTable);
	    contextRightTable.setMenu(contextRightMenu);
	    MenuItem newItem = new MenuItem(contextRightMenu, SWT.NONE);
        newItem.setText("Doesn't Matter ");
        
	    // add Columns for the mainTable
	    new TableColumn(table, SWT.CENTER).setText(CONTROLLER);
	    new TableColumn(table, SWT.CENTER).setText(PM);
	    new TableColumn(table, SWT.CENTER).setText(PMV);
	    new TableColumn(table, SWT.CENTER).setText(PMVV);
	    new TableColumn(table, SWT.CENTER).setText(COMMENTS);
	    
	    // add Columns for Control Actions table (list of control actions)
	    
	    new TableColumn(controlActionTable, SWT.CENTER).setText(CONTROL_ACTIONS);
	    new TableColumn(controlActionTable, SWT.CENTER).setText(SAFETY_CRITICAL);
	    new TableColumn(controlActionTable, SWT.CENTER).setText(COMMENTS);

	    // Columns for Dependencies table
	    new TableColumn(dependencyTable, SWT.CENTER).setText(ENTRY_ID);
	    new TableColumn(dependencyTable, SWT.CENTER).setText(CONTROL_ACTIONS);
	    
	    // Columns for dependencies top (Process model Variables)
	    new TableColumn(dependencyTopTable, SWT.CENTER).setText(ENTRY_ID);
	    new TableColumn(dependencyTopTable, SWT.CENTER).setText(PMV);
	    
	    // Columns for dependencies bottom (Dependencies)
	    new TableColumn(dependencyBottomTable, SWT.CENTER).setText(ENTRY_ID);
	    new TableColumn(dependencyBottomTable, SWT.CENTER).setText(PMV);
	    
	    // add columns for context tables
	    new TableColumn(contextTable, SWT.CENTER).setText(LIST_of_CA);
	    
	    // add columns for ltl tables	   
	    new TableColumn(ltlTable, SWT.CENTER).setText(ENTRY_ID);
	    new TableColumn(ltlTable, SWT.CENTER).setText(LTL_RULES);
	    
	    // add columns for ltl tables	   
	    new TableColumn(refinedSafetyTable, SWT.CENTER).setText(ENTRY_ID);
	    new TableColumn(refinedSafetyTable, SWT.CENTER).setText(CONTROL_ACTIONS);
	    new TableColumn(refinedSafetyTable, SWT.CENTER).setText(CONTEXT);
	    new TableColumn(refinedSafetyTable, SWT.CENTER).setText(CRITICAL_COMBI);
	    new TableColumn(refinedSafetyTable, SWT.CENTER).setText(UCA);
	    new TableColumn(refinedSafetyTable, SWT.CENTER).setText(REL_HAZ);
	    new TableColumn(refinedSafetyTable, SWT.CENTER).setText(REFINED_SAFETY);

	    
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    controlActionTable.setHeaderVisible(true);
	    controlActionTable.setLinesVisible(true);
	    dependencyTable.setHeaderVisible(true);
	    dependencyTable.setLinesVisible(true);
	    dependencyTopTable.setHeaderVisible(true);
	    dependencyTopTable.setLinesVisible(true);
	    dependencyBottomTable.setHeaderVisible(true);
	    dependencyBottomTable.setLinesVisible(true);
	    contextTable.setHeaderVisible(true);
	    contextTable.setLinesVisible(true);
	    contextRightTable.setHeaderVisible(true);
	    contextRightTable.setLinesVisible(true);
	    refinedSafetyTable.setHeaderVisible(true);
	    refinedSafetyTable.setLinesVisible(true);
	    ltlTable.setLinesVisible(true);
	    ltlTable.setHeaderVisible(true);
	    
	    /**
	     * Listener for the generate Entry Button for Refined Safety
	     */
	    generateEntry.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
		    	  //tempTableIndex = -1;
		    	  if (getLinkedCAE() == null) {
		    		  JOptionPane.showMessageDialog(null, "Please Select a Control Action to generate the Table!");
		    	  }
		    	  else {
			    	  // creates the correct number of rows for the context table
			    	  createTablerows(0);
			    	  // writes the Data into the input file for ACTS		    	  
			    	  writeFile();
			    	  // opens ACTS with the given Parameters (the linkedControl)
			    	  open();
		    	  }
	    	}
	    });
	    
	    
	    
	    /**
	     * Listener for the add Entry Button for Refined Safety
	     */
	    addEntry.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		// create an Array which contains the Process Model Variables
	    		//String[] vars = new String[contextRightTable.getColumnCount()-1];
	    		//for (int i = 0; i<contextRightTable.getColumnCount()-1;i++) {
	    			//vars[i] = contextRightTable.getColumn(i).getText();
	    		//}
	    		//AddProcessVarWizard window = new AddProcessVarWizard(vars, view);
	    		//window.open();			
	    	
	    		//Create values for the Constructor
	    		List<String> pmVars = new ArrayList<String>();
	    		for (int i = 1; i<contextRightTable.getColumnCount()-1;i++) {
	    			pmVars.add(contextRightTable.getColumn(i).getText());
	    		}
	    		// add the New Variable
				contextRightContent.add(new ProcessModelVariables(pmVars, getLinkedCAE().getControlAction()));
				// set the ContextTableCombinations
				getLinkedCAE().setContextTableCombinations(contextRightContent);
				// refresh the Viewer
				contextRightViewer.refresh();
	    	}
	    });
	    
	    
	    /**
	     * Listener for the Delete Entry Button
	     */
	    deleteEntry.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		contextRightTable.remove(contextRightTable.getSelectionIndex());
	    	}
	    });
	    
	    /**
	     * Functionality for the Button to change to Process Model
	     */
	    PMTable.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	    	  
	    	  PMTable.setBackground(HIGHLIGHT);
	    	  controlActionTableBtn.setBackground(NORMAL);
	    	  DependenciesTableBtn.setBackground(NORMAL);
	    	  contextTableBtn2.setBackground(NORMAL);
	    	  refinedSafetyBtn.setBackground(NORMAL);
	    	  andOrBtn.setBackground(NORMAL);
	    	  
	    	  // set the old composite invisible
	    	  compositeDependencies.setVisible(false);
	    	  compositeDependenciesTopRight.setVisible(false);
	    	  compositeDependenciesBottomRight.setVisible(false);
	    	  compositeControlAction.setVisible(false);
	    	  contextComposite.setVisible(false);
	    	  contextCompositeRight.setVisible(false);
	    	  ltlComposite.setVisible(false);  
	    	  
	    	  mainViewer.setInput(pmList);
			  for (int i = 0, n = table.getColumnCount(); i < n; i++) {
				  
				  table.getColumn(i).pack();
			  }
	    	  
	    	  
	  	      // set the new composite visible
	  	      compositeTable.setVisible(true);
	  	                  
	      }
	    });
	    
	    /**
	     *  Functionality for the Button to change to Control Action
	     */
	    controlActionTableBtn.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	    	  
	    	  PMTable.setBackground(NORMAL);
	    	  controlActionTableBtn.setBackground(HIGHLIGHT);
	    	  DependenciesTableBtn.setBackground(NORMAL);
	    	  contextTableBtn2.setBackground(NORMAL);
	    	  refinedSafetyBtn.setBackground(NORMAL);
	    	  andOrBtn.setBackground(NORMAL);
	    	  
	    	  // set the old composite invisible
	    	  compositeDependencies.setVisible(false);
	    	  compositeDependenciesTopRight.setVisible(false);
	    	  compositeDependenciesBottomRight.setVisible(false);
	    	  compositeTable.setVisible(false);
	    	  contextComposite.setVisible(false);
	    	  contextCompositeRight.setVisible(false);
	    	  refinedSafetyComposite.setVisible(false);
	    	  ltlComposite.setVisible(false);  
	    	  
	    	  controlActionViewer.setInput(controlActionList);
	    	  controlActionViewer.getControl().setFocus();
	    	  
	    	  
			  for (int i = 0, n = controlActionTable.getColumnCount(); i < n; i++) {
				  controlActionTable.getColumn(i).pack();
			  }
	    	  
	    	 
	    	  
	  	      
	  	      // set the new composite visible
	    	  compositeControlAction.setVisible(true);
	  	      
	      }
	    });
	    
	    /**
	     *  Functionality for the Button to change to Dependencies
	     */
	    DependenciesTableBtn.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	    	  
	    	  PMTable.setBackground(NORMAL);
	    	  controlActionTableBtn.setBackground(NORMAL);
	    	  DependenciesTableBtn.setBackground(HIGHLIGHT);
	    	  contextTableBtn2.setBackground(NORMAL);
	    	  refinedSafetyBtn.setBackground(NORMAL);
	    	  andOrBtn.setBackground(NORMAL);
	    	  
	    	compositeControlAction.setVisible(false);
	    	compositeTable.setVisible(false);
	    	contextComposite.setVisible(false);
	    	contextCompositeRight.setVisible(false);
	    	refinedSafetyComposite.setVisible(false);
	    	ltlComposite.setVisible(false);
	    	
	    	// create input for dependencyTableViewer
	    	List<ControlActionEntrys> dependencyTableInput= new ArrayList<ControlActionEntrys>();
	    	for (int i = 0; i<dependencies.size();i++) {
	    		if (dependencies.get(i).getSafetyCritical()) {
	    			dependencyTableInput.add(dependencies.get(i));
	    		}
	    	}
	    	
	        dependencyTableViewer.setInput(dependencyTableInput);
	        //dependencyTopTableViewer.setInput(pmvList);	        
	        dependencyBottomTableViewer.setInput(dependenciesBottom);
	        dependencyTable.select(0);
	        
			for (int i = 0, n = dependencyTable.getColumnCount(); i < n; i++) {
				  dependencyTable.getColumn(i).pack();
			}
			  

			for (int i = 0, n = dependencyTopTable.getColumnCount(); i < n; i++) {
				  dependencyTopTable.getColumn(i).pack();
			}
			  

			for (int i = 0, n = dependencyBottomTable.getColumnCount(); i < n; i++) {
				  dependencyBottomTable.getColumn(i).pack();
			}
			  
			compositeDependencies.setVisible(true);
			compositeDependenciesTopRight.setVisible(true);
	    	compositeDependenciesBottomRight.setVisible(true);
	      }
	    });
	    
	    /**
	     * Functionality for the Context Button to change to Context Table
	     */
	    contextTableBtn2.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	    	  
	    	  PMTable.setBackground(NORMAL);
	    	  controlActionTableBtn.setBackground(NORMAL);
	    	  DependenciesTableBtn.setBackground(NORMAL);
	    	  contextTableBtn2.setBackground(HIGHLIGHT);
	    	  refinedSafetyBtn.setBackground(NORMAL);
	    	  andOrBtn.setBackground(NORMAL);
	    	  
	    	  // set the old composite invisible
	    	  compositeDependencies.setVisible(false);
	    	  compositeControlAction.setVisible(false);
	    	  compositeTable.setVisible(false);
	    	  compositeDependenciesTopRight.setVisible(false);
	    	  compositeDependenciesBottomRight.setVisible(false);
	    	  refinedSafetyComposite.setVisible(false);
	    	  ltlComposite.setVisible(false);  
	    	  // if the tabfolder is on "Context Provided"
	    	  if (controlActionProvided) {
	    		  
	 				// create Input for contextTableViewer
	  				List<ControlActionEntrys> contextTableInput= new ArrayList<ControlActionEntrys>();
	  		    	for (int i = 0; i<dependencies.size();i++) {
	  		    		if (dependencies.get(i).getSafetyCritical()) {
	  		    			contextTableInput.add(dependencies.get(i));
	  		    		}
	  		    	}
	  		    	
	  		    	contextViewer.setInput(contextTableInput);
	    		  //contextViewer.setInput(dependencies);
	    	  }
	    	  // if its on the other tab
	    	  else {
  				// create Input for contextTableViewer
  				List<ControlActionEntrys> contextTableInput= new ArrayList<ControlActionEntrys>();
  		    	for (int i = 0; i<dependencies.size();i++) {
  		    		if (dependencies.get(i).getSafetyCritical()) {
  		    			contextTableInput.add(dependenciesNotProvided.get(i));
  		    		}
  		    	}
	    		  
	    		  contextViewer.setInput(contextTableInput);
	    	  }
	    	  
	    	  contextTable.select(0);
	    	  
			  for (int i = 0, n = contextTable.getColumnCount(); i < n; i++) {
				  contextTable.getColumn(i).pack();
			  }
	    	  
	    	  
	    	  for (int i = 0, n = contextRightTable.getColumnCount(); i < n; i++) {
				  contextRightTable.getColumn(i).pack();
			  }
	    	  
	  	      // set the new composite visible
	  	      contextComposite.setVisible(true);
	  	      contextCompositeRight.setVisible(true); 
	  	    
	      }
	    });
	    
	    
	    /**
	     * Functionality for the refinedSafety Button to change to refinedSafetyComposite
	     */
	    refinedSafetyBtn.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	    	  
	    	  PMTable.setBackground(NORMAL);
	    	  controlActionTableBtn.setBackground(NORMAL);
	    	  DependenciesTableBtn.setBackground(NORMAL);
	    	  contextTableBtn2.setBackground(NORMAL);
	    	  refinedSafetyBtn.setBackground(HIGHLIGHT);
	    	  andOrBtn.setBackground(NORMAL);
	    	  
	    	  // set the old composite invisible
	    	  compositeDependencies.setVisible(false);
	    	  compositeControlAction.setVisible(false);
	    	  compositeTable.setVisible(false);
	    	  compositeDependenciesTopRight.setVisible(false);
	    	  compositeDependenciesBottomRight.setVisible(false);
	  	      contextComposite.setVisible(false);
	  	      contextCompositeRight.setVisible(false); 
	  	      ltlComposite.setVisible(false);  
	  	      
	  	      refinedSafetyContent.clear();
	  	      for (int i=0; i<dependencies.size(); i++) {
	  	    	  for (int j=0; j<dependencies.get(i).getContextTableCombinations().size(); j++) {
	  	    		  
	  	   			  if (dependencies.get(i).getContextTableCombinations().get(j).getHAnytime()) {
	  	   				  ProcessModelVariables temp = new ProcessModelVariables();
	  	   				  temp = dependencies.get(i).getContextTableCombinations().get(j);
	  	    			  //temp.setLinkedControlActionName(dependencies.get(i).getControlAction());
	  	    			  refinedSafetyContent.add(temp);
	  	    		  }
	  	    		  
	  	    	  }
	  	      }
	  	      for (int i=0; i<dependenciesNotProvided.size(); i++) {
	  	    	  for (int j=0; j<dependenciesNotProvided.get(i).getContextTableCombinations().size(); j++) {
	  	    		  
	  	    			  if (dependenciesNotProvided.get(i).getContextTableCombinations().get(j).getHazardous()) {
	  	    				  ProcessModelVariables temp = new ProcessModelVariables();
	  	    				  temp = dependenciesNotProvided.get(i).getContextTableCombinations().get(j);
	  	    				  //temp.setLinkedControlActionName(dependenciesNotProvided.get(i).getControlAction());
	  	    				  refinedSafetyContent.add(temp);
	  	    			  }
	  	    		  
	  	    	  }
	  	      }
	  	      exportContent = new ExportContent(dependencies, dependenciesNotProvided);
	  	      
	  	      
	  	      refinedSafetyViewer.setInput(refinedSafetyContent);
	    	  
			  for (int i = 0, n = refinedSafetyTable.getColumnCount(); i < n; i++) {
				  refinedSafetyTable.getColumn(i).pack();
			  }
	    	  
	  	      // set the new composite visible
	  	      refinedSafetyComposite.setVisible(true);  
	  	    ExportJob exportjob = new ExportJob("test",Platform.getInstanceLocation().getURL().getPath()+"test.pdf", "/src/export/fopXstpa.xsl", false);
	  	    exportjob.schedule();
	      }
	    });
	    
	    /**
	     * Functionality for the ltl Button to change to ltlComposite
	     */
	    ltlBtn.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	    	  
	    	  PMTable.setBackground(NORMAL);
	    	  controlActionTableBtn.setBackground(NORMAL);
	    	  DependenciesTableBtn.setBackground(NORMAL);
	    	  contextTableBtn2.setBackground(NORMAL);
	    	  refinedSafetyBtn.setBackground(NORMAL);
	    	  andOrBtn.setBackground(NORMAL);
	    	  
	    	  // set the old composite invisible
	    	  compositeDependencies.setVisible(false);
	    	  compositeControlAction.setVisible(false);
	    	  compositeTable.setVisible(false);
	    	  compositeDependenciesTopRight.setVisible(false);
	    	  compositeDependenciesBottomRight.setVisible(false);
	  	      contextComposite.setVisible(false);
	  	      contextCompositeRight.setVisible(false); 
	  	      refinedSafetyComposite.setVisible(false); 
	  	      
	  	      ltlContent.clear();
	  	      for (int i=0; i<dependencies.size(); i++) {
	  	    	  for (int j=0; j<dependencies.get(i).getContextTableCombinations().size(); j++) {
	  	    		  
	  	   			  if (dependencies.get(i).getContextTableCombinations().get(j).getHAnytime()) {
	  	   				  ProcessModelVariables temp = new ProcessModelVariables();
	  	   				  temp = dependencies.get(i).getContextTableCombinations().get(j);
	  	    			  //temp.setLinkedControlActionName(dependencies.get(i).getControlAction());
	  	    			  ltlContent.add(temp);
	  	    		  }
	  	    		  
	  	    	  }
	  	      }
	  	      for (int i=0; i<dependenciesNotProvided.size(); i++) {
	  	    	  for (int j=0; j<dependenciesNotProvided.get(i).getContextTableCombinations().size(); j++) {
	  	    		  
	  	    			  if (dependenciesNotProvided.get(i).getContextTableCombinations().get(j).getHazardous()) {
	  	    				  ProcessModelVariables temp = new ProcessModelVariables();
	  	    				  temp = dependenciesNotProvided.get(i).getContextTableCombinations().get(j);
	  	    				  //temp.setLinkedControlActionName(dependenciesNotProvided.get(i).getControlAction());
	  	    				  ltlContent.add(temp);
	  	    			  }
	  	    		  
	  	    	  }
	  	      }
	  	      
	  	      ltlViewer.setInput(ltlContent);
	    	  
			  for (int i = 0, n = ltlTable.getColumnCount(); i < n; i++) {
				  ltlTable.getColumn(i).pack();
			  }
	    	  
	  	      // set the new composite visible
	  	      ltlComposite.setVisible(true);  	    
	      }
	    });
	    
	    
	    /**
	    *  Functionality for the AddButton (Dependencies)
	    */
	    addBtn.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		getLinkedCAE().addLinkedItem(linkedPMV);
	    		// sort the List after their "Id"
	    		getLinkedCAE().setLinkedItems(getLinkedCAE().sortItems(getLinkedCAE().getLinkedItems()));
	    		getLinkedCAE().removeAvailableItem(linkedPMV);
	    		
	    		try {
	    			  if (dependencyCombo.getText().equals("Control Action Provided")) {
		    			  // Store it in the DataModel
	    				  for (int i = 0; i<model.getCAProvidedVariables(getLinkedCAE().getId()).size();i++) {
	    					  if (model.getCAProvidedVariables(getLinkedCAE().getId()).get(i)== linkedPMV.getId()) {
	    						  
	    					  }
	    					  else {
	    						  model.addCAProvidedVariable(getLinkedCAE().getId(), linkedPMV.getId());
	    					  }
	    				  }
		    			  
	    			  }
	    		}
	    		catch (Exception e) {
	    			System.out.println("Provided List was empty");
	    			model.addCAProvidedVariable(getLinkedCAE().getId(), linkedPMV.getId());
	    		}
	    		try {
	    			if (dependencyCombo.getText().equals("Control Action not Provided")) {
	    				  
	    				  for (int i = 0; i<model.getCANotProvidedVariables(getLinkedCAE().getId()).size();i++) {
	    					  if (model.getCANotProvidedVariables(getLinkedCAE().getId()).get(i)== linkedPMV.getId()) {
	    						  
	    					  }
	    					  else {
	    						  model.addCANotProvidedVariable(getLinkedCAE().getId(), linkedPMV.getId());
	    					  }
	    				  }

	    			  }
	    		}
	    		catch (Exception e) {
	    			System.out.println("Not Provided List was empty");
	    			model.addCANotProvidedVariable(getLinkedCAE().getId(), linkedPMV.getId());
	    		}
	    		
	    		dependenciesBottom = getLinkedCAE().getLinkedItems();
	    		dependencyBottomTableViewer.setInput(dependenciesBottom);
	    		dependencyTopTableViewer.refresh();
	    	}
	    });
	    
	    
	    /**
		*  Functionality for the AddAll Button (Dependencies)
		*/
		addAllBtn.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(SelectionEvent event) {
		    	List<ProcessModelVariables> temp = new ArrayList<ProcessModelVariables>();
		    	for (ProcessModelVariables entry: pmvList) {
		    	  temp.add(entry);
		    	}
		    	getLinkedCAE().setLinkedItems(temp);
		    	getLinkedCAE().removeAllAvailableItems();
		    	
	    		
	    			  if (dependencyCombo.getText().equals("Control Action Provided")) {
		    			  // Store it in the DataModel
	    				
	    				  for (int j = 0; j<getLinkedCAE().getLinkedItems().size();j++) {
	    					  	try {
	    					  		model.removeCAProvidedVariable(getLinkedCAE().getId(), getLinkedCAE().getLinkedItems().get(j).getId());
	    					  	}
	    					  	catch (Exception e) {
	    					  		
	    					  	}
	    					  	model.addCAProvidedVariable(getLinkedCAE().getId(), getLinkedCAE().getLinkedItems().get(j).getId());	  					  
	    				  }	  		    			  
	    			  }
	    			  else {
	    				  for (int j = 0; j<getLinkedCAE().getLinkedItems().size();j++) {
	    					  	try {
	    					  		model.removeCANotProvidedVariable(getLinkedCAE().getId(), getLinkedCAE().getLinkedItems().get(j).getId());
	    					  	}
	    					  	catch (Exception e) {
	    					  		
	    					  	}
	    					  	model.addCANotProvidedVariable(getLinkedCAE().getId(), getLinkedCAE().getLinkedItems().get(j).getId());	  					  
	    				  }


	    			  }

		    	
		    	
		    	
		    	dependenciesBottom = getLinkedCAE().getLinkedItems();
		    	dependencyBottomTableViewer.setInput(dependenciesBottom);
		    	dependencyTopTableViewer.setInput(getLinkedCAE().getAvailableItems());
		    }
		});	    
		
		
		/**
		*  Functionality for the remove Button (Dependencies)
		*/
		removeBtn.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(SelectionEvent event) {
		    	getLinkedCAE().removeLinkedItem(linkedPMV);
		    	getLinkedCAE().addAvailableItem(linkedPMV);
		    	// sorts the List after their "Id"
		    	getLinkedCAE().setAvailableItems(getLinkedCAE().sortItems(getLinkedCAE().getAvailableItems()));
		    	
	    		try {
	    			  if (dependencyCombo.getText().equals("Control Action Provided")) {
		    			  // Store it in the DataModel
	    				  for (int i = 0; i<model.getCAProvidedVariables(getLinkedCAE().getId()).size();i++) {
	    					  if (model.getCAProvidedVariables(getLinkedCAE().getId()).get(i)== linkedPMV.getId()) {
	    						  model.removeCAProvidedVariable(getLinkedCAE().getId(), linkedPMV.getId());
	    					  }
	    					  else {
	    						  
	    					  }
	    				  }
		    			  
	    			  }
	    		}
	    		catch (Exception e) {
	    			System.out.println("List was already empty");
	    		}
	    		try {
	    			  if (dependencyCombo.getText().equals("Control Action not Provided")) {
	    				  
	    				  for (int i = 0; i<model.getCANotProvidedVariables(getLinkedCAE().getId()).size();i++) {
	    					  if (model.getCANotProvidedVariables(getLinkedCAE().getId()).get(i)== linkedPMV.getId()) {
	    						  model.removeCANotProvidedVariable(getLinkedCAE().getId(), linkedPMV.getId());
	    					  }
	    					  else {
	    						  
	    					  }
	    				  }

	    			  }
	    		}
	    		catch (Exception e) {
	    			System.out.println("List was already empty");
	    		}
		    	
		    	dependenciesBottom = getLinkedCAE().getLinkedItems();
		    	dependencyBottomTableViewer.setInput(dependenciesBottom);
		    	dependencyTopTableViewer.setInput(getLinkedCAE().getAvailableItems());
		    }
		});	
		
		
	    /**
		*  Functionality for the RemoveAll Button (Dependencies)
		*/
		removeAllBtn.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(SelectionEvent event) {
		    	List<ProcessModelVariables> temp = new ArrayList<ProcessModelVariables>();
		    	for (ProcessModelVariables entry: pmvList) {
		    	  temp.add(entry);
		    	}
		    	getLinkedCAE().setAvailableItems(temp);
		    	getLinkedCAE().removeAllLinkedItems();
		    	
		    	if (dependencyCombo.getText().equals("Control Action Provided")) {
	    			  // Store it in the DataModel
  				
  				  for (int j = 0; j<getLinkedCAE().getAvailableItems().size();j++) {
  					  	try {
  					  		model.removeCAProvidedVariable(getLinkedCAE().getId(), getLinkedCAE().getAvailableItems().get(j).getId());
  					  	}
  					  	catch (Exception e) {
  					  		
  					  	}
  					  		  					  
  				  }	  		    			  
  			  }
  			  else {
  				  for (int j = 0; j<getLinkedCAE().getAvailableItems().size();j++) {
  					  	try {
  					  		model.removeCANotProvidedVariable(getLinkedCAE().getId(), getLinkedCAE().getAvailableItems().get(j).getId());
  					  	}
  					  	catch (Exception e) {
  					  		
  					  	}
  					  	  					  
  				  }


  			  }
		    	
		    	dependenciesBottom = getLinkedCAE().getLinkedItems();
		    	dependencyBottomTableViewer.setInput(dependenciesBottom);
		    	dependencyTopTableViewer.setInput(getLinkedCAE().getAvailableItems());
		    }
		});	
	    
	    
	    
	    /**
		 * Listener for the dependency table, checks which item gets selected and displays the linked list
		 */
	    dependencyTable.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  //get the selected Control Action to link it to a Process model Variable
		    	  if (dependencyCombo.getText().equals("Control Action Provided")) {
		    		  setLinkedCAE(dependencies.get(dependencyTable.getSelectionIndex()));
		    	  }
		    	  else {
		    		  setLinkedCAE(dependenciesNotProvided.get(dependencyTable.getSelectionIndex()));  
		    	  }
		    	  
		    	  //setLinkedCAE((ControlActionEntrys) event.item.getData());
//		    	  getLinkedCAE().removeAllAvailableItems();
//		    	  Boolean canBeAdded = true;
//		    	  List<ProcessModelVariables> temp = new ArrayList<ProcessModelVariables>();
//		    	  for (ProcessModelVariables entry: pmvList) {
//		    		  temp.add(entry);
//		    	  }
//		    	  for (ProcessModelVariables entry: temp) {
//		    		  canBeAdded = true;
//		    		  if (!getLinkedCAE().getLinkedItems().isEmpty()) {
//			    		  for (ProcessModelVariables linkedEntry: getLinkedCAE().getLinkedItems()) {
//			    			  if (entry == linkedEntry) {
//			    				  canBeAdded = false;
//					    		  //getLinkedCAE().addLinkedItem(entry);
////					    			  if (dependencyCombo.getText().equals("Control Action Provided")) {
////						    			  // Store it in the DataModel
////					    				  for (int i = 0; i<model.getCAProvidedVariables(getLinkedCAE().getId()).size();i++) {
////					    					  if (model.getCAProvidedVariables(getLinkedCAE().getId()).get(i)== entry.getId()) {
////					    						  
////					    					  }
////					    					  else {
////					    						  model.addCAProvidedVariable(getLinkedCAE().getId(), entry.getId());
////					    					  }
////					    				  }
////						    			  
////					    			  }
////					    			  else {
////					    				  
////					    				  for (int i = 0; i<model.getCANotProvidedVariables(getLinkedCAE().getId()).size();i++) {
////					    					  if (model.getCANotProvidedVariables(getLinkedCAE().getId()).get(i)== entry.getId()) {
////					    						  
////					    					  }
////					    					  else {
////					    						  model.addCANotProvidedVariable(getLinkedCAE().getId(), entry.getId());
////					    					  }
////					    				  }
////
////					    			  }
//					    		  
//			    			  }
//
//			    		  }
//		    		  }
//			    	  
//		    		  
//		    		  if (canBeAdded) {
//		    			  getLinkedCAE().addAvailableItem(entry);
////		    			  try {
////			    			  if (dependencyCombo.getText().equals("Control Action Provided")) {
////				    			  // Store it in the DataModel
////				    			  
////			    				  for (int i = 0; i<model.getCAProvidedVariables(getLinkedCAE().getId()).size();i++) {
////			    					  if (model.getCAProvidedVariables(getLinkedCAE().getId()).get(i)== entry.getId()) {
////			    						  model.removeCAProvidedVariable(getLinkedCAE().getId(), entry.getId());
////			    					  }
////			    					  else {
////			    						  
////			    					  }
////			    				  }
////				    			  
////			    			  }
////			    			  else {
////			    				  
////			    				  for (int i = 0; i<model.getCANotProvidedVariables(getLinkedCAE().getId()).size();i++) {
////			    					  if (model.getCANotProvidedVariables(getLinkedCAE().getId()).get(i)== entry.getId()) {
////			    						  model.removeCANotProvidedVariable(getLinkedCAE().getId(), entry.getId());
////			    					  }
////			    					  else {
////			    						  
////			    					  }
////			    				  }
////			    				  // Store it in the DataModel
////			    				  //model.getCANotProvidedVariables(getLinkedCAE().getId()).clear();
////			    				  //model.addCANotProvidedVariable(getLinkedCAE().getId(), entry.getId());
////			    			  }
////				    		  
////			    		  } 
////			    		  catch (Exception e) {
////			    			  System.out.println("No linked Variables Stored");
//			    		  }
//		    		  
//
//		    	  }
		    	  
		      
		    		  
		    	  
		    	  
		    	  dependencyTopTableViewer.setInput(getLinkedCAE().getAvailableItems());
		    	  dependencyTopTableViewer.refresh();
		    	  dependenciesBottom = getLinkedCAE().getLinkedItems();
		    	  dependencyBottomTableViewer.setInput(dependenciesBottom);
		    	  dependencyBottomTableViewer.refresh();
		      }
	    });
	    
	    /**
		 * Listener for the dependency table (top)
		 * 	gets the selected item
		 */
	    dependencyTopTable.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  //get the selected Process Model Variable to link it to a control Action
		    	  linkedPMV = (ProcessModelVariables) event.item.getData();
		    	  
		      }  
		      });
	    
	    /**
		 * Listener for the dependency table (bottom)
		 * 	gets the selected item
		 */
	    dependencyBottomTable.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  //get the selected Process Model Variable to link it to a control Action
		    	  linkedPMV = (ProcessModelVariables) event.item.getData();
		    	  
		      }  
		      });
	    
	    /**
		 * Listener for the dependency Combobox
		 *  You can Select if the Control Action is provided or not and can add different Variables to them
		 */
	    dependencyCombo.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		
	    		int selectionIndex = dependencyTable.getSelectionIndex();
	    		
	    		// checks which option is selected and shows the right content
	    		if (dependencyCombo.getText().equals("Control Action Provided")) {
	    			if (!dependencyProvided) {
	    				dependencyTable.setVisible(false);
	    				
	    				// create Input for dependencyTableViewer
	    				List<ControlActionEntrys> dependencyTableInput= new ArrayList<ControlActionEntrys>();
	    		    	for (int i = 0; i<dependencies.size();i++) {
	    		    		if (dependencies.get(i).getSafetyCritical()) {
	    		    			dependencyTableInput.add(dependencies.get(i));
	    		    		}
	    		    	}
	    				
	    				dependencyTableViewer.setInput(dependencyTableInput);
			    		// packs the columns
			    		for (int j = 0, n = dependencyTable.getColumnCount(); j < n; j++) {
			    			dependencyTable.getColumn(j).pack();	    		  		  
			    		}
			    		if (selectionIndex == -1) {
			    			dependencyTable.setSelection(0);
			    		}
			    		else {
			    			dependencyTable.setSelection(selectionIndex);
			    		}
			    		org.eclipse.swt.widgets.Listener[] listeners = dependencyTable.getListeners(SWT.Selection);
			    		for (int i=0; i<listeners.length; i++) {
			    		        if (listeners[i] instanceof TypedListener) {
			    		            if (((TypedListener)listeners[i]).getEventListener() instanceof SelectionListener){
			    		Event underlyingEvent = new Event();
			    		underlyingEvent.widget = dependencyTable;
			    		SelectionEvent selectionEvent = new SelectionEvent(underlyingEvent);
			    		
			    		((SelectionListener)((TypedListener)listeners[i]).getEventListener()).widgetSelected(selectionEvent);
			    		//dependencyTable.notifyListeners(SWT.Selection, new Event());
	    
			    		            }
			    		        }
			    		}
			    		dependencyTable.setVisible(true);
			    		dependencyProvided = true;
	    				
	    			}
	    			
	    		}
	    		else if (dependencyCombo.getText().equals("Control Action not Provided")) {
	    			if (dependencyProvided) {
	    				dependencyTable.setVisible(false);
	    				
	    				// create Input for dependencyTableViewer
	    				List<ControlActionEntrys> dependencyTableInput= new ArrayList<ControlActionEntrys>();
	    		    	for (int i = 0; i<dependencies.size();i++) {
	    		    		if (dependencies.get(i).getSafetyCritical()) {
	    		    			dependencyTableInput.add(dependenciesNotProvided.get(i));
	    		    		}
	    		    	}
	    				
	    				dependencyTableViewer.setInput(dependencyTableInput);
			    		// packs the columns
			    		for (int j = 0, n = dependencyTable.getColumnCount(); j < n; j++) {
			    			dependencyTable.getColumn(j).pack();	    		  		  
			    		}
			    		if (selectionIndex == -1) {
			    			dependencyTable.setSelection(0);
			    		}			    		
			    		else {
			    			dependencyTable.setSelection(selectionIndex);
			    		}
			    		org.eclipse.swt.widgets.Listener[] listeners = dependencyTable.getListeners(SWT.Selection);
			    		for (int i=0; i<listeners.length; i++) {
			    		        if (listeners[i] instanceof TypedListener) {
			    		            if (((TypedListener)listeners[i]).getEventListener() instanceof SelectionListener){
			    		Event underlyingEvent = new Event();
			    		underlyingEvent.widget = dependencyTable;
			    		SelectionEvent selectionEvent = new SelectionEvent(underlyingEvent);
			    		
			    		((SelectionListener)((TypedListener)listeners[i]).getEventListener()).widgetSelected(selectionEvent);
			    		//dependencyTable.notifyListeners(SWT.Selection, new Event());
	    
			    		            }
			    		        }
			    		}
			    		dependencyTable.setVisible(true);    				
	    				dependencyProvided = false;
	    			}
	    		}
	    	}  
	      });
	    
	    /**
		 * Listener for the context table (middle)
		 * 	gets the selected item and creates new columns for the right table
		 */
	    contextTable.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  
		    	  tempTableIndex = -1;
		    	  //get the selected control Action
		    	  setLinkedCAE((ControlActionEntrys) event.item.getData());
		    	  // creates the correct number of rows for the context table
		    	  createTablerows(0);
		    	  
		    	  if (!getLinkedCAE().getContextTableCombinations().isEmpty()) {
		    		  contextRightContent = getLinkedCAE().getContextTableCombinations();
		    		  showContent(filterCombo);
		    		  if (controlActionProvided) {
		    			  contextRightContentProvided = contextRightContent;
		    		  }
		    		  else {
		    			  contextRightContentNotProvided = contextRightContent;
		    		  }
		    	  }	    	  
		    			  
		      }  
	    });
	    
	    /**
		 * Listener for the settings button
		 * 	opens the settings window
		 */
	    settingsBtn.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  //open the settings window
		    	  settingsWindow = new editWindow(linkedCAE, view);
		    	  settingsWindow.open();
		    	  
		      }  
		      });
	    
	    /**
		 * Listener for the checkConflicts button
		 * 	checks the Context table for possible Conflicts
		 */
	    checkConflictsBtn.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  //verfies if there are no conflicts between CAProvided and not provided
		    	  Boolean redraw = false;
		    	  conflictCounter = 0;
		    	  // set all conflicts to false
		    	  for (int i = 0; i < contextRightContentProvided.size(); i++) {
		    		  contextRightContentProvided.get(i).setConflict(false);
		    	  }
		    	  // set all conflicts to false
		    	  for (int j = 0; j < contextRightContentNotProvided.size(); j++) {
		    		  contextRightContentNotProvided.get(j).setConflict(false);
		    	  }
		    	  // checks every element with eachother if they are the same!
		    	  for (int i = 0; i < contextRightContentProvided.size(); i++) {
		    		  for (int j = 0; j < contextRightContentNotProvided.size(); j++) {
		    			  if (checkForConflicts(contextRightContentProvided.get(i), contextRightContentNotProvided.get(j))) {
			    			  contextRightContentProvided.get(i).setConflict(true);
			    			  contextRightContentNotProvided.get(j).setConflict(true);
			    			  redraw = true;
			    		  }
		    		  
		    		  }
		    		  
		    	  }
		    	  if (redraw) {
		    		  contextRightTable.setVisible(false);
		    		  contextRightTable.redraw();
		    		  contextRightViewer.refresh();
		    		  // packs the columns
		    		  for (int j = 0, n = contextRightTable.getColumnCount(); j < n; j++) {
		    			  contextRightTable.getColumn(j).pack();	    		  		  
		    		  }
		    		  contextRightTable.deselectAll();
		    		  errorLabel.setText("There are " +conflictCounter+ " Conflicts!");
		    		  contextRightTable.setVisible(true);
		    	  }
		    	  
		      }  
		      });
	    
	    /**
		 * Listener for the filter Combobox
		 *  Filters the content of the context Table
		 */
	    filterCombo.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
		    	  showContent(filterCombo);
		      }  
	    	
		      });
	    
	    refinedSafetyTable.addListener(SWT.MouseDown, new Listener() {
	        public void handleEvent(Event event) {
	    		
	    		Point pt = new Point(event.x, event.y);
	    		int index = refinedSafetyTable.getTopIndex();
	    		Rectangle clientArea = refinedSafetyTable.getClientArea();
	    		while (index < refinedSafetyTable.getItemCount()) {
	    			boolean visible = false;
	    			TableItem item = refinedSafetyTable.getItem(index);
	    			for (int i = 0; i < refinedSafetyTable.getColumnCount(); i++) {
	    				Rectangle rect = item.getBounds(i);
	              
	    				if (rect.contains(pt)) {	                
	    					refinedSafetyTableCellX = i;	
		  		    	  	if (refinedSafetyTableCellX == refinedSafetyTable.getColumnCount()-3) {
		  		    	  		
		  		    	  		editHazards = refinedSafetyContent.get(refinedSafetyTable.getSelectionIndex()).getEditWindow();
		  		    	  		editHazards.initializeUCA();
		  		    	  		editHazards.open(view);
		  		    	  	}
	    				}
	    	            if (!visible && rect.intersects(clientArea)) {
	    	                visible = true;
	    	              }
	    	            
	    	            if (!visible) {
	    	            	return;
	    	            }
	    			}
	    			index++;
	    	   }


	    	}
	        
	    
	    });
	    

	    

	    
	    /**
	     * Listener for the Right-click menu of the contextRightTable
	     * Opens the menu to create "don't care" values
	     */
	    contextRightMenu.getItem(0).addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		if (contextTableCellX < contextRightTable.getColumnCount()-1) {
	    			contextRightTable.getItem(contextRightTable.getSelectionIndex()).setText(contextTableCellX, " Doesn't Matter ");
	    			contextRightContent.get(contextTableCellY).getValues().remove(contextTableCellX);
	    			contextRightContent.get(contextTableCellY).getValues().add(contextTableCellX, "Doesn't Matter");
	    		}
	    	}
	    });
	    // This Part is responsible for checking any conflicts
	    contextRightTable.addListener(SWT.MouseDown, new Listener() {
	        public void handleEvent(Event event) {
	          Rectangle clientArea = contextRightTable.getClientArea();
	          Point pt = new Point(event.x, event.y);
	          int index = contextRightTable.getTopIndex();
	          
	          while (index < contextRightTable.getItemCount()) {
	            boolean visible = false;
	            TableItem item = contextRightTable.getItem(index);
	            for (int i = 0; i < contextRightTable.getColumnCount(); i++) {
	              Rectangle rect = item.getBounds(i);
	              
	              if (rect.contains(pt)) {
	                
	                contextTableCellX = i;
	                contextTableCellY = index;

	                if ((contextTableCellX == contextRightTable.getColumnCount()-1)&&(controlActionProvided)) {
	                	int tempWidth = rect.width / 3;
	                	if ((rect.x < pt.x)&(pt.x < rect.x+tempWidth)) {
	                		contextRightContent.get(contextTableCellY).setHAnytime(!contextRightContent.get(contextTableCellY).getHAnytime());
		            		if (contextRightContent.get(contextTableCellY).getConflict()) {
		            			contextRightContent.get(contextTableCellY).setConflict(false);
		            			contextRightContentNotProvided.get(contextTableCellY).setConflict(false);
		            			conflictCounter--;
		            		}
	                		contextRightTable.setVisible(false);
	                		contextRightViewer.refresh();
	    	    			// packs the columns
	    	    		  	for (int j = 0, n = contextRightTable.getColumnCount(); j < n; j++) {
	    	    		  		contextRightTable.getColumn(j).pack();	    		  		  
	    	    		  	}
	    	    		  	contextRightTable.deselectAll();
	    	    		  	errorLabel.setText("There are "+conflictCounter+ " Conflicts!");
	    	    		  	contextRightTable.setVisible(true);
	    	    		  	
	                	}
	                	
	                	else if ((rect.x+tempWidth < pt.x)&(pt.x < rect.x+(2*tempWidth))) {
	                		contextRightContent.get(contextTableCellY).setHEarly(!contextRightContent.get(contextTableCellY).getHEarly());
	                		contextRightTable.setVisible(false);
	                		contextRightViewer.refresh();
	    	    			// packs the columns
	    	    		  	for (int j = 0, n = contextRightTable.getColumnCount(); j < n; j++) {
	    	    		  		contextRightTable.getColumn(j).pack();	    		  		  
	    	    		  	}
	    	    		  	contextRightTable.deselectAll();
	    	    		  	contextRightTable.setVisible(true);
	    	    		  	
	                	}
	                	else if ((rect.x+(2*tempWidth) < pt.x)&(pt.x < rect.x + rect.width)) {
	                		contextRightContent.get(contextTableCellY).setHLate(!contextRightContent.get(contextTableCellY).getHLate());
	                		contextRightTable.setVisible(false);
	                		contextRightViewer.refresh();
	    	    			// packs the columns
	    	    		  	for (int j = 0, n = contextRightTable.getColumnCount(); j < n; j++) {
	    	    		  		contextRightTable.getColumn(j).pack();	    		  		  
	    	    		  	}
	    	    		  	contextRightTable.deselectAll();
	    	    		  	contextRightTable.setVisible(true);
	    	    		  	
	                	}
	                }
	                else  if ((contextTableCellX == contextRightTable.getColumnCount()-1)&&(!controlActionProvided)) {
	                	contextRightContent.get(contextTableCellY).setHazardous(!contextRightContent.get(contextTableCellY).getHazardous());
	            		if (contextRightContent.get(contextTableCellY).getConflict()) {
	            			contextRightContent.get(contextTableCellY).setConflict(false);
	            			contextRightContentProvided.get(contextTableCellY).setConflict(false);
	            			conflictCounter--;
	            		}
                		contextRightTable.setVisible(false);
                		contextRightViewer.refresh();
    	    			// packs the columns
    	    		  	for (int j = 0, n = contextRightTable.getColumnCount(); j < n; j++) {
    	    		  		contextRightTable.getColumn(j).pack();	    		  		  
    	    		  	}
    	    		  	contextRightTable.deselectAll();
    	    		  	errorLabel.setText("There are "+conflictCounter+ " Conflicts!");
    	    		  	contextRightTable.setVisible(true);
    	    		  	
	                }
	              }
	              if (!visible && rect.intersects(clientArea)) {
	                visible = true;
	              }
	            }
	            if (!visible)
	              return;
	            index++;
	          }
	        }
	      });
	    
	    // TODO SAVE, LOAD! Listener for the TabFolder in ContextRightComposite
	    contextRightFolder.addSelectionListener(new SelectionAdapter() {
	    	
	    	public void widgetSelected(SelectionEvent event) {
	    		
	    		if (contextRightFolder.getItem(contextRightFolder.getSelectionIndex()) == contextRightFolder.getItem(0)) {
	    			controlActionProvided = true;
	    			if (contextTable.getSelectionIndex() != -1) {
	    				tableIndex = contextTable.getSelectionIndex();
	    			}
	    			else {
	    				tableIndex = 0;
	    			}
	    			setLinkedCAE(dependencies.get(tableIndex));

	    			
	 				// create Input for contextTableViewer
	  				List<ControlActionEntrys> contextTableInput= new ArrayList<ControlActionEntrys>();
	  		    	for (int i = 0; i<dependencies.size();i++) {
	  		    		if (dependencies.get(i).getSafetyCritical()) {
	  		    			contextTableInput.add(dependencies.get(i));
	  		    		}
	  		    	}
	  		    	contextViewer.setInput(contextTableInput);
	  		    	
	  		    	
	    			//contextViewer.setInput(dependencies);
	    			createTablerows(0);
	    			
			    	if (!getLinkedCAE().getContextTableCombinations().isEmpty()) {
			    		contextRightContent = getLinkedCAE().getContextTableCombinations();
			    		
			    		showContent(filterCombo);
			    		if (controlActionProvided) {
			    			contextRightContentProvided = contextRightContent;
			    		}
			    		else {
			    			contextRightContentNotProvided = contextRightContent;
			    		}
			    	}
//	    			if (tableIndex == tempTableIndex) {
//	    				contextRightContent = new ArrayList<ProcessModelVariables>();
//	    				contextRightContent = contextRightContentProvided;
//	    			}
//	    			else {
//	    				// writes the Data into the input file for ACTS		    	  
//	  		    	  	writeFile();
//	  		    	  	// opens ACTS with the given Parameters (the linkedControl)
//	  		    	  	open();
//	    			}
	    			
	    			
	    			
	    		  	
	    			// packs the columns
	    		  	//for (int i = 0, n = contextRightTable.getColumnCount(); i < n; i++) {
	    		  		//contextRightTable.getColumn(i).pack();	    		  		  
	    		  	//}
	    		  	//tempTableIndex = tableIndex;
	    		  	contextTable.setSelection(tableIndex);
	    		  	//contextRightTable.setVisible(true);
	    		}
	    		else {
	    			controlActionProvided = false;
	    			if (contextTable.getSelectionIndex() != -1) {
	    				tableIndex = contextTable.getSelectionIndex();
	    			}
	    			else {
	    				tableIndex = 0;
	    			}
	    			setLinkedCAE(dependenciesNotProvided.get(tableIndex));
	    			
	 				// create Input for contextTableViewer
	  				List<ControlActionEntrys> contextTableInput= new ArrayList<ControlActionEntrys>();
	  		    	for (int i = 0; i<dependencies.size();i++) {
	  		    		if (dependencies.get(i).getSafetyCritical()) {
	  		    			contextTableInput.add(dependenciesNotProvided.get(i));
	  		    		}
	  		    	}
	  		    	contextViewer.setInput(contextTableInput);
	  		    	
	  		    	
	    			//contextViewer.setInput(dependenciesNotProvided);
	    			createTablerows(0);
	    			
			    	if (!getLinkedCAE().getContextTableCombinations().isEmpty()) {
			    		contextRightContent = getLinkedCAE().getContextTableCombinations();
			    		
			    		showContent(filterCombo);
			    		
			    		if (controlActionProvided) {
			    			contextRightContentProvided = contextRightContent;
			    		}
			    		else {
			    			contextRightContentNotProvided = contextRightContent;
			    		}
			    	}
//	    			if (tableIndex == tempTableIndex) {
//	    				contextRightContent = new ArrayList<ProcessModelVariables>();
//	    				contextRightContent = contextRightContentNotProvided;
//	    			}
//	    			else {
//	    				// writes the Data into the input file for ACTS		    	  
//	  		    	  	writeFile();
//	  		    	  	// opens ACTS with the given Parameters (the linkedControl)
//	  		    	  	open();
//	    			}
//	    			contextRightViewer.setInput(contextRightContent);
			    	
	    			//showContent(filterCombo);
	    			
	    			// packs the columns
	    		  	//for (int i = 0, n = contextRightTable.getColumnCount(); i < n; i++) {
	    		  		//contextRightTable.getColumn(i).pack();	    		  		  
	    		  	//}
	    		  	//tempTableIndex = tableIndex;
	    		  	contextTable.setSelection(tableIndex);
	    		  	//contextRightTable.setVisible(true);
	    		}
	    	}
	    	
	    });
	    
	    // listener for the checkboxes in the context table so they get drawn right
	    contextRightTable.addListener(SWT.PaintItem, new Listener() {

	        @Override
	        public void handleEvent(Event event) {
	            // if column = Hazardous, draw the right image
	        	if (controlActionProvided) {
		        	ProcessModelVariables entry = (ProcessModelVariables) event.item.getData();
	            	Image anytimeImage = UNCHECKED;
	            	Image earlyImage = UNCHECKED;
	            	Image lateImage = UNCHECKED;

	            	
		            if (event.index == contextRightTable.getColumnCount()-1){
		            	
			            	if (entry.getHLate()){
				                lateImage = CHECKED;
			            	}
			            	if (entry.getHEarly()){
			            		earlyImage = CHECKED;
			            	}
			            	if (entry.getHAnytime()){
			            		anytimeImage = CHECKED;
			            	}
			            	
			            	getPosition(event, anytimeImage, earlyImage,lateImage);		            	
		            }
	        	}
	        	
	        	else {
	        		
	        		ProcessModelVariables entry = (ProcessModelVariables) event.item.getData();
	        		Image tmpImage = UNCHECKED;
	        		if(event.index == contextRightTable.getColumnCount()-1)  {
		            	if (entry.getHazardous()){
			                tmpImage = CHECKED;
		            	}
		            	getPosition(event, tmpImage);
		            }
	        	}

	        }
	        
	        public void getPosition(Event event, Image tmpImage) {

                int tmpWidth = 0;
                int tmpHeight = 0;
                int tmpX = 0;
                int tmpY = 0;

                tmpWidth = contextRightTable.getColumn(event.index).getWidth();
                tmpHeight = ((TableItem)event.item).getBounds().height;

                tmpX = tmpImage.getBounds().width;
                tmpX = (tmpWidth / 2 - tmpX / 2);
                tmpY = tmpImage.getBounds().height;
                tmpY = (tmpHeight / 2 - tmpY / 2);
                if(tmpX <= 0) tmpX = event.x;
                else tmpX += event.x;
                if(tmpY <= 0) tmpY = event.y;
                else tmpY += event.y;
                event.gc.drawImage(tmpImage, tmpX, tmpY);

	        }
	        
	        public void getPosition(Event event, Image anytimeImage, Image earlyImage, Image lateImage) {

                int tmpWidth = 0;
                int tmpHeight = 0;
                int tmpX = 0;
                int tmpY = 0;

                tmpWidth = contextRightTable.getColumn(event.index).getWidth();
                tmpHeight = ((TableItem)event.item).getBounds().height;

                tmpX = anytimeImage.getBounds().width;
                tmpX = (tmpWidth / 4 - tmpX / 2);
                tmpY = anytimeImage.getBounds().height;
                tmpY = (tmpHeight / 2 - tmpY / 2);
                if(tmpX <= 0) tmpX = event.x;
                else tmpX += event.x;
                if(tmpY <= 0) tmpY = event.y;
                else tmpY += event.y;
                event.gc.drawImage(anytimeImage, tmpX, tmpY);
                event.gc.drawImage(earlyImage, tmpX+(tmpWidth/4), tmpY);
                event.gc.drawImage(lateImage, tmpX+(tmpWidth/2), tmpY);
	        }
	    });
	    

	    
	    // listener for the checkboxes in the control Action table so they get drawn right
	    controlActionTable.addListener(SWT.PaintItem, new Listener() {

	        @Override
	        public void handleEvent(Event event) {
	            // if column 7 (Safety critical), draw the right image
	        	
	            if((event.index == 1)&(event.item.getData().getClass().getName()==CAECLASS))  {
	            	ControlActionEntrys entry = (ControlActionEntrys) event.item.getData();
	            	Image tmpImage = UNCHECKED;
	            	if (entry.getSafetyCritical()){
	                tmpImage = CHECKED;
	            	}

	                int tmpWidth = 0;
	                int tmpHeight = 0;
	                int tmpX = 0;
	                int tmpY = 0;

	                tmpWidth = controlActionTable.getColumn(event.index).getWidth();
	                tmpHeight = ((TableItem)event.item).getBounds().height;

	                tmpX = tmpImage.getBounds().width;
	                tmpX = (tmpWidth / 2 - tmpX / 2);
	                tmpY = tmpImage.getBounds().height;
	                tmpY = (tmpHeight / 2 - tmpY / 2);
	                if(tmpX <= 0) tmpX = event.x;
	                else tmpX += event.x;
	                if(tmpY <= 0) tmpY = event.y;
	                else tmpY += event.y;
	                event.gc.drawImage(tmpImage, tmpX, tmpY);
	            }
	        }
	    });
	    

	    
	    // Create the cell editors
	    CellEditor[] editors = new CellEditor[5];
	    editors[4] = new TextCellEditor(table);
	    
	    CellEditor[] controlActionEditors = new CellEditor[4];
	    controlActionEditors[1] = new CheckboxCellEditor(controlActionTable);
	    controlActionEditors[2] = new TextCellEditor(controlActionTable);
	    
	    // Create the cell editors for refined Safety
	    CellEditor[] refinedSafetyEditors = new CellEditor[7];
	    refinedSafetyEditors[6] = new TextCellEditor(refinedSafetyTable);
	    
	   
	    
	    mainViewer.setColumnProperties(PROPS);
	    mainViewer.setCellModifier(new EntryCellModifier(mainViewer));
	    mainViewer.setCellEditors(editors);
	    controlActionViewer.setColumnProperties(CA_PROPS);
	    controlActionViewer.setCellModifier(new EntryCellModifier(controlActionViewer));
	    controlActionViewer.setCellEditors(controlActionEditors);
	    refinedSafetyViewer.setColumnProperties(RS_PROPS);
	    refinedSafetyViewer.setCellModifier(new EntryCellModifier(refinedSafetyViewer));
	    refinedSafetyViewer.setCellEditors(refinedSafetyEditors);
	    
	    // set title and Image
	    this.setPartName("XSTPA");
	    //this.setContentDescription("Shows the Process Models and Context Tables");
	    this.setTitleImage(LOGO);
	    
	    /**
		 * Listener which Gets the project-id of the currently active editor
		 */
	    
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(pL = new IPartListener() {
		
		    
		    @Override
		    public void partOpened(IWorkbenchPart part) {
		     //if the view is active, get the projectId from the ControlStructure Editor
		     
		     try{
		    	 //PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ID);
		    	 IEditorPart editorPart =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		    	 if (editorPart == null) {
		    	 }
		    	 else {
		    		 
			    	 IEditorInput input = editorPart.getEditorInput();
			    	 UUID projectId=((STPAEditorInput) input).getProjectID();
			      
			    	 model = (DataModelController) ProjectManager.getContainerInstance().getDataModel(projectId);
			    	 // observer gets added, so whenever a value changes, the view gets updated;
					 model.addObserver(ob);
					 getTableEntrys();
		    	 }  
		    	 
		     }
		     catch(ClassCastException e){
		    	 e.printStackTrace();
		     } 

		    }

			@Override
			public void partActivated(IWorkbenchPart part) {
				//  Auto-generated method stub
				
			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
				//  Auto-generated method stub
				
			}

			@Override
			public void partClosed(IWorkbenchPart part) {
				//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				//.hideView(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID));
				
			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
				// Auto-generated method stub
				
			}
			


		    
		   
		   });
		

		
		

	}
	public void showContent(Combo filterCombo) {
		// checks which option is selected and shows the right content
  	  if (filterCombo.getText().equals("Show All")) {
  		  contextRightTable.setVisible(false);
  		  contextRightViewer.setInput(contextRightContent);
  		  // packs the columns
  		  for (int j = 0, n = contextRightTable.getColumnCount(); j < n; j++) {
  			  contextRightTable.getColumn(j).pack();	    		  		  
  		  }
  		  contextRightTable.setVisible(true);
  	  }
  	  else if (filterCombo.getText().equals("Show Hazardous")) {
  		  contextRightTable.setVisible(false);
  		  contextRightHazardousContent.clear();
  		  for (int i=0; i < contextRightContent.size(); i++) {
  			  if ((controlActionProvided)&&(contextRightContent.get(i).getHAnytime())) {
  				  contextRightHazardousContent.add(contextRightContent.get(i));
  			  }
  			  else if ((!controlActionProvided)&&(contextRightContent.get(i).getHazardous())) {
  				  contextRightHazardousContent.add(contextRightContent.get(i));
  			  }
  		  }
  		  contextRightViewer.setInput(contextRightHazardousContent);
  		  // packs the columns
  		  for (int j = 0, n = contextRightTable.getColumnCount(); j < n; j++) {
  			  contextRightTable.getColumn(j).pack();	    		  		  
  		  }
  		  contextRightTable.setVisible(true);
  	  }
  	  else if (filterCombo.getText().equals("Show Not Hazardous")) {
  		  contextRightTable.setVisible(false);
  		  contextRightNotHazardousContent.clear();
  		  for (int i=0; i < contextRightContent.size(); i++) {
  			  if ((controlActionProvided)&&(!contextRightContent.get(i).getHAnytime())) {
  				  contextRightNotHazardousContent.add(contextRightContent.get(i));
  			  }
  			  else if ((!controlActionProvided)&&(!contextRightContent.get(i).getHazardous())) {
  				  contextRightNotHazardousContent.add(contextRightContent.get(i));
  			  }
  		  }
  		  contextRightViewer.setInput(contextRightNotHazardousContent);
  		  // packs the columns
  		  for (int j = 0, n = contextRightTable.getColumnCount(); j < n; j++) {
  			  contextRightTable.getColumn(j).pack();	    		  		  
  		  }
  		  contextRightTable.setVisible(true);
  	  }
    }  	
	
	
	
	public Boolean checkForConflicts(ProcessModelVariables item1, ProcessModelVariables item2) {
		Boolean conflict = true;
		if (!((item1.getHAnytime() == true) && (item2.getHazardous()== true))) {
			return false;
		}
		if (item1.getSizeOfValues() != item2.getSizeOfValues()) {
			return false;
		}
		for (int i = 0; i<item1.getValues().size(); i++) {
			if (!item1.getValues().get(i).equals(item2.getValues().get(i))) {
				conflict = false;
			}
		}
		if (conflict) {
			conflictCounter++;
		}
		return conflict;
	}
	
	
	// Provide the input to the ContentProvider
	public void getTableEntrys() {
		//List<ICorrespondingUnsafeControlAction> unsafeCA = model.getAllUnsafeControlActions();
		List<ITableModel> iHazards = model.getAllHazards();
		List<ICausalComponent> templist = model.getCausalComponents();
		List<IControlAction> iControlActions = model.getAllControlActionsU();
		List<ControlActionEntrys> controlActionList = new ArrayList<ControlActionEntrys>();
		List<ControlActionEntrys> controlActionList2 = new ArrayList<ControlActionEntrys>();
		List<ControllerWithPMEntry> pmList = new ArrayList<ControllerWithPMEntry>();
		pmvList.clear();
		setLinkedCAE(null);
		linkedPMV = null;
		allHazards.clear();
		
		
		for (int i = 0, n = iHazards.size(); i < n; i++) {
			allHazards.add(new xstpa.Hazard(iHazards.get(i).getNumber(),iHazards.get(i).getDescription() ,iHazards.get(i).getTitle()));
		}
		 
		
		
	      for (int i = 0, n = templist.size(); i < n; i++) {
	    	  
	    	  ControllerWithPMEntry tempCWPME = new ControllerWithPMEntry();
	    	  Component temp =(Component) templist.get(i);
	    	  if (temp.getComponentType().name().equals("CONTROLLER")) {
	    	  
	    	  List<IRectangleComponent> tempPMList = temp.getChildren();
	    	  tempCWPME.setController(temp.getText());
	    	  // get the process models
	    	  for (IRectangleComponent tempPM : tempPMList ) {
	    		  List<IRectangleComponent> tempPMVList = tempPM.getChildren();
	    		  tempCWPME.setPM(tempPM.getText());
	    		  
	    		  // get the variables
	    		  for (IRectangleComponent tempPMV : tempPMVList) {
	    			  List<IRectangleComponent> tempPMVVList = tempPMV.getChildren();
	    			  tempCWPME.setPMV(tempPMV.getText());
	    			  
	    			  ProcessModelVariables variables = new ProcessModelVariables();
	    			  variables.setName(tempPMV.getText());
	    			  variables.setId(tempPMV.getId());
	    			  // get the values and add the new object to the processmodel list
	    			  for (IRectangleComponent tempPMVV : tempPMVVList) {
	    				  tempCWPME.setComments(tempPMVV.getComment());
	    				  tempCWPME.setValues(tempPMVV.getText());
	    				  tempCWPME.setId(tempPMVV.getId());
	    				  
	    				  ControllerWithPMEntry finalObj = new ControllerWithPMEntry();
	    				  
	    				  finalObj.setController(tempCWPME.getController());
	    				  finalObj.setPM(tempCWPME.getPM());
	    				  finalObj.setPMV(tempCWPME.getPMV());
	    				  finalObj.setValues(tempCWPME.getValues());
	    				  finalObj.setId(tempCWPME.getId());
	    				  finalObj.setComments(tempCWPME.getComments());
	    				  //System.out.println(tempCWPME.getId());
	    				  //System.out.println("Wert kommentar: "+tempPMVV.getComment());
	    				  variables.addValue(tempCWPME.getValues());
	    				  
	    				  pmList.add(finalObj);
	    				  
	    			  }
	    			  if (!variables.getValues().isEmpty()) {
	    				  
	    				 
	    				  pmvList.add(variables);
	    				  pmvList.get(pmvList.indexOf(variables)).setNumber(pmvList.indexOf(variables)+1);
	    				  

	    			  }
	    		  }
	    		  
	    	  }
	    	  
	    	  
	    	  
	    	
	    	  
		    }
	      }
	      // get the controlActions
	      for (IControlAction entry : iControlActions) {
	    	  ControlActionEntrys tempCAE = new ControlActionEntrys();
	    	  ControlActionEntrys tempCAE2 = new ControlActionEntrys();
	    	  
	    	  tempCAE.setComments(entry.getDescription());
	    	  tempCAE.setControlAction(entry.getTitle());
	    	  tempCAE.setNumber(entry.getNumber());
	    	  tempCAE.setId(entry.getId());
	    	  tempCAE.setSafetyCritical(model.isCASafetyCritical(entry.getId()));
	    	  //tempCAE.setController(entry.);
	    	  tempCAE2.setComments(entry.getDescription());
	    	  tempCAE2.setControlAction(entry.getTitle());
	    	  tempCAE2.setNumber(entry.getNumber());
	    	  tempCAE2.setId(entry.getId());	    	  
	    	  tempCAE2.setSafetyCritical(model.isCASafetyCritical(entry.getId()));
	    	  
	    	  //if (model.getCANotProvidedVariables(entry.getId()).isEmpty()) {
	    	  try {
	    		  // set linkedItems for CA Not Provided
	    		  for (int j = 0; j<pmvList.size();j++) {
	    			  Boolean notLinked = true;
	    			  for (int i = 0; i<model.getCANotProvidedVariables(entry.getId()).size();i++) {
	    		  
	    				  if (model.getComponent(model.getCANotProvidedVariables(entry.getId()).get(i)).getId() == pmvList.get(j).getId()) {
	    					  ProcessModelVariables linkedVar = new ProcessModelVariables();
	    					  linkedVar.setId(pmvList.get(j).getId());
	    					  linkedVar.setValues(pmvList.get(j).getValues());
	    					  linkedVar.setName(pmvList.get(j).getName());
	    					  linkedVar.setNumber(pmvList.get(j).getNumber());	
	    					  
	    					  
	    					  tempCAE2.addLinkedItem(linkedVar);
	    					  notLinked = false;
	    				  }
	    			  }
	    			  


	    			  if (notLinked) {
    					  ProcessModelVariables linkedVar = new ProcessModelVariables();
    					  linkedVar.setId(pmvList.get(j).getId());
    					  linkedVar.setValues(pmvList.get(j).getValues());
    					  linkedVar.setName(pmvList.get(j).getName());
    					  linkedVar.setNumber(pmvList.get(j).getNumber());
	    				  tempCAE2.addAvailableItem(linkedVar);
	    			  }
	    		  }
	    	  }
	    	  catch (Exception e) {
	    		  System.out.println("No Variables for CA Not Provided found!");
	    		  for (int i = 0; i<pmvList.size();i++) {
					  ProcessModelVariables linkedVar = new ProcessModelVariables();
					  linkedVar.setId(pmvList.get(i).getId());
					  linkedVar.setValues(pmvList.get(i).getValues());
					  linkedVar.setName(pmvList.get(i).getName());
					  linkedVar.setNumber(pmvList.get(i).getNumber());
					  tempCAE2.addAvailableItem(linkedVar);
	    		  }
	    	  }
	      		
	    	  try {
	    	  //if (model.getCAProvidedVariables(entry.getId()) != null) {
		    	  // set linkedItems for CA Provided
		    	  for (int j = 0; j<pmvList.size();j++) {
		    		  Boolean notLinked = true;
		    		  for (int i = 0; i<model.getCAProvidedVariables(entry.getId()).size();i++) {
		     		  
		    			  if (model.getComponent(model.getCAProvidedVariables(entry.getId()).get(i)).getId() == pmvList.get(j).getId()) {
		    				  
	    					  ProcessModelVariables linkedVar = new ProcessModelVariables();
	    					  linkedVar.setId(pmvList.get(j).getId());
	    					  linkedVar.setValues(pmvList.get(j).getValues());
	    					  linkedVar.setName(pmvList.get(j).getName());
	    					  linkedVar.setNumber(pmvList.get(j).getNumber());
	    					  tempCAE.addLinkedItem(linkedVar);
	    					  notLinked = false;
		    			  }
		    		  }
		    		  if (notLinked) {
    					  ProcessModelVariables linkedVar = new ProcessModelVariables();
    					  linkedVar.setId(pmvList.get(j).getId());
    					  linkedVar.setValues(pmvList.get(j).getValues());
    					  linkedVar.setName(pmvList.get(j).getName());
    					  linkedVar.setNumber(pmvList.get(j).getNumber());
		    			  tempCAE.addAvailableItem(linkedVar);
		    		  }
		    	  }
	    	  }
	    	  catch (Exception e) {
	    		  System.out.println("No Variables for CA Provided found!");
	    		  for (int i = 0; i<pmvList.size();i++) {
					  ProcessModelVariables linkedVar = new ProcessModelVariables();
					  linkedVar.setId(pmvList.get(i).getId());
					  linkedVar.setValues(pmvList.get(i).getValues());
					  linkedVar.setName(pmvList.get(i).getName());
					  linkedVar.setNumber(pmvList.get(i).getNumber());
	    			  tempCAE.addAvailableItem(linkedVar);
	    		  }
	    	  }
	    	  
	    	  try {
	    		  
	    		  // set linkedItems for CA Not Provided
	    		  
	    		  ProcessModelVariables contextTableEntry;
	    			  for (int i = 0; i<model.getValuesWhenCANotProvided(entry.getId()).size();i++) {
	    				  contextTableEntry = new ProcessModelVariables();
	    				  for (int n = 0; n<model.getValuesWhenCANotProvided(entry.getId()).get(i).getPMValues().size();n++) {
	    					  for (int j = 0; j<pmList.size();j++) {
		    					  if (model.getValuesWhenCANotProvided(entry.getId()).get(i).getPMValues().get(n).equals(pmList.get(j).getId())) {
		    						  
		    						  contextTableEntry.addValue(pmList.get(j).getValues());		    						  
		    						  contextTableEntry.setLinkedControlActionName(entry.getTitle());

		    					  }
	    					  }
	    				  }
	    				  contextTableEntry.setContext("Not Provided");
						  contextTableEntry.setRefinedSafetyRequirements(model.getValuesWhenCANotProvided(entry.getId()).get(i).getSafetyConstraint());
						  contextTableEntry.setHazardous(model.getValuesWhenCANotProvided(entry.getId()).get(i).isCombiHazardous());
						  tempCAE2.addContextTableCombination(contextTableEntry);
	    			  }
	    		  
	    		  
				  
			  }
	    	  catch (Exception e) {
	    		  System.out.println("There was no stored Combinations for Provided");
	    	  }
	    	  
	    	  try {
	    		  
	    		  // set linkedItems for CA Provided
	    		  
	    		  ProcessModelVariables contextTableEntry;
	    			  for (int i = 0; i<model.getValuesWhenCAProvided(entry.getId()).size();i++) {
	    				  contextTableEntry = new ProcessModelVariables(); 
	    				  for (int n = 0; n<model.getValuesWhenCAProvided(entry.getId()).get(i).getPMValues().size();n++) {
	    					  
	    					  for (int j = 0; j<pmList.size();j++) {
	    						  if (model.getValuesWhenCAProvided(entry.getId()).get(i).getPMValues().get(n).equals(pmList.get(j).getId())) {
	    						  
		    						  contextTableEntry.addValue(pmList.get(j).getValues());
		    						  contextTableEntry.setLinkedControlActionName(entry.getTitle());

	    						  }
	    					 }
	    					  
    						  
	    					  
	    				  }
	    				  contextTableEntry.setContext("Provided");
						  contextTableEntry.setRefinedSafetyRequirements(model.getValuesWhenCAProvided(entry.getId()).get(i).getSafetyConstraint());
						  contextTableEntry.setHAnytime(model.getValuesWhenCAProvided(entry.getId()).get(i).isHazardousWhenAnyTime());
						  contextTableEntry.setHEarly(model.getValuesWhenCAProvided(entry.getId()).get(i).isHazardousWhenToEarly());
						  contextTableEntry.setHLate(model.getValuesWhenCAProvided(entry.getId()).get(i).isHazardousWhenToLate());
	    				  tempCAE.addContextTableCombination(contextTableEntry); 
	    			  }
	    		  
	    		  
				  
			  }
	    	  catch (Exception e) {
	    		  System.out.println("There was no stored Combinations for NotProvided");
	    	  }
	    	  
		    	  
	    	  controlActionList.add(tempCAE);
	    	  controlActionList2.add(tempCAE2);
	      }
	      this.pmList = pmList;
	      
	      this.controlActionList = controlActionList;
	      dependencies = controlActionList;
	      dependenciesNotProvided = controlActionList2;
	      mainViewer.setInput(pmList);
		  for (int i = 0; i < 5; i++) {
			  table.getColumn(i).pack();
		  }
	}
	
	/**
	 * Writing the later used input file for ACTS
	 */
	public Boolean writeFile() {
		PrintWriter writer = null;
		String paramName = null;
		try {
			if (controlActionProvided) {
				writer = new PrintWriter(INPUT, "UTF-8");
			}
			else {
				writer = new PrintWriter(INPUT2, "UTF-8");
			}
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			return false;
		}
		writer.println("[System]");
		//TODO write the name
		writer.println("Name : test");
		// Print the Parameters
		writer.println("");
		writer.println("[Parameter]");
		for (ProcessModelVariables entry : getLinkedCAE().getLinkedItems()) {
			List<String> values = entry.getValues();
			 if (entry.getName().contains(" ")) {
				 paramName = entry.getName().replace(" ", "_");
			 }
			 else {
				 paramName = entry.getName();
			 }
			writer.write(paramName+ " (enum)" + " : ");
			for (int i = 0, size = entry.getSizeOfValues();i<size; i++) {
				if (i < entry.getSizeOfValues()-1) {
					writer.write(values.get(i) + ", " );
					
				}
				else {
					writer.write(values.get(i));
				}
			}
			writer.println("");
			
		}
		// Print the Relations
		writer.println("");
		writer.println("[Relation]");
		for (int entry = 0; entry<editWindow.relations.size(); entry++) {
			String temp = "";
	    	List<String> tempList = editWindow.relations.get(entry).getVariables();
	    	for (int i =0; i<tempList.size(); i++) {
	    		
	    		if (i == tempList.size()-1) {
	    			temp = temp.concat(tempList.get(i).replace(" ", "_"));
	    		}
	    		else {
	    			temp = temp.concat(tempList.get(i).replace(" ", "_").concat(", "));
	    		}
	    	}
			writer.println("R"+entry+ " : ("+temp+", "+editWindow.relations.get(entry).getStrength()+")");
		}
		// Print the Constraints
		writer.println("");
		writer.println("[Constraint]");
		for (String entry : editWindow.constraints) {
			writer.println(entry);
		}
		writer.println("");
		
		writer.close();
		return true;
		
	}
	
	public Boolean open() {
		// Run ACTS in a separate system process
		Process proc;
		String modes = " -Ddoi=" + Integer.toString(contextRightTable.getColumnCount()-2) + " ";
		
		try {
			if (!editWindow.modes.isEmpty()) {
				for (int i=0; i<editWindow.modes.size(); i++) {
					
						if (i==0) {
							modes = " -Dalgo="+editWindow.modes.get(i)+" ";
						}
					
						else if (i==1) {
							modes = modes.concat("-Ddoi="+editWindow.modes.get(i)+" ");
						}
						else if (i==2) {
							modes = modes.concat("-Dmode="+editWindow.modes.get(i)+" ");
						}
						else if (i == 3) {
							modes = modes.concat("-Dchandler="+editWindow.modes.get(i)+" ");
						}
				}
				// clear so that the default mode gets selected again
				editWindow.modes.clear();
			}
			String location = Platform.getInstanceLocation().getURL().getPath()+"acts_cmd_2.92.jar";
			location = location.substring(1, location.length());
			if (controlActionProvided) {
				proc = Runtime.getRuntime().exec("java"+modes+"-jar " +location+" cmd "+INPUT+ " " + OUTPUT);
			}
			else {
				proc = Runtime.getRuntime().exec("java"+modes+"-jar " +location+" cmd "+INPUT2+ " " + OUTPUT2);
			}
			
			
			// Then retreive the process output
			InputStream in = proc.getInputStream();
			InputStream err = proc.getErrorStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        StringBuilder out = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
	        //System.out.println(out.toString());   //Prints the string content read from input stream
	        reader.close();

	        getEntrysFromFile(reader);
			
			
			contextRightViewer.setInput(contextRightContent);	
			// packs the columns
		  	for (int i = 0, n = contextRightTable.getColumnCount(); i < n; i++) {
		  		     		  
		  		contextRightTable.getColumn(i).pack();
		  		  
		  	}
		  	
		  	contextRightTable.setVisible(true);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void getEntrysFromFile (BufferedReader reader) {
		try {
			contextRightContent = new ArrayList<ProcessModelVariables>();
			
			if (controlActionProvided) {
				contextRightContentProvided = new ArrayList<ProcessModelVariables>();				
				reader = new BufferedReader(new FileReader(OUTPUT));
	
			}
			else {
				contextRightContentNotProvided = new ArrayList<ProcessModelVariables>();
				reader = new BufferedReader(new FileReader(OUTPUT2));
	
			}
			// go to the fourth line in the file to read the number of Configurations
			reader.readLine();
			reader.readLine();
			String line = reader.readLine();
			
			char temp = line.charAt(line.length()-1);
			
			int paramCount = Character.getNumericValue(temp);
			line = reader.readLine();
			temp = line.charAt(line.length()-1);
			int configCount = Character.getNumericValue(temp);
			//contextRightTable.setVisible(false);
			while ((line = reader.readLine()) != null) {
				if (line.contains("Configuration #")) {
					ProcessModelVariables entry = new ProcessModelVariables();
					ProcessModelVariables entry2 = new ProcessModelVariables();
					temp = line.charAt(line.length()-2);
					int currentConfig = Character.getNumericValue(temp);
					reader.readLine();
					List<String> values = new ArrayList<String>();
					List<String> variables = new ArrayList<String>();
					for (int i = 0; i<paramCount; i++) {
						line = reader.readLine();
						temp = line.charAt(0);
						int currentVariable = Character.getNumericValue(temp);
						
						variables.add(line.substring(line.indexOf("=")+2, line.lastIndexOf("=")));
						//entry.setName(line.substring(line.indexOf("=")+2, line.length()));
						line = line.substring(line.lastIndexOf("=")+1, line.length());
						entry.addValue(line);
						values.add(line);
						if  (contextTable.getSelectionIndex() != -1) {
							entry.setLinkedControlActionName(contextTable.getItem(contextTable.getSelectionIndex()).getText());
							entry2.setLinkedControlActionName(contextTable.getItem(contextTable.getSelectionIndex()).getText());
						}
						else {
							entry.setLinkedControlActionName(contextTable.getItem(0).getText());
							entry2.setLinkedControlActionName(contextTable.getItem(0).getText());
						}
						entry2.addValue(line);
						
						
					}
					entry.setPmValues(values);
					entry.setPmVariables(variables);
					if (controlActionProvided) {
						contextRightContentProvided.add(entry);
						contextRightContent.add(entry);
						//if (contextTable.getSelectionIndex() != -1) {
						getLinkedCAE().setContextTableCombinations(contextRightContent);
							
				    	  try {
				    		  List<UUID> combis = new ArrayList<UUID>();
				    		  List<ProvidedValuesCombi> valuesIfProvided = new ArrayList<ProvidedValuesCombi>();
				    		  ProvidedValuesCombi val = new ProvidedValuesCombi();
				    		  
				    		  for (int i = 0; i<getLinkedCAE().getContextTableCombinations().size();i++) {
				    			  val = new ProvidedValuesCombi();
				    			  combis = new ArrayList<UUID>();
				    			  for (int z = 0; z<getLinkedCAE().getContextTableCombinations().get(i).getValues().size();z++) {
				    				  
				    				  for (int n = 0; n<pmList.size();n++) {
				    					  if ((pmList.get(n).getValues().equals(getLinkedCAE()
				    							  .getContextTableCombinations().get(i).getValues().get(z)))) {
				    						  combis.add(pmList.get(n).getId());
				    						  
				    					  }
				    				  }
				    			  }
				    			  val.setValues(combis);
				    			  val.setConstraint(getLinkedCAE().getContextTableCombinations().get(i).getRefinedSafetyRequirements());
				    			  val.setHazardousAnyTime(getLinkedCAE().getContextTableCombinations().get(i).getHAnytime());
				    			  val.setHazardousToEarly(getLinkedCAE().getContextTableCombinations().get(i).getHEarly());
				    			  val.setHazardousToLate(getLinkedCAE().getContextTableCombinations().get(i).getHLate());
				    			  valuesIfProvided.add(val);
				    		  }
				    		  model.setValuesWhenCAProvided(getLinkedCAE().getId(),valuesIfProvided);
				    	  }
			
				    	  catch (Exception e) {
				    		  System.out.println("Couldn't save ContextTableCombis if Provided");
				    	  }
							//dependencies.get(contextTable.getSelectionIndex()).setContextTableCombinations(contextRightContentProvided);
						//}
						//else {
							//dependencies.get(0).setContextTableCombinations(contextRightContentProvided);
						//}
					}
					else {
						contextRightContentNotProvided.add(entry);
						contextRightContent.add(entry);
						//if  (contextTable.getSelectionIndex() != -1) {
							//dependenciesNotProvided.get(contextTable.getSelectionIndex()).setContextTableCombinations(contextRightContentNotProvided);				
						//}
						//else {
							//dependenciesNotProvided.get(0).setContextTableCombinations(contextRightContentNotProvided);
						//}
						getLinkedCAE().setContextTableCombinations(contextRightContent);
						
				    	  try {
				    		  List<UUID> combis = new ArrayList<UUID>();
				    		  List<NotProvidedValuesCombi> valuesIfNotProvided = new ArrayList<NotProvidedValuesCombi>();
				    		  NotProvidedValuesCombi val = new NotProvidedValuesCombi();
				    		  
				    		  for (int i = 0; i<getLinkedCAE().getContextTableCombinations().size();i++) {
				    			  val = new NotProvidedValuesCombi();
				    			  combis = new ArrayList<UUID>();
				    			  for (int z = 0; z<getLinkedCAE().getContextTableCombinations().get(i).getValues().size();z++) {
				    				  for (int n = 0; n<pmList.size();n++) {
				    					  if (pmList.get(n).getValues().equals(getLinkedCAE()
				    							  .getContextTableCombinations().get(i).getValues().get(z))) {
				    						  combis.add(pmList.get(n).getId());
				    					  }
				    				  }
				    			  }
				    			  val.setValues(combis);
				    			  val.setConstraint(getLinkedCAE().getContextTableCombinations().get(i).getRefinedSafetyRequirements());
				    			  val.setHazardous((getLinkedCAE().getContextTableCombinations().get(i).getHAnytime()));
				    			  valuesIfNotProvided.add(val);
				    		  }
				    		  model.setValuesWhenCANotProvided(getLinkedCAE().getId(),valuesIfNotProvided);
				    	  }
			
				    	  catch (Exception e) {
				    		  System.out.println("Couldn't save ContextTableCombis if Not Provided");
				    	  }
						
					}
					
				}
				
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

	
	/**
	 * Create the correct rows in the context table
	 */
	public void createTablerows(int counter) {
		
		contextRightTable.setVisible(false);
	  	
	  	  
	  	if (controlActionProvided == false) {
	  		  
	  		if (!getLinkedCAE().getLinkedItems().isEmpty()) {
	  			new TableColumn(contextRightTable, SWT.CENTER, counter).setText(ENTRY_ID);
	  			counter++;
	  			// creates new TableColumns dynamically so that the context table has the right size (and labels)
	  			for (ProcessModelVariables entry:getLinkedCAE().getLinkedItems()) {
		  		  
	  				new TableColumn(contextRightTable, SWT.CENTER, counter).setText(entry.getName());
	  				counter++;
	  			}
		  	
	  			new TableColumn(contextRightTable, SWT.CENTER, counter).setText(HAZARDOUS);
	  			counter++;
	  			for (int i=counter, columnCount=contextRightTable.getColumnCount(); i<columnCount; i++) {
	  				contextRightTable.getColumn(counter).dispose();
	  			}

	  		}

	  	}
	  	else {
	  		if (!getLinkedCAE().getLinkedItems().isEmpty()) {
	  			new TableColumn(contextRightTable, SWT.CENTER, counter).setText(ENTRY_ID);
	  			counter++;
	  			// creates new TableColumns dynamically so that the context table has the right size (and labels)
	  			for (ProcessModelVariables entry:getLinkedCAE().getLinkedItems()) {
		  		  
	  				new TableColumn(contextRightTable, SWT.CENTER, counter).setText(entry.getName());
	  				counter++;
	  			}

		  	
		  		new TableColumn(contextRightTable, SWT.CENTER, counter).setImage(HEADER);
		  		counter++;
		  	
		  		for (int i=counter, columnCount=contextRightTable.getColumnCount(); i<columnCount; i++) {
		  			contextRightTable.getColumn(counter).dispose();
		  		}
	  		}

		  	
	  	
		}
	  	// Create the cell editors
	  	CellEditor[] contextEditors = new CellEditor[contextRightTable.getColumnCount()];
	  	for (int i = 0; i<contextRightTable.getColumnCount();i++) {
	  		if ((i != 0) &(i!=contextRightTable.getColumnCount()-1)) {
	  			contextEditors[i] = new TextCellEditor(contextRightTable);
	  		}
	  	}
		
		    
		contextProps = new String[contextRightTable.getColumnCount()];
		for (int i=0; i<contextProps.length;i++) {
			contextProps[i] = contextRightTable.getColumn(i).getText();
		}
		contextRightViewer.setColumnProperties(contextProps);
		contextRightViewer.setCellModifier(new ContextCellModifier(contextRightViewer));
		contextRightViewer.setCellEditors(contextEditors);
	}
	

	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		mainViewer.getControl().setFocus();
	}


	@Override
	public void dispose() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(pL);
		this.model.deleteObserver(ob);
	super.dispose();
	}
	
	
	/**
	 * updates the table dynamically if something changes in the Datamodel
	 */
	@Override
	public void update(Observable o, Object arg) {
		getTableEntrys();
		
	}

	public ControlActionEntrys getLinkedCAE() {
		return linkedCAE;
	}

	public void setLinkedCAE(ControlActionEntrys linkedCAE) {
		this.linkedCAE = linkedCAE;
	}
	
	

	
}