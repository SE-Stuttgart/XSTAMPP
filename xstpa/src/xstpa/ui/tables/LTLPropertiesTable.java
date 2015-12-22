package xstpa.ui.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import xstpa.model.ProcessModelVariables;
import xstpa.model.XSTPADataController;
import xstpa.ui.View;

public class LTLPropertiesTable extends AbstractTableComposite{
	private class LtlViewLabelProvider extends LabelProvider implements
	ITableLabelProvider{
		
		@Override
		public Image getColumnImage(Object element, int columnIndex) {

			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if(element instanceof Entry<?, ?>){
				switch(columnIndex){
				case 0: 
					return  (String) ((Entry<?, ?>) element).getKey();
				case 1:
					return  (String) ((Entry<?, ?>) element).getValue();
					
				}
				
			}
			return null;
		}
	}
	
	private TableViewer ltlViewer;
	private Table ltlTable;
	private List<ProcessModelVariables> ltlContent;

	public LTLPropertiesTable(Composite parent, XSTPADataController controller) {
		super(parent, controller);
		setLayout( new GridLayout(1, false));	    
		ltlViewer = new TableViewer(this, SWT.FULL_SELECTION );
		ltlViewer.setContentProvider(new ArrayContentProvider());
		ltlViewer.setLabelProvider(new LtlViewLabelProvider());
		ltlContent = new ArrayList<>();
	    ltlTable = ltlViewer.getTable();
	    ltlTable.setHeaderVisible(true);
	    ltlTable.setLinesVisible(true);
	    ltlTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    // add columns for ltl tables	   
	    new TableColumn(ltlTable, SWT.LEFT).setText(View.ENTRY_ID);
	    new TableColumn(ltlTable, SWT.LEFT).setText(View.LTL_RULES);
	    ltlTable.setLinesVisible(true);
	    ltlTable.setHeaderVisible(true);
	    
	}

	@Override
	public void activate() {
	      if(dataController != null && dataController.getModel() != null){
		      ltlViewer.setInput(dataController.getModel().getLTLPropertys(0).entrySet());
			  for (int i = 0, n = ltlTable.getColumnCount(); i < n; i++) {
				  ltlTable.getColumn(i).pack();
			  }
	      }
  	  
	      // set the new composite visible
	      setVisible(true);
	}

	@Override
	public void refreshTable() {
		// TODO Auto-generated method stub
		
	}

}
