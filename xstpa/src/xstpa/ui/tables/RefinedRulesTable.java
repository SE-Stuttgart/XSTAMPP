package xstpa.ui.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.model.controlaction.IValueCombie;
import xstampp.util.STPAPluginUtils;
import xstpa.model.ControlActionEntry;
import xstpa.model.ProcessModelVariables;
import xstpa.model.RefinedSafetyEntry;
import xstpa.ui.View;
import xstpa.ui.dialogs.EditRelatedUcaWizard;
import xstpa.ui.tables.utils.MainViewContentProvider;

public class RefinedRulesTable extends AbstractTableComposite {
	
	private class RefinedSafetyViewLabelProvider extends LabelProvider implements
	ITableLabelProvider{
		
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			String columnName = columns[columnIndex];
			if(columnName.equals(View.UCA)){
				return View.ADD;
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			String columnName = columns[columnIndex];
			RefinedSafetyEntry entry = (RefinedSafetyEntry) element;
			switch (columnName) {
			case View.ENTRY_ID:
				return "SR" + String.valueOf(refinedSafetyContent.indexOf(entry)+1);
			case View.CONTROL_ACTIONS:
				return entry.getVariable().getLinkedControlActionName();
			case View.CONTEXT:
				return entry.getContext();
			case View.CONTEXT_TYPE:
				return entry.getType();
			case View.CRITICAL_COMBI:	
				return entry.getCriticalCombinations(" == ", ",", false, false);
				
			case View.UCA:
				String tempUcas =entry.getUCALinks();
				if (tempUcas.isEmpty()) {
					return "Click to see UCA's";
				}
				return tempUcas;
			case View.REL_HAZ:
				return entry.getRelatedHazards();
			case View.REFINED_RULES:
				StringBuffer rule = new StringBuffer();
				rule.append(entry.getVariable().getLinkedControlActionName());
				rule.append(" ");
				rule.append(entry.getType());
				rule.append(" ");
				rule.append("is hazardous when ");
				rule.append(entry.getCriticalCombinations(" == ", ",", false, false));
				return rule.toString();
			}

				
			return null;
		}

	}

	private TableViewer refinedSafetyViewer;
	private List<RefinedSafetyEntry> refinedSafetyContent;
	private Table refinedSafetyTable;
	private String[] columns = new String[]{
			View.ENTRY_ID,View.CONTROL_ACTIONS,View.CONTEXT,"Type",
			View.CRITICAL_COMBI,View.UCA,View.REL_HAZ,View.REFINED_RULES
	};
	
	public RefinedRulesTable(Composite parent) {
		super(parent);
		this.refinedSafetyContent = new ArrayList<>();
	    setLayout( new GridLayout(2, false));	
	    Composite tableComp = new Composite(this, SWT.None);
	    tableComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TableColumnLayout tLayout = new TableColumnLayout();
		tableComp.setLayout(tLayout);
		
	    refinedSafetyViewer = new TableViewer(tableComp,SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		refinedSafetyViewer.setContentProvider(new MainViewContentProvider());
		refinedSafetyViewer.setLabelProvider(new RefinedSafetyViewLabelProvider());
		refinedSafetyTable = refinedSafetyViewer.getTable();
	    
	    refinedSafetyTable.setHeaderVisible(true);
	    refinedSafetyTable.setLinesVisible(true);
	    
	    // add columns for ltl tables	
	    for(int i= 0;i<columns.length;i++){
	    	TableColumn col = new TableColumn(refinedSafetyTable, SWT.LEFT);
	    	col.setText(columns[i]);
		    tLayout.setColumnData(col, new ColumnWeightData(1, 30, false));
	    }
	    
	    /*
	     * Functionality to recognize if the user selectes the uca linking cell
	     */
	    refinedSafetyTable.addListener(SWT.MouseDown, new Listener() {
	        public void handleEvent(Event event) {
	    		
	    		Point pt = new Point(event.x, event.y);
	    		
	    		int index = refinedSafetyTable.getTopIndex();
	    		while (index < refinedSafetyTable.getItemCount()) {
	    			TableItem item = refinedSafetyTable.getItem(index);
	    			for (int i = 0; i < refinedSafetyTable.getColumnCount(); i++) {
	    				Rectangle rect = item.getBounds(i);
	              
	    				if (rect.contains(pt)) {	                
	    					int refinedSafetyTableCellX = i;	
		  		    	  	if ((refinedSafetyTableCellX == refinedSafetyTable.getColumnCount()-3)& (refinedSafetyTable.getSelectionIndex() != -1)) {	  		    	  		
		  		    	  		RefinedSafetyEntry entry = (RefinedSafetyEntry) refinedSafetyTable.getSelection()[0].getData();
		  		    	  		
		  		    	  		EditRelatedUcaWizard editUCALinks = new EditRelatedUcaWizard(dataController.getModel(),
		  		    	  																	 entry.getVariable().getUcaLinks(entry.getType()));
		  		    	  		if(editUCALinks.open()){
		  		    	  			entry.getVariable().setUcaLinks(editUCALinks.getUcaLinks(),entry.getType());
		  		    	  			storeRefinedSafety();
		  		    	  		}
		  		    	  		refinedSafetyViewer.setInput(refinedSafetyContent);
		  		    	  		refinedSafetyTable.getColumn(4).pack();
		  		    	  	}
	    				}
	    			}
	    			index++;
	    	   }
	    	}
	    });
	    
	    refinedSafetyViewer.setColumnProperties(View.RS_PROPS_COLUMS);
	    
		// Add a Composite which contains tools to edit refinedSafetyTable
	    Composite editRefinedSafetyTableComposite = new Composite( this, SWT.NONE);
	    editRefinedSafetyTableComposite.setLayout( new GridLayout(1, false) );
	    GridData data = new GridData(SWT.RIGHT, SWT.TOP, false, true); 
//	    data.verticalIndent = 5;
	    editRefinedSafetyTableComposite.setLayoutData(data);
	    
		// Add a button to export all tables
	    final Button exportBtn = new Button(editRefinedSafetyTableComposite, SWT.PUSH);
	    exportBtn.setToolTipText("Exports all Tables");
	    exportBtn.setImage(View.EXPORT);
	    exportBtn.pack();
	    
	    // Add a button to switch tables (LTL Button)
	    final Button bRemoveEntry = new Button(editRefinedSafetyTableComposite, SWT.PUSH);
	    bRemoveEntry.setToolTipText("removes the selected entry in the from the rules table");
	    bRemoveEntry.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_REMOVE));
	    bRemoveEntry.pack();
	    
	    // Add a button to switch tables (LTL Button)
	    final Button bAllRemoveEntry = new Button(editRefinedSafetyTableComposite, SWT.PUSH);
	    bAllRemoveEntry.setToolTipText("removes all entries from the rules table");
	    bAllRemoveEntry.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_REMOVEALL));
	    bAllRemoveEntry.pack();
	    
	    
	    /**
	     * Functionality for the Export Button
	     */
	    exportBtn.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	    	  
	    	  Map<String,String> values=new HashMap<>();
	    	  values.put("xstampp.commandParameter.openwizard", "export.wizard");
	    	  STPAPluginUtils.executeParaCommand("xstampp.command.openWizard", values);
	      }
	    });
	    
	    bRemoveEntry.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		if (refinedSafetyTable.getSelectionIndex() != -1) {
	    			RefinedSafetyEntry entry = (RefinedSafetyEntry) refinedSafetyTable.getSelection()[0].getData();
		    		removeEntry(entry);
					refreshTable();
				}
	    	}
		});
	    bAllRemoveEntry.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		ArrayList<ControlActionEntry> allCAEntrys = new ArrayList<>();
	      	    allCAEntrys.addAll(dataController.getDependenciesIFProvided());
	      	    allCAEntrys.addAll(dataController.getDependenciesNotProvided());
	      	    
	    	    for (ControlActionEntry caEntry : allCAEntrys) {
	    	    	for(ProcessModelVariables variable: caEntry.getContextTableCombinations()){
	    	    		variable.setGlobalHazardous(false);
	    	    	}
		    	    dataController.storeBooleans(caEntry);
	    	    }
				dataController.getModel().removeSafetyRule(true, null);
				refreshTable();
	    	}
		});
	    setVisible(false);
	}

	private void removeEntry(RefinedSafetyEntry entry){
		List<ProcessModelVariables> combinations = dataController.getControlActionEntry(entry.getContext().equals(IValueCombie.CONTEXT_PROVIDED),
				 entry.getVariable().getLinkedControlActionID()).getContextTableCombinations();
		switch(entry.getType()){
			case IValueCombie.TYPE_ANYTIME: 
				entry.getVariable().setHAnytime(false);
				break;
			case IValueCombie.TYPE_TOO_EARLY:  
				entry.getVariable().setHEarly(false);
				break;
			case IValueCombie.TYPE_TOO_LATE:  
				entry.getVariable().setHLate(false);
				break;
			case IValueCombie.TYPE_NOT_PROVIDED:  
				entry.getVariable().setHazardous(false);
				break;
			default:
				return;
		}
		if (entry.getVariable().isArchived() && !entry.getVariable().getGlobalHazardous()) {
			combinations.remove(entry.getVariable());
		}
		dataController.storeBooleans(dataController.getControlActionEntry(entry.getContext().equals(IValueCombie.CONTEXT_PROVIDED),
				 entry.getVariable().getLinkedControlActionID()));
		dataController.getModel().removeSafetyRule(false, entry.getDataRef());
		dataController.storeBooleans(dataController.getControlActionEntry(entry.getContext().equals(IValueCombie.CONTEXT_PROVIDED),
				 entry.getVariable().getLinkedControlActionID()));
	}
	
	@Override
	public void activate() {
		refreshTable();
		setVisible(true);  
	}

	@Override
	public boolean refreshTable() {
		if(refinedSafetyViewer.getControl() == null || refinedSafetyViewer.getControl().isDisposed()){
			return false;
		}
		refinedSafetyContent.clear();
		Map<String, ArrayList<RefinedSafetyEntry>> refinedEntrys = dataController.getHazardousCombinations(null); 
  	    refinedSafetyContent.addAll(refinedEntrys.get(IValueCombie.HAZ_IF_PROVIDED));
  	    refinedSafetyContent.addAll(refinedEntrys.get(IValueCombie.HAZ_IF_WRONG_PROVIDED));
  	    refinedSafetyContent.addAll(refinedEntrys.get(IValueCombie.HAZ_IF_NOT_PROVIDED));

  	    Collections.sort(refinedSafetyContent);
  	    if (refinedSafetyContent.isEmpty()) {
  	    	writeStatus("No Hazardous Combinations - Please check some Combinations as Hazardous");
  	    }
  
  	    refinedSafetyViewer.setInput(refinedSafetyContent);
  	    for (int i = 0, n = refinedSafetyTable.getColumnCount(); i < n; i++) {
  	    	refinedSafetyTable.getColumn(i).pack();
  	    }
		return true;
	}

	/**
	 * Store the Boolean Data (from the Context Table) in the Datamodel
	 */
	public void storeRefinedSafety() {
		
  	    ArrayList<ControlActionEntry> allCAEntrys = new ArrayList<>();
  	    allCAEntrys.addAll(dataController.getDependenciesIFProvided());
  	    allCAEntrys.addAll(dataController.getDependenciesNotProvided());
  	    
	    for (ControlActionEntry caEntry : allCAEntrys) {	
	    	dataController.storeBooleans(caEntry);	
	    }
	}

}
