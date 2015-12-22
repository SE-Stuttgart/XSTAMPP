package xstpa.ui.tables;

import java.util.ArrayList;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import xstpa.model.ProcessModelValue;
import xstpa.model.XSTPADataController;
import xstpa.ui.View;
import xstpa.ui.tables.utils.EntryCellModifier;
import xstpa.ui.tables.utils.MainViewContentProvider;

public class ProcessValuesTable extends AbstractTableComposite {

	private class ValueLabelsProvider extends LabelProvider implements
	ITableLabelProvider, IColorProvider {
			
		public String getColumnText(Object element, int columnIndex) {
			
			ProcessModelValue entry = (ProcessModelValue) element;
				switch (columnIndex) {
				case 0:
					return entry.getController();
				case 1:
					return entry.getPM();
				case 2:
					return entry.getPMV();
				case 3:
					return entry.getValueText();
				
				case 4:
					return entry.getComments();
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
		public Color getBackground(Object element) {
			ArrayList<?> list = (ArrayList<?>) mainViewer.getInput();
			int index = list.indexOf(element);
			if ((index % 2) == 0) {
				return View.BACKGROUND;
			} else {	    
				return null;
			}
		}
	}
	
	private TableViewer mainViewer;
	private Table table;

	public ProcessValuesTable(Composite parent,XSTPADataController controller) {
		super(parent, controller);
		setLayout(new GridLayout(1, false));
		 // Add the TableViewers   
		mainViewer = new TableViewer(this, SWT.FULL_SELECTION );
		mainViewer.setContentProvider(new MainViewContentProvider());
		mainViewer.setLabelProvider(new ValueLabelsProvider());
		table = mainViewer.getTable();
		table.setHeaderVisible(true);
	    table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		 // Create the cell editors
	    CellEditor[] editors = new CellEditor[5];
	    editors[4] = new TextCellEditor(table);
	    
	    mainViewer.setColumnProperties(View.PROPS_COLUMNS);
	    mainViewer.setCellModifier(new EntryCellModifier(mainViewer));
	    mainViewer.setCellEditors(editors);
	    
		// add Columns for the mainTable
	    new TableColumn(table, SWT.LEFT).setText(View.CONTROLLER);
	    new TableColumn(table, SWT.LEFT).setText(View.PM);
	    new TableColumn(table, SWT.LEFT).setText(View.PMV);
	    new TableColumn(table, SWT.LEFT).setText(View.PMVV);
	    new TableColumn(table, SWT.LEFT).setText(View.COMMENTS);

	    setVisible(false);
	}

	public void activate(){
		 mainViewer.setInput(dataController.getValuesList());
		  for (int i = 0, n = table.getColumnCount(); i < n; i++) {
			  
			  table.getColumn(i).pack();
		  }
		  setVisible(true);
	}
	
	public void refreshTable(){
		if(mainViewer.getControl() != null && !mainViewer.getControl().isDisposed()){
	    	  mainViewer.setInput(dataController.getValuesList());	      
	      }
	     
		  for (int i = 0; i < 5 && !table.isDisposed(); i++) {
			  if (table.getColumn(i).getWidth() < 1) {
				  table.getColumn(i).pack();
			  }
		  }
	}
}
