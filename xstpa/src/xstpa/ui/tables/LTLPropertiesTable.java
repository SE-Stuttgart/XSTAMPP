package xstpa.ui.tables;

import java.util.Map.Entry;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

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

	public LTLPropertiesTable(Composite parent) {
		super(parent);

		TableColumnLayout tLayout = new TableColumnLayout();
		setLayout(tLayout);
		
		ltlViewer = new TableViewer(this, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL );
		ltlViewer.setContentProvider(new ArrayContentProvider());
		ltlViewer.setLabelProvider(new LtlViewLabelProvider());
	    ltlTable = ltlViewer.getTable();
	    ltlTable.setHeaderVisible(true);
	    ltlTable.setLinesVisible(true);
	    ltlTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    // add columns for ltl tables	   
	    TableColumn ltlColumn = new TableColumn(ltlTable, SWT.LEFT);
	    ltlColumn.setText(View.ENTRY_ID);
	    tLayout.setColumnData(ltlColumn, new ColumnWeightData(1, 30, false));
	    
	    ltlColumn = new TableColumn(ltlTable, SWT.LEFT);
	    ltlColumn.setText(View.LTL_RULES);
	    tLayout.setColumnData(ltlColumn, new ColumnWeightData(1, 30, false));
	    
	    setVisible(false);
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
	public boolean refreshTable() {
		return true;
		
	}

}
